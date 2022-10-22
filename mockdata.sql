-- MySQL dump 10.13  Distrib 8.0.30, for Linux (x86_64)
--
-- Host: localhost    Database: stacksurge
-- ------------------------------------------------------
-- Server version	8.0.30-0ubuntu0.22.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (24);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instance`
--

DROP TABLE IF EXISTS `instance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `instance` (
  `id` int NOT NULL,
  `creation_time` datetime DEFAULT NULL,
  `port` int NOT NULL,
  `stack_container_id` varchar(255) DEFAULT NULL,
  `tech_stack_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3d6g5w0723snkvs2eef56v0mf` (`tech_stack_id`),
  KEY `FK9fpuy4f3due5fmfwtys5mto7l` (`user_id`),
  CONSTRAINT `FK3d6g5w0723snkvs2eef56v0mf` FOREIGN KEY (`tech_stack_id`) REFERENCES `tech_stack` (`id`),
  CONSTRAINT `FK9fpuy4f3due5fmfwtys5mto7l` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instance`
--

LOCK TABLES `instance` WRITE;
/*!40000 ALTER TABLE `instance` DISABLE KEYS */;
/*!40000 ALTER TABLE `instance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tech_stack`
--

DROP TABLE IF EXISTS `tech_stack`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tech_stack` (
  `id` int NOT NULL,
  `codename` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `is_primary` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `tags` tinyblob,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_n9e00ga54o7egxx4y8mv7n9a2` (`codename`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tech_stack`
--

LOCK TABLES `tech_stack` WRITE;
/*!40000 ALTER TABLE `tech_stack` DISABLE KEYS */;
INSERT INTO `tech_stack` VALUES (1,'BASE','A base linux system.','basesystem.png',_binary '','base-system',NULL),(2,'TOM','Apache Tomcat is a free and open-source implementation of the Jakarta Servlet, Jakarta Expression Language, and WebSocket technologies. Tomcat provides a \"pure Java\" HTTP web server environment in which Java code can run.','tomstack.png',_binary '','tomstack',NULL),(3,'CODE','Features a fully integrated vscode container with support for C, C++, Python and Java.','codestack.png',_binary '','codestack',NULL);
/*!40000 ALTER TABLE `tech_stack` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `reg_date` datetime DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `volume` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (3,'chinmoy12c@gmail.com','$2a$10$/fsttUP3sVQu3YiXbrX6deAFvtaBGCyoLMH7IDdXKvBZeTGN8AYxu','2022-08-02 00:03:10','admin','5b0c24d6-82ed-43e0-ace4-c522bb7f5f08'),(7,'master@gmail.com','$2a$10$ea9z/gfKz0jo92ElWPJXGu3kM9He63B0JMBo6Hh0feForW0xiVFIu','2022-09-08 16:19:07','user','918d42aa-aafb-4410-a44a-ee7768bce9c5'),(8,'master12@gmail.com','$2a$10$h2iwzZjO9bktskLTNvk3RuyLiAmtMa1fS590LaQwkmjvtCwALLN2C','2022-09-08 16:22:29','user','97f368a6-99fa-47ed-980c-7f67176c4171'),(17,'testuser@gmail.com','$2a$10$7ZHuUAWAFaiHVpNjmQTul.AOUmFagAnep4HAEc37aozUTtQkMb0aC','2022-09-10 14:45:34','user','544d5afd-d9dd-46de-a4a4-661550a35483');
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

-- Dump completed on 2022-09-10 17:21:16
