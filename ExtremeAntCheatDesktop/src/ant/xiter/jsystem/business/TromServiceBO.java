package ant.xiter.jsystem.business;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.sun.xml.internal.ws.org.objectweb.asm.Type;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import ant.xiter.jsystem.dao.server.XitersDAO;
import ant.xiter.jsystem.dao.server.impl.XitersDAOImpl;
import ant.xiter.jsystem.entity.Jogador;
import ant.xiter.jsystem.entity.PlayersOn;
import ant.xiter.jsystem.utilinterface.Util;
import ant.xiter.jsystem.utilinterface.UtilFile;
import ant.xiter.jsystem.views.MonitorandoPlayer;

public class TromServiceBO implements Runnable {

	private List<String> xiters;
	private List<String> xitersExistente;
	private List<String> macrosExistente;
	private boolean shutdown = true;
	private List<String> dadosQueNaoPrecisaMais;
	
	private List<String> loadXiters(){
		InputStream xitersInputStream = getClass().getResourceAsStream("/tiposXiters/xiters.txt");
		List<String> lista = new ArrayList<String>();
		
		try(BufferedReader leitorTxt = new BufferedReader(new InputStreamReader(xitersInputStream))){
			String linha = null;
			while((linha = leitorTxt.readLine()) != null){
				lista.add(linha);
			}
			
			return lista;
		}catch(Exception e){
			System.err.println(e.getMessage());
			return null;
		}
	}
	
	private List<String> loadMacro(){
		InputStream xitersInputStream = getClass().getResourceAsStream("/tiposMacro/macros.txt");
		List<String> lista = new ArrayList<String>();
		
		try(BufferedReader leitorTxt = new BufferedReader(new InputStreamReader(xitersInputStream))){
			String linha = null;
			while((linha = leitorTxt.readLine()) != null){
				lista.add(linha);
			}
			
			return lista;
		}catch(Exception e){
			System.err.println(e.getMessage());
			return null;
		}
	}
	
