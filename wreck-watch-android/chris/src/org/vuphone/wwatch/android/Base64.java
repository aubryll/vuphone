package org.vuphone.wwatch.android;

public class Base64 {
	 
    public static final String base64code = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
 
        "abcdefghijklmnopqrstuvwxyz" + "0123456789" + "+/";    
    
    public static int splitLinesAt = 76;
    

    private static final byte[] byteCode = {
    	'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
    	'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
    	'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
    	'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
    	'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
    	'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
    	'w', 'x', 'y', 'z', '0', '1', '2', '3',
    	'4', '5', '6', '7', '8', '9', '+', '/'
    };
    
    public static byte[] base64Encode(byte[] msg) {
    	int len = msg.length;
    	
    	// Calculate the padding and the size of result array
    	// Simply doing len % 3 fails when len < 3
    	int pad = (3 - (len % 3)) % 3;
    	int size = (len + pad) * 4 / 3;
    	
    	// TODO - make creating plain[] more efficient. Maybe just stick an if into the for loop
    	byte[] plain = new byte[len + pad];
    	for (int i = 0; i < len; ++i)
    		plain[i] = msg[i];
    	for (int i = len; i < len + pad; ++i)
    		plain[i] = 0;
    	
    	
    	// The result array
    	byte[] encoded = new byte[size];
    	// An index to the result array
    	int index = 0;
    	
    	int mask = 63; // 00111111 in binary.
    	
    	for (int i = 0; i < len; i += 3) {
    		// Read in 3 bytes to make a 24-bit int
    		int threeBytes = (plain[i] << 16) + (plain[i + 1] << 8) + plain[i + 2];
    			
    		// Split the 24 bit int into 4 6-bit bytes
    		encoded[index] = byteCode[(threeBytes >> 18) & mask];
    		encoded[index + 1] = byteCode[(threeBytes >> 12) & mask];
    		encoded[index + 2] = byteCode[(threeBytes >> 6) & mask];
    		encoded[index + 3] = byteCode[threeBytes & mask];
    		
    		index += 4;
    	}
    	
    	for (int i = size - pad; i < size; ++i)
    		encoded[i] = '=';
    	
    	return encoded;
    }
   
    public static String base64Encode(String string) {
 
        String encoded = "";
        // determine how many padding bytes to add to the output
        int paddingCount = (3 - (string.length() % 3)) % 3;
        // add any necessary padding to the input
        string += "\0\0".substring(0, paddingCount);
        // process 3 bytes at a time, churning out 4 output bytes
        // worry about CRLF insertions later
        for (int i = 0; i < string.length(); i += 3) {
 
            int j = (string.charAt(i) << 16) + (string.charAt(i + 1) << 8) + string.charAt(i + 2);
            encoded = encoded + base64code.charAt((j >> 18) & 0x3f) +
 
                base64code.charAt((j >> 12) & 0x3f) +
                base64code.charAt((j >> 6) & 0x3f) +
                base64code.charAt(j & 0x3f);
 
        }
        // replace encoded padding nulls with "="
        return splitLines(encoded.substring(0, encoded.length() -
 
            paddingCount) + "==".substring(0, paddingCount));
 
    }
    public static String splitLines(String string) {
 
        String lines = "";
        for (int i = 0; i < string.length(); i += splitLinesAt) {
 
            lines += string.substring(i, Math.min(string.length(), i + splitLinesAt));
            lines += "\r\n";
 
        }
        lines = lines.substring(0,lines.length()-2);
        return lines;
 
    }
/*    
    public static void main(String[] arg) {
    	String str = "Hello World";
    	System.out.println(base64Encode(str));
    	System.out.println(new String(base64Encode(str.getBytes())));
    }
*/	
}

