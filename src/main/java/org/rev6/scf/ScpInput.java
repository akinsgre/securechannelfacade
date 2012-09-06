package org.rev6.scf;

public class ScpInput extends ScpFile {
public ScpInput(byte[] byteArray, String remotePath) {
		
    	if (remotePath == null) {
            throw new IllegalArgumentException("File reference and path must " +
                    "be non-null");
        }
    	
    	init(remotePath);
    	this.setByteArray(byteArray);
    	
	}
}
