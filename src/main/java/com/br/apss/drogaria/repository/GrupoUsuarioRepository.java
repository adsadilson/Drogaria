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

import com.br.apss.drogaria.model.GrupoUsuario;
import com.br.apss.drogaria.model.filter.GrupoUsuarioFilter;
import com.br.apss.drogaria.util.jsf.NegocioException;

public class GrupoUsuarioRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public GrupoUsuario salvar(GrupoUsuario obj) {
		return manager.merge(obj);
	}

	public void excluir(GrupoUsuario obj) {
		try {
			obj = porId(obj.getId());
			manager.remove(obj);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("Grupo de Usuario de Produto não pode ser excluído");
		}
	}

	public GrupoUsuario porId(Long id) {
		return manager.find(GrupoUsuario.class, id);
	}

	public List<GrupoUsuario> listarTodos() {
		return manager.createQuery("from GrupoUsuario order by nome", GrupoUsuario.class).getResultList();
	}

	public GrupoUsuario porNome(String nome) {
		try {
			return manager.createQuery("from GrupoUsuario where upper(nome) = :nome", GrupoUsuario.class)
					.setParameter("nome", nome.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings({ "deprecation" })
	private Criteria criarCriteriaParaFiltro(GrupoUsuarioFilter filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(GrupoUsuario.class);

		if (StringUtils.isNotBlank(filtro.getNome())) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
		}

		if (filtro.getStatus() != null) {
			criteria.add(Restrictions.eq("status", filtro.getStatus()));
		}

		return criteria;
	}

	@SuppressWarnings("unchecked")
	public List<GrupoUsuario> filtrados(GrupoUsuarioFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);

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

		if (filtro.getCampoOrdenacao() != null) {

			if (filtro.isAscendente()) {
				criteria.addOrder(Order.asc(filtro.getCampoOrdenacao()));
			} else {
				criteria.addOrder(Order.desc(filtro.getCampoOrdenacao()));
			}
		}

		return criteria.addOrder(Order.asc("nome")).list();
	}

	public int quantidadeFiltrados(GrupoUsuarioFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

}
