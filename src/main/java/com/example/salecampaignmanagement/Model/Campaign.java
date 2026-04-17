package com.example.salecampaignmanagement.Model;

import com.example.salecampaignmanagement.ENUM.CampaignStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compaign")
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int compaignId;

    private LocalDate startDate;
    private LocalDate endDate;
    private String title;

    @Enumerated(EnumType.STRING)
    private CampaignStatus status;

    @JsonIgnore
    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL)
    private List<CampaignDiscount> campaignDiscounts = new ArrayList<>();

    public int getCompaignId() {
        return compaignId;
    }

    public void setCompaignId(int compaignId) {
        this.compaignId = compaignId;
    }

    @JsonIgnore
    public List<CampaignDiscount> getCompaignDiscounts() {
        return campaignDiscounts;
    }

    public void setCompaignDiscounts(List<CampaignDiscount> campaignDiscounts) {
        this.campaignDiscounts = campaignDiscounts;
    }

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

    public CampaignStatus getStatus() {
        return status;
    }

    public void setStatus(CampaignStatus status) {
        this.status = status;
    }
}