// cn.huahai.entity.HHSysRoleMenu.java
package cn.huahai.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("hh_sys_role_menu")
public class HHSysRoleMenu {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer roleId;
    private Integer menuId;
    private Date createTime;
}