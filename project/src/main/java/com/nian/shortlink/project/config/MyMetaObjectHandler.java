package com.nian.shortlink.project.config;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        //数据库中的创建时间与更新时间都是date类型的，所以后面需要设置成Date的字节码文件
        strictInsertFill(metaObject, "create_time", Date::new, Date.class);
        strictInsertFill(metaObject, "update_time", Date::new, Date.class);
        strictInsertFill(metaObject, "del_flag", () -> 0, Integer.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        strictInsertFill(metaObject, "update_time", Date::new, Date.class);
    }
}
