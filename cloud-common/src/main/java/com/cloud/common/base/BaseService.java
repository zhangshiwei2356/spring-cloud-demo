package com.cloud.common.base;

import com.cloud.common.domain.PageResult;

import java.util.List;

/**
 * Service 抽象基类：定义通用 CRUD 骨架，由业务实现类按需实现。
 *
 * @param <D> 业务入参 DTO 类型
 * @param <V> 业务出参 VO 类型
 */
public abstract class BaseService<D, V> {

    /**
     * 列表查询
     */
    public abstract List<V> list(D dto);

    /**
     * 分页查询
     */
    public abstract PageResult<V> page(D dto);

    /**
     * 新增
     */
    public abstract Boolean save(D dto);

    /**
     * 编辑（按约定从 dto 中取主键）
     */
    public abstract Boolean update(D dto);

    /**
     * 按主键删除
     */
    public abstract Boolean delete(Long id);
}
