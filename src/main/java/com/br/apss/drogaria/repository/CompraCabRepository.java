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

import com.br.apss.drogaria.model.CompraCab;
import com.br.apss.drogaria.model.filter.CompraCabFilter;
import com.br.apss.drogaria.util.jsf.NegocioException;

public class CompraCabRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public CompraCab salvar(CompraCab obj) {
		return manager.merge(obj);
	}

	public void excluir(CompraCab obj) {
		try {
			obj = porId(obj.getId());
			manager.remove(obj);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("Compra não pode ser excluído");
		}
	}

	public CompraCab porId(Long id) {
		return manager.find(CompraCab.class, id);
	}

	public CompraCab porDocumento(String documento) {
		try {
			return manager.createQuery("from CompraCab where documento = :documento", CompraCab.class)
					.setParameter("documento", documento).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public CompraCab buscarCompraComItens(Long id) {
		return (CompraCab) manager.createQuery("select p from CompraCab p JOIN p.itens i where p.id =:id")
				.setParameter("id", id).getSingleResult();
	}


	@SuppressWarnings("unchecked")
	public List<CompraCab> filtrados(CompraCabFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);

		criteria.setFirstResult(filtro.getPriRegistro());
		criteria.setMaxResults(filtro.getQtdeRegistros());

		if (filtro.getCampoOrder() != null) {
			if (filtro.isAsc()) {
				criteria.addOrder(Order.asc(filtro.getCampoOrder()));
			} else {
				criteria.addOrder(Order.desc(filtro.getCampoOrder()));
			}
		}

		return criteria.addOrder(Order.asc("id")).list();
	}

	@SuppressWarnings({ "deprecation" })
	private Criteria criarCriteriaParaFiltro(CompraCabFilter filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(CompraCab.class);

		// fazemos uma associação (join) com cliente e Vendedor nomeamos como "c
		// e v"
		criteria.createAlias("fornecedor", "f");
		
		if (StringUtils.isNotBlank(filtro.getDocumento())) {
			criteria.add(Restrictions.ilike("documento", filtro.getDocumento(), MatchMode.ANYWHERE));
		}


		if (filtro.getNumeroDe() != null) {
			// id deve ser maior ou igual (ge = greater or equals) a
			// filtro.numeroDe
			criteria.add(Restrictions.ge("id", filtro.getNumeroDe()));
		}

		if (filtro.getNumeroAte() != null) {
			// id deve ser menor ou igual (le = lower or equal) a
			// filtro.numeroDe
			criteria.add(Restrictions.le("id", filtro.getNumeroAte()));
		}

		if (filtro.getDataCriacaoDe() != null) {
			criteria.add(Restrictions.ge("dataCriacao", filtro.getDataCriacaoDe()));
		}

		if (filtro.getDataCriacaoAte() != null) {
			criteria.add(Restrictions.le("dataCriacao", filtro.getDataCriacaoAte()));
		}

		if (StringUtils.isNotBlank(filtro.getNomeCliente())) {
			// acessamos o nome do cliente associado ao compraCab pelo alias
			// "c",
			// criado anteriormente
			criteria.add(Restrictions.ilike("f.nome", filtro.getNomeCliente(), MatchMode.ANYWHERE));
		}

		if (StringUtils.isNotBlank(filtro.getNomeVendedor())) {
			// acessamos o nome do vendedor associado ao compraCab pelo alias
			// "v",
			// criado anteriormente
			criteria.add(Restrictions.ilike("u.nome", filtro.getNomeVendedor(), MatchMode.ANYWHERE));
		}

		if (filtro.getStatuses() != null && filtro.getStatuses().length > 0) {
			// adicionamos uma restrição "in", passando um array de constantes
			// da enum StatusCompraCab
			// criteria.add(Restrictions.in("status", filtro.getStatuses()));
		}

		return criteria;
	}

	public int qtdeFiltrados(CompraCabFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

}
