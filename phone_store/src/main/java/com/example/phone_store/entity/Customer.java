package com.example.phone_store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Tên khách hàng không được để trống")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Column(nullable = false, length = 20, unique = true)
    private String phone;

    @Email(message = "Email không đúng định dạng")
    @Column(length = 100, unique = true)
    private String email;

    @Column(length = 255)
    private String address;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}
