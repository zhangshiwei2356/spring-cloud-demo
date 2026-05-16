package com.cloud.business.converter;

import com.cloud.business.dto.SubmitOrderDTO;
import com.cloud.business.entity.OrderEntity;
import com.cloud.business.feign.dto.ProductRemoteVO;
import com.cloud.business.vo.OrderVO;
import com.cloud.common.base.BaseConverter;
import com.cloud.common.util.DateUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Component
public class OrderConverter extends BaseConverter<OrderEntity, OrderVO> {

    @Override
    public OrderVO convert(OrderEntity source) {
        if (source == null) {
            return null;
        }
        OrderVO vo = new OrderVO();
        vo.setOrderId(source.getOrderId());
        vo.setOrderNo(source.getOrderNo());
        vo.setBuyerUserId(source.getBuyerUserId());
        vo.setProductCode(source.getProductCode());
        vo.setQuantity(source.getQuantity());
        vo.setTotalAmount(source.getTotalAmount());
        vo.setStatus(source.getStatus());
        vo.setCreateTimeDisplay(DateUtils.format(source.getCreateTime()));
        return vo;
    }

    public OrderEntity toNewEntity(String orderNo, SubmitOrderDTO dto, ProductRemoteVO pricing,
                                   int orderStatus, BigDecimal totalAmount) {
        LocalDateTime now = LocalDateTime.now();
        OrderEntity entity = new OrderEntity();
        entity.setOrderNo(orderNo);
        entity.setBuyerUserId(dto.getBuyerUserId());
        entity.setProductCode(dto.getProductCode().trim());
        entity.setQuantity(dto.getQuantity());
        entity.setTotalAmount(totalAmount.setScale(2, RoundingMode.HALF_UP));
        entity.setStatus(orderStatus);
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        return entity;
    }

    /** 明细金额计算：单价 * 数量（四舍五入） */
    public BigDecimal calculateTotal(ProductRemoteVO product, int quantity) {
        return product.getUnitPrice()
                .multiply(BigDecimal.valueOf(quantity))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
