package com.shose.shoseshop.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.shose.shoseshop.constant.ShippingMethod;
import com.shose.shoseshop.controller.request.VoucherRequest;
import com.shose.shoseshop.entity.Order;
import com.shose.shoseshop.entity.OrderDetail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendMail(String subject, String body, Set<String> recipients) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

        try {
            String[] to = recipients.toArray(new String[0]);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            helper.setFrom("vhai31102002@gmail.com");
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendNewVoucherEmail(VoucherRequest request, Set<String> emails) {
        String subject = "Thông báo khuyến mãi mới từ Shose Shop!";
        String description = "Giảm " + request.getValue() + " tối đa " + formatCurrency(request.getMaxMoney());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = request.getExpiredTime().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(formatter);
        Context context = new Context();
        context.setVariable("code", request.getCode());
        context.setVariable("description", description);
        context.setVariable("endDate", formattedDate);
        String body = templateEngine.process("voucher", context);
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
            helper.setTo(emails.toArray(new String[0]));
            helper.setSubject(subject);
            helper.setText(body, true);
            helper.setFrom("vhai31102002@gmail.com");
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendOTP(String otp, Set<String> emails) {
        String subject = "Yêu cầu lấy lại mật khẩu!";
        Context context = new Context();
        context.setVariable("otp", otp);
        String body = templateEngine.process("password", context);
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
            helper.setTo(emails.toArray(new String[0]));
            helper.setSubject(subject);
            helper.setText(body, true);
            helper.setFrom("vhai31102002@gmail.com");
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendBirthDay(String to, String name) {
        String subject = "Happy birthday " + name;
        Context context = new Context();
        context.setVariable("name", name);
        String body = templateEngine.process("hpbd", context);
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
        try {
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            helper.setFrom("vhai31102002@gmail.com");

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendInvoiceWithAttachment(String to, Order order) throws MessagingException {
        String subject = "Hóa đơn đặt hàng của bạn";
        List<OrderDetail> orderDetails = order.getOrderDetails();
        String fullName = order.getFullName();
        BigDecimal totalAmount = order.getTotalAmount();
        Context context = new Context();
        context.setVariable("fullName", fullName);
        context.setVariable("totalAmount", formatCurrency(totalAmount));
        if (order.getShippingMethod() == ShippingMethod.FAST) {
            context.setVariable("shipPrice", formatCurrency(BigDecimal.valueOf(15000)));
        } else {
            context.setVariable("shipPrice", formatCurrency(BigDecimal.valueOf(50000)));
        }
        List<Map<String, Object>> details = orderDetails.stream().map(detail -> {
            Map<String, Object> map = new HashMap<>();
            map.put("productName", detail.getProductDetail().getProduct().getName());
            map.put("color", convertColor(detail.getProductDetail().getColor()));
            map.put("size", detail.getProductDetail().getSize());
            map.put("quantity", detail.getQuantity());
            map.put("price", formatCurrency(detail.getProductDetail().getPrice()));
            map.put("total", formatCurrency(detail.getProductDetail().getPrice()
                    .multiply(BigDecimal.valueOf(detail.getQuantity()))));
            map.put("image", detail.getProductDetail().getImg());
            return map;
        }).collect(Collectors.toList());
        context.setVariable("orderDetails", details);

        String body = templateEngine.process("hoadon", context);
        byte[] pdfInvoice = generateInvoicePdf(order);
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        helper.setFrom("vhai31102002@gmail.com");
        helper.addAttachment("HoaDon.pdf", new ByteArrayResource(pdfInvoice));
        javaMailSender.send(message);
    }

    private String formatCurrency(BigDecimal value) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return format.format(value);
    }

    public byte[] generateInvoicePdf(Order order) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String fullName = order.getFullName();
        List<OrderDetail> orderDetails = order.getOrderDetails();
        String phone = order.getPhone();
        Instant orderDate = order.getCreatedAt();
        String shippingAddress = order.getShippingAddress();
        String note = order.getNote();
        BigDecimal totalAmount = order.getTotalAmount();
        LocalDate localDate = orderDate.atZone(ZoneId.systemDefault()).toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = localDate.format(formatter);
        BigDecimal shippingPrice;
        if (order.getShippingMethod() == ShippingMethod.FAST) {
            shippingPrice = BigDecimal.valueOf(15000);
        } else {
            shippingPrice = BigDecimal.valueOf(15000);
        }
        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            // Load custom font
            String fontPath = "/fonts/arial.ttf";
            BaseFont baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(baseFont, 14);
            Font boldFont = new Font(baseFont, 14, Font.BOLD);

            // Tiêu đề hóa đơn (căn giữa, in đậm)
            Paragraph title = new Paragraph("Hóa Đơn Đặt Hàng", boldFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // Thông tin khách hàng
            document.add(new Paragraph("Tên khách hàng: " + fullName, boldFont));
            document.add(new Paragraph("Số điện thoại: " + phone, font));
            document.add(new Paragraph("Địa chỉ: " + shippingAddress, font));
            document.add(new Paragraph("Ngày đặt hàng: " + formattedDate, font));
            document.add(new Paragraph(" "));

            // Bảng chi tiết đơn hàng
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2, 1.5f, 1.5f, 2, 2f, 2});

            // Thêm tiêu đề bảng
            table.addCell(new Paragraph("Sản phẩm", boldFont));
            table.addCell(new Paragraph("Màu sắc", boldFont));
            table.addCell(new Paragraph("Kích thước", boldFont));
            table.addCell(new Paragraph("Số lượng", boldFont));
            table.addCell(new Paragraph("Giá", boldFont));
            table.addCell(new Paragraph("Thành tiền", boldFont));

            // Thêm chi tiết đơn hàng
            for (OrderDetail detail : orderDetails) {
                table.addCell(new Paragraph(detail.getProductDetail().getProduct().getName(), font));
                table.addCell(new Paragraph(convertColor(detail.getProductDetail().getColor()), font));
                table.addCell(new Paragraph(detail.getProductDetail().getSize(), font));
                table.addCell(new Paragraph(String.valueOf(detail.getQuantity()), font));
                table.addCell(new Paragraph(formatCurrency(detail.getProductDetail().getPrice()), font));
                table.addCell(new Paragraph(formatCurrency(detail.getProductDetail().getPrice()
                        .multiply(BigDecimal.valueOf(detail.getQuantity()))), font));
            }
            document.add(table);

            document.add(new Paragraph(" "));
            Paragraph shipPrice = new Paragraph("Phí vận chuyển: " + formatCurrency(shippingPrice), boldFont);
            document.add(shipPrice);

            // Thêm tổng tiền bên dưới bảng
            document.add(new Paragraph(" "));
            Paragraph totalParagraph = new Paragraph("Tổng tiền: " + formatCurrency(totalAmount), boldFont);
            document.add(totalParagraph);

            // Thêm ghi chú nếu có
            if (note != null && !note.isEmpty()) {
                document.add(new Paragraph("Ghi chú: " + note, font));
            }

            PdfContentByte canvas = writer.getDirectContent();
            float centerX = document.getPageSize().getWidth() / 2;
            float startY = 80;
            float lineSpacing = 20;
            ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER,
                    new Phrase("================================================", boldFont),
                    centerX, startY, 0);

            ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER,
                    new Phrase("ShoseShop cảm ơn bạn đã đặt hàng!", boldFont),
                    centerX, startY - lineSpacing, 0);

            ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER,
                    new Phrase("Số điện thoại: 0382952063", font),
                    centerX, startY - 2 * lineSpacing, 0);

            ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER,
                    new Phrase("Địa chỉ: Số 88-Cầu Diễn - Bắc Từ Liêm - Hà Nội", font),
                    centerX, startY - 3 * lineSpacing, 0);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }


    private String convertColor(String rgb) {
        Map<String, String> colorMap = new HashMap<>();
        colorMap.put("rgb(0, 0, 0)", "Đen");
        colorMap.put("rgb(255, 255, 255)", "Trắng");
        colorMap.put("rgb(139, 69, 19)", "Nâu");
        colorMap.put("rgb(245, 245, 220)", "Be (Kem)");
        colorMap.put("rgb(255, 0, 0)", "Đỏ");
        colorMap.put("rgb(255, 255, 0)", "Vàng");
        colorMap.put("rgb(0, 0, 255)", "Xanh dương");
        colorMap.put("rgb(0, 255, 0)", "Xanh lá cây");
        colorMap.put("rgb(169, 169, 169)", "Xám");
        colorMap.put("rgb(255, 182, 193)", "Hồng pastel");
        colorMap.put("rgb(170, 255, 195)", "Xanh bạc hà");
        colorMap.put("rgb(230, 230, 250)", "Tím nhạt");
        colorMap.put("rgb(255, 223, 0)", "Vàng kim");
        colorMap.put("rgb(192, 192, 192)", "Bạc");
        colorMap.put("rgb(127, 140, 141)", "Camo");
        colorMap.put("rgb(255, 105, 180)", "Hồng");
        colorMap.put("rgb(255, 165, 0)", "Cam");
        colorMap.put("rgb(128, 0, 128)", "Tím");
        return colorMap.get(rgb);
    }
}
