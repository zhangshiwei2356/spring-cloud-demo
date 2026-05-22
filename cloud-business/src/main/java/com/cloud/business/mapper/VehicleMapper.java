package com.cloud.business.mapper;

import com.cloud.business.config.BusinessDataProperties;
import com.cloud.business.entity.VehicleEntity;
import com.cloud.business.mapper.support.AbstractJsonFileMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Repository
public class VehicleMapper extends AbstractJsonFileMapper<VehicleEntity> {

    public VehicleMapper(BusinessDataProperties properties) throws IOException {
        super(
                Path.of(properties.getDataDir()).toAbsolutePath().normalize().resolve("vehicles.json"),
                new TypeReference<List<VehicleEntity>>() { },
                "demo-seed/vehicles.json",
                null);
    }
}
