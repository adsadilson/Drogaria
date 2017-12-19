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
import com.br.apss.drogaria.model.PermissaoGrupo;
import com.br.apss.drogaria.model.filter.PermissaoGrupoUserFilter;
import com.br.apss.drogaria.util.jsf.NegocioException;

public class PermissaoGrupoRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public PermissaoGrupo salvar(PermissaoGrupo obj) {
		return manager.merge(obj);
	}

	public void excluir(PermissaoGrupo obj) {
		try {
			obj = porId(obj.getId());
			manager.remove(obj);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("Permissão não pode ser excluída");
		}
	}

	public PermissaoGrupo porId(Long id) {
		return manager.find(PermissaoGrupo.class, id);
	}

	public List<PermissaoGrupo> listarTodos() {
		return manager.createQuery("from PermissaoGrupoUser order by tela", PermissaoGrupo.class).getResultList();
	}

	public PermissaoGrupo porNome(String tela) {
		try {
			return manager.createQuery("from PermissaoGrupoUser where upper(tela) = :tela", PermissaoGrupo.class)
					.setParameter("tela", tela.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings({ "deprecation" })
	private Criteria criarCriteriaParaFiltro(PermissaoGrupoUserFilter filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(PermissaoGrupo.class);

		if (StringUtils.isNotBlank(filtro.getOrigem())) {

			Criterion p1 = Restrictions.ilike("tela", filtro.getNome(), MatchMode.ANYWHERE);
			Criterion p2 = Restrictions.ilike("descricao", filtro.getNome(), MatchMode.ANYWHERE);

			if (filtro.getOrigem().equals("principal")) {

				p1 = Restrictions.ilike("tela", filtro.getNome(), MatchMode.ANYWHERE);
				p2 = Restrictions.ilike("descricao", filtro.getNome(), MatchMode.ANYWHERE);
				criteria.add(Restrictions.or(p1, p2));

			}
		} else {
			if (StringUtils.isNotBlank(filtro.getNome())) {
				criteria.add(Restrictions.ilike("tela", filtro.getNome(), MatchMode.ANYWHERE));
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
	public List<PermissaoGrupo> filtrados(PermissaoGrupoUserFilter filtro) {
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

	public int quantidadeFiltrados(PermissaoGrupoUserFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

}
