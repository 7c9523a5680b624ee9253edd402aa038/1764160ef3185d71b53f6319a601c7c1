package ant.xiter.jsystem.utilinterface;

import java.io.IOException;
import java.io.InputStream;

public class UtilFile {
	
	public static byte[] inputStreamToByte(InputStream inputStream, Long size) throws Exception {
		byte[] bytes = new byte[size.intValue()];  
		int offset = 0;  
		int numRead = 0;  
		while (offset < bytes.length && (numRead=inputStream.read(bytes, offset, bytes.length-offset)) >= 0) {  
		    offset += numRead;  
		}  
		          
		if (offset < bytes.length) {  
		    throw new IOException("Could not completely read file ");  
		}
		
		return bytes;
	}

}
