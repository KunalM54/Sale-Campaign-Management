package com.example.salecampaignmanagement.Repository;

import com.example.salecampaignmanagement.Model.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceHistoryRepo extends JpaRepository<PriceHistory, Integer> {
}
