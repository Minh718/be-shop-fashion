package com.shopro.shop1905.templates;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

/**
 * Template
 */
@Component
public class Template {
    @Autowired
    ResourceLoader resourceLoader;

    public String getTemplateEmail(String uuid, String email) throws IOException {
        // String template = new String(
        // Files.readAllBytes(Paths.get("src/main/resources/templates/test.txt")),
        // StandardCharsets.UTF_8);
        Resource cpr = resourceLoader.getResource("classpath:templates/email-template.html");
        String template = new String(FileCopyUtils.copyToByteArray(cpr.getInputStream()),
                StandardCharsets.UTF_8);
        // String template = new String(cpr.getInputStream().readAllBytes(),
        // StandardCharsets.UTF_8);cclea
        // Step 2: Prepare the dynamic content
        Map<String, String> valuesMap = Map.of(
                "Tên người dùng", email,
                "Tên Công Ty", "Công ty ABC",
                "Link xác nhận", "http://localhost:3000/register?token=" + uuid,
                "Năm hiện tại", String.valueOf(java.time.Year.now()));

        StringSubstitutor sub = new StringSubstitutor(valuesMap);
        String emailContent = sub.replace(template);

        return emailContent;
    }
}