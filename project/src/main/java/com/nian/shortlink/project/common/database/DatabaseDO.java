package com.nian.shortlink.project.common.database;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * 数据库持久层对象基础属性
 */
@Data
public class DatabaseDO {
    /**
     * 创建时间
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date create_time;

    /**
     * 修改时间
     */
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Date update_time;

    /**
     * 删除标识 0：未删除 1：已删除
     */
    @TableField(value = "del_flag",fill = FieldFill.INSERT)
    private Integer del_flag;
}
