package com.br.apss.drogaria.util.email;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class Email {
	public static void main(String[] args) throws EmailException {
		SimpleEmail email = new SimpleEmail();
		try {
			System.out.println("Enviando...");
			   // email.setDebug(true);   
	            email.setHostName("smtp.gmail.com");  
	            email.setSmtpPort(465);
	            email.setSSL(true); 
	            email.setAuthentication("adilson.paraguai.31@gmail.com","*news2016*");   
	              
	            email.addTo("suporte@redereforco.com.br");  //aqui voce coloca o seu email de destinatario.
	            email.setFrom("adilson.paraguai.31@gmail.com");   
	            email.setSubject("Rescupera��o de Senha SimuladoOnline");   
	            email.setMsg("Ol�, "   
	                        + "BUSCA NO BANCO"+"\n"   
	                        + "Estamos enviando conforme solicita��o seu Login e senha para acesso ao sistema. \n"   
	                        +"\n"+ "Login:" + "BUSCA DO BANCO"+"\n" + "Senha:"   
	                        + "NOVA SENHA");   
	            email.send();   
			System.out.println("Email enviado.");
		} catch (EmailException ex) {
			ex.printStackTrace();
			System.out.println("Email n�o enviado.");
		}
	}
}
