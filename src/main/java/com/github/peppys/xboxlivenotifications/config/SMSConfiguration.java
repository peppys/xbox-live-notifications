package com.github.peppys.xboxlivenotifications.config;

import com.github.peppys.xboxlivenotifications.services.sms.SMSService;
import com.github.peppys.xboxlivenotifications.services.sms.TwilioSMSService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SMSConfiguration {

    @Bean
    public SMSService smsService() {
        return new TwilioSMSService();
    }
}
