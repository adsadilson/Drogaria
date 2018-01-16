package com.br.apss.drogaria.repository;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;


public class VinculoRepository implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	@Inject
	private EntityManager em;

	@SuppressWarnings({ "rawtypes" })
	public Boolean porVinculo(Class clazz, Long value) {
		try {
			long valor = (long) em
					.createQuery("select count(*) from " + clazz.getName() + " where vinculo=:value")
					.setParameter("value", value).getSingleResult();
			System.out.println(valor);
			return valor > 0 ? true : false;
		} catch (NoResultException e) {
			return null;
		}
	}

}
