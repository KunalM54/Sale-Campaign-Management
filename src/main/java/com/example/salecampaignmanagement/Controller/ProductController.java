package com.example.salecampaignmanagement.Controller;

import com.example.salecampaignmanagement.DTO.CampaignDTO;
import com.example.salecampaignmanagement.DTO.PaginationDTO;
import com.example.salecampaignmanagement.Model.Product;
import com.example.salecampaignmanagement.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/saveAll")
    public List<Product> saveAll(@RequestBody List<Product> productList) {
        return productService.saveAll(productList);
    }

    @GetMapping("/findAll")
    public List<Product> findAll() {
        return productService.findAll();
    }

    @GetMapping
    public PaginationDTO getByPage(Pageable pageable) {
        return productService.getByPage(pageable);
    }

    @PostMapping("/createCampaign")
    public ResponseEntity<?> createCampaign(@RequestBody CampaignDTO campaignDetails) {
        try {
            String res = productService.createCampaign(campaignDetails);
            return ResponseEntity.ok(res);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
        }
    }
}