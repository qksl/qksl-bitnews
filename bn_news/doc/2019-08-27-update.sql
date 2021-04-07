
DROP TABLE IF EXISTS `t_promoter_topic`;
CREATE TABLE `t_promoter_topic` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `discussion_id` bigint(20) COMMENT '讨论ID',
  `topic` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '竞猜主题',
  `token_type` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '竞猜币种',
  `context` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
  `winner` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '胜方：0-能; 1-不能',
  `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '发布状态：0-开盘, 1-封盘, 2-结算, 3-流局',
  `type` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '1' COMMENT '消息面: 1-自动收益, 2-水友开盘',
  `permit_time` timestamp  COMMENT '投注截止时间',
  `settlement_time` TIMESTAMP  COMMENT '结算时间',
  `create_user_id` bigint(20) NOT NULL COMMENT '发起人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='竞猜主题';


DROP TABLE IF EXISTS `t_promoter`;

CREATE TABLE `t_promoter` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `topic_id` bigint(20) NOT NULL COMMENT '竞猜主题ID',
  `user_id` bigint(20) NOT NULL COMMENT '发起人ID',
  `promoter` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发起竞猜：0-能; 1-不能',
  `winner` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '胜方：0-能; 1-不能',
  `odds` decimal(2,1) NOT NULL COMMENT '赔率',
  `bottom_gold` bigint(20) NOT NULL COMMENT '底金',
  `remain_gold` bigint(20) NOT NULL COMMENT '剩余金额',
  `back_gold` bigint(20) NOT NULL DEFAULT 0 COMMENT '退款金额',
  `back_status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '余款状态：0-未退款, 1-已退款',
  `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '发布状态：0-未成交, 1-成交',
  `income` bigint(20) NOT NULL DEFAULT 0 COMMENT '收益',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='开猜表';

DROP TABLE IF EXISTS `t_bets`;

CREATE TABLE `t_bets` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `promoter_topic_id` bigint(20) NOT NULL COMMENT '竞猜盘主题id',
  `promoter_id` bigint(20) NOT NULL COMMENT '竞猜盘id',
  `odds` decimal(2,1) NOT NULL COMMENT '赔率',
  `bets` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '下注：0-能; 1-不能',
  `winner` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '胜方：0-能; 1-不能',
  `bets_gold` bigint(20) NOT NULL COMMENT '投入金额',
  `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '发布状态：, 1-赢, 2-输',
  `income` bigint(20) NOT NULL DEFAULT 0 COMMENT '收益',
  `user_id` bigint(11) NOT NULL COMMENT '参与用户ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='下注表';


DROP TABLE IF EXISTS `t_token`;
CREATE TABLE `t_token` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
   `user_id` bigint(20) NOT NULL COMMENT '用户id',
   `token` bigint(20) NOT NULL COMMENT '代币数量',
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '变动时间',
   PRIMARY KEY (`id`),
   UNIQUE KEY `unq_user_id` (`user_id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='代币变动表'


DROP TABLE IF EXISTS `t_token_history`;

CREATE TABLE `t_token_history` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` BIGINT(20) NOT NULL COMMENT '用户id',
  `token` BIGINT(20) NOT NULL COMMENT '变动代币数量',
  `type` CHAR(1) COLLATE utf8mb4_unicode_ci COMMENT '发布状态：, 1-新增, 2-消费',
  `reason` VARCHAR(100) COLLATE utf8mb4_unicode_ci COMMENT '原因',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '变动时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='代币变动表';

/*Data for the table `t_token` */

insert  into `t_token`(`id`,`user_id`,`token`,`update_time`) values (null,10000,0,null),(null,10002,0,null),(null,10003,0,null);
