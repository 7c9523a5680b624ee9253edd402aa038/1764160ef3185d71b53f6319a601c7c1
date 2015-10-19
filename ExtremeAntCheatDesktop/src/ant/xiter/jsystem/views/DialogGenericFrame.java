package ant.xiter.jsystem.views;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import ant.xiter.jsystem.dao.server.impl.XitersDAOImpl;
import ant.xiter.jsystem.entity.JogadorWarface;
import ant.xiter.jsystem.utilinterface.Util;

public class DialogGenericFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	public JogadorWarface jogadorWarface;
	public boolean finalizadoDialogo = true;
	private JLabel message;
	
	public DialogGenericFrame() {
		this.setLayout(null);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("Resources/imagens/extremeAntXiter.png"));  
		
		JLabel emailLabel = new JLabel("Seu email");
		emailLabel.setBounds(20, 10, 100, 10);
		this.add(emailLabel);
		final JTextField emailFild = new JTextField();
		emailFild.setBounds(20, 25, 200, 25);
		this.add(emailFild);
		
		JLabel senhaLabel = new JLabel("Sua senha");
		senhaLabel.setBounds(20, 53, 100, 10);
		this.add(senhaLabel);
		final JTextField senhaFild = new JTextField();
		senhaFild.setBounds(20, 65, 200, 25);
		this.add(senhaFild);
		
		JButton buttonLogar = new JButton("Autenticar");
		buttonLogar.setBounds(20, 90, 95, 30);
		buttonLogar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jogadorWarface = new XitersDAOImpl().autenticar(emailFild.getText(), senhaFild.getText());
				if(jogadorWarface != null && jogadorWarface.getNome() != null){
					setVisible(false);
					
					Icon figura = new ImageIcon("Resources/imagens/extremeAntXiter.png"); 

					boolean isBanido = new XitersDAOImpl().isBanido(jogadorWarface.getNickJogo(), verificaMacEnderec());
					if(isBanido){
						JOptionPane.showMessageDialog(null, "Seu Nick ou Endereco Mac foi banido em nosso sistema!","Banido",JOptionPane.INFORMATION_MESSAGE,figura);
						System.exit(0);
					}
					
					String codigoSala = (String) JOptionPane.showInputDialog(null,"Math da sala","Math de entrada",JOptionPane.INFORMATION_MESSAGE,figura,null,"");
					if(Util.isInt(codigoSala)){
						jogadorWarface.setCodigoSala(Integer.valueOf(codigoSala));
					}else{
						JOptionPane.showMessageDialog(null, "Erro ao processar Math da sala","Erro",JOptionPane.OK_OPTION,figura);
						System.exit(0);
					}
					
					finalizadoDialogo = false;
				}else{
					message.setVisible(true);
				}
			}
		});
		this.add(buttonLogar);
		
		JButton buttonSair = new JButton("Sair");
		buttonSair.setBounds(125, 90, 95, 30);
		buttonSair.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		this.add(buttonSair);
		
		message = new JLabel("Senha ou email invalido!");
		message.setBounds(50, 130, 200, 10);
		message.setBackground(Color.RED);
		message.setVisible(false);
		this.add(message);
		
		this.setTitle("Login");
		this.setSize(250, 200);
		this.setLocation(500, 300);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new DialogGenericFrame();
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
	
}
