package com.example.salecampaignmanagement.Repository;

import com.example.salecampaignmanagement.Model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CampaignRepo extends JpaRepository<Campaign, Integer> {

    @Query(value = "select * from compaign where start_date=?1 and status='UPCOMING'", nativeQuery = true)
    List<Campaign> findByStartDate(LocalDate startDate);

    @Query(value = "select * from compaign where end_date=?1 and status='CURRENT'", nativeQuery = true)
    List<Campaign> findByEndDate(LocalDate endDate);
}
