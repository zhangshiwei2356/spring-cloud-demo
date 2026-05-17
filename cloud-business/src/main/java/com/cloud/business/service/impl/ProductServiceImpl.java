package com.cloud.business.service.impl;

import com.cloud.business.converter.ProductConverter;
import com.cloud.business.dto.EmptyQueryDTO;
import com.cloud.business.dto.ProductSaveDTO;
import com.cloud.business.entity.ProductEntity;
import com.cloud.business.mapper.ProductMapper;
import com.cloud.business.service.ProductService;
import com.cloud.business.vo.ProductVO;
import com.cloud.common.base.BaseService;
import com.cloud.common.domain.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl extends BaseService<EmptyQueryDTO, ProductVO> implements ProductService {

    private final ProductMapper productMapper;
    private final ProductConverter productConverter;

    public ProductServiceImpl(ProductMapper productMapper, ProductConverter productConverter) {
        this.productMapper = productMapper;
        this.productConverter = productConverter;
    }

    @Override
    public List<ProductVO> listAll() {
        return productMapper.selectAll().stream().map(productConverter::convert).toList();
    }

    @Override
    public ProductVO getById(Long id) {
        return productConverter.convert(productMapper.selectById(id));
    }

    @Override
    public ProductVO create(ProductSaveDTO dto) {
        ProductEntity entity = productConverter.toNewEntity(dto);
        return productConverter.convert(productMapper.insert(entity));
    }

    @Override
    public ProductVO update(Long id, ProductSaveDTO dto) {
        ProductEntity entity = productMapper.selectById(id);
        productConverter.applyDto(entity, dto);
        return productConverter.convert(productMapper.updateById(id, entity));
    }

    @Override
    public void remove(Long id) {
        productMapper.deleteById(id);
    }

    @Override
    public List<ProductVO> reset() {
        return productMapper.resetFromSeed().stream().map(productConverter::convert).toList();
    }

    @Override
    public List<ProductVO> list(EmptyQueryDTO dto) {
        return listAll();
    }

    @Override
    public PageResult<ProductVO> page(EmptyQueryDTO dto) {
        List<ProductVO> all = listAll();
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
