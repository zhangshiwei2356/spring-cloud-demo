package com.cloud.business.demo;

import com.cloud.business.demo.entity.CompanyRecord;
import com.cloud.business.demo.entity.DemoOrderRecord;
import com.cloud.business.demo.entity.DemoUserRecord;
import com.cloud.business.demo.entity.DepartmentRecord;
import com.cloud.business.demo.entity.ProductRecord;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class DemoDataRegistry {

    private final Map<String, DemoCrudService<?>> services = new LinkedHashMap<>();
    private final Path rootDir;

    public DemoDataRegistry(DemoDataProperties properties) throws IOException {
        this.rootDir = Path.of(properties.getDataDir()).toAbsolutePath().normalize();
        services.put("companies", new DemoCrudService<>(
                rootDir.resolve("companies.json"),
                new TypeReference<List<CompanyRecord>>() { },
                "demo-seed/companies.json",
                (CompanyRecord r) -> {
                    if (DemoCrudService.isBlank(r.getCreateTime())) {
                        r.setCreateTime(DemoCrudService.now());
                    }
                }));
        services.put("departments", new DemoCrudService<>(
                rootDir.resolve("departments.json"),
                new TypeReference<List<DepartmentRecord>>() { },
                "demo-seed/departments.json",
                null));
        services.put("users", new DemoCrudService<>(
                rootDir.resolve("users.json"),
                new TypeReference<List<DemoUserRecord>>() { },
                "demo-seed/users.json",
                null));
        services.put("products", new DemoCrudService<>(
                rootDir.resolve("products.json"),
                new TypeReference<List<ProductRecord>>() { },
                "demo-seed/products.json",
                null));
        services.put("orders", new DemoCrudService<>(
                rootDir.resolve("orders.json"),
                new TypeReference<List<DemoOrderRecord>>() { },
                "demo-seed/orders.json",
                (DemoOrderRecord r) -> {
                    if (DemoCrudService.isBlank(r.getOrderNo())) {
                        r.setOrderNo("ORD" + System.currentTimeMillis());
                    }
                    if (DemoCrudService.isBlank(r.getCreateTime())) {
                        r.setCreateTime(DemoCrudService.now());
                    }
                }));
    }

    public String rootDirPath() {
        return rootDir.toString();
    }

    @SuppressWarnings("unchecked")
    public <T extends Identifiable> DemoCrudService<T> get(String resource) {
        DemoCrudService<?> service = services.get(resource);
        if (service == null) {
            throw new IllegalArgumentException("未知资源: " + resource);
        }
        return (DemoCrudService<T>) service;
    }

    public boolean contains(String resource) {
        return services.containsKey(resource);
    }
}
