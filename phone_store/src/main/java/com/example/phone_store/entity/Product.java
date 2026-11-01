package com.example.phone_store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Nhãn hàng không được để trống")
    @Column(nullable = false, length = 50)
    private String brand;

    @NotNull(message = "Giá sản phẩm không được để trống")
    @Min(value = 0, message = "Giá phải lớn hơn hoặc bằng 0")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @NotNull(message = "Số lượng tồn kho không được để trống")
    @Min(value = 0, message = "Tồn kho phải lớn hơn hoặc bằng 0")
    @Column(nullable = false)
    private Integer stock;

    @NotBlank(message = "Đường dẫn ảnh không được để trống")
    @Column(nullable = false, length = 255)
    private String image;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}