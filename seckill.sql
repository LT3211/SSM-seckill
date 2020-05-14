/*
Navicat MySQL Data Transfer

Source Server         : Mysql
Source Server Version : 80016
Source Host           : localhost:3306
Source Database       : seckill

Target Server Type    : MYSQL
Target Server Version : 80016
File Encoding         : 65001

Date: 2020-05-12 17:21:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for seckill
-- ----------------------------
DROP TABLE IF EXISTS `seckill`;
CREATE TABLE `seckill` (
  `seckill_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `title` varchar(1000) DEFAULT NULL COMMENT '商品标题',
  `image` varchar(1000) DEFAULT NULL COMMENT '商品图片',
  `price` decimal(10,2) DEFAULT NULL COMMENT '商品原价格',
  `cost_price` decimal(10,2) DEFAULT NULL COMMENT '商品秒杀价格',
  `stock_count` bigint(20) DEFAULT NULL COMMENT '剩余库存数量',
  `start_time` timestamp NOT NULL DEFAULT '1970-02-01 00:00:01' COMMENT '秒杀开始时间',
  `end_time` timestamp NOT NULL DEFAULT '1970-02-01 00:00:01' COMMENT '秒杀结束时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`seckill_id`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_end_time` (`end_time`),
  KEY `idx_create_time` (`end_time`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='秒杀商品表';

-- ----------------------------
-- Records of seckill
-- ----------------------------
INSERT INTO `seckill` VALUES ('1', 'Apple/苹果 iPhone 6s Plus 国行原装苹果6sp 5.5寸全网通4G手机', 'https://g-search3.alicdn.com/img/bao/uploaded/i4/i3/2249262840/O1CN011WqlHkrSuPEiHxd_!!2249262840.jpg_230x230.jpg', '2600.00', '1100.00', '8', '2018-10-06 16:30:00', '2020-05-14 16:30:00', '2020-05-11 20:12:21');
INSERT INTO `seckill` VALUES ('2', 'ins新款连帽毛领棉袄宽松棉衣女冬外套学生棉服', 'https://gw.alicdn.com/bao/uploaded/i3/2007932029/TB1vdlyaVzqK1RjSZFzXXXjrpXa_!!0-item_pic.jpg_180x180xz.jpg', '200.00', '150.00', '10', '2018-10-06 16:30:00', '2018-10-17 16:30:00', '2020-05-11 20:12:21');
INSERT INTO `seckill` VALUES ('3', '可爱超萌兔子毛绒玩具垂耳兔公仔布娃娃睡觉抱女孩玩偶大号女生 ', 'https://g-search3.alicdn.com/img/bao/uploaded/i4/i2/3828650009/TB22CvKkeOSBuNjy0FdXXbDnVXa_!!3828650009.jpg_230x230.jpg', '160.00', '130.00', '20', '2018-10-06 16:30:00', '2018-10-17 16:30:00', '2020-05-11 20:12:21');
INSERT INTO `seckill` VALUES ('4', '魅族17', 'https://img10.360buyimg.com/n1/jfs/t1/110831/33/8422/66344/5eb52b83E3641c70d/186c57987b616dab.jpg', '3699.00', '3399.00', '100', '2020-10-01 00:00:01', '2021-02-13 00:00:01', '2020-05-12 15:30:45');
INSERT INTO `seckill` VALUES ('5', '计算机网络自顶向下', 'https://img10.360buyimg.com/n1/jfs/t1/109072/26/12193/493921/5e955d51Edc657b33/8d93684243d2da9a.jpg', '69.00', '20.00', '100', '2020-05-30 00:00:01', '2020-11-27 00:00:01', '2020-05-12 15:34:50');
INSERT INTO `seckill` VALUES ('6', '图解TCP/IP 第5版', 'https://img10.360buyimg.com/n1/jfs/t5797/240/2066590798/290678/a64c50f0/592bf164N5dde581e.jpg', '60.00', '20.00', '1000', '2020-06-06 00:00:01', '2020-10-25 00:00:01', '2020-05-12 15:37:29');

-- ----------------------------
-- Table structure for seckill_order
-- ----------------------------
DROP TABLE IF EXISTS `seckill_order`;
CREATE TABLE `seckill_order` (
  `seckill_id` bigint(20) NOT NULL COMMENT '秒杀商品ID',
  `money` decimal(10,2) DEFAULT NULL COMMENT '支付金额',
  `user_phone` bigint(20) NOT NULL COMMENT '用户手机号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `state` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态：-1无效 0成功 1已付款',
  PRIMARY KEY (`seckill_id`,`user_phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀订单表';

-- ----------------------------
-- Records of seckill_order
-- ----------------------------
INSERT INTO `seckill_order` VALUES ('1', '200.00', '137337879', '2020-05-12 13:56:16', '0');
INSERT INTO `seckill_order` VALUES ('1', '1100.00', '17857324868', '2020-05-12 15:25:59', '0');
