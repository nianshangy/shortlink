package com.nian.shortlink.project.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nian.shortlink.project.common.database.DatabaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 短链接实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_link")
public class ShortLink extends DatabaseDO implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * id
     */
    private Long id;

    /**
     * 域名
     */
    private String domain;

    /**
     * 短链接
     */
    private String shortUri;

    /**
     * 完整短链接
     */
    private String fullShortUrl;

    /**
     * 原始链接
     */
    private String originUrl;

    /**
     * 点击量
     */
    private Integer clickNum;

    /**
     * 分组标识
     */

    private String gid;

    /**
     * 启用标识 0：未启用 1：已启用
     */
    private Integer enableStatus;

    /**
     * 创建类型 0：控制台 1：接口
     */
    private Integer createdType;

    /**
     * 有效期类型 0：永久有效 1：用户自定义
     */
    private Integer validDateType;

    /**
     * 有效期
     */
    private Date validDate;

    /**
     * 描述
     */
    @TableField("`describe`")
    private String describe;

    /**
     * 网站标识
     */
    private String favicon;

    /**
     * 历史pv
     */
    private Integer totalPv;

    /**
     * 历史uv
     */
    private Integer totalUv;

    /**
     * 历史uip
     */
    private Integer totalUip;

    /**
     * 今日PV
     */
    @TableField(exist = false)
    private Integer todayPv;

    /**
     * 今日UV
     */
    @TableField(exist = false)
    private Integer todayUv;

    /**
     * 今日UIP
     */
    @TableField(exist = false)
    private Integer todayUip;
}
