package com.example.phone_store.controller;

import com.example.phone_store.entity.Customer;
import com.example.phone_store.entity.Invoice;
import com.example.phone_store.entity.InvoiceDetail;
import com.example.phone_store.entity.Product;
import com.example.phone_store.repository.CustomerRepository;
import com.example.phone_store.repository.InvoiceRepository;
import com.example.phone_store.repository.InvoiceSpecification;
import com.example.phone_store.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public InvoiceController(InvoiceRepository invoiceRepository,
                             CustomerRepository customerRepository,
                             ProductRepository productRepository) {
        this.invoiceRepository = invoiceRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @GetMapping({"", "/search-pagination"})
    public String searchAndPaginateInvoices(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            Model model) {

        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Invoice> spec = Specification.where(InvoiceSpecification.hasKeyword(keyword));
        Page<Invoice> invoicePage = invoiceRepository.findAll(spec, pageable);

        model.addAttribute("invoicePage", invoicePage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("customers", customerRepository.findByIsDeletedFalse());
        model.addAttribute("products", productRepository.findByIsDeletedFalse());

        return "invoices";
    }

    @PostMapping("/add")
    @Transactional
    public String addInvoice(@RequestParam Integer customerId,
                             @RequestParam Integer productId,
                             @RequestParam Integer quantity) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        // Kiểm tra tồn kho
        if (product.getStock() < quantity) {
            return "redirect:/invoices?error=out_of_stock";
        }

        // Trừ tồn kho của sản phẩm
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);

        // Tạo Invoice
        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setStatus("PENDING");

        BigDecimal totalAmount = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        invoice.setTotalAmount(totalAmount);

        InvoiceDetail detail = new InvoiceDetail();
        detail.setInvoice(invoice);
        detail.setProduct(product);
        detail.setQuantity(quantity);
        detail.setUnitPrice(product.getPrice());

        List<InvoiceDetail> details = new ArrayList<>();
        details.add(detail);
        invoice.setInvoiceDetails(details);

        invoiceRepository.save(invoice);
        return "redirect:/invoices";
    }

    @PostMapping("/update-status/{id}")
    @Transactional
    public String updateStatus(@PathVariable Integer id, @RequestParam String status) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        String oldStatus = invoice.getStatus();

        if (!"CANCELED".equalsIgnoreCase(oldStatus) && "CANCELED".equalsIgnoreCase(status)) {
            for (InvoiceDetail detail : invoice.getInvoiceDetails()) {
                Product product = detail.getProduct();
                product.setStock(product.getStock() + detail.getQuantity());
                productRepository.save(product);
            }
        }

        invoice.setStatus(status);
        invoiceRepository.save(invoice);
        return "redirect:/invoices";
    }
}