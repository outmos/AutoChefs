-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: data
-- ------------------------------------------------------
-- Server version	5.7.14
CREATE SCHEMA `data` DEFAULT CHARACTER SET utf8 ;
USE data;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `history`
--

DROP TABLE IF EXISTS `history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `history` (
  `idhistory` int(11) NOT NULL AUTO_INCREMENT,
  `idclient` int(11) NOT NULL,
  `research` varchar(1028) NOT NULL,
  PRIMARY KEY (`idhistory`),
  KEY `fk_user_idx` (`idclient`),
  CONSTRAINT `fk_user` FOREIGN KEY (`idclient`) REFERENCES `user` (`iduser`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `history`
--

LOCK TABLES `history` WRITE;
/*!40000 ALTER TABLE `history` DISABLE KEYS */;
/*!40000 ALTER TABLE `history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `map_prod_shop`
--

DROP TABLE IF EXISTS `map_prod_shop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `map_prod_shop` (
  `id_shop` int(11) NOT NULL,
  `id_prod` int(11) NOT NULL,
  `price` decimal(8,2) NOT NULL,
  PRIMARY KEY (`id_shop`,`id_prod`),
  UNIQUE KEY `id_shop` (`id_shop`,`id_prod`),
  KEY `fk_prod_prod_idx` (`id_prod`),
  CONSTRAINT `fk_prod_prod` FOREIGN KEY (`id_prod`) REFERENCES `product` (`idproduct`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_shop_shop` FOREIGN KEY (`id_shop`) REFERENCES `shop` (`idshop`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `map_prod_shop`
--

LOCK TABLES `map_prod_shop` WRITE;
/*!40000 ALTER TABLE `map_prod_shop` DISABLE KEYS */;
INSERT INTO `map_prod_shop` VALUES (1,1,3.03),(1,2,2.48),(1,3,4.07),(1,4,4.15),(1,5,5.96),(1,6,4.17),(1,7,4.90),(1,8,1.04),(1,9,6.10),(1,10,7.96),(1,11,3.14),(1,12,6.49),(1,13,4.77),(1,14,2.61),(1,15,6.93),(1,16,6.14),(2,1,7.85),(2,2,5.26),(2,3,1.17),(2,4,0.08),(2,5,1.24),(2,6,3.74),(2,7,6.30),(2,8,8.31),(2,9,0.39),(2,10,3.37),(2,11,0.70),(2,12,6.97),(2,13,6.90),(2,14,2.60),(2,15,6.15),(2,16,4.87),(3,1,2.16),(3,2,0.78),(3,3,8.23),(3,4,8.96),(3,5,4.50),(3,6,7.31),(3,8,5.64),(3,9,6.10),(3,10,9.51),(3,11,3.38),(3,12,6.43),(3,13,6.22),(3,14,8.44),(3,15,4.37),(3,16,0.05),(4,1,1.83),(4,2,9.80),(4,3,0.92),(4,4,6.43),(4,5,7.03),(4,6,5.44),(4,8,9.67),(4,9,7.11),(4,10,9.96),(4,11,1.57),(4,12,4.06),(4,13,7.79),(4,14,9.05),(4,15,8.68),(4,16,6.42),(5,1,4.65),(5,2,6.45),(5,3,1.00),(5,4,4.60),(5,5,3.38),(5,6,7.40),(5,7,2.78),(5,8,6.32),(5,9,0.76),(5,10,6.67),(5,11,9.20),(5,12,3.80),(5,13,1.65),(5,14,0.86),(5,15,3.59),(5,16,7.91),(6,1,0.86),(6,2,4.31),(6,3,7.79),(6,4,3.00),(6,5,4.48),(6,6,9.48),(6,7,1.19),(6,8,3.94),(6,9,8.37),(6,10,7.00),(6,11,9.84),(6,12,4.04),(6,13,1.34),(6,14,5.26),(6,15,6.86),(6,16,6.02);
/*!40000 ALTER TABLE `map_prod_shop` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `map_recipe_product`
--

DROP TABLE IF EXISTS `map_recipe_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `map_recipe_product` (
  `recipe_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` decimal(6,2) NOT NULL,
  KEY `recipe_id_idx` (`recipe_id`),
  KEY `prod_id_idx` (`product_id`),
  CONSTRAINT `prod_id` FOREIGN KEY (`product_id`) REFERENCES `product` (`idproduct`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `rec_id` FOREIGN KEY (`recipe_id`) REFERENCES `recipe` (`idrecipe`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `map_recipe_product`
--

LOCK TABLES `map_recipe_product` WRITE;
/*!40000 ALTER TABLE `map_recipe_product` DISABLE KEYS */;
INSERT INTO `map_recipe_product` VALUES (999,1,2.00);
/*!40000 ALTER TABLE `map_recipe_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product` (
  `idproduct` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `unit` varchar(128) NOT NULL,
  `kcal` decimal(6,2) NOT NULL,
  `lipid` decimal(6,2) NOT NULL,
  `glucid` decimal(6,2) NOT NULL,
  `protein` decimal(6,2) NOT NULL,
  PRIMARY KEY (`idproduct`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'pomme','pcs',83.00,47.00,54.00,6.00),(2,'banane','pcs',1.00,10.00,47.00,70.00),(3,'kiwi','pcs',31.00,40.00,63.00,25.00),(4,'fraise','pcs',74.00,55.00,79.00,97.00),(5,'sucre','g',4.00,42.00,70.00,64.00),(6,'lait','L',4.00,63.00,6.00,60.00),(7,'cafe','g',1.00,52.00,68.00,73.00),(8,'poire','pcs',52.00,47.00,74.00,97.00),(9,'sel','g',19.00,33.00,100.00,44.00),(10,'oeuf','pcs',16.00,59.00,61.00,33.00),(11,'extrait de vanille','g',73.00,22.00,35.00,8.00),(12,'farine','g',44.00,9.00,88.00,58.00),(13,'eau','L',60.00,74.00,51.00,49.00),(14,'chocolat','g',53.00,9.00,26.00,39.00),(15,'levure','g',49.00,40.00,55.00,32.00),(16,'creme','L',37.00,90.00,34.00,100.00);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recipe`
--

DROP TABLE IF EXISTS `recipe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recipe` (
  `idrecipe` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `iduser` int(11) NOT NULL,
  `operations` longtext,
  `nb_person` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`idrecipe`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `iduser_idx` (`iduser`),
  CONSTRAINT `user_id` FOREIGN KEY (`iduser`) REFERENCES `user` (`iduser`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1003 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recipe`
--

LOCK TABLES `recipe` WRITE;
/*!40000 ALTER TABLE `recipe` DISABLE KEYS */;
INSERT INTO `recipe` VALUES (0,'test',0,NULL,1),(999,'test_fill',0,'test_operations',10);
/*!40000 ALTER TABLE `recipe` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shop`
--

DROP TABLE IF EXISTS `shop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shop` (
  `idshop` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(256) NOT NULL,
  `name` varchar(125) NOT NULL,
  `logo` blob,
  `city` varchar(45) NOT NULL,
  `characteristics` varchar(256) DEFAULT NULL,
  `openinghours` varchar(256) NOT NULL,
  `lattitude` decimal(10,6) NOT NULL,
  `longitude` decimal(10,6) NOT NULL,
  PRIMARY KEY (`idshop`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shop`
--

LOCK TABLES `shop` WRITE;
/*!40000 ALTER TABLE `shop` DISABLE KEYS */;
INSERT INTO `shop` VALUES (1,'rue du carrefour 55','Carrefour','/images/carrefour.jpg','Bruxelles','Bio;Local ','09:00-19:00;09:00-19:00;09:00-19:00;09:00-19:00;09:00-19:00;10:00-16:00;00:00-00:00',50.83334319999999,4.366629399999965),(2,'rue du match 55','Match','/images/match.gif','Bruxelles','Bio;Local','09:00-15:00;09:00-19:00;09:00-19:00;09:00-19:00;09:00-19:00;10:00-16:00;10:00-12:00',50.8808582,4.322789400000033),(3,'rue du Aldi 66','Aldi','/images/aldi.jpg','Bruxelles','Local, vegan','09:00-15:00;09:00-18:00;09:00-19:00;09:00-19:00;09:00-19:00;10:00-16:00;10:00-12:00',50.83257800000001,4.3889939000000595),(4,'rue du Delhaize 55','Delhaize','/images/delhaize.png','Bruxelles','','09:00-19:00;09:00-19:00;09:00-19:00;09:00-19:00;09:00-19:00;10:00-16:00;10:00-12:00',50.8503396,4.351710300000036),(5,'rue du Colruyt 55','Colruyt','/images/colruyt.png','Bruxelles','Bio','09:00-19:00;09:00-19:00;09:00-19:00;09:00-19:00;09:00-19:00;10:00-16:00;10:00-12:00',50.8771683,4.354508699999997),(6,'rue Van Meyel 64','ChezMouad','/images/Chez_MouMou.png','Bruxelles','','09:00-19:00;09:00-19:00;09:00-19:00;09:00-19:00;09:00-19:00;10:00-16:00;10:00-12:00',50.8607881,4.33847650000007);
/*!40000 ALTER TABLE `shop` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `iduser` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) DEFAULT NULL,
  `password` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`iduser`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (0,'TEST',NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-05-19 22:24:21
