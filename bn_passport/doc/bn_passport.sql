CREATE DATABASE  IF NOT EXISTS `bn_passport` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `bn_passport`;

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `username` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '密码',
  `email` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `weixin_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信ID',
  `type` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL DEFAULT '0' COMMENT '类别：0-普通用户; 1-认证用户',
  `tx_picture` varchar(200) COLLATE utf8mb4_unicode_ci COMMENT '用户头像',
  `status` char(1) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '1' COMMENT '用户状态：1:正常, 2:禁封',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_login_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近登录时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_user` (`username`),
  UNIQUE KEY `unq_user_email` (`email`),
  UNIQUE KEY `unq_user_weixin` (`weixin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';


*Data for the table `t_user` */

insert  into `t_user`(`id`,`username`,`password`,`email`,`tx_picture`,`weixin_id`,`type`,`status`,`create_time`,`last_login_time`) values (10000,'test1','$2a$10$wtM46M6RbeOCDSB1cuqfz.xidypq.leRrWQbOn8noRJkr0Ram9BYm','429227510@qq.com','http://bitnewstest.oss-cn-hangzhou.aliyuncs.com/tx_default_10001.jpg',NULL,'0','1','2019-07-17 18:20:57','2019-09-04 17:12:17'),(10001,'admin','$2a$10$2f/A2.NOBqgJpDM5lnWDNuWcMm0t9sfm3f2V8tc7R.tH1OWRqx8He',NULL,'http://bitnewstest.oss-cn-hangzhou.aliyuncs.com/tx_default_10002.jpg',NULL,'2','2','2019-07-22 20:34:40','2019-09-04 17:12:10'),(10002,'test2','$2a$10$wtM46M6RbeOCDSB1cuqfz.xidypq.leRrWQbOn8noRJkr0Ram9BYm','test2@sina.com','http://bitnewstest.oss-cn-hangzhou.aliyuncs.com/tx_default_10003.jpg',NULL,'0','1','2019-08-01 18:11:57','2019-09-04 17:12:06'),(10003,'test3','$2a$10$wtM46M6RbeOCDSB1cuqfz.xidypq.leRrWQbOn8noRJkr0Ram9BYm','test3@sina.com','http://bitnewstest.oss-cn-hangzhou.aliyuncs.com/tx_default_10003.jpg',NULL,'0','1','2019-08-01 18:13:36','2019-09-04 17:12:03'),(10004,'251228611','$2a$10$YE9BcFjLil88eDrskCaateQEB4MZocEq5Fhgi3Nrfwd3.Qros6K7.','251228611@qq.com','http://bitnewstest.oss-cn-hangzhou.aliyuncs.com/tx_default_10002.jpg',NULL,'0','1','2019-08-28 09:58:37','2019-09-04 17:12:01');
