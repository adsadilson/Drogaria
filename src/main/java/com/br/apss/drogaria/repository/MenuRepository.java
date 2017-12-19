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

import com.br.apss.drogaria.model.Menu;
import com.br.apss.drogaria.model.filter.MenuFilter;
import com.br.apss.drogaria.util.jsf.NegocioException;

public class MenuRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Menu salvar(Menu obj) {
		return manager.merge(obj);
	}

	public void excluir(Menu obj) {
		try {
			obj = porId(obj.getId());
			manager.remove(obj);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("Menu não pode ser excluído.");
		}
	}

	public Menu porId(Long id) {
		return manager.find(Menu.class, id);
	}

	public List<Menu> listarTodos() {
		return manager.createQuery("from Menu order by funcao", Menu.class).getResultList();
	}

	public Menu porNome(String funcao) {
		try {
			return manager.createQuery("from Menu where upper(funcao) = :funcao", Menu.class)
					.setParameter("funcao", funcao.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings({ "deprecation" })
	private Criteria criarCriteriaParaFiltro(MenuFilter filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Menu.class);

		if (StringUtils.isNotBlank(filtro.getNome())) {
			criteria.add(Restrictions.ilike("funcao", filtro.getNome(), MatchMode.ANYWHERE));
		}

		if (filtro.getStatus() != null) {
			criteria.add(Restrictions.eq("status", filtro.getStatus()));
		}

		return criteria;
	}

	@SuppressWarnings("unchecked")
	public List<Menu> filtrados(MenuFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		return criteria.addOrder(Order.asc("funcao")).list();
	}

	public int quantidadeFiltrados(MenuFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

}
