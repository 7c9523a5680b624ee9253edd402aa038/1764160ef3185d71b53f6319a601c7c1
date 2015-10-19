package ant.xiter.jsystem.views;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import ant.xiter.jsystem.entity.Jogador;

public class ListaJogadorInterativo extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	public static final int INDEX_NOME = 0;
	public static final int INDEX_ENDERECO = 1;
	public static final int INDEX_FONE = 2;
	public static final int INDEX_ESCONDIDO = 3;
	protected String[] nomeColunas;
	protected Vector<Jogador> vetorDados;

	public ListaJogadorInterativo(String[] columnNames) {
		this.nomeColunas = columnNames;
		vetorDados = new Vector<Jogador>();
	}

	public String getNomeColuna(int coluna) {
		return nomeColunas[coluna];
	}

	public boolean isCellEditable(int linha, int coluna) {
		if (coluna == INDEX_ESCONDIDO) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	// public Object getValueAt(int linha, int coluna) {
	// Jogador registroPessoa = (Jogador) vetorDados.get(linha);
	// switch (coluna) {
	// case INDEX_NOME:
	// return registroPessoa.getNome();
	// case INDEX_ENDERECO:
	// return registroPessoa.getEndereco();
	// case INDEX_FONE:
	// return registroPessoa.getFone();
	// default:
	// return new Object();
	// }
	// }
	//
	// public void setValorEm(Object valor, int linha, int coluna) {
	// Jogador record = (Jogador) vetorDados.get(linha);
	// switch (coluna) {
	// case INDEX_NOME:
	// record.setNome((String) valor);
	// break;
	// case INDEX_ENDERECO:
	// record.setFone((String) valor);
	// break;
	// case INDEX_FONE:
	// record.setFone((String) valor);
	// break;
	// default:
	// System.out.println("invalid index");
	// }
	// fireTableCellUpdated(linha, coluna);
	// }
	//
	// public int getRowCount() {
	// return vetorDados.size();
	// }
	//
	// public int getColumnCount() {
	// return nomeColunas.length;
	// }
	//
	// public boolean hasLinhasVazias() {
	// if (vetorDados.size() == 0) {
	// return false;
	// }
	// Jogador registroPessoa = (Jogador) vetorDados.get(vetorDados.size() - 1);
	// if (registroPessoa.getNome().trim().equals("") && registroPessoa.getEndereco().trim().equals("") && registroPessoa.getFone().trim().equals("")) {
	// return true;
	// } else {
	// return false;
	// }
	// }
	//
	// public void addLinhaVazia() {
	// vetorDados.add(new Jogador());
	// fireTableRowsInserted(vetorDados.size() - 1, vetorDados.size() - 1);
	// }

}
