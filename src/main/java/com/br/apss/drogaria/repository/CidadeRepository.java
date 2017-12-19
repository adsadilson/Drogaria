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

import com.br.apss.drogaria.enums.Estado;
import com.br.apss.drogaria.model.Cidade;
import com.br.apss.drogaria.model.filter.CidadeFilter;
import com.br.apss.drogaria.util.jsf.NegocioException;


public class CidadeRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Cidade salvar(Cidade obj) {
		return manager.merge(obj);
	}

	public void excluir(Cidade obj) {
		try {
			obj = porId(obj.getId());
			manager.remove(obj);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("Cidade não pode ser excluído");
		}
	}

	public Cidade porId(Long id) {
		return manager.find(Cidade.class, id);
	}

	public Cidade porNome(String nome) {
		try {
			return manager.createQuery("from Cidade where nome = :nome", Cidade.class).setParameter("nome", nome)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Cidade porNomeEstado(String nome, Estado uf) {
		try {
			return manager.createQuery("from Cidade where nome = :nome and uf=:uf", Cidade.class)
					.setParameter("nome", nome).setParameter("uf", uf).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Cidade> listAll() {
		return manager.createNativeQuery("SELECT * FROM Cidade order by nome", Cidade.class).getResultList();
	}

	public List<Cidade> buscarPorEstado(Estado estado) {
		try {
			return manager.createQuery("from Cidade where uf=:uf", Cidade.class).setParameter("uf", estado)
					.getResultList();
		} catch (RuntimeException erro) {
			throw erro;
		} finally {

		}
	}

	@SuppressWarnings("unchecked")
	public List<Cidade> filtrados(CidadeFilter filtro) {
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

		return criteria.addOrder(Order.asc("nome")).list();
	}

	@SuppressWarnings({ "deprecation" })
	private Criteria criarCriteriaParaFiltro(CidadeFilter filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Cidade.class);

		if (StringUtils.isNotBlank(filtro.getNome())) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
		}

		if (filtro.getUf() != null) {
			criteria.add(Restrictions.eq("uf", filtro.getUf()));
		}

		if (filtro.getStatus() != null) {
			criteria.add(Restrictions.eq("status", filtro.getStatus()));
		}

		return criteria;
	}

	public int qtdeFiltrados(CidadeFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

}
