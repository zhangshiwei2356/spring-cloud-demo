package com.cloud.business.service;

import com.cloud.business.vo.VehicleVO;

import java.util.List;

public interface VehicleService {

    List<VehicleVO> listOnSale(String keyword);

    VehicleVO getById(Long id);
}
