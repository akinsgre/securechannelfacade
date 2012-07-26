package org.rev6.scf;


public class ScpOutput extends ScpFile {

	public ScpOutput(String remotePath) {
		
    	if (remotePath == null) {
            throw new IllegalArgumentException("File reference and path must " +
                    "be non-null");
        }
    	
    	init(remotePath);
    	
	}


}
