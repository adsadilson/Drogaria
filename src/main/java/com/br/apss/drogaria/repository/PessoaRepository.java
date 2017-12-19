package com.br.apss.drogaria.repository;

import java.io.Serializable;
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

import com.br.apss.drogaria.model.Pessoa;
import com.br.apss.drogaria.model.filter.PessoaFilter;
import com.br.apss.drogaria.util.jsf.NegocioException;

public class PessoaRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Pessoa salvar(Pessoa obj) {
		return manager.merge(obj);
	}

	public void excluir(Pessoa obj) {
		try {
			obj = porId(obj.getId());
			manager.remove(obj);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("Pessoa n�o pode ser exclu�da");
		}
	}

	public Pessoa porId(Long id) {
		return manager.find(Pessoa.class, id);
	}

	public List<Pessoa> listarTodos() {
		return manager.createQuery("from Pessoa order by nome", Pessoa.class).getResultList();
	}

	public Pessoa porNome(String nome) {
		try {
			return manager.createQuery("from Pessoa where upper(nome) = :nome", Pessoa.class)
					.setParameter("nome", nome.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Pessoa porCpf(String cpf) {
		try {
			return manager.createQuery("from Pessoa where cpf_cnpj = :cpf", Pessoa.class).setParameter("cpf", cpf)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings({ "deprecation" })
	private Criteria criarCriteriaParaFiltro(PessoaFilter filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Pessoa.class);

		if (filtro.getCliente()) {
			criteria.add(Restrictions.eq("cliente", true));
		}
		if (filtro.getFonecedor()) {
			criteria.add(Restrictions.eq("fornecedor", true));
		}
		if (filtro.getFuncionario()) {
			criteria.add(Restrictions.eq("funcionario", true));
		}

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

			if (StringUtils.isNotBlank(filtro.getCpf())) {
				criteria.add(Restrictions.ilike("cpfCnpj", filtro.getCpf(), MatchMode.ANYWHERE));
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
	public List<Pessoa> filtrados(PessoaFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);

		criteria.setFirstResult(filtro.getPrimeiroRegistro());
		criteria.setMaxResults(filtro.getQuantidadeRegistros());

		if (filtro.getCampoOrdenacao() != null) {
			if (filtro.isAscendente()) {
				criteria.addOrder(Order.asc(filtro.getCampoOrdenacao()));
			} else {
				criteria.addOrder(Order.desc(filtro.getCampoOrdenacao()));
			}
		}

		return criteria.addOrder(Order.asc("nome")).list();
	}

	public int quantidadeFiltrados(PessoaFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

}
