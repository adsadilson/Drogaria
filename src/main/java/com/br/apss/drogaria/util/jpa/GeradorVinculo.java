package com.br.apss.drogaria.util.jpa;

import java.io.Serializable;

import javax.inject.Inject;

import com.br.apss.drogaria.model.Movimentacao;
import com.br.apss.drogaria.repository.VinculoRepository;


public class GeradorVinculo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private VinculoRepository service;

	@SuppressWarnings("rawtypes")
	public Long gerar(Class clazz) {
		String num = "";
		for (int i = 0; i < 10; i++) {
			Integer numeroRandomico = (int) (1 + (Math.random() * 9));
			num += numeroRandomico.toString();
		}
		Long cod = Long.valueOf(num);
		boolean existe = service.porVinculo(clazz, cod);
		if (existe) {
			gerar(clazz);
		}
		return cod;
	}

	public static void main(String[] args) {
		String num = "";
		for (int i = 0; i < 10; i++) {
			Integer numeroRandomico = (int) (1 + (Math.random() * 9));
			num += numeroRandomico.toString();
		}
		Long n = Long.valueOf(num);
		VinculoRepository im = new VinculoRepository();
		im.porVinculo(Movimentacao.class, n);
		System.out.println(n);
	}
}
