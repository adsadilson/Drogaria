package com.br.apss.drogaria.security;

import java.io.IOException;

import javax.faces.application.NavigationHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import com.br.apss.drogaria.model.GrupoUsuario;
import com.br.apss.drogaria.model.Permissao;
import com.br.apss.drogaria.model.Usuario;

public class AutenticacaoListener implements PhaseListener {

	private static final long serialVersionUID = 1L;

	@Override
	public void beforePhase(PhaseEvent event) {

	}

	@Override
	public void afterPhase(PhaseEvent event) {

		String pageAtual = Faces.getViewId();
		System.out.println("Pagina atual: " + pageAtual);

		boolean ehPageDeAutenticacao = pageAtual.contains("login.xhtml") || 
			pageAtual.contains("acessoNegado.xhtml");
		if (!ehPageDeAutenticacao) {

			Usuario userLogado = Faces.getSessionAttribute("usuarioAutenticado");
			

			if (null == userLogado) {
				try {
					Faces.redirect("./login.xhtml");
				} catch (IOException e) {
					e.printStackTrace();
					Messages.addGlobalError(e.getMessage());
				}
				return;
			}else{
				boolean possui = false;
				
				/*for(GrupoUsuario g : userLogado.getGrupos()){
					for(Permissao p : g.getPermissoes()){
						if(p.getControleMenu().getFormulario().equals(pageAtual)){
							possui = p.getMenu();
						}
					}
				}*/
				if(!possui){
					try {
						Faces.redirect("./acessoNegado.xhtml");
					} catch (IOException e) {
						e.printStackTrace();
						Messages.addGlobalError(e.getMessage());
					}
				}
			}
		}

	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