	@Override
	public void run() {
		//Starta a thread q ira verificar o thema do windows.
		this.startThreadCheckColorImage();
		
		while (shutdown) {
			try {
				Thread.sleep(5000);
				//Pergunta se tem comando para o trom executar
				this.hasComandoToTrom();
				//Verificar os xiters e macro nos processos do 'SO'
				this.verificaServicoSO();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void startThreadCheckColorImage(){
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (shutdown) {
					try{
						Thread.sleep(5000);
						byte[] capturePrintTela = Util.capturePrintTela();
						if(checkContentsImageIsBlack(capturePrintTela)){
							Util.executeComandoCMD(Util.CMD_THEMA_BAISC);
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		});
		thread.setName("ThreadCheckColorImage");
		thread.start();
	}
	
	public void hasComandoToTrom(){
		XitersDAO dao =  new XitersDAOImpl();
		switch (dao.findComenadosAll()) {
		case 1://Tira print
			dao.sendRetornoByComando(Util.capturePrintTela(), 1,Util.getJogadorWarface().getId());
			//remove o comando executado da lista de espera
			dao.deleteComandoAntXiterTrom(Util.getJogadorWarface().getId());
			break;
		case 2://copia todos processos
			dao.sendRetornoByComando(Util.executeComandoCMDReturn(Util.TASK_LIST).getBytes(), 2,Util.getJogadorWarface().getId());
			//remove o comando executado da lista de espera.
			dao.deleteComandoAntXiterTrom(Util.getJogadorWarface().getId());
			break;
		case 3://Muda o thema basic do win
			Util.executeComandoCMD(Util.CMD_THEMA_BAISC);
			//remove o comando executado da lista de espera.
			dao.deleteComandoAntXiterTrom(Util.getJogadorWarface().getId());
			break;
		case 4://Servidor Off
			Icon figura = new ImageIcon("Resources/imagens/extremeAntXiter.png"); 
			//remove o comando executado da lista de espera.
			dao.deleteComandoAntXiterTrom(Util.getJogadorWarface().getId());
			JOptionPane.showMessageDialog(null, "Administrador fecho servidor","Servidor Off",JOptionPane.ERROR_MESSAGE,figura);
			
			try {
				new XitersDAOImpl().deletePlayerOff(new MonitorandoPlayer().getIdByFileSession());
				Util.removeJogadorWarfaceLocal();
			} catch (Exception e) {
				e.printStackTrace();
			}	

			System.exit(0);
			break;
		case 0:
			//Comando nao existe
			break;
		default :
			break;
		}
	}
	
	
	public TromServiceBO() {
		xiters = new ArrayList<>();
		dadosQueNaoPrecisaMais = new ArrayList<>();
		xitersExistente = this.loadXiters();
		macrosExistente = this.loadMacro();
	}

	private String verificaMacEnderec() {
		try{
			Process process = Runtime.getRuntime().exec(Util.ENDERECO_MAC);	
			BufferedReader leitor = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String linha,retorno = null;
			while((linha = leitor.readLine()) != null){
				retorno += linha+"\n %";
			}
			
			return retorno.split("%")[3];
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}

	public void verificaServicoSO() {
		try {
			String cmd = "tasklist";
			Process process = Runtime.getRuntime().exec(cmd);
			Scanner leitor = new Scanner(process.getInputStream());
			while (leitor.hasNext()) {
				String linha = leitor.next();
				linha = linha.toLowerCase();
				
				//Verifica os xiters que existem no jogo.
				for(String xiterExistente : xitersExistente){
					if (linha.equalsIgnoreCase(xiterExistente)) {
						// nome do xiter com toLowerCase mais o tipo mais nome original
						xiters.add(xiterExistente+"-xiter-"+linha);
						System.out.println(leitor.next());
					}
				}
				
				//Verifica os macros que existem no jogo.
				for(String macro : macrosExistente){
					if (linha.equalsIgnoreCase(macro)) {
						if(!dadosQueNaoPrecisaMais.contains(macro)){
							// Busca o player on atraves da id do usurio.
							PlayersOn playersOn = new XitersDAOImpl().findByUsuarioId(Util.getJogadorWarface().getId());
							//Seta o possivel macro
							playersOn.setDetalhes("Programa que pode ser um possivel Macro:"+macro);
							
							//Envia pro servidor detalhes do programa suspeito
							new XitersDAOImpl().updateDetalhesPlayerOn(playersOn);
							
							dadosQueNaoPrecisaMais.add(macro);
							try{System.out.println(leitor.next());}catch(Exception e){}
						}
					}
				}
				
			}
			leitor.close();
			if(xiters != null &&  !xiters.isEmpty()){
				sendDadosToServer(xiters);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static boolean checkContentsImageIsBlack(byte[] img){
		try(@SuppressWarnings("resource")ByteArrayInputStream in = new ByteArrayInputStream(img)){
			BufferedImage bufferedImage = ImageIO.read(in); 
			int rgbCorPreta = -16777216;
			
			for (int y = 0; y < bufferedImage.getHeight(); y++)  
			for (int x = 0; x < bufferedImage.getWidth(); x++){  
	            Color pixel = new Color(bufferedImage.getRGB(x, y));  
	            if(rgbCorPreta != pixel.getRGB()){
	            	return false;
	            }
			}  
			
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void sendDadosToServer(List<String> listXiter) throws Exception {
		Jogador jogador = new Jogador();
		jogador.setNome(Util.getJogadorWarface().getNome());
		jogador.setNickJogo(Util.getJogadorWarface().getNickJogo());
		jogador.setXiters(listXiter);
		jogador.setTxtDescricaoDoPc(verificaMacEnderec().getBytes());
		if(listXiter.contains("-xiter-")){
			byte[] img = Util.capturePrintTela();
			if(checkContentsImageIsBlack(img)){
				Util.executeComandoCMD(Util.CMD_THEMA_BAISC);
				Thread.sleep(7000);
				jogador.setPrintXiterOrMacro(Util.capturePrintTela());
			}else{
				jogador.setPrintXiterOrMacro(img);
			}
			
			
		}
		jogador.setDescricaoServicos(Util.executeComandoCMDReturn(Util.TASK_LIST));
		new XitersDAOImpl().insert(jogador);
		
		String cmd = "taskkill /F /IM Game.exe";
		@SuppressWarnings("unused")
		Process process = Runtime.getRuntime().exec(cmd);

		for(String xiter : listXiter){
			String[] nameXiterProcess = xiter.split("-");
			@SuppressWarnings("unused")
			Process processoKillTask = Runtime.getRuntime().exec("taskkill /F /IM "+nameXiterProcess[2].toString());
		}
		xiters = new ArrayList<>();
		
		FileInputStream audio = new FileInputStream(new File("Resources/audio/alarme.mp3"));
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
		JOptionPane.showMessageDialog(null, "'Xiter' indetificado\nNotificação para servidor enviado.");
		player.close();
		System.exit(0);
		
	}

}