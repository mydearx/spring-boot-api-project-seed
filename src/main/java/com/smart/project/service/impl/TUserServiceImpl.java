package com.smart.project.service.impl;

import com.smart.project.dao.TUserMapper;
import com.smart.project.model.TUser;
import com.smart.project.service.TUserService;
import com.smart.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by mydearx on 2020-04-14 15:38:41.
 */
@Service
@Transactional
public class TUserServiceImpl extends AbstractService<TUser> implements TUserService {
    @Resource
    private TUserMapper tUserMapper;

}
