package com.example.packend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Getter
@Setter
@Configuration
@PropertySource(ignoreResourceNotFound = true, value = "classpath:admin.configuration.properties")
public class VerfuegbareUhrzeitenConfiguration {
    @Value("#{${montag}}")
    private List<String> montag;
    @Value("#{${dienstag}}")
    private List<String> dienstag;
    @Value("#{${mittwoch}}")
    private List<String> mittwoch;
    @Value("#{${donnerstag}}")
    private List<String> donnerstag;
    @Value("#{${freitag}}")
    private List<String> freitag;

}
