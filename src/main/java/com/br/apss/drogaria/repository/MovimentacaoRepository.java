package com.br.apss.drogaria.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.br.apss.drogaria.model.Movimentacao;
import com.br.apss.drogaria.model.filter.MovimentacaoFilter;
import com.br.apss.drogaria.util.jsf.NegocioException;



public class MovimentacaoRepository implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;

	public Movimentacao save(Movimentacao e) {
		return manager.merge(e);
	}
	
	public List<Movimentacao> save(List<Movimentacao> list) {
		List<Movimentacao> retorno = new ArrayList<>();
		for(Movimentacao m : list){
			retorno.add(manager.merge(m));
		}
		return retorno;
	}

	public void remover(Movimentacao movto) {
		try {
			movto = porId(movto.getId());
			manager.remove(movto);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Movimentação não pode ser excluído pois possui vinculo com outra tabela.");
		}
	}

	public Movimentacao porId(Long value) {
		return manager.find(Movimentacao.class, value);
	}

	public Movimentacao porNome(String descricao) {
		try {
			return manager.createQuery("from Movimentacao where descricao = :descricao", Movimentacao.class)
					.setParameter("descricao", descricao).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Movimentacao> listarTodos() {
		return manager.createQuery("from Movimentacao order by dataDoc", Movimentacao.class).getResultList();
	}

	@SuppressWarnings("unused")
	public BigDecimal pesquisaSaldo(MovimentacaoFilter filtro) {
		Session session = manager.unwrap(Session.class);

		if (filtro.getDataIni() != null) {

			String sql = "select sum(vlr_entrada) - sum(vlr_saida) as saldo "
					+ "from movimentacao where conta_id=:id and data_doc < :dtIni";

			Query query = manager.createNativeQuery(sql);
			query.setParameter("dtIni", filtro.getDataIni());
			query.setParameter("id", filtro.getContaID());
			return (BigDecimal) query.getSingleResult();

		}
		return null;

	}

	@SuppressWarnings({ "deprecation" })
	public Criteria criarCriteriaParaFiltro(MovimentacaoFilter filtro) {
		Session session = manager.unwrap(Session.class);

		Criteria criteria = session.createCriteria(Movimentacao.class);

		criteria.createAlias("conta", "conta", Criteria.INNER_JOIN);
		Criterion p1 = Restrictions.eq("conta.id", filtro.getContaID());
		criteria.add(p1);

		if (StringUtils.isNotBlank(filtro.getDoc())) {
			criteria.add(Restrictions.ilike("documento", filtro.getDoc(), MatchMode.ANYWHERE));
		}

		if (StringUtils.isNotBlank(filtro.getDescricao())) {
			criteria.add(Restrictions.ilike("descricao", filtro.getDescricao(), MatchMode.ANYWHERE));
		}

		if (filtro.getDataIni() != null) {
			criteria.add(Restrictions.ge("dataDoc", filtro.getDataIni()));
		}

		if (filtro.getDataFim() != null) {
			criteria.add(Restrictions.le("dataDoc", filtro.getDataFim()));
		}

		if (filtro.getEntrada1() != null) {
			criteria.add(Restrictions.ge("vlrEntrada", filtro.getEntrada1()));
		}

		if (filtro.getEntrada2() != null) {
			criteria.add(Restrictions.le("vlrEntrada", filtro.getEntrada2()));
		}

		if (filtro.getSaida1() != null) {
			criteria.add(Restrictions.ge("vlrSaida", filtro.getSaida1()));
		}

		if (filtro.getSaida2() != null) {
			criteria.add(Restrictions.le("vlrSaida", filtro.getSaida2()));
		}

		return criteria;

	}

	@SuppressWarnings("unchecked")
	public List<Movimentacao> filtrados(MovimentacaoFilter filtro) {

		Criteria criteria = criarCriteriaParaFiltro(filtro);

		criteria.setFirstResult(filtro.getPrimeiroRegistro());
		criteria.setMaxResults(filtro.getQtdeRegistro());

		criteria.addOrder(Order.asc("dataDoc")).addOrder(Order.asc("id"));

		return criteria.list();

	}

	public int quantidadeFiltrados(MovimentacaoFilter filtro) {

		Criteria criteria = criarCriteriaParaFiltro(filtro);

		criteria.setProjection(Projections.rowCount());

		return ((Number) criteria.uniqueResult()).intValue();
	}

	public Movimentacao porVinculo(Long vinculo, Long id) {
		try {
			return manager.createQuery("from Movimentacao where vinculo = :vinculo and id <>:id", Movimentacao.class)
					.setParameter("vinculo", vinculo).setParameter("id", id).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public void excluirPorVinculo(Long id) {
		List<Movimentacao> m = porVinculo(id);
		for (Movimentacao movimentacao : m) {
			remover(movimentacao);
		}
	}

	public List<Movimentacao> porVinculo(Long vinculo) {
		try {
			return manager.createQuery("from Movimentacao where vinculo = :vinculo", Movimentacao.class)
					.setParameter("vinculo", vinculo).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

}
