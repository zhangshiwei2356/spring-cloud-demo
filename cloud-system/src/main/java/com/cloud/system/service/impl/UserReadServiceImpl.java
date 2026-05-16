package com.cloud.system.service.impl;

import com.cloud.common.base.BaseService;
import com.cloud.common.domain.PageResult;
import com.cloud.common.exception.GlobalException;
import com.cloud.system.dto.UserQueryDTO;
import com.cloud.system.service.UserReadService;
import com.cloud.system.vo.UserVO;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 演示内存用户表。
 */
@Service
public class UserReadServiceImpl extends BaseService<UserQueryDTO, UserVO> implements UserReadService {

    private static final Map<Long, UserVO> DEMO_USERS = new HashMap<>();

    static {
        DEMO_USERS.put(1L, new UserVO(1L, "演示用户一号"));
        DEMO_USERS.put(2L, new UserVO(2L, "演示用户二号"));
        DEMO_USERS.put(10086L, new UserVO(10086L, "演示用户10086"));
    }

    @Override
    public UserVO getByUserId(Long userId) {
        if (userId == null) {
            throw new GlobalException("用户不存在");
        }
        UserVO u = DEMO_USERS.get(userId);
        if (u == null) {
            throw new GlobalException("用户不存在");
        }
        return new UserVO(u.getUserId(), u.getUserName());
    }

    @Override
    public List<UserVO> list(UserQueryDTO dto) {
        return Collections.emptyList();
    }

    @Override
    public PageResult<UserVO> page(UserQueryDTO dto) {
        return new PageResult<>(1, 10, 0, Collections.emptyList());
    }

    @Override
    public Boolean save(UserQueryDTO dto) {
        return Boolean.FALSE;
    }

    @Override
    public Boolean update(UserQueryDTO dto) {
        return Boolean.FALSE;
    }

    @Override
    public Boolean delete(Long id) {
        return Boolean.FALSE;
    }
}
