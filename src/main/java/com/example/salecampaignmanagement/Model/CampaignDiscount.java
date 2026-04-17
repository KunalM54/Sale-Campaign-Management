package com.example.salecampaignmanagement.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "compaign_discount")
public class CampaignDiscount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int discountId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "compaign_id")
    private Campaign campaign;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private double discount;

    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    @JsonIgnore
    public Campaign getCompaign() {
        return campaign;
    }

    public void setCompaign(Campaign campaign) {
        this.campaign = campaign;
    }

    @JsonIgnore
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
