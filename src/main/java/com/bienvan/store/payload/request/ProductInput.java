package com.bienvan.store.payload.request;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInput {

    private Long id;

    @NotBlank(message = "Tên không được để trống")
    private String name;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;

    @Min(value = 1, message = "Giá phải lớn hơn hoặc bằng 1")
    private double price;

    private MultipartFile image;

    @Min(value = 0, message = "Số lượng phải lớn hơn 0")
    private int quantity;

    private Long color_id;

    private Long brand_id;

    private Long user_id;

    private Long category_id;

    // public ProductDto covert(Product p){
    //     return new ProductDto(p.getId(),p.getName(),p.getDescription(),p.getPrice(),p.getImage())
    // }
}
