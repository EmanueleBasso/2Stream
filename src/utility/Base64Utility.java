package utility;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Base64;

import org.apache.commons.io.IOUtils;

public class Base64Utility {	
	public static String encodeFileToBase64(InputStream immagine) throws IOException{
		byte[] bytes = IOUtils.toByteArray(immagine);
		bytes = Base64.getEncoder().encode(bytes);
		
		String encoded = new String(bytes, Charset.forName("UTF-8"));
		
		return encoded;
	}
	
}

