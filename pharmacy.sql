-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        10.2.14-MariaDB - mariadb.org binary distribution
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 pharmacy 的数据库结构
CREATE DATABASE IF NOT EXISTS `pharmacy` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `pharmacy`;

-- 导出  表 pharmacy.currentproject 结构
CREATE TABLE IF NOT EXISTS `currentproject` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL DEFAULT '0',
  `projectId` int(11) NOT NULL,
  UNIQUE KEY `username` (`username`),
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='用户当前的项目';

-- 数据导出被取消选择。
-- 导出  表 pharmacy.groupusage 结构
CREATE TABLE IF NOT EXISTS `groupusage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `projectid` int(11) NOT NULL DEFAULT 0,
  `stratificationfactor` varchar(50) NOT NULL COMMENT '分层信息',
  `groupnumber` int(11) NOT NULL COMMENT '组号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组号使用情况';

-- 数据导出被取消选择。
-- 导出  表 pharmacy.investigroup 结构
CREATE TABLE IF NOT EXISTS `investigroup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `projectid` int(11) NOT NULL,
  `grouptype` varchar(50) NOT NULL DEFAULT '0' COMMENT '试验组类型',
  `groupname` varchar(50) NOT NULL DEFAULT '0' COMMENT '试验组名',
  `occupy` int(11) NOT NULL COMMENT '分组比例',
  `subjectnumber` int(11) NOT NULL COMMENT '受试者数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8 COMMENT='实验分组信息';

-- 数据导出被取消选择。
-- 导出  表 pharmacy.investiorganization 结构
CREATE TABLE IF NOT EXISTS `investiorganization` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `projectId` int(11) NOT NULL DEFAULT 0,
  `orgId` int(11) NOT NULL DEFAULT 0,
  `enrollNumber` int(11) DEFAULT 0,
  `valid` varchar(50) DEFAULT 'true',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 pharmacy.offernumberresult 结构
CREATE TABLE IF NOT EXISTS `offernumberresult` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `projectid` int(11) NOT NULL,
  `rnid` varchar(50) NOT NULL COMMENT '显示给医生的号儿',
  `randomnumber` int(11) NOT NULL COMMENT '随机号',
  `randomresult` varchar(50) NOT NULL DEFAULT '0' COMMENT '随机数',
  `groupnumber` int(11) NOT NULL COMMENT '组号',
  `orderIngroup` int(11) NOT NULL COMMENT '组内序号',
  `grouptype` varchar(50) NOT NULL DEFAULT '0' COMMENT '分配类型',
  `stratificationFactor` varchar(50) NOT NULL DEFAULT '0' COMMENT '分层标记',
  `organizationId` int(11) NOT NULL DEFAULT 0 COMMENT '机构id',
  `investigatorid` varchar(50) DEFAULT NULL COMMENT '研究者id',
  `subjectid` varchar(50) DEFAULT NULL COMMENT '参试者id',
  `offerdate` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8 COMMENT='取号结果';

-- 数据导出被取消选择。
-- 导出  表 pharmacy.offernumberstatus 结构
CREATE TABLE IF NOT EXISTS `offernumberstatus` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `projectid` int(11) NOT NULL DEFAULT 0,
  `stratificationfactor` varchar(50) NOT NULL DEFAULT '0' COMMENT '分层标识',
  `organizationId` int(11) NOT NULL DEFAULT 0 COMMENT '机构id',
  `groupnumber` int(11) NOT NULL COMMENT '组号',
  `orderIngroup` int(11) NOT NULL COMMENT '组内序号',
  `currentnumber` int(11) NOT NULL COMMENT '当前随机序号',
  `investigatorid` varchar(50) NOT NULL COMMENT '研究者ID',
  `randomnumberid` int(11) NOT NULL COMMENT '随机号ID',
  `randomnumber` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='取号情况';

-- 数据导出被取消选择。
-- 导出  表 pharmacy.operationrecord 结构
CREATE TABLE IF NOT EXISTS `operationrecord` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `projectId` int(11) NOT NULL,
  `username` varchar(50) NOT NULL DEFAULT '0' COMMENT '谁',
  `name` varchar(50) NOT NULL DEFAULT '0' COMMENT '姓名',
  `role` varchar(50) NOT NULL DEFAULT '0' COMMENT 'role in project',
  `module` varchar(50) NOT NULL DEFAULT '0' COMMENT '什么模块',
  `operation` varchar(50) NOT NULL DEFAULT '0' COMMENT '操作',
  `createtime` datetime NOT NULL DEFAULT current_timestamp(),
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=910 DEFAULT CHARSET=utf8 COMMENT='存储用户操作信息：谁，什么时间，什么模块，什么操作，输入了什么参数';

