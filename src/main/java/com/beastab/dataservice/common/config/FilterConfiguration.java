package com.beastab.dataservice.common.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {
    @Bean
    public FilterRegistrationBean jwtFilter() {
        FilterRegistrationBean filter= new FilterRegistrationBean();
        filter.setFilter(new JwtFilter());
        // provide endpoints which needs to be restricted.
        // All Endpoints would be restricted if unspecified
        filter.addUrlPatterns("/api/v1/address/*");
        filter.addUrlPatterns("/api/v1/client/team/*");
        filter.addUrlPatterns("/api/v1/client/account-section-data");
        filter.addUrlPatterns("/api/v1/client/switch-company");
        filter.addUrlPatterns("/api/v1/client/update-image");
        filter.addUrlPatterns("/api/v1/client/companies");
        filter.addUrlPatterns("/api/v1/client/delete");
        filter.addUrlPatterns("/api/v1/client/delete-user/*");
        filter.addUrlPatterns("/api/v1/cart/*");
        filter.addUrlPatterns("/api/v1/salesOrders/*");
        //filter.addUrlPatterns("/api/v1/plytix/*");
        filter.addUrlPatterns("/api/v1/product/re-order");
        filter.addUrlPatterns("/api/v1/notification/*");
        filter.addUrlPatterns("/api/v1/ledger/*");
        filter.addUrlPatterns("/api/v1/sample/*");
        filter.addUrlPatterns("/api/v2/sample/*");
        filter.addUrlPatterns("/api/v1/rfq/*");
        filter.addUrlPatterns("/api/v1/client/profile/*");
        filter.addUrlPatterns("/api/v1/leads/callback");
        filter.addUrlPatterns("/api/v1/leads/product-callback");
        filter.addUrlPatterns("/api/v1/lead/*");
        //filter.addUrlPatterns("/api/v1/widget/upload-File-s3");
        filter.addUrlPatterns("/api/v1/product-family/*");
        filter.addUrlPatterns("/api/v1/callback/callback");
        filter.addUrlPatterns("/api/v1/callback/manufacturer/callback");
        filter.addUrlPatterns("/api/v1/email-verification/*");
        filter.addUrlPatterns("/api/v1/po-upload/*");
        filter.addUrlPatterns("/api/v1/Mailer/*");

        return filter;
    }

}
