-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: chatpro
-- ------------------------------------------------------
-- Server version	8.0.34

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
-- Table structure for table `archivos`
--

DROP TABLE IF EXISTS `archivos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `archivos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre_archivo` varchar(255) DEFAULT NULL,
  `ruta_archivo` varchar(255) DEFAULT NULL,
  `tipo` enum('publico','privado','protegido') DEFAULT NULL,
  `usuario_id` int DEFAULT NULL,
  `grupo_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  KEY `grupo_id` (`grupo_id`),
  CONSTRAINT `archivos_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `archivos_ibfk_2` FOREIGN KEY (`grupo_id`) REFERENCES `grupos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `archivos`
--

LOCK TABLES `archivos` WRITE;
/*!40000 ALTER TABLE `archivos` DISABLE KEYS */;
/*!40000 ALTER TABLE `archivos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `configuracioncliente`
--

DROP TABLE IF EXISTS `configuracioncliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `configuracioncliente` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre_cliente` varchar(255) DEFAULT NULL,
  `tamano_maximo` int DEFAULT NULL,
  `ip_servidor_predeterminada` varchar(255) DEFAULT NULL,
  `puerto_servidor_predeterminado` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configuracioncliente`
--

LOCK TABLES `configuracioncliente` WRITE;
/*!40000 ALTER TABLE `configuracioncliente` DISABLE KEYS */;
/*!40000 ALTER TABLE `configuracioncliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `configuracionservidor`
--

DROP TABLE IF EXISTS `configuracionservidor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `configuracionservidor` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tamano_maximo` int DEFAULT NULL,
  `max_conexiones_simultaneas` int DEFAULT NULL,
  `contrasena_bd` varchar(255) DEFAULT NULL,
  `cliente_admin` varchar(255) DEFAULT NULL,
  `nombre_servidor` varchar(255) DEFAULT NULL,
  `ruta_archivos` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configuracionservidor`
--

LOCK TABLES `configuracionservidor` WRITE;
/*!40000 ALTER TABLE `configuracionservidor` DISABLE KEYS */;
/*!40000 ALTER TABLE `configuracionservidor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grupos`
--

DROP TABLE IF EXISTS `grupos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grupos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre_grupo` varchar(255) NOT NULL,
  `administrador_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre_grupo` (`nombre_grupo`),
  KEY `administrador_id` (`administrador_id`),
  CONSTRAINT `grupos_ibfk_1` FOREIGN KEY (`administrador_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grupos`
--

LOCK TABLES `grupos` WRITE;
/*!40000 ALTER TABLE `grupos` DISABLE KEYS */;
INSERT INTO `grupos` VALUES (2,'test1 ',NULL);
/*!40000 ALTER TABLE `grupos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mensajes`
--

DROP TABLE IF EXISTS `mensajes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mensajes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `contenido` text,
  `usuario_id` int DEFAULT NULL,
  `grupo_id` int DEFAULT NULL,
  `fecha_envio` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  KEY `grupo_id` (`grupo_id`),
  CONSTRAINT `mensajes_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `mensajes_ibfk_2` FOREIGN KEY (`grupo_id`) REFERENCES `grupos` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=130 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mensajes`
--

LOCK TABLES `mensajes` WRITE;
/*!40000 ALTER TABLE `mensajes` DISABLE KEYS */;
INSERT INTO `mensajes` VALUES (1,'HolaQuetal',NULL,NULL,'2023-10-09 10:46:14'),(2,'HolaQuetal',NULL,NULL,'2023-10-09 10:54:27'),(3,'HolaQuetal',NULL,NULL,'2023-10-09 10:55:08'),(4,'HolaQuetal',NULL,NULL,'2023-10-09 10:55:34'),(5,'HolaQuetal',NULL,NULL,'2023-10-09 10:56:01'),(6,'HolaQuetal',NULL,NULL,'2023-10-09 10:58:27'),(7,'HolaQuetal',NULL,NULL,'2023-10-09 10:59:22'),(8,'HolaQuetal',NULL,NULL,'2023-10-09 11:48:11'),(9,'HolaQuetal',NULL,NULL,'2023-10-09 11:50:10'),(10,'HolaQuetal',NULL,NULL,'2023-10-09 11:54:41'),(11,'HolaQuetal',NULL,NULL,'2023-10-09 12:34:08'),(12,'HolnQuetal',NULL,NULL,'2023-10-09 12:44:48'),(13,'HolnQuetal',NULL,NULL,'2023-10-09 12:45:06'),(14,'HolnQuetal',NULL,NULL,'2023-10-09 15:22:48'),(15,'HolnQuetal',NULL,NULL,'2023-10-09 15:23:01'),(16,'HolnQuetal',NULL,NULL,'2023-10-09 17:16:06'),(17,'HolnQuetal',NULL,NULL,'2023-10-10 13:47:17'),(18,'HolnQuetal',NULL,NULL,'2023-10-10 13:47:44'),(19,'HolnQuetal',NULL,NULL,'2023-10-10 13:48:23'),(20,'HolnQuetal',NULL,NULL,'2023-10-10 13:49:01'),(21,'HolnQuetal',NULL,NULL,'2023-10-10 13:49:38'),(22,'HolnQuetal',NULL,NULL,'2023-10-10 13:51:50'),(23,'HolnQuetal',NULL,NULL,'2023-10-10 13:52:33'),(24,'HolnQuetal',NULL,NULL,'2023-10-10 13:52:56'),(25,'HolnQuetal',NULL,NULL,'2023-10-10 13:56:38'),(26,'HolnQuetal',NULL,NULL,'2023-10-10 13:56:48'),(27,'HolnQuetal',NULL,NULL,'2023-10-10 13:57:24'),(28,'HolnQuetal',NULL,NULL,'2023-10-10 14:07:41'),(29,'HolnQuetal',NULL,NULL,'2023-10-10 14:08:52'),(30,'HolnQuetal',NULL,NULL,'2023-10-10 14:12:58'),(31,'HolnQuetal',NULL,NULL,'2023-10-10 14:13:09'),(32,'HolnQuetal',NULL,NULL,'2023-10-10 14:14:40'),(33,'HolnQuetal',NULL,NULL,'2023-10-10 14:17:56'),(34,'HolnQuetal',NULL,NULL,'2023-10-10 14:25:44'),(35,'HolnQuetal',NULL,NULL,'2023-10-10 14:26:02'),(36,'HolnQuetal',NULL,NULL,'2023-10-10 14:26:27'),(37,'HolnQuetal',NULL,NULL,'2023-10-10 14:36:22'),(38,'HolnQuetal',NULL,NULL,'2023-10-10 14:37:07'),(39,'HolnQuetal',NULL,NULL,'2023-10-10 14:37:31'),(40,'HolnQuetal',NULL,NULL,'2023-10-10 14:53:55'),(41,'HolnQuetal',NULL,NULL,'2023-10-10 14:57:18'),(42,'HolnQuetal',NULL,NULL,'2023-10-10 14:59:54'),(43,'HolnQuetal',NULL,NULL,'2023-10-10 15:01:18'),(44,'HolnQuetal',NULL,NULL,'2023-10-10 15:04:15'),(45,'HolnQuetal',NULL,NULL,'2023-10-10 15:04:52'),(46,'HolnQuetal',NULL,NULL,'2023-10-10 15:18:25'),(47,'HolnQuetal',NULL,NULL,'2023-10-10 15:18:38'),(48,'HolnQuetal',NULL,NULL,'2023-10-10 15:18:48'),(49,'HolnQuetal',NULL,NULL,'2023-10-10 15:26:53'),(50,'HolnQuetal',NULL,NULL,'2023-10-10 15:27:08'),(51,'HolnQuetal',NULL,NULL,'2023-10-10 15:27:21'),(52,'HolnQuetal',NULL,NULL,'2023-10-10 15:27:41'),(53,'HolnQuetal',NULL,NULL,'2023-10-10 15:30:03'),(54,'HolnQuetal',NULL,NULL,'2023-10-10 15:32:15'),(55,'HolnQuetal',NULL,NULL,'2023-10-10 15:56:49'),(56,'HolnQuetal',NULL,NULL,'2023-10-10 15:57:12'),(57,'HolnQuetal',NULL,NULL,'2023-10-10 15:58:07'),(58,'HolnQuetal',NULL,NULL,'2023-10-10 15:58:26'),(59,'HolnQuetal',NULL,NULL,'2023-10-10 15:59:05'),(60,'HolnQuetal',NULL,NULL,'2023-10-10 15:59:23'),(61,'HolnQuetal',NULL,NULL,'2023-10-10 15:59:46'),(62,'HolnQuetal',NULL,NULL,'2023-10-10 16:05:59'),(63,'HolnQuetal',NULL,NULL,'2023-10-10 16:07:44'),(64,'HolnQuetal',NULL,NULL,'2023-10-10 16:09:38'),(65,'HolnQuetal',NULL,NULL,'2023-10-10 16:11:12'),(66,'HolnQuetal',NULL,NULL,'2023-10-10 17:17:07'),(67,'HolnQuetal',NULL,NULL,'2023-10-10 17:17:34'),(68,'HolnQuetal',NULL,NULL,'2023-10-10 17:18:56'),(69,'HolnQuetal',NULL,NULL,'2023-10-10 17:20:01'),(70,'HolnQuetal',NULL,NULL,'2023-10-10 17:21:51'),(71,'HolnQuetal',NULL,NULL,'2023-10-10 17:24:54'),(72,'HolnQuetal',NULL,NULL,'2023-10-10 17:31:13'),(73,'HolnQuetal',NULL,NULL,'2023-10-10 17:42:35'),(74,'HolnQuetal',NULL,NULL,'2023-10-10 17:42:47'),(75,'HolnQuetal',NULL,NULL,'2023-10-10 17:43:14'),(76,'HolnQuetal',NULL,NULL,'2023-10-10 17:49:51'),(77,'HolnQuetal',NULL,NULL,'2023-10-10 17:50:32'),(78,'HolnQuetal',NULL,NULL,'2023-10-10 17:54:33'),(79,'HolnQuetal',NULL,NULL,'2023-10-10 17:54:56'),(80,'HolnQuetal',NULL,NULL,'2023-10-10 18:16:00'),(81,'HolnQuetal',NULL,NULL,'2023-10-10 18:16:23'),(82,'HolnQuetal',NULL,NULL,'2023-10-10 18:18:05'),(83,'HolnQuetal',NULL,NULL,'2023-10-10 18:19:25'),(84,'HolnQuetal',NULL,NULL,'2023-10-10 19:04:33'),(85,'HolnQuetal',NULL,NULL,'2023-10-10 19:04:45'),(86,'HolnQuetal',NULL,NULL,'2023-10-10 19:05:27'),(87,'HolnQuetal',NULL,NULL,'2023-10-10 19:05:37'),(88,'HolaQuetal',NULL,NULL,'2023-10-11 15:30:46'),(89,'HolaQuetal',NULL,NULL,'2023-10-11 15:31:14'),(90,'HolaQuetal',NULL,NULL,'2023-10-11 15:31:38'),(91,'HolaQuetal',NULL,NULL,'2023-10-11 15:34:12'),(92,'HolaQuetal',NULL,NULL,'2023-10-11 15:36:55'),(93,'HolaQuetal',NULL,NULL,'2023-10-11 15:37:24'),(94,'HolaQuetal',NULL,NULL,'2023-10-11 15:38:47'),(95,'HolaQuetal',NULL,NULL,'2023-10-11 15:39:03'),(96,'HolaQuetal',NULL,NULL,'2023-10-11 15:39:43'),(97,'HolaQuetal',NULL,NULL,'2023-10-11 15:40:32'),(98,'HolaQuetal',NULL,NULL,'2023-10-11 15:41:36'),(99,'HolaQuetal',NULL,NULL,'2023-10-11 15:43:17'),(100,'HolaQuetal',NULL,NULL,'2023-10-11 15:47:56'),(101,'HolaQuetal',NULL,NULL,'2023-10-11 15:48:56'),(102,'HolaQuetal',NULL,NULL,'2023-10-11 15:49:45'),(103,'HolaQuetal',NULL,NULL,'2023-10-11 15:51:42'),(104,'HolaQuetal',NULL,NULL,'2023-10-11 15:51:58'),(105,'HolaQuetal',NULL,NULL,'2023-10-11 15:52:30'),(106,'HolaQuetal',NULL,NULL,'2023-10-11 17:13:10'),(107,'HolaQuetal',NULL,NULL,'2023-10-11 17:13:27'),(108,'HolaQuetal',NULL,NULL,'2023-10-11 17:14:34'),(109,'HolaQuetal',NULL,NULL,'2023-10-11 17:16:12'),(110,'HolaQuetal',NULL,NULL,'2023-10-11 17:16:51'),(111,'HolaQuetal',NULL,NULL,'2023-10-11 17:17:07'),(112,'HolaQuetal',NULL,NULL,'2023-10-11 17:17:27'),(113,'HolaQuetal',NULL,NULL,'2023-10-11 17:29:47'),(114,'HolaQuetal',NULL,NULL,'2023-10-11 17:34:26'),(115,'HolaQuetal',NULL,NULL,'2023-10-11 17:39:51'),(116,'HolaQuetal',NULL,NULL,'2023-10-13 10:48:20'),(117,'HolaQuetal',NULL,NULL,'2023-10-13 11:00:05'),(118,'HolaQuetal',NULL,NULL,'2023-10-13 11:00:22'),(119,'HolaQuetal',NULL,NULL,'2023-10-13 11:03:26'),(120,'HolaQuetal',NULL,NULL,'2023-10-13 11:08:31'),(121,'HolaQuetal',NULL,NULL,'2023-10-13 11:20:33'),(122,'HolaQuetal',NULL,NULL,'2023-10-13 11:22:23'),(123,'HolaQuetal',NULL,NULL,'2023-10-13 11:42:59'),(124,'HolaQuetal',NULL,NULL,'2023-10-13 11:54:25'),(125,'HolaQuetal',NULL,NULL,'2023-10-13 11:55:08'),(126,'HolaQuetal',NULL,NULL,'2023-10-13 13:28:21'),(127,'HolaQuetal',NULL,NULL,'2023-10-13 16:52:09'),(128,'HolaQuetal',NULL,NULL,'2023-10-14 22:03:08'),(129,'HolaQuetal',NULL,NULL,'2023-10-16 12:47:47');
/*!40000 ALTER TABLE `mensajes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `miembrosgrupos`
--

DROP TABLE IF EXISTS `miembrosgrupos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `miembrosgrupos` (
  `usuario_id` int DEFAULT NULL,
  `grupo_id` int DEFAULT NULL,
  `rol` enum('admin','miembro') NOT NULL DEFAULT 'miembro',
  KEY `usuario_id` (`usuario_id`),
  KEY `grupo_id` (`grupo_id`),
  CONSTRAINT `miembrosgrupos_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `miembrosgrupos_ibfk_2` FOREIGN KEY (`grupo_id`) REFERENCES `grupos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `miembrosgrupos`
--

LOCK TABLES `miembrosgrupos` WRITE;
/*!40000 ALTER TABLE `miembrosgrupos` DISABLE KEYS */;
/*!40000 ALTER TABLE `miembrosgrupos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre_usuario` varchar(255) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `conectado` tinyint(1) DEFAULT NULL,
  `grupo_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre_usuario` (`nombre_usuario`),
  KEY `grupo_id` (`grupo_id`),
  CONSTRAINT `usuarios_ibfk_1` FOREIGN KEY (`grupo_id`) REFERENCES `grupos` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'vane','123456',NULL,NULL),(5,'kevin','123456',NULL,NULL),(6,'geri','123456',NULL,NULL),(7,'toto','123456',NULL,NULL),(8,'nepe','123456',NULL,NULL),(9,'tutu','123456',NULL,NULL),(10,'nuevo2','cuaccuac',NULL,NULL),(14,'titi','123456',NULL,NULL),(15,'tito','123456',NULL,NULL),(16,'turururu','123456',NULL,NULL),(17,'tilotilo','123456',NULL,NULL),(18,'jihihcoksjcjkhshic','hcfjwhodiqiofdh',NULL,NULL),(20,'viena','hhaihcagc',NULL,NULL);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-17 20:01:24
