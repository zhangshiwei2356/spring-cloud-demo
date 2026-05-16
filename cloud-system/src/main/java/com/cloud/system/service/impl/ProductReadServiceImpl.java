package com.cloud.system.service.impl;

import com.cloud.common.base.BaseService;
import com.cloud.common.domain.PageResult;
import com.cloud.common.exception.GlobalException;
import com.cloud.system.dto.ProductPriceQueryDTO;
import com.cloud.system.service.ProductReadService;
import com.cloud.system.vo.ProductPriceVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 演示商品价格表。
 */
@Service
public class ProductReadServiceImpl extends BaseService<ProductPriceQueryDTO, ProductPriceVO> implements ProductReadService {

    private static final Map<String, BigDecimal> DEMO_PRICE = new HashMap<>();

    static {
        DEMO_PRICE.put("SKU-PHONE", new BigDecimal("3999.00"));
        DEMO_PRICE.put("SKU-BOOK", new BigDecimal("59.90"));
        DEMO_PRICE.put("SKU-DEMO", new BigDecimal("9.99"));
    }

    @Override
    public ProductPriceVO getPrice(ProductPriceQueryDTO dto) {
        if (dto == null || dto.getProductCode() == null) {
            throw new GlobalException("商品编码不能为空");
        }
        BigDecimal price = DEMO_PRICE.get(dto.getProductCode().trim());
        if (price == null) {
            throw new GlobalException("商品不存在或未定价");
        }
        ProductPriceVO vo = new ProductPriceVO();
        vo.setProductCode(dto.getProductCode().trim());
        vo.setUnitPrice(price);
        return vo;
    }

    @Override
    public List<ProductPriceVO> list(ProductPriceQueryDTO dto) {
        return Collections.emptyList();
    }

    @Override
    public PageResult<ProductPriceVO> page(ProductPriceQueryDTO dto) {
        return new PageResult<>(1, 10, 0, Collections.emptyList());
    }

    @Override
    public Boolean save(ProductPriceQueryDTO dto) {
        return Boolean.FALSE;
    }

    @Override
    public Boolean update(ProductPriceQueryDTO dto) {
        return Boolean.FALSE;
    }

    @Override
    public Boolean delete(Long id) {
        return Boolean.FALSE;
    }
}
