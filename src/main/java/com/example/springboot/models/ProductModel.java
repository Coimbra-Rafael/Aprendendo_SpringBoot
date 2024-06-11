package com.example.springboot.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_PRODUCT")
public class ProductModel extends RepresentationModel<ProductModel> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idProduct;

    @Column
    private String name;

    @Column
    private BigDecimal value;


}
