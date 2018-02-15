package com.br.apss.drogaria.util.jpa;

import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGPoolingDataSource;

public class FlyWay {
	
	public static void uploadBaseDado() {
		PGPoolingDataSource dataSource = new PGPoolingDataSource();
		dataSource.setUser("postgres");
		dataSource.setPassword("postgres");
		dataSource.setDatabaseName("db-drogaria");
		dataSource.setInitialConnections(10);
		dataSource.setPortNumber(5432);
		dataSource.setServerName("localhost");

		// Inicializado do FlyWay
		Flyway flyway = new Flyway();
		flyway.setDataSource(dataSource);
		flyway.setBaselineOnMigrate(true);
		flyway.setTable("version");
		flyway.setSqlMigrationPrefix("V");
		flyway.setSqlMigrationSeparator("_");
		flyway.setEncoding("ISO-8859-1");
		flyway.setValidateOnMigrate(true);

		flyway.migrate();
		System.out.println("carregou o flyway...");
	}
}
