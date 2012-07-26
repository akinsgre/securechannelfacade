package org.rev6.scf;

import java.io.File;

/**
 * This is not a Junit/TestNG test since there isn't an implemented way to mock an ssh server on this project yet.
 * Please fill in the blanks and run manually.
 */
public class SCFTest {

    //Fill in the blanks here
    private static final String HOST = "10.211.55.18";
    private static final String USER = "oracle";
    private static final String PASSWORD = "Salvation";
    private static final String PK_FILE_PATH = "/Users/gakins/.ssh/id_dsa";
    private static final String LOCAL_PATH = "test.txt";
    private static final String REMOTE_PATH = "/u01/reports";


    public static void main(String[] args) throws Exception {

        SshConnection sshConnection = new SshConnection(
                HOST, USER, PASSWORD);
//        SshConnection sshConnection = new SshConnection(
//                HOST, USER, new File(PK_FILE_PATH)); //Uses Private Key

        sshConnection.connect();

        ScpDownload download = new ScpDownload(new ScpFile(new File(LOCAL_PATH),
                REMOTE_PATH));
        ScpUpload upload = new ScpUpload(new ScpFile(new File(LOCAL_PATH),REMOTE_PATH));
        SshCommand command = new SshCommand("ls");

        sshConnection.executeTask(upload);
        sshConnection.executeTask(download);
        sshConnection.executeTask(command);

        sshConnection.disconnect();
    }
}