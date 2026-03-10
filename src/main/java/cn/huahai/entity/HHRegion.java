package cn.huahai.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 地址实体类（100% 对齐数据库表 base_area_3 字段）
 */
@Data
@TableName("base_area_3") // 表名必须完全一致
public class HHRegion {

    /**
     * 地址编码（主键）
     */
    @TableId("NODE_CODE")
    private String nodeCode;

    /**
     * 地区名称
     */
    @TableField("NODE_NAME")
    private String nodeName;

    /**
     * 地区全称
     */
    @TableField("NODE_SNAME")
    private String nodeSname;

    /**
     * 父编码
     */
    @TableField("CODE_PARENT")
    private String codeParent;

    /**
     * 默认值(预留)
     */
    @TableField("NODE_INITIALITION")
    private String nodeInitialition;

    /**
     * 首字母
     */
    @TableField("NODE_SPELL")
    private String nodeSpell;

    /**
     * 类型: 1是省会, 2直辖市, 3港澳台, 4其它
     */
    @TableField("NODE_TYPE")
    private BigDecimal nodeType;

    /**
     * 同级下排序
     */
    @TableField("NODER_ORDER")
    private BigDecimal noderOrder;

    /**
     * 0全国、1省、2市区、3郊县、4街道、5居委会
     */
    @TableField("NODE_LEVEL")
    private BigDecimal nodeLevel;

    /**
     * 备注
     */
    @TableField("NODE_REMARK")
    private String nodeRemark;

    /**
     * 城乡分类代码
     */
    @TableField("VILLAGE_TYPE")
    private String villageType;

    /**
     * 所属国家名
     */
    @TableField("NATION_NAME")
    private String nationName;

    /**
     * 所属省名称
     */
    @TableField("PROVINCE_NAME")
    private String provinceName;

    /**
     * 所属市名称
     */
    @TableField("CITY_NAME")
    private String cityName;

    /**
     * 所属区县名称
     */
    @TableField("COUNTY_NAME")
    private String countyName;

    /**
     * 所属街道名称
     */
    @TableField("TOWN_NAME")
    private String townName;

    /**
     * 经度
     */
    @TableField("LNG")
    private String lng;

    /**
     * 纬度
     */
    @TableField("LAT")
    private String lat;

    /**
     * 来源地图, 百度1,高德2
     */
    @TableField("MAPTYPE")
    private BigDecimal maptype;
}

//package cn.huahai.entity;
//
//import com.baomidou.mybatisplus.annotation.TableField;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableName;
//import lombok.Data;
//import java.math.BigDecimal;
//
///**
// * 省市区地址表实体类（修正字段映射）
// */
//@Data
//@TableName("BASE_AREA_3") // 和数据库表名一致
//public class HHRegion {
//
//    /**
//     * 地址编码（主键）
//     */
//    @TableId("NODE_CODE")
//    private String nodeCode;
//
//    /**
//     * 地址名称（如：北京市、朝阳区）
//     */
//    @TableField("NODE_NAME")
//    private String nodeName;
//
//    /**
//     * 地区全称
//     */
//    @TableField("NODE_SNAME")
//    private String nodeSname;
//
//    /**
//     * 父级编码
//     */
//    @TableField("CODE_PARENT")
//    private String codeParent;
//
//    /**
//     * 默认值(预留)
//     */
//    @TableField("NODE_INITIALITION")
//    private String nodeInitialition;
//
//    /**
//     * 首字母
//     */
//    @TableField("NODE_SPELL")
//    private String nodeSpell;
//
//    /**
//     * 类型：1是省会，2直辖市,3港澳台,4其它
//     */
//    @TableField("NODE_TYPE")
//    private BigDecimal nodeType;
//
//    /**
//     * 同级下排序（关键修正：NODER_ORDER，不是NODE_ORDER）
//     */
//    @TableField("NODER_ORDER")
//    private BigDecimal noderOrder;
//
//    /**
//     * 层级：0全国、1省、2市区、3郊县、4街道、5居委会
//     */
//    @TableField("NODE_LEVEL")
//    private BigDecimal nodeLevel;
//
//    /**
//     * 备注
//     */
//    @TableField("NODE_REMARK")
//    private String nodeRemark;
//
//    /**
//     * 城乡分类代码
//     */
//    @TableField("VILLAGE_TYPE")
//    private String villageType;
//
//    /**
//     * 所属国家名
//     */
//    @TableField("NATION_NAME")
//    private String nationName;
//
//    /**
//     * 省份名称（冗余字段）
//     */
//    @TableField("PROVINCE_NAME")
//    private String provinceName;
//
//    /**
//     * 城市名称（冗余字段）
//     */
//    @TableField("CITY_NAME")
//    private String cityName;
//
//    /**
//     * 区县名称（冗余字段）
//     */
//    @TableField("COUNTY_NAME")
//    private String countyName;
//
//    /**
//     * 所属街道名称
//     */
//    @TableField("TOWN_NAME")
//    private String townName;
//
//    /**
//     * 经度（关键修正：LNG，不是LONGITUDE）
//     */
//    @TableField("LNG")
//    private String lng;
//
//    /**
//     * 纬度（关键修正：LAT，不是LATITUDE）
//     */
//    @TableField("LAT")
//    private String lat;
//
//    /**
//     * 来源地图，百度1,高德2
//     */
//    @TableField("MAPTYPE")
//    private BigDecimal maptype;
//}