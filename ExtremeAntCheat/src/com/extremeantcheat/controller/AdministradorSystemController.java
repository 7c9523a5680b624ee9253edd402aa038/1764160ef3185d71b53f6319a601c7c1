package com.extremeantcheat.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.context.RequestContext;

import com.extremeantcheat.business.AdministrativoBO;
import com.extremeantcheat.business.ContadorTempoBO;
import com.extremeantcheat.business.SerialPlayerBO;
import com.extremeantcheat.entity.Admin;
import com.extremeantcheat.entity.SerialPlayer;
import com.extremeantcheat.util.Util;

@ManagedBean
@ViewScoped
public class AdministradorSystemController {

	private AdministrativoBO administrativoBO;
	private SerialPlayer serialPlayer; 
	private String email,senha,siglaParaHash;
	private Admin admin;
	private SerialPlayerBO serialPlayerBO;
	
	@PostConstruct
	public void construtor(){
		administrativoBO = new AdministrativoBO();
		admin = new Admin();
		
		serialPlayerBO = new SerialPlayerBO();
		serialPlayer = new SerialPlayer();
		
		RequestContext.getCurrentInstance().execute("PF('login-admin-system').show()");
	}
	
	public void cadastraLiga(){
		boolean cadastrado = administrativoBO.cadastraLiga(serialPlayer);
		if(cadastrado){
			serialPlayer = new SerialPlayer();
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Liga Cadastrada Com Sucesso!",""));
		}else{
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR,"Não foi possivel cadastrar!",""));
		}
	}
	
	public void loginAdminSystem(){
		boolean autenticado = administrativoBO.autenticarAdminSystem(this.email, this.senha);
		if(autenticado){
			setSiglaParaHash("");
			RequestContext.getCurrentInstance().execute("PF('login-admin-system').hide();PF('painel-admin-system').show();");
		}else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"email ou senha incorreto.",""));
		}
	}
	
	public void cadastrarAdmin(){
		if(administrativoBO.cadastrarAdmin(admin)){
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Admin Cadastrado com sucesso!",""));
			admin = new Admin();
		}else{
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR,"Não foi possivel cadastrar Admin!",""));
		}
	}
	
	public void atualizaSerialPlayer(){
		if(serialPlayerBO.update(serialPlayer)){
			serialPlayer = new SerialPlayer();
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Serial Player Ataulizado com sucesso!",""));
		}else{
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO,"Não foi possivel atualizar Serial Player!",""));
		}
	}
	
	public void deleteSerialPlayer(int id){
		if(serialPlayerBO.delete(id)){
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Liga deletada com sucesso!",""));
		}else{
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR,"Não foi possivel deletar a liga!",""));
		}
	}
	
	public void atualizaSerialPlayer(SerialPlayer serialPlayer){
		this.serialPlayer = serialPlayer;
	}
	
	public void deleteAdmin(int id){
		if(administrativoBO.deleteAdmin(id)){
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Admin deletada com sucesso!",""));
		}else{
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR,"Não foi possivel deletar o Admin!",""));
		}
	}
	
	public void atualizaAdmin(){
		if(administrativoBO.updateAdmin(admin)){
			admin = new Admin();
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Admin atualizado com sucesso!",""));
		}else{
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Não possivel atualizar admin!",""));
		}
	}
	
	public void atualizaAdmin(Admin admin){
		this.admin = admin;
	}
	
	public List<SerialPlayer> getSerialPlayers(){
		return new SerialPlayerBO().findAll();
	}
	
	public void gerarHashAtomatico(){
		this.serialPlayer.setSerialHash(Util.parseHashMD5(this.siglaParaHash));
		RequestContext.getCurrentInstance().execute("PF('mdl-sorte-hash').hide();");
	}
	
	public List<Admin> getAdmins(){
		return administrativoBO.findAdminAll();
	}
	
	public String getViewTableTotalHoraContadorTempo(int serialPlayerId){
		return new ContadorTempoBO().getContadorTempoRelatorio(serialPlayerId).replace("Total de horas: ", "");
	}
	
	public String getStatusServidorSerialPlayer(boolean status){
		return status ? "Ativo" : "Inativo";
	}
	
	public Integer getTotalContasUsadas(Integer id){
		return id != null ? this.serialPlayerBO.getCountSerialToUsuarioId(id) : 0; 
	}
	
	public boolean rederedViewSerialPlayerIsNotNull(){
		return  serialPlayer.getId() != null;
	}
	
	public boolean rederedViewAdminIsNotNull(){
		return admin.getId() != null;
	}
	
	public String getSiglaParaHash() {
		return siglaParaHash;
	}
	
	public void setSiglaParaHash(String siglaParaHash) {
		this.siglaParaHash = siglaParaHash;
	}
	
	public Admin getAdmin() {
		return admin;
	}
	
	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public SerialPlayer getSerialPlayer() {
		return serialPlayer;
	}

	public void setSerialPlayer(SerialPlayer serialPlayer) {
		this.serialPlayer = serialPlayer;
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
