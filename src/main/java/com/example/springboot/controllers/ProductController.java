package com.example.springboot.controllers;

import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@RestController
@RequestMapping("/v1/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<ProductModel> sabeProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
    }

    @GetMapping
    public ResponseEntity<List<ProductModel>> getAllProducts() {
        List<ProductModel> productList =  this.productRepository.findAll();
        if(!productList.isEmpty()) {
            for(ProductModel product : productList) {
                product.add(linkTo(methodOn(ProductController.class).getOneProduct(product.getIdProduct().toString())).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Object> getOneProduct(@PathVariable("productId") String productId) {
        Optional<ProductModel> productO = this.productRepository.findById(UUID.fromString(productId));
        if(productO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        productO.get().add(linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(productO.get());
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Object> updateProduct (@PathVariable("productId") String productId,
                                              @RequestBody @Valid ProductRecordDto productRecordDto) {

        Optional<ProductModel> productO = this.productRepository.findById(UUID.fromString(productId));
        if(productO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        var productModel = productO.get();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
    }

    @DeleteMapping("/productId")
    public ResponseEntity<Object> deleteProduct(@PathVariable("productId") String productId) {
        Optional<ProductModel> productO = this.productRepository.findById(UUID.fromString(productId));
        if(productO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        this.productRepository.delete(productO.get());
        return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully.");
    }
}
