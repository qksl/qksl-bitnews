CREATE DATABASE  IF NOT EXISTS `bn_admin` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `bn_admin`;
--
-- Table structure for table `t_admin`
--

DROP TABLE IF EXISTS `t_admin`;
CREATE TABLE `t_admin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `username` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '密码',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_login_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近登录时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_admin` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='管理员表';
