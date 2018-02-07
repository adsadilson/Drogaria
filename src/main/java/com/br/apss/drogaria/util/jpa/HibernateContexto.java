package com.br.apss.drogaria.util.jpa;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class HibernateContexto implements ServletContextListener {


	private EntityManagerFactory factory;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		factory.close();
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		factory = Persistence.createEntityManagerFactory("drogariaPU");
	}

}