-- 数据导出被取消选择。
-- 导出  表 pharmacy.organization 结构
CREATE TABLE IF NOT EXISTS `organization` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `organizationNumber` varchar(50) NOT NULL COMMENT '组织机构编号',
  `name` varchar(200) NOT NULL COMMENT '单位名称',
  `address` varchar(200) DEFAULT NULL COMMENT '地址',
  `contactName` varchar(50) DEFAULT NULL COMMENT '联系人姓名',
  `contactPhone` varchar(50) DEFAULT NULL COMMENT '联系人电话',
  `contactDepartment` varchar(50) DEFAULT NULL,
  `property` varchar(50) NOT NULL COMMENT '属性',
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='组织机构';

-- 数据导出被取消选择。
-- 导出  表 pharmacy.project 结构
CREATE TABLE IF NOT EXISTS `project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `projectNumber` varchar(50) NOT NULL COMMENT '项目编码：公司简称+项目序号: XFC001',
  `researchId` varchar(50) NOT NULL COMMENT '研究编号',
  `abbreviation` varchar(50) NOT NULL COMMENT '项目简称',
  `organizationId` int(11) NOT NULL COMMENT '申办方，公司名称',
  `status` varchar(50) NOT NULL COMMENT '状态：准备中，启动，暂停，完成',
  `moduleList` varchar(50) NOT NULL DEFAULT '中央随机系统-CRS' COMMENT '启用模块',
  `planNumber` varchar(50) DEFAULT NULL COMMENT '方案号',
  `batchNumber` varchar(50) DEFAULT NULL COMMENT '批号',
  `ethicNumber` varchar(50) DEFAULT NULL COMMENT '伦理号',
  `description` varchar(1000) DEFAULT NULL COMMENT '摘要',
  `createTime` datetime NOT NULL,
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 pharmacy.randomschedule 结构
CREATE TABLE IF NOT EXISTS `randomschedule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `projectid` int(11) NOT NULL,
  `randomnumber` int(11) NOT NULL COMMENT '随机号',
  `randomresult` varchar(50) NOT NULL DEFAULT '0' COMMENT '随机数',
  `groupnumber` int(11) NOT NULL COMMENT '组号',
  `orderingroup` int(11) NOT NULL COMMENT '组内序号',
  `grouptype` varchar(50) NOT NULL DEFAULT '0' COMMENT '分配类型',
  `stratificationfactor` varchar(50) NOT NULL DEFAULT '0' COMMENT '分层标记',
  `isused` varchar(50) DEFAULT '0' COMMENT '使用标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2141 DEFAULT CHARSET=utf8 COMMENT='随机表\r\n';

-- 数据导出被取消选择。
-- 导出  表 pharmacy.randomsettings 结构
CREATE TABLE IF NOT EXISTS `randomsettings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `projectid` int(11) NOT NULL DEFAULT 0,
  `blindtype` varchar(50) NOT NULL COMMENT '盲态类型',
  `simplesize` int(11) NOT NULL COMMENT '样本量',
  `isctrlsubenroll` varchar(50) NOT NULL COMMENT '是否控制项目收拾者',
  `randomseed` varchar(50) NOT NULL COMMENT '随机种子',
  `numofgroup` int(11) NOT NULL COMMENT '试验组数',
  `balanceType` varchar(50) NOT NULL COMMENT '中心平衡类型',
  `isStratifi` varchar(50) NOT NULL COMMENT '是否进行分层',
  `lengthofblock` int(11) NOT NULL COMMENT '区段长度',
  `numofblock` int(11) NOT NULL COMMENT '区段数量',
  `unfoldmgr` varchar(50) DEFAULT NULL COMMENT '揭盲管理员',
  `createdate` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COMMENT='随机信息记录';

-- 数据导出被取消选择。
-- 导出  表 pharmacy.roleinproject 结构
CREATE TABLE IF NOT EXISTS `roleinproject` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `projectId` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `role` varchar(50) NOT NULL,
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 pharmacy.stratification 结构
CREATE TABLE IF NOT EXISTS `stratification` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `projectid` int(11) NOT NULL DEFAULT 0,
  `stratifiid` varchar(50) NOT NULL DEFAULT '0' COMMENT '分层编号',
  `stratifiname` varchar(50) DEFAULT NULL COMMENT '分层名称',
  `stratififactorid` varchar(50) NOT NULL DEFAULT '0' COMMENT '分层因素编号',
  `stratififactorname` varchar(50) DEFAULT NULL COMMENT '分层因素名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COMMENT='分层信息';

-- 数据导出被取消选择。
-- 导出  表 pharmacy.user 结构
CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `name` varchar(50) NOT NULL,
  `phone` varchar(50) DEFAULT '0',
  `cellphone` varchar(50) DEFAULT '0',
  `email` varchar(50) DEFAULT '0',
  `organizationId` int(11) NOT NULL,
  `role` varchar(100) NOT NULL COMMENT '属性：独立统计师，主要研究者，临床协调员，其他',
  `valid` varchar(50) NOT NULL,
  `activated` varchar(50) NOT NULL,
  `createTime` datetime NOT NULL,
  UNIQUE KEY `username` (`username`),
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
