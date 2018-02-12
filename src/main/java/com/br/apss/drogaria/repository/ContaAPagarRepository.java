package com.br.apss.drogaria.repository;

import java.io.Serializable;
import java.util.ArrayList;
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
		
		//Exclusão da tabela cab_conta_apagar 
		for (ContaAPagar c : contas) {
			excluirCabCContaApagar(c.getId());
		}

		for (ContaAPagar c : contas) {

			// carregar a lista de movimentação
			m = c.getMovimentacoes();

			c = manager.find(ContaAPagar.class, c.getId());
			excluirCabContaApagar(c.getVinculo());
			excluirVinculo(c.getId());

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

	public void excluirVinculo(Long id) {
		manager.createNativeQuery("delete from conta_apagar_movimentacao where conta_apagar_id = :id")
				.setParameter("id", id).executeUpdate();
	}
	
	
	public void excluirCabContaApagar(Long id) {
		manager.createNativeQuery("delete from cab_conta_apagar where vinculo = :id")
				.setParameter("id", id).executeUpdate();
	}
	
	public void excluirCabCContaApagar(Long id) {
		manager.createNativeQuery("delete from cab_conta_apagar_conta_apagar where listacontaapagars_id = :id")
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
			return manager.createQuery("from ContaAPagar where vinculo = :vinculo order by id", ContaAPagar.class)
					.setParameter("vinculo", vinculo).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings({ "deprecation" })
	private Criteria criarCriteriaParaFiltro(ContaAPagarFilter filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(ContaAPagar.class);

		if (StringUtils.isNotBlank(filtro.getOrigem())) {

			if (filtro.getOrigem().equals("principal")) {

				Criterion p1 = Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE);
				Criterion p2 = Restrictions.ilike("cpfCnpj", filtro.getNome(), MatchMode.ANYWHERE);
				criteria.add(Restrictions.or(p1, p2));

			}
		} else {
			if (StringUtils.isNotBlank(filtro.getNome())) {
				criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
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
