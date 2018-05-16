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
import com.br.apss.drogaria.model.ContaAPagar;
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
			excluirListaContasApagar(obj);
			obj = porId(obj.getId());
			manager.remove(obj);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("Compra não pode ser excluído");
		}
	}

	public void excluirListaContasApagar(CompraCab obj) {

		List<ContaAPagar> listContas = manager
				.createQuery("from ContaAPagar where movimentacao_vinculo = :vinculo order by id", ContaAPagar.class)
				.setParameter("vinculo", obj.getVinculo()).getResultList();

		for (ContaAPagar cp : listContas) {
			manager.createNativeQuery("delete from cab_conta_apagar_conta_apagar where conta_apagar_id = :id")
					.setParameter("id", cp.getId()).executeUpdate();
		}
		
		manager.createNativeQuery("delete from cab_conta_apagar where vinculo = :vinculo")
		.setParameter("vinculo", obj.getVinculo()).executeUpdate();

		manager.createNativeQuery("delete from conta_apagar where movimentacao_vinculo = :vinculo")
				.setParameter("vinculo", obj.getVinculo()).executeUpdate();

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

		if (StringUtils.isNotBlank(filtro.getDoc())) {
			criteria.add(Restrictions.ilike("documento", filtro.getDoc(), MatchMode.ANYWHERE));
		}


		if (filtro.getEmissaoIni() != null) {
			criteria.add(Restrictions.ge("dataEmissao", filtro.getEmissaoIni()));
		}

		if (filtro.getEmissaoFim() != null) {
			criteria.add(Restrictions.le("dataEmissao", filtro.getEmissaoFim()));
		}
		
		if (filtro.getEntradaIni() != null) {
			criteria.add(Restrictions.ge("dataEntrada", filtro.getEntradaIni()));
		}

		if (filtro.getEntradaFim() != null) {
			criteria.add(Restrictions.le("dataEntrada", filtro.getEntradaFim()));
		}

		if (filtro.getFornecedor() != null) {
			criteria.add(Restrictions.eq("fornecedor.id", filtro.getFornecedor().getId()));
		}

		if (filtro.getValorNT1() != null) {
			criteria.add(Restrictions.ge("valorNota", filtro.getValorNT1()));
		}

		if (filtro.getValorNT2() != null) {
			criteria.add(Restrictions.le("valorNota", filtro.getValorNT2()));
		}
		
		if (filtro.getValorP1() != null) {
			criteria.add(Restrictions.ge("valorItens", filtro.getValorP1()));
		}

		if (filtro.getValorP2() != null) {
			criteria.add(Restrictions.le("valorItens", filtro.getValorP2()));
		}

		return criteria;
	}

	public int qtdeFiltrados(CompraCabFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

}
