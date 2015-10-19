package ant.xiter.jsystem.views;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ListaJogadoresViews extends JPanel {

	private static final long serialVersionUID = 1L;
	public static final String[] nomeColunas = { "Nome", "Endereço", "Fone", "" };
	protected JTable table;
	protected JScrollPane scroller;
	protected ListaJogadorInterativo listaJogadorInterativo;

	public ListaJogadoresViews() {
		iniciarComponentes();
	}

	public void iniciarComponentes() {
		listaJogadorInterativo = new ListaJogadorInterativo(nomeColunas);
		// listaJogadorInterativo.addTableModelListener(new ListaJogadoresViews.TableModelListener());
		table = new JTable();
		table.setModel(listaJogadorInterativo);
		table.setSurrendersFocusOnKeystroke(true);
		// if (!listaJogadorInterativo.hasLinhasVazias()) {
		// listaJogadorInterativo.addLinhaVazia();
		// }
		scroller = new javax.swing.JScrollPane(table);
		table.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 300));
		// Essa coluna ficará escondida até que o Tab seja dado TableColumn
		// colunaEscondida =
		// table.getColumnModel().getColumn(TableModelInterativo.INDEX_ESCONDIDO);
		// colunaEscondida.setMinWidth(2); colunaEscondida.setPreferredWidth(2);
		// colunaEscondida.setMaxWidth(2); colunaEscondida.setCellRenderer(new
		// InteractiveRenderer(TableModelInterativo.INDEX_ESCONDIDO)); setLayout(new
		// BorderLayout()); add(scroller, BorderLayout.CENTER); } public void
		// destacarUltimaLinha(int linha) { int ultimaLinha =
		// modeloInterativo.getRowCount(); if (linha == ultimaLinha - 1) {
		// table.setRowSelectionInterval(ultimaLinha - 1, ultimaLinha - 1); } else {
		// table.setRowSelectionInterval(linha + 1, linha + 1); }
		// table.setColumnSelectionInterval(0, 0); } /** * Classe interna para
		// rendereizar as interações */ private class InteractiveRenderer extends
		// DefaultTableCellRenderer { private static final long serialVersionUID =
		// 1L; protected int colunaInterativa; public InteractiveRenderer(int
		// colunaInterativa) { this.colunaInterativa = colunaInterativa; } public
		// Component getTableCellRendererComponent(JTable table, Object valor,
		// boolean isSelecionado, boolean hasFoco, int linha, int coluna) {
		// Component c = super.getTableCellRendererComponent(table, valor,
		// isSelecionado, hasFoco, linha, coluna); if (coluna == colunaInterativa &&
		// hasFoco) { if ((FormInterativo.this.modeloInterativo.getRowCount() - 1)
		// == linha && !FormInterativo.this.modeloInterativo.hasLinhasVazias()) {
		// FormInterativo.this.modeloInterativo.addLinhaVazia(); }
		// destacarUltimaLinha(linha); } return c; } } /** * Ouvinte para o modelo
		// interativo */ public class TableModelListenerInterativo implements
		// TableModelListener { /** * Método que recebe os valores de linha
		// modificados */ public void tableChanged(TableModelEvent evt) { if
		// (evt.getType() == TableModelEvent.UPDATE) { int column = evt.getColumn();
		// int row = evt.getFirstRow(); table.setColumnSelectionInterval(column + 1,
		// column + 1); table.setRowSelectionInterval(row, row); } } } /* * Método
		// main */ public static void main(String[] args) { try {
		// UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		// JFrame frame = new JFrame("Formulário Interativo");
		// frame.addWindowListener(new WindowAdapter() { public void
		// windowClosing(WindowEvent evt) { System.exit(0); } });
		// frame.getContentPane().add(new FormInterativo()); frame.pack();
		// frame.setVisible(true); } catch (Exception e) { e.printStackTrace(); } }
	}
}
