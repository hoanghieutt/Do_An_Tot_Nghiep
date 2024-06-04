package com.poly.datn.sd18.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "product_details")
public class ProductDetail extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "cost")
    private Float cost; // TODO giá nhập

    @Column(name = "price")
    private Float price; // TODO giá bán

    @Column(name = "weight")
    private Float weight; // TODO khối lượng của áo

    @Column(name = "status")
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "size_id", referencedColumnName = "id")
    private Size size;

    @ManyToOne
    @JoinColumn(name = "color_id", referencedColumnName = "id")
    private Color color;

    @JsonIgnore
    @OneToMany(mappedBy = "productDetail")
    private List<Evaluate> evaluates;

    @JsonIgnore
    @OneToMany(mappedBy = "productDetail")
    private List<CartDetail> cartDetails;
}
