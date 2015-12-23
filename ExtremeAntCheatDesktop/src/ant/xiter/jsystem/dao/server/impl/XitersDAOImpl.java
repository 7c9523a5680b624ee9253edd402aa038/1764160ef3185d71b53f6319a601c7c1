package ant.xiter.jsystem.dao.server.impl;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import ant.xiter.jsystem.dao.factory.FactoryConnectJDBC;
import ant.xiter.jsystem.dao.server.XitersDAO;
import ant.xiter.jsystem.entity.Jogador;
import ant.xiter.jsystem.entity.JogadorWarface;
import ant.xiter.jsystem.entity.PlayersOn;
import ant.xiter.jsystem.utilinterface.Util;

public class XitersDAOImpl implements XitersDAO {

	private static final String SQL_INSERT_JOGADOR = "insert into xiter(nome_xiter, nome_jogador, descricao_pc, data_usa_xiter, foto_xiter, descricao_processo, is_view_admin)"
			+ "values(?,?,?,?,?,?,?)";
	private static final String SQL_INSERT_PLAYER_ON = "insert into player_on(nome_player,ativo,data,usuario_id,numero_sala,serial_player_id)"
			+ "values(?,?,?,?,?,?)";	
	private static final String SQL_SELECT_SERVIDOR = "select status_servidor from serial_player where id = ?";
	private static final String SQL_SELECT_PLAYER_ON = "select id,nome_player,ativo,numero_sala from player_on";
	private static final String SQL_DELETE_PLAYER_OFF = "delete from player_on where id = ?";
	private static final String SQL_SELECT_COUNT_PLAYER_ON = "select count(id) as players from player_on where ativo = true";
	private static final String SQL_SELECT_ULTIMA_VERSAO = "select ultima_versao from versao_sistema";
	private static final String SQL_SELECT_JOGADOR_WARFACE = "select nome,email,nick,codigoAntXiter,foto from jogador_warface where codigoAntXiter = ?";
	private static final String SQL_SELECT_COMANDOS_BY_TROM = "select comando from comando_antxiter_trom where usuario_id = ?";
	private static final String SQL_INSERT_RETORNO_COMANDO = "insert into return_comandos(dados,tipo,usuario_id)values(?,?,?);  ";
	private static final String SQL_SELECT_AUTENTICAR = "select u.id,u.nome,u.email,u.nick,u.sexo,sp.remetente,sp.id as liga_id,sp.numero_chaves as chaves_liga from usuario u"
			+ " left join serial_player sp on (sp.id = u.serial_player_id)"
			+ " where u.email = ? and u.senha_ant_xiter = ?";
	private static final String SQL_DELETE_COMANDO_EXECUTADO = "delete from comando_antxiter_trom where usuario_id = ?";
	private static final String SQL_SELECT_FIND_PLAYER_ON = "select * from player_on where usuario_id = ?";
	private static final String SQL_UPDATE_DETALHES_PLAYER_ON = "update player_on set detalhes = ? where id = ?";
	private static final String SQL_SELECT_IS_BANIDO = "select id from xiter where nome_jogador = ? or descricao_pc = ? ";
	private static final String SQL_SELECT_COUNT_SERIAL_PLAYER = "select count(id) from player_on where serial_player_id = ?";

	@Override
	public Integer getCountSerialHashToPlayerOn(Integer id) {
		try(Connection connection = FactoryConnectJDBC.getConnection();
				PreparedStatement statement = connection.prepareStatement(SQL_SELECT_COUNT_SERIAL_PLAYER)){
			
			statement.setInt(1, id);
			try(ResultSet rs = statement.executeQuery()){
				if(rs.next()) {
					return rs.getInt(1);
				}
			}
			
			return -0;
		}catch (Exception e) {
			System.err.println(e.getMessage());
			return -0;
		}
	}
	
	
	@Override
	public Integer insert(Jogador jogador) throws Exception {
		try(Connection connection = FactoryConnectJDBC.getConnection();
				PreparedStatement statement = connection.prepareStatement(SQL_INSERT_JOGADOR)){
			
			int index = 1;
			statement.setString(index++, getXitersAndMacro(jogador.getXiters()));
			statement.setString(index++, jogador.getNickJogo());
			statement.setBytes(index++, jogador.getTxtDescricaoDoPc());
			statement.setTimestamp(index++, new java.sql.Timestamp(new java.util.Date().getTime()));
			statement.setBytes(index++, jogador.getPrintXiterOrMacro());
			statement.setString(index++, jogador.getDescricaoServicos());
			statement.setBoolean(index++, false);
			
			statement.execute();
		}catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		return null;
	}

