package com.br.apss.drogaria.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.br.apss.drogaria.model.ContaAPagar;
import com.br.apss.drogaria.model.ContaAPagarHistorico;
import com.br.apss.drogaria.model.Pagamento;
import com.br.apss.drogaria.util.jsf.NegocioException;

public class ContaAPagarHistoricoRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public ContaAPagarHistorico salvar(ContaAPagarHistorico obj) {
		return manager.merge(obj);
	}

	public List<ContaAPagarHistorico> salvar(List<ContaAPagarHistorico> list) {
		List<ContaAPagarHistorico> retorno = new ArrayList<>();
		for (ContaAPagarHistorico cp : list) {
			retorno.add(manager.merge(cp));
		}
		return retorno;
	}

	public void excluir(ContaAPagarHistorico obj) {
		try {
			obj = porId(obj.getId());
			manager.remove(obj);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("Historico de Conta A Pagar não pode ser excluída");
		}
	}

	public ContaAPagarHistorico porId(Long id) {
		return manager.find(ContaAPagarHistorico.class, id);
	}

	public ContaAPagarHistorico porVinculo(Long vinculo) {
		return manager.createQuery("from ContaAPagarHistorico where agrupadorPagamento = :vinculo order by id",
				ContaAPagarHistorico.class).setParameter("vinculo", vinculo).getSingleResult();
	}

	public List<ContaAPagarHistorico> listaVinculo(Long vinculo) {
		try {
			return manager.createQuery("from ContaAPagarHistorico where agrupadorPagamento = :vinculo order by id",
					ContaAPagarHistorico.class).setParameter("vinculo", vinculo).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	public ContaAPagarHistorico ultimoHistorico(Pagamento p) {
		return manager.createQuery("from ContaAPagarHistorico where agrupadorPagamento = :vinculo order by id",
				ContaAPagarHistorico.class).setParameter("vinculo", p).getSingleResult();
	}

	public Long maiorRegistroPeloID(ContaAPagar obj) {
		return manager.createQuery("select max(c.id) from ContaAPagar c where c.contaApagar.id = :id", Long.class)
				.setParameter("id", obj.getId()).getSingleResult();
	}

}
