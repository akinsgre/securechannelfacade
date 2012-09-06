package org.rev6.scf;

import java.io.File;
import java.io.OutputStream;

public class ScpFile {
    private File localFile = null;
    private String remoteDirectory;
    private String remoteFilename;
    private OutputStream outputStream ;
    private byte[] byteArray;
    
    public ScpFile(final File localFile, final String remotePath) {
        if (localFile == null || remotePath == null) {
            throw new IllegalArgumentException("File reference and path must " +
                    "be non-null");
        }

        this.localFile = localFile;

        init(remotePath);
    }
    protected ScpFile() {
    	
    }
    public ScpFile(final File file) {
        this(file, file.getName());
    }
    

    public OutputStream getOutputStream() {
    	return this.outputStream;
    }
    public void setOutputStream(OutputStream outputStream) {
    	this.outputStream = outputStream;
    }
    
    protected void init(String remotePath) {
    	if (remotePath.contains("/")) {
            int lastSlashIndex = remotePath.lastIndexOf("/");
            this.remoteDirectory = remotePath.substring(0,lastSlashIndex + 1);
            String filename = remotePath.substring(lastSlashIndex + 1);
            if ("".equals(filename.trim())) {
                this.remoteFilename = localFile.getName();
            }
            else {
                this.remoteFilename  = filename;
            }
        }
        else {
            this.remoteDirectory = remotePath;
            this.remoteFilename = remotePath;
        }
    }

	File getLocalFile() {
        return this.localFile;
    }

    String getRemoteDirectory() {
        return this.remoteDirectory;
    }

    String getRemoteFilename() {
        return this.remoteFilename;
    }

    String getRemoteFullPath() {
        if (this.remoteDirectory.equals(this.remoteFilename)) {
            return this.remoteFilename;
        }
        return this.remoteDirectory + this.remoteFilename;
    }

    long getFileSize() {
    	long result ;
    	if (this.localFile != null) {
    		result = this.localFile.length();
    	}
    	else {
    		result = this.getByteArray().length;
    	}
    	return result;
    }
	public byte[] getByteArray() {
		return byteArray;
	}
	public void setByteArray(byte[] byteArray) {
		this.byteArray = byteArray;
	}
}
