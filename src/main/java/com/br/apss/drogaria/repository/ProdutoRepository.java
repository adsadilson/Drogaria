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

import com.br.apss.drogaria.model.Produto;
import com.br.apss.drogaria.model.filter.ProdutoFilter;
import com.br.apss.drogaria.util.jsf.NegocioException;

public class ProdutoRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Produto salvar(Produto obj) {
		return manager.merge(obj);
	}

	public void excluir(Produto obj) {
		try {
			obj = porId(obj.getId());
			manager.remove(obj);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("Produto n�o pode ser exclu�da");
		}
	}

	public Produto porId(Long id) {
		return manager.find(Produto.class, id);
	}

	public List<Produto> listarTodos() {
		return manager.createQuery("from Produto order by nome", Produto.class).getResultList();
	}

	public Produto porNome(String nome) {
		try {
			return manager.createQuery("from Produto where upper(nome) = :nome", Produto.class)
					.setParameter("nome", nome.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Produto> porListaProdutoPorNome(String nome) {
		return this.manager.createQuery("from Produto where upper(nome) like :nome", Produto.class)
				.setParameter("nome", nome.toUpperCase() + "%").getResultList();
	}

	@SuppressWarnings({ "deprecation" })
	private Criteria criarCriteriaParaFiltro(ProdutoFilter filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Produto.class);

		if (StringUtils.isNotBlank(filtro.getOrigem())) {

			Criterion p1 = Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE);
			Criterion p2 = Restrictions.ilike("descricao", filtro.getNome(), MatchMode.ANYWHERE);
			Criterion p3 = Restrictions.ilike("sku", filtro.getNome(), MatchMode.ANYWHERE);

			if (filtro.getOrigem().equals("principal")) {

				p1 = Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE);
				p2 = Restrictions.ilike("codigoBarra", filtro.getNome(), MatchMode.ANYWHERE);
				p3 = Restrictions.ilike("sku", filtro.getNome(), MatchMode.ANYWHERE);
				criteria.add(Restrictions.or(p1, p2, p3));

			}
		} else {
			if (StringUtils.isNotBlank(filtro.getNome())) {
				criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
			}

			if (StringUtils.isNotBlank(filtro.getSku())) {
				criteria.add(Restrictions.ilike("sku", filtro.getSku(), MatchMode.ANYWHERE));
			}

			if (StringUtils.isNotBlank(filtro.getCodigoBarra())) {
				criteria.add(Restrictions.ilike("codigoBarra", filtro.getCodigoBarra(), MatchMode.ANYWHERE));
			}

			if (filtro.getTipoProduto() != null) {
				criteria.add(Restrictions.eq("tipoProduto", filtro.getTipoProduto()));
			}
			if (filtro.getCategoria() != null) {
				criteria.add(
						Restrictions.eq("categoria", filtro.getCategoria()));
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
	public List<Produto> filtrados(ProdutoFilter filtro) {
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

	public int quantidadeFiltrados(ProdutoFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

	public Produto porCodigoBarra(String codigoBarra) {
		try {
			return manager.createQuery("from Produto where codigoBarra = :codigoBarra", Produto.class)
					.setParameter("codigoBarra", codigoBarra).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Produto> buscarPorCodigoNome(String value) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Produto.class);

		Criterion p1 = Restrictions.ilike("codigoBarra", value.toUpperCase(), MatchMode.ANYWHERE);
		Criterion p2 = Restrictions.ilike("nome", value.toUpperCase(), MatchMode.ANYWHERE);
		criteria.add(Restrictions.or(p1, p2));

		return criteria.addOrder(Order.asc("nome")).list();
	}

}
