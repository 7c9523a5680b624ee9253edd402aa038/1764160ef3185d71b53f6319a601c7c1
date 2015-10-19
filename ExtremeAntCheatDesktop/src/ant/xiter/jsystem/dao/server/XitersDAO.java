package ant.xiter.jsystem.dao.server;

import java.util.List;

import ant.xiter.jsystem.entity.Jogador;
import ant.xiter.jsystem.entity.PlayersOn;

public interface XitersDAO {

	Integer insert(Jogador jogador) throws Exception;
	
	Integer insertPlayerOn(String nome,int usuario_id,int numeroSala,int ligaId) throws Exception;

	boolean getServidorLiberado(int id) throws Exception;

	List<PlayersOn> findAllPlayerOn() throws Exception;
	
	PlayersOn findByUsuarioId(int id) throws Exception;
	
	boolean updateDetalhesPlayerOn(PlayersOn playersOn) throws Exception;

	void deletePlayerOff(Integer id) throws Exception;

	Integer getNumeroPlayerOn();

	String getUltimaVersao();

	int findComenadosAll();
	
	boolean sendRetornoByComando(byte[] retorno, int tipo,int usuario_id);

	void deleteComandoAntXiterTrom(int id);
}
