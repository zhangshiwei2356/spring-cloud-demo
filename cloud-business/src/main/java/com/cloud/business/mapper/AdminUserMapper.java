package com.cloud.business.mapper;

import com.cloud.business.config.BusinessDataProperties;
import com.cloud.business.entity.AdminUserEntity;
import com.cloud.business.mapper.support.AbstractJsonFileMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Repository
public class AdminUserMapper extends AbstractJsonFileMapper<AdminUserEntity> {

    public AdminUserMapper(BusinessDataProperties properties) throws IOException {
        super(
                Path.of(properties.getDataDir()).toAbsolutePath().normalize().resolve("users.json"),
                new TypeReference<List<AdminUserEntity>>() { },
                "demo-seed/users.json",
                null);
    }
}
