package com.example.salecampaignmanagement.Repository;

import com.example.salecampaignmanagement.Model.CampaignDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignDiscountRepo extends JpaRepository<CampaignDiscount, Integer> {
}
