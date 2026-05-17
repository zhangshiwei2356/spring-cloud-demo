package com.cloud.business.mapper;

import com.cloud.business.config.BusinessDataProperties;
import com.cloud.business.entity.ProductEntity;
import com.cloud.business.mapper.support.AbstractJsonFileMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Repository
public class ProductMapper extends AbstractJsonFileMapper<ProductEntity> {

    public ProductMapper(BusinessDataProperties properties) throws IOException {
        super(
                Path.of(properties.getDataDir()).toAbsolutePath().normalize().resolve("products.json"),
                new TypeReference<List<ProductEntity>>() { },
                "demo-seed/products.json",
                null);
    }
}
