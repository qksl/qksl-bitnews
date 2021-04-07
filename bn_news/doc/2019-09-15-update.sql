
TRUNCATE t_promoter_topic;

ALTER TABLE `t_promoter_topic` ADD COLUMN `guess_winner` CHAR(1) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '猜大小: 0:大于，1：小于' AFTER `settlement_time`;

ALTER TABLE `t_promoter_topic` ADD COLUMN `guess_gold`  DECIMAL(19,8) NOT NULL COMMENT '预测价格' AFTER `settlement_time`;

