package com.example.salecampaignmanagement.Scheduler;

import com.example.salecampaignmanagement.Service.ProductService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CampaignScheduler {

    private final ProductService productService;

    public CampaignScheduler(ProductService productService) {
        this.productService = productService;
    }

    @Scheduled(cron = "*/20 * * * * *")
    public void checkCampaign() {
        LocalDate today = LocalDate.now();
        productService.startCampaign(today);
        productService.endCampaign(today);
    }
}
