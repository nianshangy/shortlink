package com.nian.shortlink.project.test;

public class UserTableShardingTest {
    /**
     * 新建0-15个数据库表（水平分表）
     */
    private final static String SQL = "CREATE TABLE t_link_locale_stats_%d (\n" +
            "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',\n" +
            "  `full_short_url` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '完整短链接',\n" +
            "  `gid` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分组标识',\n" +
            "  `date` date DEFAULT NULL COMMENT '日期',\n" +
            "  `cnt` int DEFAULT NULL COMMENT '访问量',\n" +
            "  `province` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '省份名称',\n" +
            "  `city` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '市名称',\n" +
            "  `adcode` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '城市编码',\n" +
            "  `country` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '国家标识',\n" +
            "  `create_time` datetime DEFAULT NULL COMMENT '创建时间',\n" +
            "  `update_time` datetime NOT NULL COMMENT '修改时间',\n" +
            "  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识 0表示删除 1表示未删除',\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  UNIQUE KEY `idx_unique_locale_stats` (`full_short_url`,`gid`,`date`,`adcode`,`province`) USING BTREE\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;";

    public static void main(String[] args) {
        for (int i = 0; i < 16; i++) {
            System.out.printf((SQL) + "%n",i);
        }
    }
}