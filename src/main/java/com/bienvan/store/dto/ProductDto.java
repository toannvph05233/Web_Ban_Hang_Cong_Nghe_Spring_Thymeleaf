package com.bienvan.store.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.*;

@Data
public class ProductDto {

    private Long id;

    @NotBlank(message = "Tên không được để trống")
    private String name;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;

    @Min(value = 1, message = "Giá phải lớn hơn hoặc bằng 1.")
    private double price;

    private String image;

    @Min(value = 0, message = "Số lượng phải lớn hơn 0")
    private int quantity;

    private Long color_id;

    private long brand_id;

    private Long user_id;

    private Long category_id;

    public ProductDto(Long id, String name,
            String description,
            double price, String image,
            int quantity, Long color_id, long brand_id,
            Long user_id, Long category_id) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
        this.color_id = color_id;
        this.brand_id = brand_id;
        this.user_id = user_id;
        this.category_id = category_id;
    }

}
