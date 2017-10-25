package com.ebix.licence;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.log4j.Logger;


public class Crypto128Bit {
	
	private static final Logger LOGGER = Logger.getLogger(Crypto128Bit.class);
	
    private static final String ALGORITHM_128_BIT = "AES";
    Cipher cipher = null;
    Key key = null;
    /**
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * 
     */
    public Crypto128Bit() throws NoSuchAlgorithmException, NoSuchPaddingException { 
        super();
        cipher = Cipher.getInstance( ALGORITHM_128_BIT );	// 128-bit encryption, baby!
    }
 
    public String encrypt64( byte[] b ) {
        sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        return encoder.encode( b );        
    }
    
//    public String encrypt64( String s ) {
//        return encrypt64( s.getBytes() );
//    }

    public String decrypt64String( String s ) {
        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
        try {
            return new String( decoder.decodeBuffer(s));
        } catch (IOException e) {
        	LOGGER.error(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException( e );
        }
    }
    
    public byte[] decrypt64( String s ) {
        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
        try {
            return decoder.decodeBuffer(s);
        } catch (IOException e) {
        	LOGGER.error(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException( e );
        }
    }

    public String encrypt(String s, String keyText ) throws InvalidKeyException, 
	    					BadPaddingException, IllegalBlockSizeException {
	    key = getKey( keyText );
        cipher.init(Cipher.ENCRYPT_MODE, key);
	    byte[] inputBytes = s.getBytes();
	    return encrypt64(cipher.doFinal(inputBytes));
    }

	public String decrypt( String s, String keyText )
	    throws InvalidKeyException, 
	           BadPaddingException,
	           IllegalBlockSizeException {
	    key = getKey( keyText );
	    cipher.init(Cipher.DECRYPT_MODE, key);
	    
	    return new String( cipher.doFinal(decrypt64( s )));
	  }
	
	/**
	 * Build an actual <code>Key</code> from a block of text
	 * @param keyText this will be trimmed before use to avoid
	 * trailing/leading spaces issues.
	 * @return
	 */
	private Key getKey( String keyText ) {
	    final String k = keyText.trim();
	    return new Key() {
            public byte[] getEncoded() {
                byte[] keyTextBytes = k.getBytes();
                byte[] bytes = new byte[16];
                
                // initialize all 16 bytes of our key with values 
                // from the keyText
                for ( int i = 0; i < 16; i++ )
                    bytes[i] = keyTextBytes[i%k.length()];
                
                // now loop through any bytes beyond 16 in the keyText 
                // and tweak the bytes
                for ( int i = 16; i < k.length(); i++ )
                    bytes[i%16] += keyTextBytes[i];
                
                return bytes;
            }

            public String getAlgorithm() {
                return "AES";
            }

            public String getFormat() {
                return "RAW";
            }
        };
	    
	}
}
