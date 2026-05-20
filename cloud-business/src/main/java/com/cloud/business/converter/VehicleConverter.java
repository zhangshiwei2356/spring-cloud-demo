package com.cloud.business.converter;

import com.cloud.business.entity.VehicleEntity;
import com.cloud.business.vo.VehicleVO;
import com.cloud.common.base.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class VehicleConverter extends BaseConverter<VehicleEntity, VehicleVO> {

    @Override
    public VehicleVO convert(VehicleEntity source) {
        if (source == null) {
            return null;
        }
        VehicleVO vo = new VehicleVO();
        vo.setId(source.getId());
        vo.setCode(source.getCode());
        vo.setName(source.getName());
        vo.setBrand(source.getBrand());
        vo.setModel(source.getModel());
        vo.setPrice(source.getPrice());
        vo.setColor(source.getColor());
        vo.setFuelType(source.getFuelType());
        vo.setStock(source.getStock());
        vo.setStatus(source.getStatus());
        vo.setDescription(source.getDescription());
        return vo;
    }
}
