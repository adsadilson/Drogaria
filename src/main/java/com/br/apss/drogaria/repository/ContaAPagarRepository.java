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

import com.br.apss.drogaria.model.ContaAPagar;
import com.br.apss.drogaria.model.filter.ContaAPagarFilter;
import com.br.apss.drogaria.util.jsf.NegocioException;

public class ContaAPagarRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public ContaAPagar salvar(ContaAPagar obj) {
		return manager.merge(obj);
	}

	public void excluir(ContaAPagar obj) {
		try {
			obj = porId(obj.getId());
			manager.remove(obj);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("Conta A Pagar não pode ser excluída");
		}
	}

	public ContaAPagar porId(Long id) {
		return manager.find(ContaAPagar.class, id);
	}

	public List<ContaAPagar> listarTodos() {
		return manager.createQuery("from ContaAPagar order by nome", ContaAPagar.class).getResultList();
	}

	public ContaAPagar porNome(String nome) {
		try {
			return manager.createQuery("from ContaAPagar where upper(nome) = :nome", ContaAPagar.class)
					.setParameter("nome", nome.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public ContaAPagar porCpf(String cpf) {
		try {
			return manager.createQuery("from ContaAPagar where cpf_cnpj = :cpf", ContaAPagar.class).setParameter("cpf", cpf)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings({ "deprecation" })
	private Criteria criarCriteriaParaFiltro(ContaAPagarFilter filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(ContaAPagar.class);


		if (StringUtils.isNotBlank(filtro.getOrigem())) {

			if (filtro.getOrigem().equals("principal")) {

				Criterion p1 = Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE);
				Criterion p2 = Restrictions.ilike("cpfCnpj", filtro.getNome(), MatchMode.ANYWHERE);
				criteria.add(Restrictions.or(p1, p2));

			}
		} else {
			if (StringUtils.isNotBlank(filtro.getNome())) {
				criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
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
	public List<ContaAPagar> filtrados(ContaAPagarFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);

		criteria.setFirstResult(filtro.getPrimerioRegistro());
		criteria.setMaxResults(filtro.getQuantidadeRegistros());

		if (filtro.getCampoOrdernacao() != null) {
			if (filtro.isAsc()) {
				criteria.addOrder(Order.asc(filtro.getCampoOrdernacao()));
			} else {
				criteria.addOrder(Order.desc(filtro.getCampoOrdernacao()));
			}
		}

		return criteria.addOrder(Order.asc("nome")).list();
	}

	public int quantidadeFiltrados(ContaAPagarFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

}
