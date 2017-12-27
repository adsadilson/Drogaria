package com.br.apss.drogaria.security;

import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

import com.br.apss.drogaria.model.Usuario;

public class AutenticacaoListener implements PhaseListener {

	private static final long serialVersionUID = 1L;

	@Override
	public void beforePhase(PhaseEvent event) {

	}

	@Override
	public void afterPhase(PhaseEvent event) {
		FacesContext fc = event.getFacesContext();
		ExternalContext ec = fc.getExternalContext();

		if (!fc.getViewRoot().getViewId().contains("login.xhtml")) {
			HttpSession session = (HttpSession) ec.getSession(true);
			Usuario usuario = (Usuario) session.getAttribute("loginBean");

			if (usuario == null) {
				try {
					ec.redirect(ec.getRequestContextPath() + "/login.xhtml");
				} catch (IOException ex) {
				}
			}
		}

	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
