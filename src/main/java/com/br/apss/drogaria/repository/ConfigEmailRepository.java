package com.br.apss.drogaria.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.br.apss.drogaria.model.ConfigEmail;
import com.br.apss.drogaria.util.jsf.NegocioException;

public class ConfigEmailRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public ConfigEmail salvar(ConfigEmail obj) {
		return manager.merge(obj);
	}

	public void excluir(ConfigEmail obj) {
		try {
			obj = porId(obj.getId());
			manager.remove(obj);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("Configura��o n�o pode ser exclu�da");
		}
	}

	public ConfigEmail porId(Long id) {
		return manager.find(ConfigEmail.class, id);
	}

	public List<ConfigEmail> listarTodos() {
		return manager.createQuery("from ConfigEmail order by login", ConfigEmail.class).getResultList();
	}

	public ConfigEmail emailEmUso() {
		try {
			return manager.createQuery("from ConfigEmail where status = 'true'", ConfigEmail.class).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
