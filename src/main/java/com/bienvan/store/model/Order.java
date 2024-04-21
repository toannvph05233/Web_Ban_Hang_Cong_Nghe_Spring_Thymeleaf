package com.bienvan.store.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên không được để trống")
    private String name;

    private String status;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    private Date create_at;

    private String payment_method;

    @Min(value = 1, message = "Tổng tiền phải lớn hơn hoặc bằng 1")
    private double total;

    private boolean isReview;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @OneToMany(mappedBy = "order")
    private Set<OrderItem> orderItems = new HashSet<>();


    public String getStatusColor(){
        String color = "badge-secondary";
        if(this.status.equals("Đang giao")){
            color = "badge-warning";
        }else if(this.status.equals("Đã giao")){
            color = "badge-success";
        }else if(this.status.equals("Đã hủy")){
            color = "badge-danger";
        }
        return color;
    }
}
