package com.cloud.business.service.impl;

import com.cloud.business.converter.AdminOrderConverter;
import com.cloud.business.dto.AdminOrderSaveDTO;
import com.cloud.business.dto.EmptyQueryDTO;
import com.cloud.business.entity.AdminOrderEntity;
import com.cloud.business.mapper.AdminOrderMapper;
import com.cloud.business.service.AdminOrderService;
import com.cloud.business.vo.AdminOrderVO;
import com.cloud.common.base.BaseService;
import com.cloud.common.domain.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminOrderServiceImpl extends BaseService<EmptyQueryDTO, AdminOrderVO> implements AdminOrderService {

    private final AdminOrderMapper adminOrderMapper;
    private final AdminOrderConverter adminOrderConverter;

    public AdminOrderServiceImpl(AdminOrderMapper adminOrderMapper, AdminOrderConverter adminOrderConverter) {
        this.adminOrderMapper = adminOrderMapper;
        this.adminOrderConverter = adminOrderConverter;
    }

    @Override
    public List<AdminOrderVO> listAll() {
        return adminOrderMapper.selectAll().stream().map(adminOrderConverter::convert).toList();
    }

    @Override
    public AdminOrderVO getById(Long id) {
        return adminOrderConverter.convert(adminOrderMapper.selectById(id));
    }

    @Override
    public AdminOrderVO create(AdminOrderSaveDTO dto) {
        AdminOrderEntity entity = adminOrderConverter.toNewEntity(dto);
        return adminOrderConverter.convert(adminOrderMapper.insert(entity));
    }

    @Override
    public AdminOrderVO update(Long id, AdminOrderSaveDTO dto) {
        AdminOrderEntity entity = adminOrderMapper.selectById(id);
        adminOrderConverter.applyDto(entity, dto);
        return adminOrderConverter.convert(adminOrderMapper.updateById(id, entity));
    }

    @Override
    public void remove(Long id) {
        adminOrderMapper.deleteById(id);
    }

    @Override
    public List<AdminOrderVO> reset() {
        return adminOrderMapper.resetFromSeed().stream().map(adminOrderConverter::convert).toList();
    }

    @Override
    public List<AdminOrderVO> list(EmptyQueryDTO dto) {
        return listAll();
    }

    @Override
    public PageResult<AdminOrderVO> page(EmptyQueryDTO dto) {
        List<AdminOrderVO> all = listAll();
        return new PageResult<>(dto.getPageNo(), dto.getPageSize(), all.size(), all);
    }

    @Override
    public Boolean save(EmptyQueryDTO dto) {
        return Boolean.FALSE;
    }

    @Override
    public Boolean update(EmptyQueryDTO dto) {
        return Boolean.FALSE;
    }

    @Override
    public Boolean delete(Long id) {
        remove(id);
        return Boolean.TRUE;
    }
}
