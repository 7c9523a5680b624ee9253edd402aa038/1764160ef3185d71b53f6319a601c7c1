package ant.xiter.jsystem.entity;

import java.io.InputStream;
import java.io.Serializable;

import ant.xiter.jsystem.utilinterface.UtilFile;

public class JogadorWarface implements Serializable {
	
	private static final long serialVersionUID = -133343357349772982L;
	private String nome;
	private String nickJogo;
	private String email;
	private String codigoAntXiter;
	private String foto;
	private String sexo;
	private String ligaRemetente;
	private int ligaRemetenteId;
	private int ligaNumeroChaves;
	private int codigoSala;
	private byte[] fotoByte;
	private int id;
	
	public int getCodigoSala() {
		return codigoSala;
	}
	
	public void setCodigoSala(int codigoSala) {
		this.codigoSala = codigoSala;
	}
	
	public String getLigaRemetente() {
		return ligaRemetente;
	}
	
	public void setLigaRemetente(String ligaRemetente) {
		this.ligaRemetente = ligaRemetente;
	}
	
	public String getSexo() {
		return sexo;
	}
	
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	
	public String getNickJogo() {
		return nickJogo;
	}
	
	public void setNickJogo(String nickJogo) {
		this.nickJogo = nickJogo;
	}
	
	public String getCodigoAntXiter() {
		return codigoAntXiter;
	}
	
	public void setCodigoAntXiter(String codigoAntXiter) {
		this.codigoAntXiter = codigoAntXiter;
	}
	
	public String getFoto() {
		return foto;
	}
	
	public void setFoto(String foto) {
		this.foto = foto;
	}
	
	public byte[] getFotoByte() {
		return fotoByte;
	}
	
	public void setFotoByte(byte[] fotoByte) {
		this.fotoByte = fotoByte;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getLigaRemetenteId() {
		return ligaRemetenteId;
	}
	
	public void setLigaRemetenteId(int ligaRemetenteId) {
		this.ligaRemetenteId = ligaRemetenteId;
	}
	
	public int getLigaNumeroChaves() {
		return ligaNumeroChaves;
	}
	
	public void setLigaNumeroChaves(int ligaNumeroChaves) {
		this.ligaNumeroChaves = ligaNumeroChaves;
	}

	public void convertiInputStremToByte(InputStream inputStream , Long size){
		try {
			this.fotoByte = UtilFile.inputStreamToByte(inputStream, size);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		return "JogadorWarface [nickJogo=" + nickJogo + "Id=" + id + ", codigoAntXiter="
				+ codigoAntXiter + ", foto=" + (fotoByte != null ? "Tem foto":"Não tem foto")+ "]";
	}
	
}
