package com.nian.shortlink.project.config;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Mybatis 的自动填充基本数据
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        //数据库中的创建时间与更新时间都是date类型的，所以后面需要设置成Date的字节码文件
//        metaObject.setValue("createTime", new Date());
//        metaObject.setValue("updateTime", new Date());
//        metaObject.setValue("delFlag", new Date());
        strictInsertFill(metaObject, "createTime", Date::new, Date.class);
        strictInsertFill(metaObject, "updateTime", Date::new, Date.class);
        strictInsertFill(metaObject, "delFlag", () -> 0, Integer.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //直接通过set方法赋值updateTime当前时间
//        metaObject.setValue("updateTime", new Date());
//        strictUpdateFill(metaObject, "updateTime", Date::new, Date.class);
        strictInsertFill(metaObject, "updateTime", Date::new, Date.class);
    }
}
