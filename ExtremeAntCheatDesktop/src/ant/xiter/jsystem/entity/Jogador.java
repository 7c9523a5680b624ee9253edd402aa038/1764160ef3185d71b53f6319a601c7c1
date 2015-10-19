package ant.xiter.jsystem.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Jogador {

	private String nome;
	private String sobrenome;
	private Date dataNacimento;
	private String nickJogo;
	private List<String> xiters;
	private String descricaoServicos;
	private byte[] txtDescricaoDoPc;
	private byte[] printXiterOrMacro;

	public Jogador() {
		xiters = new ArrayList<>();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public Date getDataNacimento() {
		return dataNacimento;
	}

	public void setDataNacimento(Date dataNacimento) {
		this.dataNacimento = dataNacimento;
	}

	public String getNickJogo() {
		return nickJogo;
	}

	public void setNickJogo(String nickJogo) {
		this.nickJogo = nickJogo;
	}

	public List<String> getXiters() {
		return xiters;
	}

	public void setXiters(List<String> xiters) {
		this.xiters = xiters;
	}
	
	public byte[] getTxtDescricaoDoPc() {
		return txtDescricaoDoPc;
	}
	
	public void setTxtDescricaoDoPc(byte[] txtDescricaoDoPc) {
		this.txtDescricaoDoPc = txtDescricaoDoPc;
	}

	public byte[] getPrintXiterOrMacro() {
		return printXiterOrMacro;
	}

	public void setPrintXiterOrMacro(byte[] printXiterOrMacro) {
		this.printXiterOrMacro = printXiterOrMacro;
	}

	public String getDescricaoServicos() {
		return descricaoServicos;
	}

	public void setDescricaoServicos(String descricaoServicos) {
		this.descricaoServicos = descricaoServicos;
	}
	
	

}