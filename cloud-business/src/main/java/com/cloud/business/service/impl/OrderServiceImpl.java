package com.cloud.business.service.impl;

import com.cloud.business.common.enums.OrderStatusEnum;
import com.cloud.business.converter.OrderConverter;
import com.cloud.business.dto.OrderQueryDTO;
import com.cloud.business.dto.SubmitOrderDTO;
import com.cloud.business.entity.OrderEntity;
import com.cloud.business.feign.dto.UserRemoteVO;
import com.cloud.business.handler.OrderHandler;
import com.cloud.business.persistence.OrderPersistence;
import com.cloud.business.service.OrderAsyncNotifyService;
import com.cloud.business.service.OrderService;
import com.cloud.business.vo.OrderIntegrationResult;
import com.cloud.business.vo.OrderVO;
import com.cloud.common.base.BaseService;
import com.cloud.common.constant.MdcKeys;
import com.cloud.common.context.UserContext;
import com.cloud.common.domain.PageResult;
import com.cloud.common.exception.GlobalException;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 下单主流程：Service → Converter + Handler → 持久化（演示为内存）。
 */
@Service
public class OrderServiceImpl extends BaseService<OrderQueryDTO, OrderVO> implements OrderService {

    private final OrderHandler orderHandler;
    private final OrderConverter orderConverter;
    private final OrderPersistence orderPersistence;
    private final OrderAsyncNotifyService orderAsyncNotifyService;

    public OrderServiceImpl(OrderHandler orderHandler,
                            OrderConverter orderConverter,
                            OrderPersistence orderPersistence,
                            OrderAsyncNotifyService orderAsyncNotifyService) {
        this.orderHandler = orderHandler;
        this.orderConverter = orderConverter;
        this.orderPersistence = orderPersistence;
        this.orderAsyncNotifyService = orderAsyncNotifyService;
    }

    @Override
    public OrderVO submitOrder(SubmitOrderDTO dto) {
        Long ctxUid = UserContext.getUid();
        if (ctxUid == null) {
            throw new GlobalException("请先登录后再下单");
        }
        if (!ctxUid.equals(dto.getBuyerUserId())) {
            throw new GlobalException("登录用户与下单用户不一致");
        }

        OrderIntegrationResult remoteBundle = orderHandler.execute(dto);
        UserRemoteVO user = remoteBundle.getUser();
        if (user == null || !user.getUserId().equals(dto.getBuyerUserId())) {
            throw new GlobalException("用户信息校验失败");
        }

        java.math.BigDecimal total =
                orderConverter.calculateTotal(remoteBundle.getProduct(), dto.getQuantity());

        String orderNo = genOrderNo();
        OrderEntity entity = orderConverter.toNewEntity(orderNo, dto, remoteBundle.getProduct(),
                OrderStatusEnum.CREATED.getCode(), total);
        orderPersistence.insert(entity);

        String traceId = MDC.get(MdcKeys.TRACE_ID);
        orderAsyncNotifyService.notifyOrderCreated(orderNo, dto.getBuyerUserId(), traceId);
        return orderConverter.convert(entity);
    }

    private static String genOrderNo() {
        return "ORD" + System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(100, 999);
    }

    @Override
    public List<OrderVO> list(OrderQueryDTO dto) {
        return Collections.emptyList();
    }

    @Override
    public PageResult<OrderVO> page(OrderQueryDTO dto) {
        // 演示环境占位：生产应走 MyBatis-Plus 分页并补充 jsqlparser 依赖
        return new PageResult<>(dto.getPageNo(), dto.getPageSize(), 0, Collections.emptyList());
    }

    @Override
    public Boolean save(OrderQueryDTO dto) {
        return Boolean.FALSE;
    }

    @Override
    public Boolean update(OrderQueryDTO dto) {
        return Boolean.FALSE;
    }

    @Override
    public Boolean delete(Long id) {
        return id != null && orderPersistence.deleteById(id) > 0;
    }
}
