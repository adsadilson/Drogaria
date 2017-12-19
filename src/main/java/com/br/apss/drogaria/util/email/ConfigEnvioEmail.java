package com.br.apss.drogaria.util.email;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

import com.br.apss.drogaria.model.ConfigEmail;
import com.br.apss.drogaria.service.ConfigEmailService;

public class ConfigEnvioEmail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ConfigEmailService configEmailService;

	@ApplicationScoped
	public SimpleEmail emailSimples() throws EmailException {
		SimpleEmail email = new SimpleEmail();
		ConfigEmail conf = configEmailService.emailEmUso();

		email.setHostName(conf.getSmtp());
		email.setSmtpPort(conf.getPorta());
		email.setSSL(conf.getSsl());
		email.setAuthentication(conf.getLogin(), conf.getSenha());
		email.setFrom(conf.getLogin());
		return email;
	}
	
	@ApplicationScoped
	public HtmlEmail emailHtml() throws EmailException {
		HtmlEmail email = new HtmlEmail();
		ConfigEmail conf = configEmailService.emailEmUso();

		email.setHostName(conf.getSmtp());
		email.setSmtpPort(conf.getPorta());
		email.setSSL(conf.getSsl());
		email.setAuthentication(conf.getLogin(), conf.getSenha());
		email.setFrom(conf.getLogin());
		return email;
	}

}
