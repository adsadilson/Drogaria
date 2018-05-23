package com.br.apss.drogaria.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.br.apss.drogaria.model.Caixa;
import com.br.apss.drogaria.model.filter.CaixaFilter;
import com.br.apss.drogaria.util.jsf.NegocioException;


public class CaixaRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	public Caixa consultarCaixa(Caixa cx) {
		try {
			return em.createQuery("from Caixa where data = :data and responsavel =:responsavel", Caixa.class)
					.setParameter("data", cx.getData()).setParameter("responsavel", cx.getResponsavel())
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Caixa> filtrados(CaixaFilter filtro) {
		Session session = em.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Caixa.class);

		if (filtro.isStatus()) {
			criteria.add(Restrictions.ilike("status", "ABERTO", MatchMode.ANYWHERE));
		} else {
			criteria.add(Restrictions.ilike("status", "FECHADO", MatchMode.ANYWHERE));
		}

		if (filtro.getData() != null) {
			criteria.add(Restrictions.eq("data", filtro.getData()));
		}

		if (filtro.getInicial() != null && filtro.getFim() != null) {
			criteria.add(Restrictions.between("data", filtro.getInicial(), filtro.getFim()));
		}

		if (filtro.getResponsavel() != null) {
			criteria.add(Restrictions.eq("responsavel", filtro.getResponsavel()));
		}

		return criteria.addOrder(Order.asc("data")).list();
	}

	public Caixa porId(Long id) {
		return em.find(Caixa.class, id);
	}

	public void remover(Caixa obj) {
		try {
			obj = porId(obj.getId());
			em.remove(obj);
			em.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Caixa não pode ser excluído pois possui vinculo com outra tabela.");
		}
	}

	public Caixa save(Caixa obj) {
		return em.merge(obj);
	}

	public List<Caixa> todas() {
		return em.createQuery("from Caixa", Caixa.class).getResultList();
	}

}
