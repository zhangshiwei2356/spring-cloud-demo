package com.cloud.business.service;

import com.cloud.business.dto.VehiclePurchaseDTO;
import com.cloud.business.vo.VehiclePurchaseResultVO;

public interface VehiclePurchaseService {

    VehiclePurchaseResultVO purchase(Long vehicleId, VehiclePurchaseDTO dto);
}
