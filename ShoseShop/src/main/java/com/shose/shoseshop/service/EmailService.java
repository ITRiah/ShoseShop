package com.shose.shoseshop.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendMail(String subject, String body, String to) {
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

    public void sendInvoiceWithAttachment(String to, String fullName, BigDecimal totalAmount,
                                          List<OrderDetail> orderDetails) throws MessagingException {
        // Tiêu đề email
        String subject = "Hóa đơn đặt hàng của bạn";

        // Tạo context để truyền dữ liệu vào template
        Context context = new Context();
        context.setVariable("fullName", fullName);
        context.setVariable("totalAmount", formatCurrency(totalAmount));

        // Map danh sách chi tiết đơn hàng
        List<Map<String, Object>> details = orderDetails.stream().map(detail -> {
            Map<String, Object> map = new HashMap<>();
            map.put("productName", detail.getProductDetail().getProduct().getName());
            map.put("color", detail.getProductDetail().getColor());
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
        byte[] pdfInvoice = generateInvoicePdf(fullName, totalAmount, orderDetails);

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

    // Hàm tạo file PDF hóa đơn
    private byte[] generateInvoicePdf(String fullName, BigDecimal totalAmount, List<OrderDetail> orderDetails) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();
            String fontPath = "/fonts/arial.ttf";
            BaseFont baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(baseFont, 14);

            // Thêm tiêu đề
            document.add(new Paragraph("Hóa Đơn Đặt Hàng", font));
            document.add(new Paragraph("Khách hàng: " + fullName, font));
            document.add(new Paragraph("Tổng tiền: " + formatCurrency(totalAmount), font));
            document.add(new Paragraph(" "));

            // Thêm bảng chi tiết đơn hàng
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2, 1.5f, 1.5f, 1, 1.5f, 2});
            table.addCell(new Paragraph("Sản phẩm", font));
            table.addCell(new Paragraph("Màu sắc", font));
            table.addCell(new Paragraph("Kích thước", font));
            table.addCell(new Paragraph("Số lượng", font));
            table.addCell(new Paragraph("Giá", font));
            table.addCell(new Paragraph("Thành tiền", font));

            for (OrderDetail detail : orderDetails) {
                table.addCell(new Paragraph(detail.getProductDetail().getProduct().getName(), font));
                table.addCell(new Paragraph(detail.getProductDetail().getColor(), font));
                table.addCell(new Paragraph(detail.getProductDetail().getSize(), font));
                table.addCell(new Paragraph(String.valueOf(detail.getQuantity()), font));
                table.addCell(new Paragraph(formatCurrency(detail.getProductDetail().getPrice()), font));
                table.addCell(new Paragraph(formatCurrency(detail.getProductDetail().getPrice()
                        .multiply(BigDecimal.valueOf(detail.getQuantity()))), font));
            }
            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
}
