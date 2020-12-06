-- MySQL dump 10.13  Distrib 8.0.17, for Win64 (x86_64)
--
-- Host: localhost    Database: dbproj
-- ------------------------------------------------------
-- Server version	5.5.5-10.5.6-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `dbproj`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `dbproj` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `dbproj`;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `UserIndex` int(11) NOT NULL AUTO_INCREMENT,
  `ID` varchar(30) NOT NULL,
  `Ussn` char(14) NOT NULL,
  `PW` varchar(50) NOT NULL,
  `Nickname` varchar(30) NOT NULL,
  PRIMARY KEY (`UserIndex`),
  UNIQUE KEY `ID` (`ID`),
  UNIQUE KEY `UC_USSN` (`Ussn`),
  KEY `USSN_FK` (`Ussn`),
  CONSTRAINT `USSN_FK` FOREIGN KEY (`Ussn`) REFERENCES `serviceuser` (`SSN`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,'iraira7777','123456-1234567','1234','KyumKyum');
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `account_music`
--

DROP TABLE IF EXISTS `account_music`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account_music` (
  `uidx` int(11) NOT NULL,
  `midx` int(11) NOT NULL,
  `numPlayed` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`uidx`,`midx`),
  KEY `AMMIDX_FK` (`midx`),
  CONSTRAINT `AMMIDX_FK` FOREIGN KEY (`midx`) REFERENCES `music` (`IDX`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `AMUIDX_FK` FOREIGN KEY (`uidx`) REFERENCES `account` (`UserIndex`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_music`
--

