package org.rev6.scf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

/**
 * This is not a Junit/TestNG test since there isn't an implemented way to mock an ssh server on this project yet.
 * Please fill in the blanks and run manually.
 */
public class SCFTest {

    //Fill in the blanks here
    private static final String HOST = System.getProperty("scf.host");
    private static final String USER = System.getProperty("scf.user");
    private static final String PASSWORD = System.getProperty("scf.password");
    private static final String PK_FILE_PATH = System.getProperty("scf.keypath");
    private static final String LOCAL_PATH = "src/test/resources/test.txt";
    private static final String REMOTE_PATH = System.getProperty("scf.remotepath");


    public static void main(String[] args) throws Exception {
    	tryOutputStreamDownload();
        tryUpload();
        tryDownload();
        tryByteArrayUpload();
    }
    public static void tryByteArrayUpload() throws SshException {
    	byte[] byteArray = "This is a byte array test".getBytes();
    	SshConnection sshConnection = new SshConnection(
                HOST, USER, PASSWORD);
        sshConnection.connect();
        
        //new Download method which results in an ScpFile which has the OutputStream of the file associated
        ScpUpload upload = new ScpUpload(new ScpInput(byteArray,REMOTE_PATH + "test_byte.txt"));

        sshConnection.executeTask(upload);
        System.out.println("Nothing was returned from tryByteArrayUpload");
        sshConnection.disconnect();
    }
    public static void tryDownload() throws SshException {
    	SshConnection sshConnection = new SshConnection(
                HOST, USER, PASSWORD);
        sshConnection.connect();
        // Original download method which writes the file to local disk
        ScpDownload download = new ScpDownload(
        			new ScpFile(new File(LOCAL_PATH),  REMOTE_PATH + "ppr_clm")
        				);
        
        sshConnection.executeTask(download);	
        FileInputStream input;
		OutputStream out = new ByteArrayOutputStream();
		try {
			input = new FileInputStream(new File(LOCAL_PATH));

	        IOUtils.copy(input, out);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		System.out.println("This was returned from tryDownload \n" +  ((ByteArrayOutputStream)out).toString() );
        //sshConnection.executeTask(command);

        sshConnection.disconnect();
    }
    public static void tryOutputStreamDownload() throws SshException {
    	SshConnection sshConnection = new SshConnection(
                HOST, USER, PASSWORD);
        sshConnection.connect();
        // Original download method which writes the file to local disk
//        ScpDownload download = new ScpDownload(
//        			new ScpFile(new File(LOCAL_PATH),  REMOTE_PATH)
//        				);
        
        //new Download method which results in an ScpFile which has the OutputStream of the file associated
        ScpFile scpFile = new ScpOutput(REMOTE_PATH + "ppr_clm");
        
        ScpDownload outDownload = new ScpDownload(scpFile);
//        ScpUpload upload = new ScpUpload(new ScpFile(new File(LOCAL_PATH),REMOTE_PATH));
//        SshCommand command = new SshCommand("ls");

        //sshConnection.executeTask(download);	
        sshConnection.executeTask(outDownload);	
        OutputStream out = scpFile.getOutputStream();
        System.out.println("this was returned from tryOutputStreamDownload\n" +  ((ByteArrayOutputStream)out).toString() );
        //sshConnection.executeTask(command);

        sshConnection.disconnect();
    }
    public static void tryUpload() throws SshException {
    	SshConnection sshConnection = new SshConnection(
                HOST, USER, PASSWORD);
        sshConnection.connect();
        
        //new Download method which results in an ScpFile which has the OutputStream of the file associated
        ScpUpload upload = new ScpUpload(new ScpFile(new File(LOCAL_PATH),REMOTE_PATH + "test.txt"));
        
        sshConnection.executeTask(upload);
        System.out.println("Nothing was returned from tryUpload" );
        sshConnection.disconnect();
    }
}