package ant.xiter.jsystem.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import ant.xiter.jsystem.business.TromServiceBO;
import ant.xiter.jsystem.dao.server.impl.XitersDAOImpl;
import ant.xiter.jsystem.utilinterface.Util;

public class StartProcessViews extends JFrame {

	private static final long serialVersionUID = 1L;
	private TromServiceBO tromServiceBO;
	
	public StartProcessViews() {
		this.themaNimbus();
		this.setTitle("Start ExtremeAntXiter version 1.0");
		this.setSize(600, 400);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("Resources/imagens/extremeAntXiter.png"));  
		this.setVisible(true);

		this.setLayout(new BorderLayout());
		this.setContentPane(new JLabel(new ImageIcon("Resources/imagens/warface-xk.png")));
		this.setLayout(new FlowLayout());
		this.setSize(399,399);
		this.setSize(600,337);
		
		//Verfifica se ja tem um processo do AntXiter ativo
		//this.verificaSeJaEstaABerto();
		
		//Carrega a barra de progresso e verifica se o servido esta on
		this.progressBarShow();
		
		//Start o servico ant xiter
		this.startServicoAntXiter();
		
		//Redireciona para tela de monitoramento.
		new MonitorandoPlayer();
		setVisible(false);
		
	}
	
	private void themaNimbus(){
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void verificaSeJaEstaABerto(){
		try{
			String cmd = "tasklist";
			Process process = Runtime.getRuntime().exec(cmd);
			int i = 0;
			try(Scanner leitor = new Scanner(process.getInputStream())){
				while (leitor.hasNext()) {
					String linha = leitor.next();
					if(linha.contains("javaw.exe")){
						i++;
					}
				}
			}
			if(i > 2){
				JOptionPane.showMessageDialog(null, "Voce ja esta com AntXiter Ativo...");
				System.exit(0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void progressBarShow(){
		this.setLayout(null);
		JProgressBar progress = new JProgressBar(); 
		progress.setBounds(100,125,400,20);	
		progress.setMinimum(0); 
		progress.setMaximum(100); 
		progress.setStringPainted(true); 
		
		this.add(progress);
		progress.setValue(5);
		Player loderSom = this.startMusicLoder();
		for (int i = 0; i < 100; i++) {
			try{
				if(i == 20){
					//Verifica ultima versao
					String versaoBD = new XitersDAOImpl().getUltimaVersao();
					String versaoDesktop = getUltimaVersaoUtilsData();
					if(!versaoDesktop.equals(Util.parseHashMD5(versaoBD))){
						JOptionPane.showMessageDialog(null, "Ant Xiter desatualizado \nAtualize no site do "
								+ "clan.\n Caso permaneça o erro favor entrar\n em contato com 'Jadeson'.");
						System.exit(0);
					}
				}
				if(i == 30){
					DialogGenericFrame genericFrame = new DialogGenericFrame();
					while(genericFrame.finalizadoDialogo){
						System.out.println("");
					}
					Util.JogadorWarfaceCreateLocalData(genericFrame.jogadorWarface);
				}
				if(i == 35){
					
				}
				if(i == 40){
					if(!new XitersDAOImpl().getServidorLiberado(Util.getJogadorWarface().getLigaRemetenteId())){
						JOptionPane.showMessageDialog(null, "Sua liga ainda não liberou o servidor para monitoramento !");
						Util.removeJogadorWarfaceLocal();
						System.exit(0);
					}
				}
				if(i == 50){
					//Carrega dados do usuario
					this.playerOn();
					
					if(!new File("Resources/audio/alarme.mp3").exists()){
						JOptionPane.showMessageDialog(null, "Can't read input file!\n Programa corrompido! \nReinstale o programa");
						System.exit(0);
					}
				}
				if(i == 60){
					boolean comandoExecutado = Util.executeComandoCMD(Util.CMD_THEMA_BAISC);
					if(!comandoExecutado){
						JOptionPane.showMessageDialog(null, "Obrigatório o basic.theme do sistema operacional\nReinicie aplicação");
						System.exit(0);
					}
				}
				
				if(i == 80){
					//Util.executeComandoCMD(Util.CMD_THEMA_BAISC);
				}
				
				if(i == 90){
					loderSom.close();
				}
				
				Thread.sleep(200);
				progress.setValue(i+10);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private void playerOn(){
		try{
			Integer id = new XitersDAOImpl().insertPlayerOn(Util.getJogadorWarface().getNickJogo(), Util.getJogadorWarface().getId(), 
					Util.getJogadorWarface().getCodigoSala(), Util.getJogadorWarface().getLigaRemetenteId());
			saveData(id);
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
	}

	private void startServicoAntXiter(){
		try {
			tromServiceBO = new TromServiceBO();
			Thread tromServiceThread = new Thread(tromServiceBO);
			tromServiceThread.setName("TromService - Jaderson_berti - Extremekillers");
			tromServiceThread.start();
		} catch (Exception e) {

		}
	}
	
	@SuppressWarnings("unused")
	private static ImageIcon createImageIcon(String path, String description) {
		ImageIcon imgURL = new ImageIcon(path,description);
	      if (imgURL != null) {
	         return imgURL;
	      }
	      System.out.println("Não encontrou a imagem");
	      return null;
	}   
	
	private void saveData(Integer id){
		File gravaIdTemp = new File("FileSession.data");
		try{
			if(!gravaIdTemp.exists()){
				gravaIdTemp.createNewFile();
			}else{
				gravaIdTemp.delete();
				gravaIdTemp.createNewFile();
			}
			try(OutputStream escritor = new FileOutputStream(gravaIdTemp, true)){
				String hash = id.toString() +"%"+Util.SEQUENCIAL_HASH.toString();
				byte[] idByte = hash.getBytes();
				escritor.write(idByte);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private String getUltimaVersaoUtilsData(){
		File versao = new File("Utils.data");
		if(versao.exists()){
			try(BufferedReader leitor = new BufferedReader(new FileReader(versao))){
				return leitor.readLine();
			}catch(Exception e){
				JOptionPane.showMessageDialog(null, "Arquivo de versao não encontrado\nReinstale aplicação!");
				e.printStackTrace();
				System.exit(0);
			}
		}else{
			JOptionPane.showMessageDialog(null, "Arquivo de versão não encontrado\nReinstale aplicação!\nOu entre em contato com 'Jaderson'.");
			System.exit(0);
		}
		return null;
	}
	
	private Player startMusicLoder(){
		try{
			FileInputStream audio = new FileInputStream(new File("Resources/audio/loderInical.mp3"));
			BufferedInputStream leitorAudio = new BufferedInputStream(audio);
			final Player player = new Player(leitorAudio);
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						player.play();
					} catch (JavaLayerException e) {
						e.printStackTrace();
					}
				}
			}).start();
			return player;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) throws Exception {
		@SuppressWarnings("unused")
		StartProcessViews startProcessViews = new StartProcessViews();
	}

}