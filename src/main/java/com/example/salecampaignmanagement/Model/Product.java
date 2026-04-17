package com.example.salecampaignmanagement.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    private String title;
    private double mrp;
    private double currentPrice;
    private double discount;
    private long inventory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CampaignDiscount> campaignDiscountList = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PriceHistory> priceHistoryList = new ArrayList<>();

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getMrp() {
        return mrp;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public long getInventory() {
        return inventory;
    }

    public void setInventory(long inventory) {
        this.inventory = inventory;
    }

    @JsonIgnore
    public List<CampaignDiscount> getCompaignDiscountList() {
        return campaignDiscountList;
    }

    public void setCompaignDiscountList(List<CampaignDiscount> campaignDiscountList) {
        this.campaignDiscountList = campaignDiscountList;
    }

    @JsonIgnore
    public List<PriceHistory> getPriceHistoryList() {
        return priceHistoryList;
    }

    public void setPriceHistoryList(List<PriceHistory> priceHistoryList) {
        this.priceHistoryList = priceHistoryList;
    }
}
