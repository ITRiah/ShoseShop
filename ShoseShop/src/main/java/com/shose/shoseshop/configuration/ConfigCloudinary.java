package com.shose.shoseshop.configuration;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConfigCloudinary {
    @Bean
    public Cloudinary configKey(){
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dqikebvgu");
        config.put("api_key", "193244175828982");
        config.put("api_secret", "M-aHdo3n3-vW_Yt0l6g4D18TxqY");
        return new Cloudinary(config);
    }
}
