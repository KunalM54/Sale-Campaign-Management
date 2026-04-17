package com.example.salecampaignmanagement.Service;



import com.example.salecampaignmanagement.DTO.CampaignDTO;
import com.example.salecampaignmanagement.DTO.DiscountDTO;
import com.example.salecampaignmanagement.DTO.PaginationDTO;
import com.example.salecampaignmanagement.ENUM.CampaignStatus;
import com.example.salecampaignmanagement.Model.Campaign;
import com.example.salecampaignmanagement.Model.CampaignDiscount;
import com.example.salecampaignmanagement.Model.PriceHistory;
import com.example.salecampaignmanagement.Model.Product;
import com.example.salecampaignmanagement.Repository.CampaignDiscountRepo;
import com.example.salecampaignmanagement.Repository.CampaignRepo;
import com.example.salecampaignmanagement.Repository.PriceHistoryRepo;
import com.example.salecampaignmanagement.Repository.ProductRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProductService {

    private final com.example.salecampaignmanagement.Repository.ProductRepo productRepo;
    private final com.example.salecampaignmanagement.Repository.CampaignRepo campaignRepo;
    private final com.example.salecampaignmanagement.Repository.CampaignDiscountRepo campaignDiscountRepo;
    private final com.example.salecampaignmanagement.Repository.PriceHistoryRepo priceHistoryRepo;

    public ProductService(com.example.salecampaignmanagement.Repository.ProductRepo productRepo,
                          com.example.salecampaignmanagement.Repository.CampaignRepo campaignRepo,
                          com.example.salecampaignmanagement.Repository.CampaignDiscountRepo campaignDiscountRepo,
                          com.example.salecampaignmanagement.Repository.PriceHistoryRepo priceHistoryRepo) {
        this.productRepo = productRepo;
        this.campaignRepo = campaignRepo;
        this.campaignDiscountRepo = campaignDiscountRepo;
        this.priceHistoryRepo = priceHistoryRepo;
    }

    public List<Product> saveAll(List<Product> productList) {
        return productRepo.saveAll(productList);
    }

    public PaginationDTO getByPage(Pageable pageable) {
        Page<Product> result = productRepo.findAll(pageable);

        PaginationDTO dto = new PaginationDTO();
        dto.setProductList(result.getContent());
        dto.setPage(pageable.getPageNumber());
        dto.setPageSize(pageable.getPageSize());
        dto.setTotalPages(result.getTotalPages());

        return dto;
    }

    public List<Product> findAll() {
        return productRepo.findAll();
    }

    @Transactional
    public String createCampaign(CampaignDTO campaignDTO) {

        // 1. Create Campaign
        Campaign campaign = new Campaign();
        campaign.setTitle(campaignDTO.getTitle());
        campaign.setStartDate(campaignDTO.getStartDate());
        campaign.setEndDate(campaignDTO.getEndDate());
        campaign.setStatus(CampaignStatus.UPCOMING);

        campaignRepo.save(campaign);

        // 2. Extract product IDs
        List<Integer> productIds = new ArrayList<>();
        for (DiscountDTO dto : campaignDTO.getCampaignDiscount()) {
            productIds.add(dto.getProductId());
        }

        // 3. Fetch products in single query
        List<Product> products = productRepo.findAllById(productIds);

        // 4. Create productId → Product map
        Map<Integer, Product> productMap = new HashMap<>();
        for (Product product : products) {
            productMap.put(product.getProductId(), product);
        }

        // 5. Prepare discount list
        List<CampaignDiscount> discountList = new ArrayList<>();
        List<Integer> missingProductIds = new ArrayList<>();

        for (DiscountDTO dto : campaignDTO.getCampaignDiscount()) {

            Product product = productMap.get(dto.getProductId());

            if (product == null) {
                // collect missing IDs instead of throwing exception
                missingProductIds.add(dto.getProductId());
                continue;
            }

            CampaignDiscount discount = new CampaignDiscount();
            discount.setCompaign(campaign);
            discount.setProduct(product);
            discount.setDiscount(dto.getDiscount());

            discountList.add(discount);
        }

        // 6. Batch save valid discounts
        if (!discountList.isEmpty()) {
            campaignDiscountRepo.saveAll(discountList);
        }

        // 7. Response message
        if (missingProductIds.isEmpty()) {
            return "Campaign created successfully with all products.";
        } else {
            return "Campaign created with missing products: " + missingProductIds;
        }
    }

    @Transactional
    public void applyDiscount(Campaign campaign)
    {

        List<CampaignDiscount> discounts = campaign.getCompaignDiscounts();

        List<Integer> productIds = new ArrayList<>();
        for (CampaignDiscount cd : discounts) {
            productIds.add(cd.getProduct().getProductId());
        }

        List<Product> products = productRepo.findAllById(productIds);

        Map<Integer, Product> productMap = new HashMap<>();
        for (Product p : products) {
            productMap.put(p.getProductId(), p);
        }

        List<Product> updatedProducts = new ArrayList<>();
        List<PriceHistory> historyList = new ArrayList<>();

        for (CampaignDiscount cd : discounts) {

            Product product = productMap.get(cd.getProduct().getProductId());

            product.setDiscount(product.getDiscount() + cd.getDiscount());

            double oldPrice = product.getCurrentPrice();
            double newPrice = oldPrice * (1 - cd.getDiscount() / 100.0);

            saveHistory(updatedProducts, historyList, product, oldPrice, newPrice);
        }

        productRepo.saveAll(updatedProducts);
        priceHistoryRepo.saveAll(historyList);

        campaign.setStatus(CampaignStatus.CURRENT);
        campaignRepo.save(campaign);

        System.out.println("✅ Discount Applied");
    }


    private PriceHistory saveHistory(List<Product> updatedProducts, List<PriceHistory> historyList, Product product, double oldPrice, double newPrice) {
        product.setCurrentPrice(newPrice);

        PriceHistory history = new PriceHistory();
        history.setProduct(product);
        history.setOldPrice(oldPrice);
        history.setNewPrice(newPrice);
        history.setChangedAt(LocalDateTime.now());

        historyList.add(history);
        updatedProducts.add(product);
        return history;
    }


    @Transactional
    public void revertDiscount(Campaign campaign) {

        List<CampaignDiscount> discounts = campaign.getCompaignDiscounts();

        List<Integer> productIds = new ArrayList<>();
        for (CampaignDiscount cd : discounts) {
            productIds.add(cd.getProduct().getProductId());
        }

        List<Product> products = productRepo.findAllById(productIds);

        Map<Integer, Product> productMap = new HashMap<>();
        for (Product p : products) {
            productMap.put(p.getProductId(), p);
        }

        List<Product> updatedProducts = new ArrayList<>();
        List<PriceHistory> historyList = new ArrayList<>();

        for (CampaignDiscount cd : discounts) {

            Product product = productMap.get(cd.getProduct().getProductId());

            product.setDiscount(product.getDiscount() - cd.getDiscount());

            double oldPrice = product.getCurrentPrice();
            double newPrice = oldPrice / (1 - cd.getDiscount() / 100.0);

            saveHistory(updatedProducts, historyList, product, oldPrice, newPrice);
        }

        productRepo.saveAll(updatedProducts);
        priceHistoryRepo.saveAll(historyList);

        campaign.setStatus(CampaignStatus.PAST);
        campaignRepo.save(campaign);

        System.out.println("⛔ Discount Removed");
    }


    @Transactional
    public void startCampaign(LocalDate today) {
        List<Campaign> campaigns = campaignRepo.findByStartDate(today);

        for (Campaign campaign : campaigns) {
            applyDiscount(campaign);
        }
    }

    @Transactional
    public void endCampaign(LocalDate today) {
        List<Campaign> campaigns = campaignRepo.findByEndDate(today);

        for (Campaign campaign : campaigns) {
            revertDiscount(campaign);
        }
    }
}
