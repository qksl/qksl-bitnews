CREATE TABLE `t_fear_greed` (
   `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
   `fear_value` INT NOT NULL COMMENT '恐慌值',
   `value_classification` VARCHAR(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '值类型',
   `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
   PRIMARY KEY (`id`)
 ) ENGINE=INNODB AUTO_INCREMENT=9697 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='市场恐慌表'