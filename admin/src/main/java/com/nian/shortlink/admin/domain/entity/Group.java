package com.nian.shortlink.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nian.shortlink.admin.common.database.DatabaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.aop.target.AbstractBeanFactoryBasedTargetSource;

import java.io.Serializable;
import java.util.Date;

/**
 * 短链接分组实体类
 */
@Data
@TableName("t_group")
@Builder //使用builder时全参，无参都需要传
@NoArgsConstructor
@AllArgsConstructor
public class Group extends DatabaseDO implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 分组标识
     */
    private String gid;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 创建分组用户名
     */
    private String username;

    /**
     * 分组排序
     */
    private Integer sortOrder;

}
