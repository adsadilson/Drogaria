package com.br.apss.drogaria.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.br.apss.drogaria.model.Deposito;
import com.br.apss.drogaria.model.filter.DepositoFilter;
import com.br.apss.drogaria.util.jsf.NegocioException;

public class DepositoRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Deposito salvar(Deposito obj) {
		return manager.merge(obj);
	}

	public void excluir(Deposito obj) {
		try {
			obj = porId(obj.getId());
			manager.remove(obj);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("Deposito não pode ser excluído");
		}
	}

	public Deposito porId(Long id) {
		return manager.find(Deposito.class, id);
	}

	public List<Deposito> listarTodos() {
		return manager.createQuery("from Deposito order by nome", Deposito.class).getResultList();
	}

	public Deposito porNome(String nome) {
		try {
			return manager.createQuery("from Deposito where upper(nome) = :nome", Deposito.class)
					.setParameter("nome", nome.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings({ "deprecation" })
	private Criteria criarCriteriaParaFiltro(DepositoFilter filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Deposito.class);

		if (null != filtro) {
			if (null != filtro) {
				if (StringUtils.isNotBlank(filtro.getNome())) {
					criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
				}

				if (filtro.getStatus() != null) {
					criteria.add(Restrictions.eq("status", filtro.getStatus()));
				}
			}
		}

		return criteria;
	}

	@SuppressWarnings("unchecked")
	public List<Deposito> filtrados(DepositoFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		return criteria.addOrder(Order.asc("nome")).list();
	}

	public int quantidadeFiltrados(DepositoFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

}
