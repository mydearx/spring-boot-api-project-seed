package com.smart.project.web;

import com.smart.project.annotation.Log;
import com.smart.project.core.Result;
import com.smart.project.core.ResultGenerator;
import com.smart.project.model.TUser;
import com.smart.project.service.TUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by mydearx on 2020-04-14 15:38:41.
*/
@RestController
@RequestMapping("/user")
public class TUserController {
    @Resource
    private TUserService tUserService;

    @PostMapping("/add")
    public Result add(TUser tUser) {
        tUserService.save(tUser);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        tUserService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(TUser tUser) {
        tUserService.update(tUser);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    @Log(operateType = "1", console = true, description = "哈哈哈")
    public Result detail(@RequestParam Integer id) {
        TUser tUser = tUserService.findById(id);
        return ResultGenerator.genSuccessResult(tUser);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<TUser> list = tUserService.findAll();
        PageInfo<TUser> pageInfo = new PageInfo<TUser>(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
