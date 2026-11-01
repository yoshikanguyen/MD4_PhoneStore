package com.example.phone_store.controller;

import com.example.phone_store.entity.Customer;
import com.example.phone_store.repository.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Gộp chung đường dẫn gốc và phân trang --> Tránh lỗi hiển thị khi mới vào trang
    @GetMapping({"", "/pagination"})
    public String listCustomersWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {

        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Customer> customerPage = customerRepository.findByIsDeletedFalse(pageable);

        model.addAttribute("customerPage", customerPage);
        model.addAttribute("newCustomer", new Customer());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);

        return "customers"; // Khớp với tên file templates/customers.html
    }

    @PostMapping("/add")
    public String addCustomer(@Valid @ModelAttribute("newCustomer") Customer customer, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // Nếu lỗi validate, cần nạp lại phân trang để không bị lỗi trắng trang
            Pageable pageable = PageRequest.of(0, 5, Sort.by("id").ascending());
            model.addAttribute("customerPage", customerRepository.findByIsDeletedFalse(pageable));
            return "customers";
        }
        customer.setIsDeleted(false);
        customerRepository.save(customer);
        return "redirect:/customers";
    }

    @PostMapping("/update/{id}")
    public String updateCustomer(@PathVariable Integer id, @Valid @ModelAttribute("customer") Customer customerDetails, BindingResult result, Model model) {
        if (result.hasErrors()) {
            Pageable pageable = PageRequest.of(0, 5, Sort.by("id").ascending());
            model.addAttribute("customerPage", customerRepository.findByIsDeletedFalse(pageable));
            return "customers";
        }
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        customer.setName(customerDetails.getName());
        customer.setPhone(customerDetails.getPhone());
        customer.setEmail(customerDetails.getEmail());
        customer.setAddress(customerDetails.getAddress());
        customerRepository.save(customer);
        return "redirect:/customers";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Integer id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        customer.setIsDeleted(true);
        customerRepository.save(customer);
        return "redirect:/customers";
    }
}