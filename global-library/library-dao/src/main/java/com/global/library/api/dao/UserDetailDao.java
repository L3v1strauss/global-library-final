package com.global.library.api.dao;

import com.global.library.entity.UserDetail;
import org.springframework.stereotype.Repository;

@Repository
public class UserDetailDao extends AGenericDao<UserDetail> implements IUserDetailDao{
    public UserDetailDao() {
        super(UserDetail.class);
    }
}
