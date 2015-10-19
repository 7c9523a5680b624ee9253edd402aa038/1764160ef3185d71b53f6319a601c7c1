package ant.xiter.jsystem.enums;

public enum TipoHackers {

	WXF("wfx.exe"), GAME("game.exe"), XITER_SEM_NOME("");

	private TipoHackers(String hack) {
		this.hack = hack;
	}

	private String hack;

	public String getHack() {
		return hack;
	}

	public void setHack(String hack) {
		this.hack = hack;
	}

}