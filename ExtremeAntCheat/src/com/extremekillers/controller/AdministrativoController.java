package com.extremekillers.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.jcookies.cookieapi.CookieApi;
import br.com.jcookies.enums.TimeSleep;
import br.com.jcookies.generic.GenericCookie;

import com.extremekillers.business.AdministrativoBO;
import com.extremekillers.business.ContadorTempoBO;
import com.extremekillers.business.JogadorWarfaceBO;
import com.extremekillers.business.PlayerBO;
import com.extremekillers.business.SerialPlayerBO;
import com.extremekillers.entity.Admin;
import com.extremekillers.entity.ContadorTempo;
import com.extremekillers.entity.JogadorWarface;
import com.extremekillers.entity.Player;
import com.extremekillers.entity.SerialPlayer;
import com.google.gson.Gson;

@ManagedBean
@ViewScoped
public class AdministrativoController implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final String EXTREME_ANT_CHEAT = "ExtremeAntCheat";
	private static final String EXTREME_ANT_CHEAT_TEMPORARIO = "ExtremeAntCheatTemporario";
	
	private String email,senha,siglaParaHash,totalHoraContadorTempo = "";
	private AdministrativoBO administrativoBO;
	private String printTela;
	private byte[] printTelaBytes;
	private String processosTela;
	private StreamedContent file;
	private SerialPlayer serialPlayer; 
	private SerialPlayerBO serialPlayerBO;
	private Admin admin;
	private ContadorTempoBO contadorTempoBO; 
	private ContadorTempo contadorTempo;
	private List<ContadorTempo> contadorTempos = new ArrayList<>();
	private List<JogadorWarface> blackList = new ArrayList<>();
	private JogadorWarfaceBO jogadorWarfaceBO  = new JogadorWarfaceBO();
	private String caminhoAudio = "";
	private boolean checkBoxLogado = false;
	private CookieApi cookieApi;
	private boolean dialogIsOpen = false;
	
	
	@PostConstruct
	public void cosntrutor(){
		administrativoBO = new AdministrativoBO();
		contadorTempoBO = new ContadorTempoBO();
		jogadorWarfaceBO = new JogadorWarfaceBO();
		
		serialPlayer = new SerialPlayer();
		serialPlayerBO = new SerialPlayerBO();
		admin = new Admin();
		
		this.loadDatasViewAdmin();
	}
	
	public void getBlackListAdminIsView() throws Exception{
		blackList = jogadorWarfaceBO.findBlackListAdminIsView();
		if(blackList != null && !blackList.isEmpty()){
			jogadorWarfaceBO.updateIsViewAdmin(blackList);
			this.startAudio();
			RequestContext.getCurrentInstance().execute("PF('mdl-blackList').show();");
			RequestContext.getCurrentInstance().update("blackList");
		}
	}
	
	public void limpaBlackListAdminIsView(CloseEvent event){
		blackList = new ArrayList<>();
	}
	
	private void startAudio(){
		this.caminhoAudio = "/resources/audio/alarme.mp3";	
	}
	

	public void loginAdmin() throws IOException{
		this.cookieApi = GenericCookie.getInstance(getRequest(), getResponse());
		admin = administrativoBO.autenticar(this.email, this.senha);
		
		if(admin != null && admin.getSerialPlayerId() != null){
			String jsonAdmin = new Gson().toJson(admin);
			if(this.checkBoxLogado){
				this.cookieApi.createCookiesCustom(jsonAdmin, "Cash para armazenar informacoes do admin", 
						null, TimeSleep.VINTE_QUATRO_HORAS.getTime(), 1, EXTREME_ANT_CHEAT);
			}
			
			this.cookieApi.createCookiesCustom(jsonAdmin, "Cash usuario Temp", null, TimeSleep.TIME_WHILE_LIVE_BROWSER.getTime(),
					null, EXTREME_ANT_CHEAT_TEMPORARIO);
			dialogIsOpen = true;
			contadorTempo = new ContadorTempo();
			contadorTempoBO = new ContadorTempoBO();
			contadorTempo = contadorTempoBO.retornaUltimoContadorAtivo(admin.getSerialPlayerId());
			this.serialPlayer = new SerialPlayerBO().findById(admin.getSerialPlayerId());
 			FacesContext.getCurrentInstance().getExternalContext().redirect("../views/admin.xhtml");
		}else{
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR,"email ou senha incorreto.",""));
		}
	}
	
	public void deslogar() throws IOException{
		this.cookieApi = GenericCookie.getInstance(getRequest(), getResponse());
		if(this.cookieApi.existCookieByName(EXTREME_ANT_CHEAT_TEMPORARIO) || 
				this.cookieApi.existCookieByName(EXTREME_ANT_CHEAT)){
			
			this.cookieApi.removeCookie(EXTREME_ANT_CHEAT);
			this.cookieApi.removeCookie(EXTREME_ANT_CHEAT_TEMPORARIO);
		}
		FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
	}
	
	public void comandoThemaBasic(int usuario_id){
		administrativoBO.executeComandoThemaBaisc(usuario_id);
	}
	
	public void comandoCopieProcesso(int usuario_id) throws Exception {
		//executar comando para scaninar os processos ativos
		administrativoBO.executeComandoCopieProcesso(usuario_id);	
		
		//da um tempo para o agente trom processar no desktop.
		Thread.sleep(8000);
		
		//Pega o comando processado e renderiza na tela.
		byte[] processosByte = administrativoBO.returnComandoTypePrint(usuario_id);
		if(processosByte != null){
			this.processosTela = new String (processosByte, "ISO-8859-1").replaceAll("null", "").replaceAll("\n\n", "")
					.replaceAll("Nome da imagem ", "Nome do processo");
			
			administrativoBO.deleteRetunrComando(usuario_id);
			
			printTela = null;
			printTelaBytes = null;
			RequestContext.getCurrentInstance().update("painel");
			
			//Atualiza a imagem PF('leitura_processo').show();
			//RequestContext.getCurrentInstance().execute("PF('statusDialog').hide();PF('leitura_processo').show();");
		}else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro ao processar evento. "
					+ "Verifique se o usuario esta realmente online. Ou atualize sua tela. Caso erro persista entre contato com Jaderson.",""));
		}
	}
	
	public void exportTxt() {
		HttpServletResponse responce = ((HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse());
		administrativoBO.exportTxt(responce, this.processosTela);
	}
	
	public void comandoPrintDoUsuario(int usuarioId) throws Exception {
		//executa comanod de print
		administrativoBO.executeComandoPrint(usuarioId);
		
		//da um tempo para o agente trom processar no desktop.
		Thread.sleep(8000);
		
		//Pega o comando processado e renderiza na tela.
		this.printTelaBytes = administrativoBO.returnComandoTypePrint(usuarioId);	
		if(this.printTelaBytes != null){
			byte[] fotoBase64 = Base64.encodeBase64(this.printTelaBytes);
			this.printTela =  "data:image/png;base64,"+new String(fotoBase64);
			
			administrativoBO.deleteRetunrComando(usuarioId);
			
			//Atualiza a imagem 
			RequestContext.getCurrentInstance().update("painel");
		}else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro ao processar evento. Verifique se o usuario esta realmente online.",""));
		}
	}
	
	private void comandoFechaServidor(){
		//executa comando para fechar o servidor
		administrativoBO.executaComandoFechaServidor(this.serialPlayer.getId());
	}
	
	public void liberarServidorLiga(){
		if(serialPlayerBO.isTempoAtingido(this.serialPlayer.getTempoLiberado(), 
				this.serialPlayer.getId())){
			FacesContext.getCurrentInstance().addMessage("msg-status-servidor", 
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Desculpe seu tempo chegou no limite!",""));
			RequestContext.getCurrentInstance().update("msg-status-servidor");
			return;
		}
		if(new SerialPlayerBO().updateStatusServidor(this.serialPlayer.getId(), this.serialPlayer.isStatusServidor())){
			//Verifica se o servidor esta on ou off
			if(this.serialPlayer.isStatusServidor()){
				//Starta o contador
				Integer id = contadorTempoBO.startContador(this.serialPlayer.getId(), new Date(), admin.getId());
				contadorTempo = contadorTempoBO.findById(id);
			}else{
				//Desliga o contador
				contadorTempoBO.desligarContador(contadorTempo, new Date(), admin.getId());
				contadorTempo = contadorTempoBO.findById(contadorTempo.getId());
				
				//Fecha o sistema para todos os usuarios.
				this.comandoFechaServidor();
			}
			FacesContext.getCurrentInstance().addMessage("msg-status-servidor", 
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Status do servidor foi alterado com sucesso !",""));
			RequestContext.getCurrentInstance().update("msg-status-servidor");
		}else{
			FacesContext.getCurrentInstance().addMessage("msg-status-servidor", 
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao alterar status do servidor !",""));
			RequestContext.getCurrentInstance().update("msg-status-servidor");
		}
	}
	
	public List<Player> getPlayers() throws Exception {
		this.loadDatasViewAdmin();
		return (this.serialPlayer != null && this.serialPlayer.getId() != null) ? 
				new PlayerBO().findAll(this.serialPlayer.getId()) : new ArrayList<Player>();
	}
	
	private void loadDatasViewAdmin(){
		this.cookieApi = GenericCookie.getInstance(getRequest(), getResponse());
		if(this.cookieApi.existCookieByName(EXTREME_ANT_CHEAT_TEMPORARIO)){
			this.admin =  new Gson().fromJson(this.cookieApi.getCookie(EXTREME_ANT_CHEAT_TEMPORARIO).getValue(), Admin.class);
			
			contadorTempo = new ContadorTempo();
			contadorTempo = contadorTempoBO.retornaUltimoContadorAtivo(admin.getSerialPlayerId());
			
			this.serialPlayer = serialPlayerBO.findById(admin.getSerialPlayerId());
			RequestContext.getCurrentInstance().execute("PF('admin_painel').show();");
		}else if(this.cookieApi.existCookieByName(EXTREME_ANT_CHEAT)){
			this.admin =  new Gson().fromJson(this.cookieApi.getCookie(EXTREME_ANT_CHEAT).getValue(), Admin.class);
			
			contadorTempo = new ContadorTempo();
			contadorTempo = contadorTempoBO.retornaUltimoContadorAtivo(admin.getSerialPlayerId());
			
			this.serialPlayer = serialPlayerBO.findById(admin.getSerialPlayerId());
			RequestContext.getCurrentInstance().execute("PF('admin_login_paienl').hide();PF('admin_painel').show();");
		}else{
			RequestContext.getCurrentInstance().execute("PF('admin_login_paienl').show();");
		}
		
	}
	
	public void handleClose(CloseEvent event){
		System.out.println("TEste: " + event.getComponent().getId() + " closed" +"So you don't like nature?");
		try{
			System.out.println("TEste");
			FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public String getMsgContadorTempo(){
		StringBuilder builder = new StringBuilder();
		
		if(contadorTempo != null && contadorTempo.getInicio() != null && contadorTempo.getTermino() == null){
			builder.append("Servidor iniciado em: ");
			builder.append(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(contadorTempo.getInicio()));
			builder.append(" , pelo administrador: ");
			builder.append(admin.getId().equals(contadorTempo.getInicio_admin_id()) ? 
					admin.getEmail(): administrativoBO.getAdmin(contadorTempo.getInicio_admin_id()).getEmail());
		}else{
			if(serialPlayer != null && serialPlayer.getId() != null){
				ContadorTempo contadorTempoParaDemonstracao = contadorTempoBO.retornaUtilmaConcluidaBySerialPlayerId(serialPlayer.getId());
				if(contadorTempoParaDemonstracao.getId() != null){//Caso seja a primeira vez, ainda nao exite um contador
					builder.append("Ultimo log do servidor ao ser desativado foi em: ");
					builder.append(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(contadorTempoParaDemonstracao.getInicio()));
					builder.append(" , pelo administrador: ");
					builder.append(admin.getId().equals(contadorTempoParaDemonstracao.getInicio_admin_id()) ? 
							admin.getEmail(): administrativoBO.getAdmin(contadorTempoParaDemonstracao.getInicio_admin_id()).getEmail());
					builder.append(", e foi finalizado em: ");
					builder.append(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(contadorTempoParaDemonstracao.getTermino()));
					builder.append(" pelo administrador: ");
					builder.append(admin.getId().equals(contadorTempoParaDemonstracao.getTermino_admin_id()) ? 
							admin.getEmail(): administrativoBO.getAdmin(contadorTempoParaDemonstracao.getTermino_admin_id()).getEmail());
					builder.append(".\n Com um total de tempo de ");
					String[] hsm = contadorTempoParaDemonstracao.getTotalHoras().split(":");
					builder.append(hsm[0]+" horas: "+hsm[1]+" minutos: "+hsm[2]+" segundos");
				}
			}
		}
		
		return builder.toString();
	}
	
	public void getContadorTemposBySerialPlayer(){
		if(this.serialPlayer.getId() != null){
			contadorTempos = contadorTempoBO.getContadorTemposBySerialPlayer(serialPlayer.getId());
			totalHoraContadorTempo = contadorTempoBO.getContadorTempoRelatorio(serialPlayer.getId());
		}
	}
	
	public String indexRedirect(){
		return "index.xhtml";
	}
	
	public boolean isNotNullRendered(String value){
		return value != null && !value.isEmpty();
	}
	
	public boolean isNotNullPrintTelaBytes(){
		return this.printTelaBytes != null;
	}
	
	public boolean isNotNullProcessosTela(){
		return this.processosTela != null;
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

	public String getPrintTela() {
		return printTela;
	}

	public void setPrintTela(String printTela) {
		this.printTela = printTela;
	}
	
	public SerialPlayer getSerialPlayer() {
		return serialPlayer;
	}
	
	public void setSerialPlayer(SerialPlayer serialPlayer) {
		this.serialPlayer = serialPlayer;
	}
	
	public String getProcessosTela() {
		return processosTela;
	}
	
	public void setProcessosTela(String processosTela) {
		this.processosTela = processosTela;
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
	
	public ContadorTempo getContadorTempo() {
		return contadorTempo;
	}
	
	public void setContadorTempo(ContadorTempo contadorTempo) {
		this.contadorTempo = contadorTempo;
	}
	
	public List<ContadorTempo> getContadorTempos() {
		return contadorTempos;
	}
	
	public void setContadorTempos(List<ContadorTempo> contadorTempos) {
		this.contadorTempos = contadorTempos;
	}
	
	public String getTotalHoraContadorTempo() {
		return totalHoraContadorTempo;
	}
	
	public void setTotalHoraContadorTempo(String totalHoraContadorTempo) {
		this.totalHoraContadorTempo = totalHoraContadorTempo;
	}
	
	public List<JogadorWarface> getBlackList() {
		return blackList;
	}
	
	public void setBlackList(List<JogadorWarface> blackList) {
		this.blackList = blackList;
	}
	
	public String getCaminhoAudio() {
		return caminhoAudio;
	}
	
	public void setCaminhoAudio(String caminhoAudio) {
		this.caminhoAudio = caminhoAudio;
	}
	
	public boolean isCheckBoxLogado() {
		return checkBoxLogado;
	}

	public void setCheckBoxLogado(boolean checkBoxLogado) {
		this.checkBoxLogado = checkBoxLogado;
	}
	
	public HttpServletRequest getRequest(){
		return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest());
	}
	
	public HttpServletResponse getResponse(){
		return ((HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse());
	}
	
	public boolean isDialogIsOpen() {
		return dialogIsOpen;
	}
	
	public void setDialogIsOpen(boolean dialogIsOpen) {
		this.dialogIsOpen = dialogIsOpen;
	}

	public StreamedContent getFile() {
		InputStream stream = new ByteArrayInputStream(this.printTelaBytes);
        file = new DefaultStreamedContent(stream, "image/jpg", "Print.jpg");
        return file;
    }
	
	public void limpaCachVar(){
		this.email = null;
		this.senha = null;
	}
	
	public void modoDesenvolvimentoAdminSystem(){
		serialPlayer = new SerialPlayer();
		serialPlayerBO = new SerialPlayerBO();
		admin = new Admin();
		administrativoBO.autenticarAdminSystem("admin", "admin");
		RequestContext.getCurrentInstance().execute("PF('painel-admin-system').show();");
	}
	
	public void modoDesenvolvimentoAdmin(){
		admin = new Admin();
		serialPlayer = new SerialPlayer();
		serialPlayerBO = new SerialPlayerBO();
		contadorTempoBO = new ContadorTempoBO();
		
		admin = administrativoBO.autenticar("lbw", "123");
		contadorTempo = contadorTempoBO.retornaUltimoContadorAtivo(admin.getSerialPlayerId());
		this.serialPlayer = serialPlayerBO.findById(admin.getSerialPlayerId());
		RequestContext.getCurrentInstance().execute("PF('admin_painel').show();");
	}

}