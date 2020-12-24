package com.shop.manage.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Mr.joey
 */
@ApiModel(value="TUser",description = "t_user 表对应")
@TableName("t_user")
public class TUser implements Serializable {

    @ApiModelProperty(value = "主键",required = true,notes = "必填")
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField("login_name")
    private String loginName;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