	public String getXitersAndMacro(List<String> list){
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		for(String value : list){
			builder.append(value);
		}
		builder.append(")");
		
		return builder.toString();
	}

	
	@Override
	public Integer insertPlayerOn(String nome,int usuario_id,int numeroSala,int ligaId) throws Exception {
		try(Connection connection = FactoryConnectJDBC.getConnection();
				PreparedStatement statement = connection.prepareStatement(SQL_INSERT_PLAYER_ON, PreparedStatement.RETURN_GENERATED_KEYS)){
			
			int index = 1;
			statement.setString(index++, nome);
			statement.setBoolean(index++, Boolean.TRUE);
			statement.setDate(index++, new Date(new java.util.Date().getTime()));
			statement.setInt(index++, usuario_id);
			statement.setInt(index++, numeroSala);
			statement.setInt(index++, ligaId);
			
			statement.executeUpdate();
			try(ResultSet rs = statement.getGeneratedKeys()){
				if(rs.next()){
					return rs.getInt(1);
				}
			}
		}catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
		return null;
	}
	
	@Override
	public boolean getServidorLiberado(int id) throws Exception {
		try(Connection connection = FactoryConnectJDBC.getConnection();
				PreparedStatement statement = connection.prepareStatement(SQL_SELECT_SERVIDOR)){
			
			statement.setInt(1, id);
			try(ResultSet rs = statement.executeQuery()){
				if(rs.next()){
					return rs.getBoolean("status_servidor");
				}
			}
			
			return false;
		}catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	@Override
	public List<PlayersOn> findAllPlayerOn() throws Exception {
		try(Connection connection = FactoryConnectJDBC.getConnection();
				PreparedStatement statement = connection.prepareStatement(SQL_SELECT_PLAYER_ON);
				ResultSet rs = statement.executeQuery()){
			
			List<PlayersOn> listaPlayerOn = new ArrayList<>();
			while(rs.next()){
				boolean ativo = rs.getBoolean("ativo");
				if(ativo){
					PlayersOn on = new PlayersOn();
					on.setId(rs.getInt("id"));
					on.setNick(rs.getString("nome_player"));
					on.setNumeroSala(rs.getInt("numero_sala"));
					
					listaPlayerOn.add(on);
				}
			}
			
			return listaPlayerOn;
		}catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}
	
	@Override
	public PlayersOn findByUsuarioId(int id) throws Exception {
		try(Connection connection = FactoryConnectJDBC.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_FIND_PLAYER_ON)){
			
			preparedStatement.setInt(1, id);
			try(ResultSet rs = preparedStatement.executeQuery()){
				if(rs.next()){
					PlayersOn playersOn = new PlayersOn();
					playersOn.setId(rs.getInt("id"));
					playersOn.setDetalhes("detalhes");
					
					return playersOn;
				}
			}
			
			return null;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean updateDetalhesPlayerOn(PlayersOn playersOn) throws Exception {
		try(Connection connection = FactoryConnectJDBC.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_DETALHES_PLAYER_ON)){
			
			preparedStatement.setString(1, playersOn.getDetalhes());
			preparedStatement.setInt(2, playersOn.getId());
			preparedStatement.executeUpdate();
			
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void deletePlayerOff(Integer id) throws Exception {
		try(Connection connection = FactoryConnectJDBC.getConnection();
				PreparedStatement statement = connection.prepareStatement(SQL_DELETE_PLAYER_OFF)){
			
			statement.setInt(1, id);
			statement.executeUpdate();	
			
		}catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public Integer getNumeroPlayerOn() {
		try(Connection connection = FactoryConnectJDBC.getConnection();
				PreparedStatement statement = connection.prepareStatement(SQL_SELECT_COUNT_PLAYER_ON);
				ResultSet rs = statement.executeQuery()){
			
			if(rs.next()){
				return rs.getInt("players");
			}
			return null;
		}catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	@Override
	public String getUltimaVersao() {
		try(Connection connection = FactoryConnectJDBC.getConnection();
				PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ULTIMA_VERSAO);
				ResultSet rs = statement.executeQuery()){
			
			if(rs.next()){
				return rs.getString("ultima_versao");
			}
			return null;
		}catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}
	
	public JogadorWarface findJogadorWarfaceByCodigoAntXiter(String codigoAntXiter){
		try(Connection connection = FactoryConnectJDBC.getConnection();
				PreparedStatement statement = connection.prepareStatement(SQL_SELECT_JOGADOR_WARFACE)){
			
			statement.setString(1, codigoAntXiter);
			JogadorWarface warface = new JogadorWarface();
			try(ResultSet rs = statement.executeQuery()){
				if (rs.next()) {
					warface.setNome(rs.getString("nome"));
					warface.setNickJogo(rs.getString("nick"));
					warface.setEmail(rs.getString("email"));
					warface.setCodigoAntXiter(rs.getString("codigoAntXiter"));
					Blob fotoBlob = rs.getBlob("foto");
					int fotoLength = (int) fotoBlob.length();  
					warface.setFotoByte(fotoBlob.getBytes(1, fotoLength));
					
				}
			}
			return warface;
		}catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	@Override
	public int findComenadosAll() {
		try(Connection connection = FactoryConnectJDBC.getConnection();
				PreparedStatement statement = connection.prepareStatement(SQL_SELECT_COMANDOS_BY_TROM)){
			
			statement.setInt(1, Util.getJogadorWarface().getId());
			try(ResultSet rs = statement.executeQuery()){
				if (rs.next()) {
					return rs.getInt("comando");
				}
			}
			return 0;
		}catch (Exception e) {
			System.err.println(e.getMessage());
			return 0;
		}
	}

	@Override
	public boolean sendRetornoByComando(byte[] retorno,int tipo,int usuario_id) {
		try(Connection connection = FactoryConnectJDBC.getConnection();
				PreparedStatement statement = connection.prepareStatement(SQL_INSERT_RETORNO_COMANDO)){
			
			statement.setBytes(1, retorno);
			statement.setInt(2, tipo);
			statement.setInt(3, usuario_id);
			statement.execute();
			
			return true;
		}catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	public JogadorWarface autenticar(String email, String senha) {
		try(Connection connection = FactoryConnectJDBC.getConnection();
				PreparedStatement statement = connection.prepareStatement(SQL_SELECT_AUTENTICAR)){
			
			statement.setString(1, email);
			statement.setString(2, senha);
			JogadorWarface warface = new JogadorWarface();
			try(ResultSet rs = statement.executeQuery()){
				if (rs.next()) {
					warface.setNome(rs.getString("nome"));
					warface.setNickJogo(rs.getString("nick"));
					warface.setEmail(rs.getString("email"));
					warface.setSexo(rs.getString("sexo"));
					//warface.setFotoByte(rs.getBytes("foto"));
					warface.setId(rs.getInt("id"));
					warface.setLigaRemetente(rs.getString("remetente"));
					warface.setLigaRemetenteId(rs.getInt("liga_id"));
					warface.setLigaNumeroChaves(rs.getInt("chaves_liga"));
				}
			}
			return warface;
		}catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	@Override
	public void deleteComandoAntXiterTrom(int id) {
		try(Connection connection = FactoryConnectJDBC.getConnection();
				PreparedStatement statement = connection.prepareStatement(SQL_DELETE_COMANDO_EXECUTADO)){
			
			statement.setInt(1, id);
			statement.executeUpdate();	
			
		}catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public boolean isBanido(String nickJogo, String macEnderec) {
		try(Connection connection = FactoryConnectJDBC.getConnection();
				PreparedStatement statement = connection.prepareStatement(SQL_SELECT_IS_BANIDO)){
			
			statement.setString(1, nickJogo);
			statement.setString(2, macEnderec);
			try(ResultSet rs = statement.executeQuery()){
				if(rs.next()){
					return rs.getInt("id") > 0;
				}
			}
			
			return false;
		}catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

}