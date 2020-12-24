package com.shop.manage.system.controller;

import com.alibaba.fastjson.JSON;
import com.shop.manage.system.contant.ProjectContant;
import com.shop.manage.system.entity.TUser;
import com.shop.manage.system.exception.CustomException;
import com.shop.manage.system.service.TUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Mr.joey
 */
@Api(tags = "TUserController测试")
@RestController
@RequestMapping("/user")
public class TUserController {

    private static final Logger logger = LoggerFactory.getLogger(TUserController.class);

    @Autowired
    TUserService tUserService;

    @ApiOperation(value = "多数据源获取测试",notes = "测试test")
    @GetMapping("/test")
    public List<TUser> test(){

        //数据源一信息
        System.out.println(JSON.toJSONString(tUserService.getBaseDbAllUser()));

        //数据源二信息
        System.out.println(JSON.toJSONString(tUserService.getBackDbAllUser()));

        return tUserService.getBaseDbAllUser();
    }


    @GetMapping("/test1")
    public TUser test1(){

        //数据源一信息
        System.out.println(JSON.toJSONString(tUserService.getBaseDbAllUser()));

        //数据源二信息
        System.out.println(JSON.toJSONString(tUserService.getBackDbAllUser()));

        return tUserService.getBaseDbAllUser().get(0);
    }

    @GetMapping("/test2")
    public TUser test2() throws CustomException {
        //数据源一信息
        System.out.println(JSON.toJSONString(tUserService.getBaseDbAllUser().get(0)));

        //数据源二信息
        System.out.println(JSON.toJSONString(tUserService.getBackDbAllUser().get(0)));

        try {
            int i = 1/0;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new CustomException(ProjectContant.ERROR_500,e.getMessage());
        }


        return tUserService.getBaseDbAllUser().get(0);
    }


    @ApiOperation(value = "测试传参id",notes = "测试test3")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "记录id", dataType = "String", defaultValue = "")
    })
    @GetMapping("/test3")
    public List<TUser> test3(@RequestParam(value = "id",defaultValue = "")String id){

        return tUserService.getBaseDbAllUser();
    }


    @ApiOperation(value = "测试传参body",notes = "测试test5")
    @PostMapping("/test4")
    public List<TUser> test4(@RequestBody TUser tUser){

        return tUserService.getBaseDbAllUser();
    }
}
