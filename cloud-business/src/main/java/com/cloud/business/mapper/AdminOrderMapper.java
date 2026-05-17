package com.cloud.business.mapper;

import com.cloud.business.config.BusinessDataProperties;
import com.cloud.business.entity.AdminOrderEntity;
import com.cloud.business.mapper.support.AbstractJsonFileMapper;
import com.cloud.common.util.DateUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Repository
public class AdminOrderMapper extends AbstractJsonFileMapper<AdminOrderEntity> {

    public AdminOrderMapper(BusinessDataProperties properties) throws IOException {
        super(
                Path.of(properties.getDataDir()).toAbsolutePath().normalize().resolve("orders.json"),
                new TypeReference<List<AdminOrderEntity>>() { },
                "demo-seed/orders.json",
                entity -> {
                    if (isBlank(entity.getOrderNo())) {
                        entity.setOrderNo("ORD" + System.currentTimeMillis());
                    }
                    if (isBlank(entity.getCreateTime())) {
                        entity.setCreateTime(DateUtils.formatNow());
                    }
                });
    }
}
