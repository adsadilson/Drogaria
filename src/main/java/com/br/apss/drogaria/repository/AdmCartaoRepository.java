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
import com.br.apss.drogaria.model.AdmCartao;
import com.br.apss.drogaria.model.filter.AdmCartaoFilter;
import com.br.apss.drogaria.util.jsf.NegocioException;

public class AdmCartaoRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public AdmCartao salvar(AdmCartao obj) {
		return manager.merge(obj);
	}

	public void excluir(AdmCartao obj) {
		try {
			obj = porId(obj.getId());
			manager.remove(obj);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("Cartão não pode ser excluída");
		}
	}

	public AdmCartao porId(Long id) {
		return manager.find(AdmCartao.class, id);
	}

	public List<AdmCartao> listarTodos() {
		return manager.createQuery("from AdmCartao order by nome", AdmCartao.class).getResultList();
	}

	public AdmCartao porNome(String nome) {
		try {
			return manager.createQuery("from AdmCartao where upper(nome) = :nome", AdmCartao.class)
					.setParameter("nome", nome.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public AdmCartao porNomeTipo(String nome, TipoConta tipo) {
		try {
			return manager.createQuery("from AdmCartao where nome = :nome and tipo=:tipo", AdmCartao.class)
					.setParameter("nome", nome).setParameter("tipo", tipo).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public AdmCartao porMascara(String mascara) {
		try {
			return manager.createQuery("from AdmCartao where mascara = :mascara", AdmCartao.class)
					.setParameter("mascara", mascara).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<AdmCartao> listarContasPais(AdmCartao contaPai, TipoConta tipo, TipoRelatorio categoria) {
		return manager
				.createQuery(
						"from AdmCartao where contaPai = :contaPai and tipo = :tipo " + "and categoria = :categoria",
						AdmCartao.class)
				.setParameter("contaPai", contaPai).setParameter("tipo", tipo).setParameter("categoria", categoria)
				.getResultList();
	}

	public List<AdmCartao> listarContasPorTipoCategorias(TipoConta tipo, TipoRelatorio categoria) {
		return manager.createQuery("from AdmCartao where tipo = :tipo " + "and categoria = :categoria", AdmCartao.class)
				.setParameter("tipo", tipo).setParameter("categoria", categoria).getResultList();
	}

	@SuppressWarnings({ "deprecation" })
	private Criteria criarCriteriaParaFiltro(AdmCartaoFilter filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(AdmCartao.class);

		if (StringUtils.isNotBlank(filtro.getNome())) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
		}

		if (filtro.getTipo() != null) {
			criteria.add(Restrictions.eq("tipo", filtro.getTipo()));
		}

		if (filtro.getStatus() != null) {
			criteria.add(Restrictions.eq("status", filtro.getStatus()));
		}

		/*
		 * if (filtro.getAdmCartaoPai() != null) {
		 * criteria.add(Restrictions.eq("contaPai", filtro.getAdmCartaoPai())); }
		 * 
		 * if (filtro.getAdmCartao() != null) { criteria.add(Restrictions.eq("id",
		 * filtro.getAdmCartao().getId())); }
		 */

		return criteria;
	}

	@SuppressWarnings("unchecked")
	public List<AdmCartao> filtrados(AdmCartaoFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		return criteria.addOrder(Order.asc("nome")).list();
	}

	public int quantidadeFiltrados(AdmCartaoFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

}
