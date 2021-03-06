package com.br.apss.drogaria.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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

import com.br.apss.drogaria.model.ContaAPagar;
import com.br.apss.drogaria.model.Movimentacao;
import com.br.apss.drogaria.model.filter.ContaAPagarFilter;
import com.br.apss.drogaria.util.jsf.NegocioException;

public class ContaAPagarRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	List<Movimentacao> m = null;

	public ContaAPagar salvar(ContaAPagar obj) {
		return manager.merge(obj);
	}

	public List<ContaAPagar> salvar(List<ContaAPagar> list) {
		List<ContaAPagar> retorno = new ArrayList<>();
		for (ContaAPagar cp : list) {
			retorno.add(manager.merge(cp));
		}
		return retorno;
	}

	public void updateNasContasApagar(ContaAPagar obj) {
		manager.createNativeQuery(
				"update conta_apagar set valor_apagar = :valorApagar, " + "vinculo = :vinculo where id = :id")
				.setParameter("valorApagar", obj.getValorApagar()).setParameter("vinculo", obj.getVinculo())
				.setParameter("id", obj.getId()).executeUpdate();
	}

	public void cancelarPagto(ContaAPagar contaAPagar) {
		manager.createNativeQuery(
				"update conta_apagar set valor_apagar =:valorApagar, " + "vinculo =:vinculo where id = :id")
				.setParameter("valorApagar", contaAPagar.getValorApagar())
				.setParameter("vinculo", contaAPagar.getVinculo()).setParameter("id", contaAPagar.getId())
				.executeUpdate();
	}

	public void excluir(ContaAPagar obj) {
		try {
			obj = porId(obj.getId());
			manager.remove(obj);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("Conta A Pagar não pode ser excluída");
		}
	}

	public void excluirContas(List<ContaAPagar> contas) throws Exception {

		// lista de movimentação
		List<Movimentacao> m = new ArrayList<>();

		// Exclusão da tabela cab_conta_apagar_conta_apagar
		for (ContaAPagar c : contas) {
			excluirCabCContaApagar(c.getId());
		}

		for (ContaAPagar c : contas) {

			// carregar a lista de movimentação
			m = c.getMovimentacoes();

			c = manager.find(ContaAPagar.class, c.getId());
			excluirCabContaApagar(c.getAgrupadorMovimentacao());
			excluirContaApagarMovimentacao(c.getId());

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

	public void excluirContaApagarMovto(Long id) {
		manager.createNativeQuery("delete from conta_apagar_movimentacao where conta_apagar_id = :id")
				.setParameter("id", id).executeUpdate();
	}

	public void excluirCabContaApagar(Long id) {
		manager.createNativeQuery("delete from cab_conta_apagar where vinculo = :id").setParameter("id", id)
				.executeUpdate();
	}

	public void excluirCabCContaApagar(Long id) {
		manager.createNativeQuery("delete from cab_conta_apagar_conta_apagar where conta_apagar_id = :id")
				.setParameter("id", id).executeUpdate();
	}

	public void excluirPagamentoContaApagar(Long id) {

		Long p = (Long) manager
				.createNativeQuery("select pagamento_id from pagamento_conta_apagar where conta_apagar_id = :id")
				.setParameter("id", id).getSingleResult();

		manager.createNativeQuery("delete from pagamento_conta_apagar where conta_apagar_id = :id")
				.setParameter("id", id).executeUpdate();

		manager.createNativeQuery("delete from pagamento where pagamento_id = :p").setParameter("p", p).executeUpdate();

	}

	public void excluirContaApagarMovimentacao(Long id) {
		manager.createNativeQuery("delete from conta_apagar_movimentacao where conta_apagar_id = :id")
				.setParameter("id", id).executeUpdate();
	}

	public void excluirMovimentacao(Long id) throws Exception {
		try {
			manager.createNativeQuery("delete from movimentacao where id = :id").setParameter("id", id).executeUpdate();
		} catch (Exception e) {
			throw e;
		}
	}

	public ContaAPagar porId(Long id) {
		return manager.find(ContaAPagar.class, id);
	}

	public List<ContaAPagar> listarTodos() {
		return manager.createQuery("from ContaAPagar order by nome", ContaAPagar.class).getResultList();
	}

	public ContaAPagar porNome(String descricao) {
		try {
			return manager.createQuery("from ContaAPagar where upper(descricao) = :descricao", ContaAPagar.class)
					.setParameter("descricao", descricao.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public ContaAPagar porCpf(String cpf) {
		try {
			return manager.createQuery("from ContaAPagar where cpf_cnpj = :cpf", ContaAPagar.class)
					.setParameter("cpf", cpf).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<ContaAPagar> porVinculo(Long vinculo) {
		try {
			return manager.createQuery("from ContaAPagar where movimentacao_vinculo = :vinculo order by id",
					ContaAPagar.class).setParameter("vinculo", vinculo).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings({ "deprecation" })
	private Criteria criarCriteriaParaFiltro(ContaAPagarFilter filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(ContaAPagar.class);

		criteria.add(Restrictions.gt("valorApagar", BigDecimal.ZERO));

		criteria.createAlias("fornecedor", "fornecedor", Criteria.INNER_JOIN);

		if (filtro.getFornecedor() != null) {
			criteria.add(Restrictions.eq("fornecedor.id", filtro.getFornecedor().getId()));
		}

		if (StringUtils.isNotBlank(filtro.getNome())) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
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
	public List<ContaAPagar> filtrados(ContaAPagarFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);

		criteria.setFirstResult(filtro.getPrimerioRegistro());
		criteria.setMaxResults(filtro.getQuantidadeRegistros());

		if (filtro.getCampoOrdernacao() != null) {
			if (filtro.isAsc()) {
				criteria.addOrder(Order.asc(filtro.getCampoOrdernacao()));
			} else {
				criteria.addOrder(Order.desc(filtro.getCampoOrdernacao()));
			}
		}

		return criteria.addOrder(Order.asc("dataVencto")).list();
	}

	public int quantidadeFiltrados(ContaAPagarFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

}
