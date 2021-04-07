CREATE DATABASE  IF NOT EXISTS `bn_news` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `bn_news`;

--
-- Table structure for table `t_banner`
--

DROP TABLE IF EXISTS `t_banner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_banner` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `picture_order` int(11) NOT NULL DEFAULT 0 COMMENT '图片顺序',
  `picture_path` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '图片路径',
  `type` char(1) COLLATE utf8_unicode_ci DEFAULT '1' COMMENT '横幅类型 1-首页, 2-资讯',
  `jump` char(1) COLLATE utf8_unicode_ci NOT NULL DEFAULT '2' COMMENT '是否跳转 1-是, 2-否',
  `jump_url` text COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '跳转链接',
  `admin_id` int(11) NOT NULL COMMENT '管理员ID',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='横幅表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_discussion`
--

DROP TABLE IF EXISTS `t_discussion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_discussion` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `title` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
  `picture_path` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片路径',
  `tag` char(1) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标识：1-政策, 2-区块链,3-交易所',
  `source` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '来源',
  `sticky_post_id` bigint(20) DEFAULT NULL COMMENT '置顶用户ID',
  `bull_count` int(11) NOT NULL DEFAULT 0 COMMENT '利好',
  `bear_count` int(11) NOT NULL DEFAULT 0 COMMENT '利空',
  `status` char(1) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '1' COMMENT '发布状态：1:创建, 2:发布, 3:删除',
  `event_flag` char(1) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '1' COMMENT '重大事件：1:是, 2:否',  
  `event_time` timestamp COMMENT '事件日期：格式如2019-01-01',
  `admin_id` int(11) NOT NULL COMMENT '管理员ID',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='讨论主题表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_liked`
--

DROP TABLE IF EXISTS `t_liked`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_liked` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `post_id` bigint(20) NOT NULL COMMENT '讨论ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_liked` (`post_id`,`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='发布点赞表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_post`
--

DROP TABLE IF EXISTS `t_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_post` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `discussion_id` bigint(20) NOT NULL COMMENT '讨论ID',
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
  `liked_sum` int(20) NOT NULL DEFAULT 0 COMMENT '点赞数',
  `type` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '1' COMMENT '消息面: 1-利好, 2-利空',
  `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '2' COMMENT '发布状态：2-发布, 3-删除',
  `user_id` int(11) NOT NULL COMMENT '发布的用户ID',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='讨论发布表';


DROP TABLE IF EXISTS `t_coin_market`;
CREATE TABLE `t_coin_market` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `coin_code` varchar(20) NOT NULL COMMENT '币种代码: BTC',
  `price` decimal(19,8) NOT NULL DEFAULT 0 COMMENT '行情价格，单位USDT',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='市场行情价格表';




DROP TABLE IF EXISTS `t_promoter_topic`;
DROP TABLE IF EXISTS `t_promoter`;
DROP TABLE IF EXISTS `t_bets`;

CREATE TABLE `t_promoter_topic` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
   `promoter_id` bigint(20) DEFAULT NULL,
   `discussion_id` bigint(20) DEFAULT NULL COMMENT '讨论ID',
   `topic` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '竞猜主题',
   `token_type` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '竞猜币种',
   `context` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
   `winner` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '猜胜方：0-能; 1-不能',
   `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '发布状态：0-开盘, 1-封盘, 2-结算，3-流局',
   `type` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '1' COMMENT '消息面: 1-自动收益, 2-水友开盘',
   `permit_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '投注截止时间',
   `settlement_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '结算时间',
   `guess_gold` decimal(19,8) NOT NULL COMMENT '预测价格',
   `guess_winner` char(1) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '猜大小: 0:高于，1：低于',
   `create_user_id` bigint(20) NOT NULL COMMENT '发起人',
   `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='竞猜主题';

CREATE TABLE `t_bets` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
   `promoter_topic_id` bigint(20) NOT NULL COMMENT '竞猜盘id',
   `odds` decimal(7,2) DEFAULT NULL,
   `bets` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '下注：0-支持; 1-反对',
   `winner` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '胜方：0-支持; 1-反对',
   `bets_gold` decimal(19,8) DEFAULT NULL,
   `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '发布状态：, 1-赢, 2-输',
   `income` decimal(19,8) DEFAULT NULL,
   `user_id` bigint(11) NOT NULL COMMENT '参与用户ID',
   `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='下注表';




DROP TABLE IF EXISTS `t_token`;
CREATE TABLE `t_token` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
   `user_id` bigint(20) NOT NULL COMMENT '用户id',
   `token` bigint(20) NOT NULL COMMENT '代币数量',
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '变动时间',
   PRIMARY KEY (`id`),
   UNIQUE KEY `unq_user_id` (`user_id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='代币变动表'


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

DROP TABLE IF EXISTS `t_fear_greed`;
CREATE TABLE `t_fear_greed` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
   `fear_value` int NOT NULL COMMENT '恐慌值',
   `value_classification` VARCHAR(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '值类型',
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='市场恐慌表';



DROP TABLE IF EXISTS `t_tasks`;
CREATE TABLE `t_tasks` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
   `reward` int(11) NOT NULL COMMENT '任务奖励',
   `context` text COLLATE utf8mb4_unicode_ci COMMENT '内容',
   `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '状态：0-正常, 1-关闭',
   `type` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '类型: 0-每日, 1-成长,2-运营',
   `unique_mark` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '唯一标识',
   `desc` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '描叙',
   `sort_num` int(5) DEFAULT '0' COMMENT '排序',
   `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务表';


DROP TABLE IF EXISTS `t_tasks_complete`;
CREATE TABLE `t_tasks_complete` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
   `task_id` bigint(20) DEFAULT NULL COMMENT '任务ID',
   `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
   `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务完成记录表';