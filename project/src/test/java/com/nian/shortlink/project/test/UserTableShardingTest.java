package com.nian.shortlink.project.test;

public class UserTableShardingTest {
    /**
     * 新建0-15个数据库表（水平分表）
     */
    private final static String SQL = "create table t_link_access_stats_%d\n" +
            "(\n" +
            "    id             bigint auto_increment comment 'ID'\n" +
            "        primary key,\n" +
            "    gid            varchar(32)  null comment '分组标识',\n" +
            "    full_short_url varchar(128) null comment '完整短链接',\n" +
            "    date           date         null comment '日期',\n" +
            "    pv             int          null comment '访问量',\n" +
            "    uv             int          null comment '独立访问数',\n" +
            "    uip            int          null comment '独立IP数',\n" +
            "    hour           int          null comment '小时',\n" +
            "    weekday        int          null comment '星期',\n" +
            "    create_time    datetime     null comment '创建时间',\n" +
            "    update_time    datetime     null comment '修改时间',\n" +
            "    del_flag       tinyint(1)   null comment '删除标识：0 未删除 1 已删除',\n" +
            "    constraint idx_unique_access_stats\n" +
            "        unique (full_short_url, gid, date, hour)\n" +
            ")\n" +
            "    row_format = DYNAMIC;";

    public static void main(String[] args) {
        for (int i = 0; i < 16; i++) {
            System.out.printf((SQL) + "%n",i);
        }
    }
}