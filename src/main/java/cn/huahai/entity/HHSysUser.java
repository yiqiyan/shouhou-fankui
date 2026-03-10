package cn.huahai.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("hh_sys_user")
public class HHSysUser {
    @TableId(type = IdType.AUTO)
    private Integer userId;
    private String username;
    private String password;
    private String realName;
    private Integer status;
    private Date createTime;
}