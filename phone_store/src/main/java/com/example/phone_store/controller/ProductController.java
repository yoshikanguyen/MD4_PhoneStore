package com.example.phone_store.controller;

import com.example.phone_store.entity.Product;
import com.example.phone_store.repository.ProductRepository;
import com.example.phone_store.repository.ProductSpecification;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public String listProducts(@RequestParam(required = false) String keyword, Model model) {
        List<Product> products;
        if (keyword != null && !keyword.trim().isEmpty()) {
            products = productRepository.findByIsDeletedFalseAndBrandContainingIgnoreCaseOrNameContainingIgnoreCase(keyword, keyword);
        } else {
            products = productRepository.findByIsDeletedFalse();
        }
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        return "products";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute("product") Product product, BindingResult result) {
        if (result.hasErrors()) {
            return "products"; // Trả về trang kèm lỗi validate nếu có
        }
        product.setIsDeleted(false);
        productRepository.save(product);
        return "redirect:/products";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Integer id, @Valid @ModelAttribute("product") Product productDetails, BindingResult result) {
        if (result.hasErrors()) {
            return "products";
        }
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
        product.setName(productDetails.getName());
        product.setBrand(productDetails.getBrand());
        product.setPrice(productDetails.getPrice());
        product.setStock(productDetails.getStock());
        product.setImage(productDetails.getImage());
        productRepository.save(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Integer id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
        product.setIsDeleted(true);
        productRepository.save(product);
        return "redirect:/products";
    }

    @GetMapping({"", "/search-pagination"})
    public String searchAndPaginateProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer stock,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {

        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        // Gom nhóm điều kiện Specification
        Specification<Product> spec = Specification.where(ProductSpecification.isNotDeleted())
                .and(ProductSpecification.hasKeyword(keyword))
                .and(ProductSpecification.priceBetween(minPrice, maxPrice))
                .and(ProductSpecification.minStock(stock));

        Page<Product> productPage = productRepository.findAll(spec, pageable);

        model.addAttribute("productPage", productPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("stock", stock);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);

        return "products";
    }
}
