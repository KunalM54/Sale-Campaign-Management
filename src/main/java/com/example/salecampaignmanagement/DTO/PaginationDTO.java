package com.example.salecampaignmanagement.DTO;

import com.example.salecampaignmanagement.Model.Product;

import java.util.List;

public class PaginationDTO {
    private List<Product> productList;
    private int page;
    private int pageSize;
    private int totalPages;

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
