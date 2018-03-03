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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

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
			throw new NegocioException("Pagamento de Produto n�o pode ser exclu�da");
		}
	}

	public Pagamento porId(Long id) {
		return manager.find(Pagamento.class, id);
	}

	public List<Pagamento> listarTodos() {
		return manager.createQuery("from Pagamento order by nome", Pagamento.class).getResultList();
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

		if (null != filtro) {
			if (null != filtro) {
				if (StringUtils.isNotBlank(filtro.getNome())) {
					criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
				}

				if (filtro.getStatus() != null) {
					criteria.add(Restrictions.eq("status", filtro.getStatus()));
				}
			}
		}

		return criteria;
	}

	@SuppressWarnings("unchecked")
	public List<Pagamento> filtrados(PagamentoFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		return criteria.addOrder(Order.asc("nome")).list();
	}

	public int quantidadeFiltrados(PagamentoFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

}
