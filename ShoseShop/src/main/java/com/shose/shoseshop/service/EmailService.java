package com.shose.shoseshop.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
        // Tiêu đề email
        String subject = "Hóa đơn đặt hàng của bạn";
        List<OrderDetail> orderDetails = order.getOrderDetails();
        String fullName = order.getFullName();
        BigDecimal totalAmount = order.getTotalAmount();

        // Tạo context để truyền dữ liệu vào template
        Context context = new Context();
        context.setVariable("fullName", fullName);
        context.setVariable("totalAmount", formatCurrency(totalAmount));

        // Map danh sách chi tiết đơn hàng
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

        // Render template email từ Thymeleaf
        String body = templateEngine.process("hoadon", context);

        // Tạo PDF hóa đơn
        byte[] pdfInvoice = generateInvoicePdf(order);

        // Tạo email với tệp đính kèm
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        helper.setFrom("vhai31102002@gmail.com");

        // Đính kèm file PDF hóa đơn
        helper.addAttachment("HoaDon.pdf", new ByteArrayResource(pdfInvoice));

        // Gửi email
        javaMailSender.send(message);
    }

    // Hàm định dạng tiền tệ
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
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
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
            table.setWidths(new float[]{2, 1.5f, 1.5f, 2, 1.5f, 2});

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

            // Thêm tổng tiền bên dưới bảng
            document.add(new Paragraph(" "));
            Paragraph totalParagraph = new Paragraph("Tổng tiền: " + formatCurrency(totalAmount), boldFont);
            totalParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalParagraph);

            // Thêm ghi chú nếu có
            if (note != null && !note.isEmpty()) {
                document.add(new Paragraph("Ghi chú: " + note, font));
            }

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
