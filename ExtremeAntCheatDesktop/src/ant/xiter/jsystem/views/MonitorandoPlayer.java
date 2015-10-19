package ant.xiter.jsystem.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import ant.xiter.jsystem.dao.server.impl.XitersDAOImpl;
import ant.xiter.jsystem.entity.JogadorWarface;
import ant.xiter.jsystem.entity.PlayersOn;
import ant.xiter.jsystem.utilinterface.Util;

public class MonitorandoPlayer extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_WIDTH = 290;
    private static final int DEFAULT_HEIGHT = 500;
    private JLabel titulo;
    private JTable tabela;
    private JogadorWarface jogadorWarface = Util.getJogadorWarface();
    
	public MonitorandoPlayer() {
		this.setTitle("Monitorando Player");
		this.setLayout(null);
		this.getContentPane().setBackground(Color.black);

		//Inicia o monitoramento
		this.monitorando();
		
		//ConteudoGrafico
		this.conteudoGrafico();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setLocation(400,200);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("Resources/imagens/extremeAntXiter.png")); 
		this.setVisible(true);
		
		this.addWindowListener(new WindowAdapter() {  
		    public void windowClosing(WindowEvent evt) { 
		    	try {
		    		new XitersDAOImpl().deletePlayerOff(getIdByFileSession());	
		    		Util.removeJogadorWarfaceLocal();
		    	} catch (Exception e) {
					e.printStackTrace();
				}
		    }  
		}); 
		
	}
	
	private void conteudoGrafico(){
		JPanel fieldset01 = new JPanel();
		fieldset01.setBackground(Color.WHITE);
		fieldset01.setLayout(null);
		Border border = BorderFactory.createTitledBorder("Dados do jogo");
		fieldset01.setBorder(border);
		fieldset01.setBounds(5, 5, 260, 190);
		titulo = new JLabel("ExtremeAntXiter - Online");
		titulo.setBackground(Color.WHITE);
		titulo.setFont(new Font("Arial",Font.BOLD,17)); 
		titulo.setBounds(15, 0, 200, 65);
		fieldset01.add(titulo);
		this.getContentPane().add(fieldset01);
		
//		ImageIcon imagem = Util.redimensionaImg(jogadorWarface.getFotoByte(), 110, 90);
//		JLabel figura = new JLabel(imagem);
//		figura.setBounds(5, 50, 110, 90);
//		fieldset01.add(figura);
		
		JLabel nome = new JLabel("Nome: "+jogadorWarface.getNome());
		nome.setBounds(15, 50, 115, 13);
		fieldset01.add(nome);
		
		JLabel nick = new JLabel("Nick: "+jogadorWarface.getNickJogo());
		nick.setBounds(15, 65, 100, 13);
		fieldset01.add(nick);

		JLabel email = new JLabel("Email: "+jogadorWarface.getEmail());
		email.setBounds(15, 80, 120, 13);
		fieldset01.add(email);
		
		String sexoa = jogadorWarface.getSexo().equals("m") ? "Masculino":"Feminino";
		JLabel sexo = new JLabel("Sexo: "+sexoa);
		sexo.setBounds(15, 95, 95, 13);
		fieldset01.add(sexo);
		
		JLabel ligaRemetente = new JLabel("Liga: "+jogadorWarface.getLigaRemetente());
		ligaRemetente.setBounds(15, 110, 95, 13);
		fieldset01.add(ligaRemetente);
		
//		JLabel nome = new JLabel("Nome:"+jogadorWarface.getNome());
//		nome.setBounds(120, 85, 100, 20);
//		fieldset01.add(nome);
		
		//Iniciando interface para monitoramento
		this.monitorando();
		
		//Carrega tabela de players on
		this.tabelaPlayers();
	}
	
	private void automaticUpdatePlayerOn(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					try{
						Thread.sleep(6000);
						int playersOn = new XitersDAOImpl().getNumeroPlayerOn(); 
						DefaultTableModel model = (DefaultTableModel) tabela.getModel();  
						if(playersOn != model.getRowCount()){
							int numeroPlayersOn = model.getRowCount() - 1;
							for (int i = numeroPlayersOn; i >=  0; i--) {
								model.removeRow(i);
							}
							Object[][] dadso = listaPlayers(new XitersDAOImpl().findAllPlayerOn());
							String [] colunas = {"Nome dos players"}; 
							model.setDataVector(dadso, colunas);
						}
					}catch(Exception e){
						System.err.println("Erro ao carregar players on \n"+e.getMessage());
					}
				}
			}
		}).start();
	}
	
	private void tabelaPlayers(){
		Object[][] dados = null;
		try{
			dados = listaPlayers(new XitersDAOImpl().findAllPlayerOn());
		}catch(Exception e){
			System.err.println("Erro ao carregar players on \n"+e.getMessage());
		}
		String [] colunas = {"Nome dos players"}; 
		
		JPanel painelFundo = new JPanel();
		Border border = BorderFactory.createTitledBorder("Tabelas");
		painelFundo.setBorder(border);
		painelFundo.setBackground(Color.WHITE);
		painelFundo.setBounds(5, 210, 260, 240);
		
		//painelFundo.setBounds(5, 200, 240, 150);
		painelFundo.setLayout(new BorderLayout()); 
		DefaultTableModel modelo = new DefaultTableModel(dados,colunas);
		tabela = new JTable(modelo);
		//tabela.setBounds(5, 20, 300, 100);
		tabela.setBackground(Color.WHITE);
		JScrollPane barraRolagem = new JScrollPane(tabela); 
		barraRolagem.setBackground(Color.WHITE);
		painelFundo.add(barraRolagem); 
		this.getContentPane().add(painelFundo);
		
		//Thread para ataulizar tabela dinamica.
		this.automaticUpdatePlayerOn();
	}
	
	public static void main(String[] args) {
		new MonitorandoPlayer();
	}
	
	private Object[][] listaPlayers(List<PlayersOn> players){
		Object[][] dados = new Object[players.size()][players.size()];
		int posicao = 0;
		for(PlayersOn player : players){
			if(jogadorWarface.getCodigoSala() == player.getNumeroSala()){
				dados[posicao][0] = player.getNick();
				posicao++;
			}
		}
		return dados;
	}
	
	private void monitorando(){
		JButton finishProcess = new JButton("Fininalizar Monitoramento");
		finishProcess.setBounds(22, 155, 225, 30);
        finishProcess.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent act) {
            	if (JOptionPane.showConfirmDialog(null,"Deseja sair") == JOptionPane.OK_OPTION){  
	            	try {
						new XitersDAOImpl().deletePlayerOff(getIdByFileSession());
						Util.removeJogadorWarfaceLocal();
					} catch (Exception e) {
						e.printStackTrace();
					}
	                System.exit(0);
            	}
            }
        });
        this.add(finishProcess);
		
	}
	
	public Integer getIdByFileSession(){
		File idFileSession = new File("FileSession.data");
		Integer id = null;
		try(BufferedReader leitor = new BufferedReader(new FileReader(idFileSession))){
			String linha = null;
			if((linha = leitor.readLine()) != null){
				String[] idSplit = linha.split("%");
				id = Integer.valueOf(idSplit[0]);
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		idFileSession.delete();
		return id;
	}
	
}