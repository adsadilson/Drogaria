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
import com.br.apss.drogaria.model.Recebimento;
import com.br.apss.drogaria.model.filter.RecebimentoFilter;
import com.br.apss.drogaria.util.jsf.NegocioException;

public class RecebimentoRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Recebimento salvar(Recebimento obj) {
		return manager.merge(obj);
	}

	public List<Recebimento> save(List<Recebimento> list) {
		List<Recebimento> retorno = new ArrayList<>();
		for (Recebimento m : list) {
			retorno.add(manager.merge(m));
		}
		return retorno;
	}

	public void excluir(Recebimento obj) {
		try {
			obj = porId(obj.getId());
			manager.remove(obj);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("Recebimento não pode ser excluída " + e.getCause().getCause());
		}
	}

	public void excluirListaPagto(List<Recebimento> obj) {
		try {

			for (Recebimento Recebimento : obj) {
				excluirRecebimentoMovimentacao(Recebimento.getId());
			}

			List<Movimentacao> movto = obj.get(0).getListaMovimentacoes();
			for (Movimentacao m : movto) {
				excluirMovimentacao(m.getId());
			}

			for (Recebimento p : obj) {
				Recebimento pg = porId(p.getId());
				manager.remove(pg);
				manager.flush();
			}

		} catch (Exception e) {
			throw new NegocioException("Recebimento não pode ser excluída " + e.getCause().getCause());
		}
	}

	public void excluirRecebimentoMovimentacao(Long id) {
		try {
			manager.createNativeQuery("delete from recebimento_movimentacao where recebimento_id = :id")
					.setParameter("id", id).executeUpdate();
		} catch (Exception e) {
			throw e;
		}
	}

	public void excluirMovimentacao(Long id) {
		try {
			manager.createNativeQuery("delete from movimentacao where id = :id").setParameter("id", id).executeUpdate();
		} catch (Exception e) {
			throw e;
		}
	}

	public Recebimento porId(Long id) {
		return manager.find(Recebimento.class, id);
	}

	@SuppressWarnings("rawtypes")
	public Recebimento buscarRecebimentoPorVinculo(Long vinculo) {
		Query query = (Query) manager.createNativeQuery(
				"select * from Recebimento where conta_apagar_vinculo =:vinculo order by id desc limit 1",
				Recebimento.class).setParameter("vinculo", vinculo);
		return (Recebimento) query.getSingleResult();
	}

	public List<Recebimento> listarTodos() {
		return manager.createQuery("from Recebimento order by nome", Recebimento.class).getResultList();
	}

	public List<Recebimento> porVinculo(Long vinculo) {
		try {
			return manager.createQuery("from Recebimento where agrupadorContaApagar = :vinculo order by id",
					Recebimento.class).setParameter("vinculo", vinculo).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Recebimento porNome(String nome) {
		try {
			return manager.createQuery("from Recebimento where upper(nome) = :nome", Recebimento.class)
					.setParameter("nome", nome.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings({ "deprecation" })
	private Criteria criarCriteriaParaFiltro(RecebimentoFilter filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Recebimento.class);

		/*
		 * criteria.createAlias("listaContaAPagars", "listaContaAPagars",
		 * Criteria.INNER_JOIN);
		 * 
		 * Criterion p1 = Restrictions.eq("listaContaAPagars.conta_apagar_id",
		 * filtro.getPlanoConta().getId()); criteria.add(p1);
		 */

		if (null != filtro.getCliente()) {
			criteria.add(Restrictions.eq("cliente", true));
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
	public List<Recebimento> filtrados(RecebimentoFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		return criteria.addOrder(Order.asc("dataPago")).list();
	}

	public int quantidadeFiltrados(RecebimentoFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

	public void excluirRecebimentoContaAReceber(Long id) {
		try {
			manager.createNativeQuery("delete from recebimento_conta_areceber where recebimento_id = :id")
					.setParameter("id", id).executeUpdate();
		} catch (Exception e) {
			throw e;
		}
	}

	public void excluirPorVinculo(Long agrupadorContaAReceber) {
		// TODO Auto-generated method stub

	}

}
