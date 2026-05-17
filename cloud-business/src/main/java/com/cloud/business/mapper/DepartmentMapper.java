package com.cloud.business.mapper;

import com.cloud.business.config.BusinessDataProperties;
import com.cloud.business.entity.DepartmentEntity;
import com.cloud.business.mapper.support.AbstractJsonFileMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Repository
public class DepartmentMapper extends AbstractJsonFileMapper<DepartmentEntity> {

    public DepartmentMapper(BusinessDataProperties properties) throws IOException {
        super(
                Path.of(properties.getDataDir()).toAbsolutePath().normalize().resolve("departments.json"),
                new TypeReference<List<DepartmentEntity>>() { },
                "demo-seed/departments.json",
                null);
    }
}
