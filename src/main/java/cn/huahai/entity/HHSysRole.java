package cn.huahai.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.Date;

@Data
@TableName("hh_sys_role")
public class HHSysRole {
    @TableId(type = IdType.AUTO)
    private Integer roleId;
    private String roleName;
    private String roleCode;

    // 关键修复：指定状态字段，禁用默认的enable
    @TableField("status") // 明确映射数据库的status字段
    private Integer status;

    private Date createTime;
}

//// cn.huahai.entity.HHSysRole.java
//package cn.huahai.entity;
//
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableName;
//import lombok.Data;
//
//import java.util.Date;
//
//@Data
//@TableName("hh_sys_role")
//public class HHSysRole {
//    @TableId(type = IdType.AUTO)
//    private Integer roleId;
//    private String roleName;
//    private String roleCode;
//    private Integer status;
//    private Date createTime;
//}