LOCK TABLES `account_music` WRITE;
/*!40000 ALTER TABLE `account_music` DISABLE KEYS */;
INSERT INTO `account_music` VALUES (1,1,7),(1,2,12),(1,3,2),(1,4,3),(1,5,1),(1,6,1),(1,7,1),(1,8,7),(1,9,1),(1,10,1),(1,11,3),(1,15,1);
/*!40000 ALTER TABLE `account_music` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `Assn` char(14) NOT NULL,
  `AID` varchar(30) NOT NULL,
  `APW` varchar(50) NOT NULL,
  `Name` varchar(30) NOT NULL,
  `Nickname` varchar(30) NOT NULL,
  `SerialNum` char(4) NOT NULL,
  PRIMARY KEY (`Assn`),
  UNIQUE KEY `AID` (`AID`),
  UNIQUE KEY `UNI_SERIAL` (`SerialNum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES ('102938-1746287','adminDebug','1234','Jay','MGR_JAY','3224'),('122238-3324433','Imadim','1224','Eric','MGR_ERIC','1456'),('378263-3324433','anotherdumb','1214','Stephanie','MGR_STEPHANIE','2351');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `music`
--

DROP TABLE IF EXISTS `music`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `music` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT,
  `Artist` varchar(50) NOT NULL DEFAULT 'ANONYMOUS',
  `Prod` varchar(50) NOT NULL DEFAULT 'ANONYNOUS',
  `Playtime` varchar(10) NOT NULL,
  `Genre` varchar(20) NOT NULL DEFAULT 'UNDEFINED',
  `T_played` int(11) NOT NULL DEFAULT 0,
  `URL` varchar(100) NOT NULL,
  `Title` varchar(50) NOT NULL,
  `Released` date NOT NULL,
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `music`
--

LOCK TABLES `music` WRITE;
/*!40000 ALTER TABLE `music` DISABLE KEYS */;
INSERT INTO `music` VALUES (1,'Eminem','Eminem','5:26','Hip-hop',7,'media\\Lose Yourself.mp3','Lose Yourself','2005-01-01'),(2,'이민석','GC','2:54','Hip-hop',24,'media\\퇴사.mp3','퇴사','2020-07-30'),(3,'Dominic Fike','Geenah Krisht','3:07','Hip-hop',10,'media\\Vampire.mp3','Vampire','2020-10-31'),(4,'The Living Tombstone','Yoav Landau & Machine','3:45','Hip-hop',8,'media\\Drunk.mp3','Drunk','2020-06-12'),(5,'Imagine Dragons','Interscope Records','3:42','Rock',3,'media\\Birds.mp3','Birds','2018-11-09'),(6,'Caravan Palace','ANONYMOUS','3:07','Electro Swing',3,'media\\Lone Digger (Phietto Remix).mp3','Lone Digger (Phietto Remix)','2018-10-10'),(7,'릴러말즈(Leellamarz)','TOIL','3:45','Hip-hop',4,'media\\GONE.mp3','GONE','2020-03-23'),(8,'BewhY','BewhY','2:13','Hip-hop',8,'media\\가라사대.mp3','가라사대','2019-07-25'),(9,'아이유(IU)','이종훈','3:37','Ballad',2,'media\\Blueming.mp3','Blueming','2019-11-18'),(10,'Kdrew','Kdrew','3:38','EDM',1,'media\\Citadel.mp3','Citadel','2012-04-19'),(11,'AKMU(악동뮤지션)','AKMU(악동뮤지션)','4:50','Ballad',7,'media\\어떻게 이별까지 사랑하겠어, 널 사랑하는 거지.mp3','어떻게 이별까지 사랑하겠어, 널 사랑하는 거지','2019-09-25'),(12,'볼빨간사춘기','볼빨간사춘기','3:11','Rock',4,'media\\워커홀릭.mp3','워커홀릭','2019-09-10'),(13,'Imagine Dragon','Imagine Dragon','3:23','Pop',1,'media\\Believer.mp3','Believer','2017-06-23'),(14,'Fall Out Boy','Fall Out Boy','3:50','Rock',1,'media\\The Last Of The Real Ones.mp3','The Last Of The Real Ones','2018-01-23'),(15,'Fall Out Boy','Fall Out Boy','4:05','Rock',2,'media\\The Phoenix.mp3','The Phoenix','2013-01-01');
/*!40000 ALTER TABLE `music` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `music_mostlylistened`
--

DROP TABLE IF EXISTS `music_mostlylistened`;
/*!50001 DROP VIEW IF EXISTS `music_mostlylistened`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `music_mostlylistened` AS SELECT 
 1 AS `uidx`,
 1 AS `midx`,
 1 AS `numPlayed`,
 1 AS `IDX`,
 1 AS `Artist`,
 1 AS `Prod`,
 1 AS `Playtime`,
 1 AS `Genre`,
 1 AS `T_played`,
 1 AS `URL`,
 1 AS `Title`,
 1 AS `Released`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `playlist`
--

DROP TABLE IF EXISTS `playlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `playlist` (
  `PIDX` int(11) NOT NULL AUTO_INCREMENT,
  `Owner_idx` int(11) NOT NULL,
  `Playlist_name` varchar(50) NOT NULL,
  PRIMARY KEY (`PIDX`),
  KEY `PLIST_FK` (`Owner_idx`),
  CONSTRAINT `PLIST_FK` FOREIGN KEY (`Owner_idx`) REFERENCES `account` (`UserIndex`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `playlist`
--

LOCK TABLES `playlist` WRITE;
/*!40000 ALTER TABLE `playlist` DISABLE KEYS */;
INSERT INTO `playlist` VALUES (2,1,'My Playlist'),(8,1,'퇴사할떄 듣고 싶은 노래 리스트');
/*!40000 ALTER TABLE `playlist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `playlist_music`
--

DROP TABLE IF EXISTS `playlist_music`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `playlist_music` (
  `PIDX` int(11) NOT NULL,
  `MUSICIDX` int(11) NOT NULL,
  PRIMARY KEY (`PIDX`,`MUSICIDX`),
  KEY `MIDX_FK` (`MUSICIDX`),
  CONSTRAINT `MIDX_FK` FOREIGN KEY (`MUSICIDX`) REFERENCES `music` (`IDX`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `PIDX_FK` FOREIGN KEY (`PIDX`) REFERENCES `playlist` (`PIDX`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `playlist_music`
--

LOCK TABLES `playlist_music` WRITE;
/*!40000 ALTER TABLE `playlist_music` DISABLE KEYS */;
INSERT INTO `playlist_music` VALUES (2,5),(2,11),(8,2),(8,6);
/*!40000 ALTER TABLE `playlist_music` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `profile`
--

DROP TABLE IF EXISTS `profile`;
/*!50001 DROP VIEW IF EXISTS `profile`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `profile` AS SELECT 
 1 AS `SSN`,
 1 AS `name`,
 1 AS `age`,
 1 AS `email`,
 1 AS `ID`,
 1 AS `PW`,
 1 AS `Nickname`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `serviceuser`
--

DROP TABLE IF EXISTS `serviceuser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `serviceuser` (
  `SSN` char(14) NOT NULL,
  `name` varchar(30) NOT NULL,
  `age` int(11) NOT NULL,
  `email` varchar(100) NOT NULL DEFAULT 'UNIDENTIFIED',
  PRIMARY KEY (`SSN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `serviceuser`
--

LOCK TABLES `serviceuser` WRITE;
/*!40000 ALTER TABLE `serviceuser` DISABLE KEYS */;
INSERT INTO `serviceuser` VALUES ('000629-0476017','debug',21,'dubug@hello.com'),('123456-1234567','Lim Kyu Min',21,'iraira7777@naver.com'),('135790-2468976','Debug',23,'debug@debug.com'),('285638-1946253','Dummy',42,'jdheue@fifbr.com'),('543210-987654','Lim',22,'example@debug.com'),('987654-9876543','Jay',21,'email@email.com');
/*!40000 ALTER TABLE `serviceuser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `dbproj`
--

USE `dbproj`;

--
-- Final view structure for view `music_mostlylistened`
--

/*!50001 DROP VIEW IF EXISTS `music_mostlylistened`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_unicode_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `music_mostlylistened` AS (select `am`.`uidx` AS `uidx`,`am`.`midx` AS `midx`,`am`.`numPlayed` AS `numPlayed`,`m`.`IDX` AS `IDX`,`m`.`Artist` AS `Artist`,`m`.`Prod` AS `Prod`,`m`.`Playtime` AS `Playtime`,`m`.`Genre` AS `Genre`,`m`.`T_played` AS `T_played`,`m`.`URL` AS `URL`,`m`.`Title` AS `Title`,`m`.`Released` AS `Released` from (`account_music` `am` join `music` `m` on(`am`.`midx` = `m`.`IDX`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `profile`
--

/*!50001 DROP VIEW IF EXISTS `profile`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_unicode_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `profile` AS select `s`.`SSN` AS `SSN`,`s`.`name` AS `name`,`s`.`age` AS `age`,`s`.`email` AS `email`,`a`.`ID` AS `ID`,`a`.`PW` AS `PW`,`a`.`Nickname` AS `Nickname` from (`serviceuser` `s` join `account` `a` on(`s`.`SSN` = `a`.`Ussn`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-12-07  0:35:27
