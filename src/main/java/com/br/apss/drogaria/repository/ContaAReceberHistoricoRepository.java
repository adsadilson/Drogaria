package com.br.apss.drogaria.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.br.apss.drogaria.model.ContaAReceber;
import com.br.apss.drogaria.model.ContaAReceberHistorico;
import com.br.apss.drogaria.util.jsf.NegocioException;

public class ContaAReceberHistoricoRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public ContaAReceberHistorico salvar(ContaAReceberHistorico obj) {
		return manager.merge(obj);
	}

	public List<ContaAReceberHistorico> salvar(List<ContaAReceberHistorico> list) {
		List<ContaAReceberHistorico> retorno = new ArrayList<>();
		for (ContaAReceberHistorico cp : list) {
			retorno.add(manager.merge(cp));
		}
		return retorno;
	}

	public void excluir(ContaAReceberHistorico obj) {
		try {
			obj = porId(obj.getId());
			manager.remove(obj);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("Historico de Conta A Receber não pode ser excluída");
		}
	}

	public ContaAReceberHistorico porId(Long id) {
		return manager.find(ContaAReceberHistorico.class, id);
	}

	public ContaAReceberHistorico porVinculo(Long vinculo) {
		return manager.createQuery("from ContaAReceberHistorico where agrupadorRecebimento = :vinculo order by id",
				ContaAReceberHistorico.class).setParameter("vinculo", vinculo).getSingleResult();
	}

	public List<ContaAReceberHistorico> listaVinculo(Long vinculo) {
		try {
			return manager.createQuery("from ContaAReceberHistorico where agrupadorRecebimento = :vinculo order by id",
					ContaAReceberHistorico.class).setParameter("vinculo", vinculo).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	// Pegar o maior registro pelo ID da conta a receber
	public Long maiorRegistroPeloID(ContaAReceber obj) {
		return manager.createQuery("select max(c.id) from ContaAReceberHistorico c where c.contaAReceber.id = :id",
				Long.class).setParameter("id", obj.getId()).getSingleResult();
	}

}
