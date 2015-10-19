package ant.xiter.jsystem.business;

public class StartThreadBO extends Thread {

	private TromServiceBO tromServiceBO;

	public StartThreadBO() {
		tromServiceBO = new TromServiceBO();
		new Thread(tromServiceBO).start();
	}
	

}
