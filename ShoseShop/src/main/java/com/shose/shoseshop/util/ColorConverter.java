package com.shose.shoseshop.util;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.HashMap;
import java.util.Map;

public class ColorConverter implements Converter<String, String> {

    @Override
    public String convert(MappingContext<String, String> context) {
        String rgb = context.getSource(); // Giá trị màu RGB từ nguồn
        Map<String, String> colorMap = new HashMap<>();

        // Bản đồ chuyển đổi màu
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

        // Trả về tên màu hoặc giá trị mặc định
        return colorMap.getOrDefault(rgb, "Không xác định");
    }
}
