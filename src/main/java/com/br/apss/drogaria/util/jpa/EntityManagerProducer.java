package com.br.apss.drogaria.util.jpa;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;

@ApplicationScoped
public class EntityManagerProducer {

    @Inject
    private PersistenceProperties properties;
    
    private EntityManagerFactory factory;

    @PostConstruct
    public void postConstruct() {
        this.factory = Persistence.createEntityManagerFactory("drogariaPU", properties.get());
    }

    @Produces
    @RequestScoped
    public Session  createEntityManager() {
		return (Session) this.factory.createEntityManager();
	}

    public void closeEntityManager(@Disposes EntityManager manager) {
        manager.close();
    }
}