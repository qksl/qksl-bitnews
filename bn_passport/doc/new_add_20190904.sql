USE `bn_passport`;
ALTER TABLE `t_user` ADD COLUMN `tx_picture` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT 'http://bitnewstest.oss-cn-hangzhou.aliyuncs.com/tx_default_10001.jpg' COMMENT '用户头像' AFTER `weixin_id`;
update t_user set type=3 where id=10001;