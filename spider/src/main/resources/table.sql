CREATE DATABASE `gossip`;

USE `gossip`;

DROP TABLE IF EXISTS `news`;

CREATE TABLE `news` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(150) DEFAULT NULL,
  `time` varchar(150) DEFAULT NULL,
  `source` varchar(150) DEFAULT NULL,
  `content` text,
  `editor` varchar(150) DEFAULT NULL,
  `docurl` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8