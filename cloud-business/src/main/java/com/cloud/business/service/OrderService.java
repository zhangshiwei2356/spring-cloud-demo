package com.cloud.business.service;

import com.cloud.business.dto.OrderQueryDTO;
import com.cloud.business.dto.SubmitOrderDTO;
import com.cloud.business.vo.OrderVO;

/**
 * 订单服务。
 */
public interface OrderService {

    OrderVO submitOrder(SubmitOrderDTO dto);
}
