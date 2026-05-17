package com.cloud.business.service;

import com.cloud.business.dto.AdminOrderSaveDTO;
import com.cloud.business.vo.AdminOrderVO;

import java.util.List;

public interface AdminOrderService {

    List<AdminOrderVO> listAll();

    AdminOrderVO getById(Long id);

    AdminOrderVO create(AdminOrderSaveDTO dto);

    AdminOrderVO update(Long id, AdminOrderSaveDTO dto);

    void remove(Long id);

    List<AdminOrderVO> reset();
}
