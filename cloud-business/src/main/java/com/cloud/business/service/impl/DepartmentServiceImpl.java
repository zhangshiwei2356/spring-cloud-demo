package com.cloud.business.service.impl;

import com.cloud.business.converter.DepartmentConverter;
import com.cloud.business.dto.DepartmentSaveDTO;
import com.cloud.business.dto.EmptyQueryDTO;
import com.cloud.business.entity.DepartmentEntity;
import com.cloud.business.mapper.DepartmentMapper;
import com.cloud.business.service.DepartmentService;
import com.cloud.business.vo.DepartmentVO;
import com.cloud.common.base.BaseService;
import com.cloud.common.domain.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl extends BaseService<EmptyQueryDTO, DepartmentVO> implements DepartmentService {

    private final DepartmentMapper departmentMapper;
    private final DepartmentConverter departmentConverter;

    public DepartmentServiceImpl(DepartmentMapper departmentMapper, DepartmentConverter departmentConverter) {
        this.departmentMapper = departmentMapper;
        this.departmentConverter = departmentConverter;
    }

    @Override
    public List<DepartmentVO> listAll() {
        return departmentMapper.selectAll().stream().map(departmentConverter::convert).toList();
    }

    @Override
    public DepartmentVO getById(Long id) {
        return departmentConverter.convert(departmentMapper.selectById(id));
    }

    @Override
    public DepartmentVO create(DepartmentSaveDTO dto) {
        DepartmentEntity entity = departmentConverter.toNewEntity(dto);
        return departmentConverter.convert(departmentMapper.insert(entity));
    }

    @Override
    public DepartmentVO update(Long id, DepartmentSaveDTO dto) {
        DepartmentEntity entity = departmentMapper.selectById(id);
        departmentConverter.applyDto(entity, dto);
        return departmentConverter.convert(departmentMapper.updateById(id, entity));
    }

    @Override
    public void remove(Long id) {
        departmentMapper.deleteById(id);
    }

    @Override
    public List<DepartmentVO> reset() {
        return departmentMapper.resetFromSeed().stream().map(departmentConverter::convert).toList();
    }

    @Override
    public List<DepartmentVO> list(EmptyQueryDTO dto) {
        return listAll();
    }

    @Override
    public PageResult<DepartmentVO> page(EmptyQueryDTO dto) {
        List<DepartmentVO> all = listAll();
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
