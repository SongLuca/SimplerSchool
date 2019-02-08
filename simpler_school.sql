-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- 主机： 127.0.0.1
-- 生成日期： 2019-02-08 02:38:13
-- 服务器版本： 10.1.37-MariaDB
-- PHP 版本： 7.3.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 数据库： `simpler_school`
--

-- --------------------------------------------------------

--
-- 表的结构 `utente`
--

CREATE TABLE `utente` (
  `username` varchar(255) NOT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `cognome` varchar(255) DEFAULT NULL,
  `pass_hash` varchar(255) NOT NULL,
  `scuola` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- 转存表中的数据 `utente`
--

INSERT INTO `utente` (`username`, `nome`, `cognome`, `pass_hash`, `scuola`) VALUES
('qweewq', 'matteo', 'zheng', '1000:44902066434490a47d1cda3a7398401b31031bcf166c1161:20ae56f97a1ccf16f6bf946625b3e8863acab471fd723674', 'T.Buzzi');

--
-- 转储表的索引
--

--
-- 表的索引 `utente`
--
ALTER TABLE `utente`
  ADD PRIMARY KEY (`username`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
