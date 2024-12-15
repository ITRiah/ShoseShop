package com.shose.shoseshop.configuration;

import com.shose.shoseshop.controller.response.ProductDetailResponse;
import com.shose.shoseshop.entity.ProductDetail;
import com.shose.shoseshop.util.ColorConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<ProductDetail, ProductDetailResponse>() {
            @Override
            protected void configure() {
                using(new ColorConverter()).map(source.getColor(), destination.getColor()); // Sử dụng ColorConverter cho trường 'color'
            }
        });
        return modelMapper;
    }
}

