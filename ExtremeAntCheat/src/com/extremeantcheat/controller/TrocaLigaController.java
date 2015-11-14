package com.extremeantcheat.controller;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import com.extremeantcheat.business.TrocaLidaBO;
import com.extremeantcheat.business.UsuarioBO;
import com.extremeantcheat.entity.SerialPlayer;
import com.extremeantcheat.entity.Usuario;

@ManagedBean
public class TrocaLigaController {

	private String serialHash;
	private String email;
	private String senha;
	private TrocaLidaBO trocaLidaBO;
	private SerialPlayer serialPlayer;
	
	
	@PostConstruct
	public void cosntrutor(){
		this.serialHash = ""; 
		this.email = ""; 
		this.senha = "";
		
		serialPlayer = new SerialPlayer();
		trocaLidaBO = new TrocaLidaBO();
	}
	
	public void validaChave(){
		serialPlayer = trocaLidaBO.findBySerialHash(this.serialHash);
		boolean hasLigaByName = serialPlayer.getId() != null;
		
		if(!hasLigaByName){
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR,"Esta chave não existe",""));
			setSerialHash("");
		}
	}
	
	public void alterarChave(){
		UsuarioBO usuarioBO  = new UsuarioBO();
		Usuario usuario = usuarioBO.autentica(this.email, this.senha);
		
		if(usuario.getId() != null){
			usuario.setSerialPlayerId(serialPlayer.getId());
			usuarioBO.update(usuario); 
			
			FacesContext.getCurrentInstance().addMessage(null, new 
					FacesMessage(FacesMessage.SEVERITY_INFO,"Chave alterada com sucesso!",""));
			setSerialHash("");
			setEmail("");
			setSenha("");
		}else{
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR,"Email ou Senha incorreto",""));
		}
	}
	
	public boolean isNotNullSerialPalyer(){
		return serialPlayer != null && serialPlayer.getId() != null;
	}

	public String getSerialHash() {
		return serialHash;
	}

	public void setSerialHash(String serialHash) {
		this.serialHash = serialHash;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
}
