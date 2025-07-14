package com.jose.demoia.product.infrastructure.persistence;

import com.jose.demoia.product.domain.model.Product;
import com.jose.demoia.product.domain.ports.out.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProductRepositoryAdapter implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;

    public ProductRepositoryAdapter(JpaProductRepository jpaProductRepository) {
        this.jpaProductRepository = jpaProductRepository;
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = toEntity(product);
        ProductEntity savedEntity = jpaProductRepository.save(entity);
        return toModel(savedEntity);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpaProductRepository.findById(id)
                .map(this::toModel);
    }

    @Override
    public List<Product> findAll() {
        return jpaProductRepository.findAll().stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaProductRepository.deleteById(id);
    }

    private ProductEntity toEntity(Product product) {
        return new ProductEntity(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice()
        );
    }

    private Product toModel(ProductEntity entity) {
        return new Product(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getPrice()
        );
    }
}
