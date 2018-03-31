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

import com.br.apss.drogaria.model.CabContaAReceber;
import com.br.apss.drogaria.model.filter.CabContaAReceberFilter;
import com.br.apss.drogaria.util.jsf.NegocioException;

public class CabContaAReceberRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public CabContaAReceber salvar(CabContaAReceber obj) {
		return manager.merge(obj);
	}

	public void excluir(CabContaAReceber obj) {
		try {
			obj = porId(obj.getId());
			manager.remove(obj);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("CabContaAReceber não pode ser excluída");
		}
	}

	public CabContaAReceber porId(Long id) {
		return manager.find(CabContaAReceber.class, id);
	}

	public List<CabContaAReceber> listarTodos() {
		return manager.createQuery("from CabContaAReceber order by nome", CabContaAReceber.class).getResultList();
	}

	public CabContaAReceber porNome(String nome) {
		try {
			return manager.createQuery("from CabContaAReceber where upper(nome) = :nome", CabContaAReceber.class)
					.setParameter("nome", nome.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public CabContaAReceber porVinculo(Long vinculo) {
		try {
			return manager.createQuery("from CabContaAReceber where vinculo = :vinculo", CabContaAReceber.class)
					.setParameter("vinculo", vinculo).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings({ "deprecation" })
	private Criteria criarCriteriaParaFiltro(CabContaAReceberFilter filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(CabContaAReceber.class);

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
				if (filtro.getStatus()) {
					criteria.add(Restrictions.eq("status", true));
				} else {
					criteria.add(Restrictions.eq("status", false));
				}

			}
		}
		return criteria;
	}

	@SuppressWarnings("unchecked")
	public List<CabContaAReceber> filtrados(CabContaAReceberFilter filtro) {
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

		return criteria.addOrder(Order.asc("nome")).list();
	}

	public int quantidadeFiltrados(CabContaAReceberFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

}
