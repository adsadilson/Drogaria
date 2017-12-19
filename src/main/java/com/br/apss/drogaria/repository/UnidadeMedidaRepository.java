package com.br.apss.drogaria.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.br.apss.drogaria.enums.Status;
import com.br.apss.drogaria.model.UnidadeMedida;
import com.br.apss.drogaria.model.filter.UnidadeMedidaFilter;
import com.br.apss.drogaria.util.jsf.NegocioException;

public class UnidadeMedidaRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public UnidadeMedida salvar(UnidadeMedida obj) {
		return manager.merge(obj);
	}

	public void excluir(UnidadeMedida obj) {
		try {
			obj = porId(obj.getId());
			manager.remove(obj);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("Unidade de Medida n�o pode ser exclu�da");
		}
	}

	public UnidadeMedida porId(Long id) {
		return manager.find(UnidadeMedida.class, id);
	}

	public List<UnidadeMedida> listarTodos() {
		return manager.createQuery("from UnidadeMedida order by nome", UnidadeMedida.class).getResultList();
	}

	public UnidadeMedida porNome(String nome) {
		try {
			return manager.createQuery("from UnidadeMedida where upper(nome) = :nome", UnidadeMedida.class)
					.setParameter("nome", nome.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings({ "deprecation" })
	private Criteria criarCriteriaParaFiltro(UnidadeMedidaFilter filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(UnidadeMedida.class);

		if (StringUtils.isNotBlank(filtro.getOrigem())) {

			Criterion p1 = Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE);
			Criterion p2 = Restrictions.ilike("descricao", filtro.getNome(), MatchMode.ANYWHERE);

			if (filtro.getOrigem().equals("principal")) {

				p1 = Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE);
				p2 = Restrictions.ilike("descricao", filtro.getNome(), MatchMode.ANYWHERE);
				criteria.add(Restrictions.or(p1, p2));

			}
		} else {
			if (StringUtils.isNotBlank(filtro.getNome())) {
				criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
			}

			if (StringUtils.isNotBlank(filtro.getDescricao())) {
				criteria.add(Restrictions.ilike("descricao", filtro.getDescricao(), MatchMode.ANYWHERE));
			}

			if (filtro.getStatus() != null) {
				if (filtro.getStatus() == Status.ATIVO) {
					criteria.add(Restrictions.eq("status", true));
				} else {
					criteria.add(Restrictions.eq("status", false));
				}

			}
		}
		return criteria;
	}

	@SuppressWarnings("unchecked")
	public List<UnidadeMedida> filtrados(UnidadeMedidaFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);

		criteria.setFirstResult(filtro.getPrimeiroRegistro());
		criteria.setMaxResults(filtro.getQuantidadeRegistros());

		if (filtro.getCampoOrdenacao() != null) {
			if (filtro.isAscendente()) {
				criteria.addOrder(Order.asc(filtro.getCampoOrdenacao()));
			} else {
				criteria.addOrder(Order.desc(filtro.getCampoOrdenacao()));
			}
		}

		return criteria.list();
	}

	public int quantidadeFiltrados(UnidadeMedidaFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

}
