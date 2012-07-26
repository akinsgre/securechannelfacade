package org.rev6.scf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;

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

        SshConnection sshConnection = new SshConnection(
                HOST, USER, PASSWORD);
//        SshConnection sshConnection = new SshConnection(
//                HOST, USER, new File(PK_FILE_PATH)); //Uses Private Key

        sshConnection.connect();
        // Original download method which writes the file to local disk
        ScpDownload download = new ScpDownload(
        			new ScpFile(new File(LOCAL_PATH),  REMOTE_PATH)
        				);
        
        //new Download method which results in an ScpFile which has the OutputStream of the file associated
        ScpFile scpFile = new ScpOutput(REMOTE_PATH);
        
        ScpDownload outDownload = new ScpDownload(scpFile);
        ScpUpload upload = new ScpUpload(new ScpFile(new File(LOCAL_PATH),REMOTE_PATH));
        SshCommand command = new SshCommand("ls");

        //sshConnection.executeTask(upload);
        //sshConnection.executeTask(download);	
        sshConnection.executeTask(outDownload);	
        OutputStream out = scpFile.getOutputStream();
        System.out.println("this was returned \n" +  ((ByteArrayOutputStream)out).toString() );
        //sshConnection.executeTask(command);

        sshConnection.disconnect();
    }
}