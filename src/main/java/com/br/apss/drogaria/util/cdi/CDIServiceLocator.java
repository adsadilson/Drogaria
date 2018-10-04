package com.br.apss.drogaria.util.cdi;

import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/** Utilizar no converter por não injetar na versão jsf 2.2 **/
public class CDIServiceLocator {
	private static BeanManager getBeanManager() {
		try {
			InitialContext initialContext = new InitialContext();
			return (BeanManager) initialContext.lookup("java:comp/env/BeanManager");
		} catch (NamingException e) {
			throw new RuntimeException("Não pode encontrar o BeanManager no JNDI");
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> clazz) {
		BeanManager bm = getBeanManager();
		Set<Bean<?>> beans = bm.getBeans(clazz);
		if (beans == null || beans.isEmpty()) {
			return null;
		}
		Bean<T> bean = (Bean<T>) beans.iterator().next();
		CreationalContext<T> ctx = bm.createCreationalContext(bean);
		T o = (T) bm.getReference(bean, clazz, ctx);
		return o;
	}
}
