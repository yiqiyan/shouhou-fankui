// cn.huahai.entity.HHSysMenu.java
package cn.huahai.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName("hh_sys_menu")
public class HHSysMenu {
    @TableId(type = IdType.AUTO)
    private Integer menuId;
    private String menuName;
    private Integer parentId;
    private String path;
    private String permission;
    private Integer sort;
    private Integer status;
    private Date createTime;

    @TableField(exist = false)
    private List<HHSysMenu> children;
}