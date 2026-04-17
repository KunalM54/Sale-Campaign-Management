package com.example.salecampaignmanagement.DTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CampaignDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private String title;
    private List<DiscountDTO> campaignDiscount = new ArrayList<>();

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<DiscountDTO> getCampaignDiscount() {
        return campaignDiscount;
    }

    public void setCampaignDiscount(List<DiscountDTO> campaignDiscount) {
        this.campaignDiscount = campaignDiscount;
    }
}
