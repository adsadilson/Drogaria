package com.br.apss.drogaria.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.br.apss.drogaria.model.ContaAReceber;
import com.br.apss.drogaria.model.Movimentacao;
import com.br.apss.drogaria.model.filter.ContaAReceberFilter;
import com.br.apss.drogaria.util.jsf.NegocioException;

public class ContaAReceberRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public ContaAReceber save(ContaAReceber e) {
		return manager.merge(e);
	}

	public void remover(ContaAReceber movto) {
		try {
			movto = porId(movto.getId());
			manager.remove(movto);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Conta a Receber não pode ser excluído pois possui vinculo com outra tabela.");
		}
	}

	public ContaAReceber porId(Long value) {
		return manager.find(ContaAReceber.class, value);
	}

	public List<ContaAReceber> listarTodos() {
		return manager.createQuery("from ContaAReceber order by dataDoc", ContaAReceber.class).getResultList();
	}

	@SuppressWarnings({ "deprecation" })
	public Criteria criarCriteriaParaFiltro(ContaAReceberFilter filtro) {
		Session session = manager.unwrap(Session.class);

		Criteria criteria = session.createCriteria(ContaAReceber.class);

		criteria.add(Restrictions.gt("valorApagar", BigDecimal.ZERO));

		criteria.createAlias("cliente", "cliente", Criteria.INNER_JOIN);
		if (filtro.getCliente() != null) {
			Criterion p1 = Restrictions.eq("cliente.id", filtro.getCliente().getId());
			criteria.add(p1);
		}

		if (StringUtils.isNotBlank(filtro.getDoc())) {
			criteria.add(Restrictions.ilike("numDoc", filtro.getDoc(), MatchMode.ANYWHERE));
		}

		if (filtro.getDataEmissaoIni() != null) {
			criteria.add(Restrictions.ge("dataDoc", filtro.getDataEmissaoIni()));
		}

		if (filtro.getDataEmissaoFim() != null) {
			criteria.add(Restrictions.le("dataDoc", filtro.getDataEmissaoFim()));
		}

		if (filtro.getDataVenctoIni() != null) {
			criteria.add(Restrictions.ge("dataVencto", filtro.getDataVenctoIni()));
		}

		if (filtro.getDataVenctoFim() != null) {
			criteria.add(Restrictions.le("dataVencto", filtro.getDataVenctoFim()));
		}

		if (filtro.getValor1() != null) {
			criteria.add(Restrictions.ge("valor", filtro.getValor1()));
		}

		if (filtro.getValor2() != null) {
			criteria.add(Restrictions.le("valor", filtro.getValor2()));
		}

		return criteria;

	}

	@SuppressWarnings("unchecked")
	public List<ContaAReceber> filtrados(ContaAReceberFilter filtro) {

		Criteria criteria = criarCriteriaParaFiltro(filtro);

		criteria.setFirstResult(filtro.getPrimeiroRegistro());
		criteria.setMaxResults(filtro.getQtdeRegistro());

		criteria.addOrder(Order.asc("dataDoc")).addOrder(Order.asc("id"));

		return criteria.list();

	}

	public int quantidadeFiltrados(ContaAReceberFilter filtro) {

		Criteria criteria = criarCriteriaParaFiltro(filtro);

		criteria.setProjection(Projections.rowCount());

		return ((Number) criteria.uniqueResult()).intValue();
	}

	public ContaAReceber porVinculo(Long vinculo, Long id) {
		try {
			return manager.createQuery("from ContaAReceber where vinculo = :vinculo and id <>:id", ContaAReceber.class)
					.setParameter("vinculo", vinculo).setParameter("id", id).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public void excluirPorVinculo(Long id) {
		List<ContaAReceber> m = porVinculo(id);
		for (ContaAReceber contaAReceber : m) {
			remover(contaAReceber);
		}
	}

	public List<ContaAReceber> porVinculo(Long vinculo) {
		try {
			return manager.createQuery("from ContaAReceber where movimentacao_vinculo = :vinculo order by id",
					ContaAReceber.class).setParameter("vinculo", vinculo).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	public void updateNasContasAReceber(ContaAReceber obj) {
		manager.createNativeQuery(
				"update conta_areceber set valor_apagar = :valorApagar, " + "vinculo = :vinculo where id = :id")
				.setParameter("valorApagar", obj.getValorApagar()).setParameter("vinculo", obj.getVinculo())
				.setParameter("id", obj.getId()).executeUpdate();
	}

	public void cancelarRecebimento(ContaAReceber contaAReceber) {
		manager.createNativeQuery(
				"update conta_areceber set valor_apagar =:valorApagar, " + "vinculo =:vinculo where id = :id")
				.setParameter("valorApagar", contaAReceber.getValorApagar())
				.setParameter("vinculo", contaAReceber.getVinculo()).setParameter("id", contaAReceber.getId())
				.executeUpdate();
	}

	public void excluirContas(List<ContaAReceber> contas) throws Exception {

		// lista de movimentação
		List<Movimentacao> m = new ArrayList<>();

		// Exclusão da tabela cab_conta_areceber_conta_areceber
		for (ContaAReceber c : contas) {
			excluirCabCContaAReceber(c.getId());
		}

		for (ContaAReceber c : contas) {

			// carregar a lista de movimentação
			m = c.getMovimentacoes();

			c = manager.find(ContaAReceber.class, c.getId());
			excluirCabContaAReceber(c.getAgrupadorMovimentacao());
			excluirContaAReceberMovimentacao(c.getId());

			manager.remove(c);
		}
		// deleta os movimentos
		for (Movimentacao movto : m) {
			try {
				excluirMovimentacao(movto.getId());
			} catch (Exception e) {
				throw e;
			}
		}
	}

	private void excluirMovimentacao(Long id) {
		try {
			manager.createNativeQuery("delete from movimentacao where id = :id").setParameter("id", id).executeUpdate();
		} catch (Exception e) {
			throw e;
		}

	}

	private void excluirContaAReceberMovimentacao(Long id) {
		manager.createNativeQuery("delete from conta_areceber_movimentacao where conta_areceber_id = :id")
				.setParameter("id", id).executeUpdate();

	}

	public void excluirCabContaAReceber(Long id) {
		manager.createNativeQuery("delete from cab_conta_areceber where vinculo = :id").setParameter("id", id)
				.executeUpdate();
	}

	public void excluirCabCContaAReceber(Long id) {
		manager.createNativeQuery("delete from cab_conta_areceber_conta_areceber where conta_areceber_id = :id")
				.setParameter("id", id).executeUpdate();
	}

}
