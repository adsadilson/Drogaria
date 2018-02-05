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

import com.br.apss.drogaria.enums.TipoConta;
import com.br.apss.drogaria.enums.TipoRelatorio;
import com.br.apss.drogaria.model.PlanoConta;
import com.br.apss.drogaria.model.filter.PlanoContaFilter;
import com.br.apss.drogaria.util.jsf.NegocioException;

public class PlanoContaRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public PlanoConta salvar(PlanoConta obj) {
		return manager.merge(obj);
	}

	public void excluir(PlanoConta obj) {
		try {
			obj = porId(obj.getId());
			manager.remove(obj);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("Plano de Conta não pode ser excluída");
		}
	}

	public PlanoConta porId(Long id) {
		return manager.find(PlanoConta.class, id);
	}

	public List<PlanoConta> listarTodos() {
		return manager.createQuery("from PlanoConta order by nome", PlanoConta.class).getResultList();
	}

	public PlanoConta porNome(String nome) {
		try {
			return manager.createQuery("from PlanoConta where upper(nome) = :nome", PlanoConta.class)
					.setParameter("nome", nome.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public PlanoConta porNomeTipo(String nome, TipoConta tipo) {
		try {
			return manager.createQuery("from PlanoConta where nome = :nome and tipo=:tipo", PlanoConta.class)
					.setParameter("nome", nome).setParameter("tipo", tipo).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public PlanoConta porMascara(String mascara) {
		try {
			return manager.createQuery("from PlanoConta where mascara = :mascara", PlanoConta.class)
					.setParameter("mascara", mascara).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<PlanoConta> listarContasPais(PlanoConta contaPai, TipoConta tipo, TipoRelatorio categoria) {
		return manager.createQuery("from PlanoConta where contaPai = :contaPai and tipo = :tipo "
				+ "and categoria = :categoria", PlanoConta.class)
				.setParameter("contaPai", contaPai)
				.setParameter("tipo", tipo)
				.setParameter("categoria", categoria).getResultList();
	}

	@SuppressWarnings({ "deprecation" })
	private Criteria criarCriteriaParaFiltro(PlanoContaFilter filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(PlanoConta.class);

		if (StringUtils.isNotBlank(filtro.getNome())) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
		}

		if (filtro.getTipo() != null) {
			criteria.add(Restrictions.eq("tipo", filtro.getTipo()));
		}
		
		if (filtro.getCategoria() != null) {
			criteria.add(Restrictions.eq("categoria", filtro.getCategoria()));
		}

		if (filtro.getStatus() != null) {
			criteria.add(Restrictions.eq("status", filtro.getStatus()));
		}
		
		if (filtro.getPlanoContaPai() != null) {
			criteria.add(Restrictions.eq("contaPai", filtro.getPlanoContaPai()));
		}
		
		if (filtro.getPlanoConta() != null) {
			criteria.add(Restrictions.eq("id", filtro.getPlanoConta().getId()));
		}

		return criteria;
	}

	@SuppressWarnings("unchecked")
	public List<PlanoConta> filtrados(PlanoContaFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		return criteria.addOrder(Order.asc("mascara")).list();
	}

	public int quantidadeFiltrados(PlanoContaFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

}
