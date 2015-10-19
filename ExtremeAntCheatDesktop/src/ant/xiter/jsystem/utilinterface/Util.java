package ant.xiter.jsystem.utilinterface;

import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import ant.xiter.jsystem.entity.JogadorWarface;
import ant.xiter.jsystem.views.MonitorandoPlayer;

public class Util {

	//RzSynapse.exe
	public static final Integer SEQUENCIAL_HASH = 0201354222;
	public static final String CMD_THEMA_BAISC = "cmd_thema_basic.bat";
	public static final String TASK_LIST = "tasklist";
	public static final String ENDERECO_MAC = "getmac";
	
	public static boolean executeComandoCMD(String cmd){
		try{
			Process process = Runtime.getRuntime().exec(cmd);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			return (bufferedReader.readLine() != null);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public static void main(String[] args) {
		try(OutputStream outputStream = new FileOutputStream(new File("C:\\Users\\usuario\\Desktop\\text.txt"))){
			outputStream.write(Util.executeComandoCMDReturn(TASK_LIST).getBytes());
		} catch (Exception e) {
		}
	}
	
	public static String executeComandoCMDReturn(String cmd){
		try{
			Process process = Runtime.getRuntime().exec(cmd.toString());
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String linha,retorno = null;
			while((linha = bufferedReader.readLine()) != null){
				retorno += "\n"+linha;
			}
			retorno = retorno.contains("null") ? retorno.replace("null", "") : retorno;
			return retorno;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}	
	}
			
	public static String getComandoFileCMD(String comando){
		try(BufferedReader buffer = new BufferedReader(new InputStreamReader(MonitorandoPlayer.class.getResourceAsStream(comando)))){
			String linha = null;	
			return (linha = buffer.readLine()) != null ? linha : null;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static byte[] capturePrintTela(){
		try {
			Robot robot = new Robot();
			BufferedImage image = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));  
			return convertiBufferedImageToByteArray(image);
		} catch (AWTException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static byte[] convertiBufferedImageToByteArray(BufferedImage originalImage){
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(originalImage, "jpg", baos);
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			baos.close();
			return imageInByte;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static ImageIcon redimensionaImg(byte[] bytes, int new_w, int new_h){  
	    try {  
	    	InputStream image = new ByteArrayInputStream(bytes);
	        BufferedImage imagem = ImageIO.read(image);  
	        BufferedImage new_img = new BufferedImage(new_w, new_h, BufferedImage.TYPE_INT_RGB);  
	  
	        Graphics2D g = new_img.createGraphics();  
	        g.drawImage(imagem, 0, 0, new_w, new_h, null);  
	        g.dispose();  
	  
	        return new ImageIcon(new_img);  
	    } catch (IOException ex) {  
	        throw new RuntimeException(ex);  
	    }  
	}
	
	public static String parseHashMD5(String value){
		try {
			MessageDigest securty;
			securty = MessageDigest.getInstance("MD5");
		    securty.update(value.getBytes(),0,value.length());
		    BigInteger hash = new BigInteger(1, securty.digest(value.getBytes()));  

		    return hash.toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void JogadorWarfaceCreateLocalData(JogadorWarface jogadorWarface){
		try(OutputStream escritorByte = new FileOutputStream("user.bin");
				ObjectOutputStream escritorObjeto = new ObjectOutputStream(escritorByte)){
			
			escritorObjeto.writeObject(jogadorWarface);
			escritorObjeto.flush();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static JogadorWarface getJogadorWarface( ){
		try(InputStream leitorByte = new FileInputStream("user.bin");
				ObjectInputStream leitorObjeto = new ObjectInputStream(leitorByte)){
			
			return (JogadorWarface) leitorObjeto.readObject(); 
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void removeJogadorWarfaceLocal(){
		new File("user.bin").delete();
	}
	
	public static boolean isInt(String value){
		try{
			Integer.valueOf(value);
			return true;		
		}catch(Exception e){
			return false;
		}
	}
	
}