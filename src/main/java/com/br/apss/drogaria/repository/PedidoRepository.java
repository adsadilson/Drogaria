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
import com.br.apss.drogaria.model.Pedido;
import com.br.apss.drogaria.model.filter.PedidoFilter;
import com.br.apss.drogaria.util.jsf.NegocioException;

public class PedidoRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Pedido salvar(Pedido obj) {
		return manager.merge(obj);
	}

	public void excluir(Pedido obj) {
		try {
			obj = porId(obj.getId());
			manager.remove(obj);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("Pedido n�o pode ser exclu�do");
		}
	}

	public Pedido porId(Long id) {
		return manager.find(Pedido.class, id);
	}

	public Pedido porNome(String nome) {
		try {
			return manager.createQuery("from Pedido where nome = :nome", Pedido.class).setParameter("nome", nome)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public Pedido buscarPeidoComItens(Long id) {
		return (Pedido) manager.createQuery("select p from Pedido p JOIN p.itens i where p.id =:id")
				.setParameter("id", id).getSingleResult();
	}

	public Pedido porNomeEstado(String nome, Estado uf) {
		try {
			return manager.createQuery("from Pedido where nome = :nome and uf=:uf", Pedido.class)
					.setParameter("nome", nome).setParameter("uf", uf).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Pedido> listAll() {
		return manager.createNativeQuery("SELECT * FROM Pedido order by nome", Pedido.class).getResultList();
	}

	public List<Pedido> buscarPorEstado(Estado estado) {
		try {
			return manager.createQuery("from Pedido where uf=:uf", Pedido.class).setParameter("uf", estado)
					.getResultList();
		} catch (RuntimeException erro) {
			throw erro;
		} finally {

		}
	}

	@SuppressWarnings("unchecked")
	public List<Pedido> filtrados(PedidoFilter filtro) {
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
	private Criteria criarCriteriaParaFiltro(PedidoFilter filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Pedido.class);

		// fazemos uma associação (join) com cliente e Vendedor nomeamos como "c e v"
		criteria.createAlias("cliente", "c").createAlias("vendedor", "v");

		if (filtro.getNumeroDe() != null) {
			// id deve ser maior ou igual (ge = greater or equals) a
			// filtro.numeroDe
			criteria.add(Restrictions.ge("id", filtro.getNumeroDe()));
		}

		if (filtro.getNumeroAte() != null) {
			// id deve ser menor ou igual (le = lower or equal) a
			// filtro.numeroDe
			criteria.add(Restrictions.le("id", filtro.getNumeroAte()));
		}

		if (filtro.getDataCriacaoDe() != null) {
			criteria.add(Restrictions.ge("dataCriacao", filtro.getDataCriacaoDe()));
		}

		if (filtro.getDataCriacaoAte() != null) {
			criteria.add(Restrictions.le("dataCriacao", filtro.getDataCriacaoAte()));
		}

		if (StringUtils.isNotBlank(filtro.getNomeCliente())) {
			// acessamos o nome do cliente associado ao pedido pelo alias "c",
			// criado anteriormente
			criteria.add(Restrictions.ilike("c.nome", filtro.getNomeCliente(), MatchMode.ANYWHERE));
		}

		if (StringUtils.isNotBlank(filtro.getNomeVendedor())) {
			// acessamos o nome do vendedor associado ao pedido pelo alias "v",
			// criado anteriormente
			criteria.add(Restrictions.ilike("v.nome", filtro.getNomeVendedor(), MatchMode.ANYWHERE));
		}

		if (filtro.getStatuses() != null && filtro.getStatuses().length > 0) {
			// adicionamos uma restrição "in", passando um array de constantes
			// da enum StatusPedido
			//criteria.add(Restrictions.in("status", filtro.getStatuses()));
		}

		return criteria;
	}

	public int qtdeFiltrados(PedidoFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

}
