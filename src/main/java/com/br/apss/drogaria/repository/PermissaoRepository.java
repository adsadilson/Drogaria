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

import com.br.apss.drogaria.model.Permissao;
import com.br.apss.drogaria.model.filter.PermissaoFilter;
import com.br.apss.drogaria.util.jsf.NegocioException;

public class PermissaoRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Permissao salvar(Permissao obj) {
		return manager.merge(obj);
	}

	public void excluir(Permissao obj) {
		try {
			obj = porId(obj.getId());
			manager.remove(obj);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("Permissao de n„o pode ser exclu√≠da");
		}
	}

	public Permissao porId(Long id) {
		return manager.find(Permissao.class, id);
	}

	public List<Permissao> listarTodos() {
		return manager.createQuery("from Permissao order by id", Permissao.class).getResultList();
	}

	public List<Permissao> buscarPermissaoPorGrupo() {
		String jpql ="SELECT p FROM Permissao p right join p.controleMenu c";
		 return manager.createQuery(jpql, Permissao.class)
		        .getResultList ();
	}

	public Permissao porNome(String nome) {
		try {
			return manager.createQuery("from Permissao where upper(nome) = :nome", Permissao.class)
					.setParameter("nome", nome.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings({ "deprecation" })
	private Criteria criarCriteriaParaFiltro(PermissaoFilter filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Permissao.class);

		if (StringUtils.isNotBlank(filtro.getNome())) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
		}

		if (filtro.getStatus() != null) {
			criteria.add(Restrictions.eq("status", filtro.getStatus()));
		}

		return criteria;
	}

	@SuppressWarnings("unchecked")
	public List<Permissao> filtrados(PermissaoFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		return criteria.addOrder(Order.asc("nome")).list();
	}

	public int quantidadeFiltrados(PermissaoFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

}
