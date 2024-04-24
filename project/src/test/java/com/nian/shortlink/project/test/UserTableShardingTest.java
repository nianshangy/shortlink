package com.nian.shortlink.project.test;

public class UserTableShardingTest {
    /**
     * 新建0-15个数据库表（水平分表）
     */
    private final static String SQL = "CREATE TABLE `t_link_access_stats_%d`\n" +
            "(\n" +
            "    `id`             bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',\n" +
            "    `full_short_url` varchar(128) DEFAULT NULL COMMENT '完整短链接',\n" +
            "    `gid`            varchar(32)  DEFAULT 'default' COMMENT '分组标识',\n" +
            "    `date`           date         DEFAULT NULL COMMENT '日期',\n" +
            "    `pv`             int(11) DEFAULT NULL COMMENT '访问量',\n" +
            "    `uv`             int(11) DEFAULT NULL COMMENT '独立访客数',\n" +
            "    `uip`            int(11) DEFAULT NULL COMMENT '独立IP数',\n" +
            "    `hour`           int(3) DEFAULT NULL COMMENT '小时',\n" +
            "    `weekday`        int(3) DEFAULT NULL COMMENT '星期',\n" +
            "    `create_time`    datetime     DEFAULT NULL COMMENT '创建时间',\n" +
            "    `update_time`    datetime     DEFAULT NULL COMMENT '修改时间',\n" +
            "    `del_flag`       tinyint(1) DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',\n" +
            "    PRIMARY KEY (`id`),\n" +
            "    UNIQUE KEY `idx_unique_access_stats` (`full_short_url`,`gid`,`date`,`hour`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;;";

    public static void main(String[] args) {
        for (int i = 0; i < 16; i++) {
            System.out.printf((SQL) + "%n",i);
        }
    }
}