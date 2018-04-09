package com.br.apss.drogaria.repository;

import java.io.Serializable;
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

		criteria.createAlias("cliente", "cliente", Criteria.INNER_JOIN);
		if (filtro.getCliente() != null) {
			Criterion p1 = Restrictions.eq("cliente.id", filtro.getCliente().getId());
			criteria.add(p1);
		}
		
		if (StringUtils.isBlank(filtro.getStatus())) {
			criteria.add(Restrictions.in("status", "ABERTO", "RECEBIMENTO PARCIAL"));
		}
		
		if (StringUtils.isNotBlank(filtro.getDoc())) {
			criteria.add(Restrictions.ilike("documento", filtro.getDoc(), MatchMode.ANYWHERE));
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
			return manager.createQuery("from ContaAReceber where movimentacao_vinculo = :vinculo order by id", ContaAReceber.class)
					.setParameter("vinculo", vinculo).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
	public void baixaSimples(ContaAReceber obj) {
		manager.createNativeQuery(
				"update conta_areceber set valor_pago = :valorPago, valor_apagar = :valorApagar, "
				+ "vinculo = :vinculo, status =:status where id = :id")
				.setParameter("valorPago", obj.getValorPago()).setParameter("valorApagar", obj.getValorApagar())
				.setParameter("vinculo", obj.getVinculo()).setParameter("status", obj.getStatus())
				.setParameter("id", obj.getId()).executeUpdate();
	}

}
