package com.cloud.business.service.impl;

import com.cloud.business.converter.AdminUserConverter;
import com.cloud.business.dto.AdminUserSaveDTO;
import com.cloud.business.dto.EmptyQueryDTO;
import com.cloud.business.entity.AdminUserEntity;
import com.cloud.business.mapper.AdminUserMapper;
import com.cloud.business.service.AdminUserService;
import com.cloud.business.vo.AdminUserVO;
import com.cloud.common.base.BaseService;
import com.cloud.common.domain.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserServiceImpl extends BaseService<EmptyQueryDTO, AdminUserVO> implements AdminUserService {

    private final AdminUserMapper adminUserMapper;
    private final AdminUserConverter adminUserConverter;

    public AdminUserServiceImpl(AdminUserMapper adminUserMapper, AdminUserConverter adminUserConverter) {
        this.adminUserMapper = adminUserMapper;
        this.adminUserConverter = adminUserConverter;
    }

    @Override
    public List<AdminUserVO> listAll() {
        return adminUserMapper.selectAll().stream().map(adminUserConverter::convert).toList();
    }

    @Override
    public AdminUserVO getById(Long id) {
        return adminUserConverter.convert(adminUserMapper.selectById(id));
    }

    @Override
    public AdminUserVO create(AdminUserSaveDTO dto) {
        AdminUserEntity entity = adminUserConverter.toNewEntity(dto);
        return adminUserConverter.convert(adminUserMapper.insert(entity));
    }

    @Override
    public AdminUserVO update(Long id, AdminUserSaveDTO dto) {
        AdminUserEntity entity = adminUserMapper.selectById(id);
        adminUserConverter.applyDto(entity, dto);
        return adminUserConverter.convert(adminUserMapper.updateById(id, entity));
    }

    @Override
    public void remove(Long id) {
        adminUserMapper.deleteById(id);
    }

    @Override
    public List<AdminUserVO> reset() {
        return adminUserMapper.resetFromSeed().stream().map(adminUserConverter::convert).toList();
    }

    @Override
    public List<AdminUserVO> list(EmptyQueryDTO dto) {
        return listAll();
    }

    @Override
    public PageResult<AdminUserVO> page(EmptyQueryDTO dto) {
        List<AdminUserVO> all = listAll();
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
