package ant.xiter.jsystem.entity;

import java.util.ArrayList;
import java.util.List;

public class MemoriaCompartilhada {

	private List<String> memoriaCompartilhada = new ArrayList<>();

	private static MemoriaCompartilhada instancia = new MemoriaCompartilhada();

	private boolean shutdown;

	public MemoriaCompartilhada() {

	}

	public String recuperaElemento() {
		return this.memoriaCompartilhada.remove(0);
	}

	public void adicionarElemento(String elemento) {
		this.memoriaCompartilhada.add(elemento);
	}

	public boolean hasElemento() {
		return !this.memoriaCompartilhada.isEmpty();
	}

	public static MemoriaCompartilhada getInstancia() {
		return instancia;
	}

	public static void setInstancia(MemoriaCompartilhada instancia) {
		MemoriaCompartilhada.instancia = instancia;
	}

	public boolean isShutdown() {
		return shutdown;
	}

	public void setShutdown(boolean shutdown) {
		this.shutdown = shutdown;
	}

}