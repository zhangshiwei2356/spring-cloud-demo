package com.cloud.business.service;

import com.cloud.business.dto.ProductSaveDTO;
import com.cloud.business.vo.ProductVO;

import java.util.List;

public interface ProductService {

    List<ProductVO> listAll();

    ProductVO getById(Long id);

    ProductVO create(ProductSaveDTO dto);

    ProductVO update(Long id, ProductSaveDTO dto);

    void remove(Long id);

    List<ProductVO> reset();
}
