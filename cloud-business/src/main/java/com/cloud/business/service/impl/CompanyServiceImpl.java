package com.cloud.business.service.impl;

import com.cloud.business.converter.CompanyConverter;
import com.cloud.business.dto.CompanySaveDTO;
import com.cloud.business.dto.EmptyQueryDTO;
import com.cloud.business.entity.CompanyEntity;
import com.cloud.business.mapper.CompanyMapper;
import com.cloud.business.service.CompanyService;
import com.cloud.business.vo.CompanyVO;
import com.cloud.common.base.BaseService;
import com.cloud.common.domain.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl extends BaseService<EmptyQueryDTO, CompanyVO> implements CompanyService {

    private final CompanyMapper companyMapper;
    private final CompanyConverter companyConverter;

    public CompanyServiceImpl(CompanyMapper companyMapper, CompanyConverter companyConverter) {
        this.companyMapper = companyMapper;
        this.companyConverter = companyConverter;
    }

    @Override
    public List<CompanyVO> listAll() {
        return companyMapper.selectAll().stream().map(companyConverter::convert).toList();
    }

    @Override
    public CompanyVO getById(Long id) {
        return companyConverter.convert(companyMapper.selectById(id));
    }

    @Override
    public CompanyVO create(CompanySaveDTO dto) {
        CompanyEntity entity = companyConverter.toNewEntity(dto);
        return companyConverter.convert(companyMapper.insert(entity));
    }

    @Override
    public CompanyVO update(Long id, CompanySaveDTO dto) {
        CompanyEntity entity = companyMapper.selectById(id);
        companyConverter.applyDto(entity, dto);
        return companyConverter.convert(companyMapper.updateById(id, entity));
    }

    @Override
    public void remove(Long id) {
        companyMapper.deleteById(id);
    }

    @Override
    public List<CompanyVO> reset() {
        return companyMapper.resetFromSeed().stream().map(companyConverter::convert).toList();
    }

    @Override
    public List<CompanyVO> list(EmptyQueryDTO dto) {
        return listAll();
    }

    @Override
    public PageResult<CompanyVO> page(EmptyQueryDTO dto) {
        List<CompanyVO> all = listAll();
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
