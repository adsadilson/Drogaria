package com.br.apss.drogaria.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import com.br.apss.drogaria.model.Movimentacao;
import com.br.apss.drogaria.model.Pagamento;
import com.br.apss.drogaria.model.filter.PagamentoFilter;
import com.br.apss.drogaria.util.jsf.NegocioException;

public class PagamentoRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Pagamento salvar(Pagamento obj) {
		return manager.merge(obj);
	}

	public List<Pagamento> save(List<Pagamento> list) {
		List<Pagamento> retorno = new ArrayList<>();
		for (Pagamento m : list) {
			retorno.add(manager.merge(m));
		}
		return retorno;
	}

	public void excluir(Pagamento obj) {
		try {
			obj = porId(obj.getId());
			manager.remove(obj);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("Pagamento não pode ser excluída " + e.getCause().getCause());
		}
	}

	public void excluirListaPagto(List<Pagamento> obj) {
		try {

			for (Pagamento pagamento : obj) {
				//excluirPagamentoMovimentacao(pagamento.getId());
			}

			List<Movimentacao> movto = obj.get(0).getListaMovimentacoes();
			for (Movimentacao m : movto) {
				excluirMovimentacao(m.getId());
			}

			for (Pagamento p : obj) {
				Pagamento pg = porId(p.getId());
				manager.remove(pg);
				manager.flush();
			}

		} catch (Exception e) {
			throw new NegocioException("Pagamento não pode ser excluída " + e.getCause().getCause());
		}
	}

	public void excluirPagtoMovimentacao(Long id)  {
		try {
			manager.createNativeQuery("delete from pagamento_movimentacao where pagamento_id = :id")
					.setParameter("id", id).executeUpdate();
		} catch (Exception e) {
			throw e;
		}
	}

	public void excluirMovimentacao(Long id)  {
		try {
			manager.createNativeQuery("delete from movimentacao where id = :id").setParameter("id", id).executeUpdate();
		} catch (Exception e) {
			throw e;
		}
	}

	public void excluirPorVinculo(Long vinculo) {
		try {
			manager.createNativeQuery("delete from Pagamento where conta_apagar_vinculo = :vinculo")
					.setParameter("vinculo", vinculo).executeUpdate();
		} catch (Exception e) {
			throw e;
		}
	}

	public void excluirPagamentoContaApagar(Long id) {
		try {
			manager.createNativeQuery("delete from pagamento_conta_apagar where pagamento_id = :id")
					.setParameter("id", id).executeUpdate();
		} catch (Exception e) {
			throw e;
		}
	}

	public void excluirPagamentoMovimentacao(Long id) {
		try {
			manager.createNativeQuery("delete from pagamento_movimentacao where pagamento_id = :id").setParameter("id", id)
					.executeUpdate();
		} catch (Exception e) {
			throw e;
		}
	}

	public Pagamento porId(Long id) {
		return manager.find(Pagamento.class, id);
	}

	@SuppressWarnings("rawtypes")
	public Pagamento buscarPagamentoPorVinculo(Long vinculo) {
		Query query = (Query) manager.createNativeQuery(
				"select * from pagamento where conta_apagar_vinculo =:vinculo order by id desc limit 1",
				Pagamento.class).setParameter("vinculo", vinculo);
		return (Pagamento) query.getSingleResult();
	}

	public List<Pagamento> listarTodos() {
		return manager.createQuery("from Pagamento order by nome", Pagamento.class).getResultList();
	}

	public List<Pagamento> porVinculo(Long vinculo) {
		try {
			return manager
					.createQuery("from Pagamento where conta_apagar_vinculo = :vinculo order by id", Pagamento.class)
					.setParameter("vinculo", vinculo).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Pagamento porNome(String nome) {
		try {
			return manager.createQuery("from Pagamento where upper(nome) = :nome", Pagamento.class)
					.setParameter("nome", nome.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings({ "deprecation" })
	private Criteria criarCriteriaParaFiltro(PagamentoFilter filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Pagamento.class);

		/*
		 * criteria.createAlias("listaContaAPagars", "listaContaAPagars",
		 * Criteria.INNER_JOIN);
		 * 
		 * Criterion p1 = Restrictions.eq("listaContaAPagars.conta_apagar_id",
		 * filtro.getPlanoConta().getId()); criteria.add(p1);
		 */

		if (null != filtro.getFornecedor()) {
			criteria.add(Restrictions.eq("fornecedor", true));
		}

		if (filtro.getDtIni() != null) {
			criteria.add(Restrictions.ge("dataPago", filtro.getDtIni()));
		}

		if (filtro.getDtFim() != null) {
			criteria.add(Restrictions.le("dataPago", filtro.getDtFim()));
		}

		if (filtro.getValor1() != null) {
			criteria.add(Restrictions.ge("valorPago", filtro.getValor1()));
		}

		if (filtro.getValor2() != null) {
			criteria.add(Restrictions.le("valorPago", filtro.getValor2()));
		}

		return criteria;
	}

	@SuppressWarnings("unchecked")
	public List<Pagamento> filtrados(PagamentoFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		return criteria.addOrder(Order.asc("dataPago")).list();
	}

	public int quantidadeFiltrados(PagamentoFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

}
