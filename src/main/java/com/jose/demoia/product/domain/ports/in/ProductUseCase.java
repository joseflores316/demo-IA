package com.jose.demoia.product.domain.ports.in;

import com.jose.demoia.product.domain.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductUseCase {
    Product createProduct(Product product);
    Optional<Product> getProduct(Long id);
    List<Product> getAllProducts();
    Product updateProduct(Product product);
    void deleteProduct(Long id);
}
