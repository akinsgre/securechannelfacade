package org.rev6.scf;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;

/**
 * SshTask for simulating an scp from a remote server.  A valid ScpFile
 * must be passed in either during construction or via the set
 *
 * @author jwhaley
 */
public class ScpDownload extends SshTask {
    private static final String SCP_DOWNLOAD_COMMAND = "scp -f ";

    private ScpFile scpFile;

    public ScpDownload() {
        super();
    }

    public ScpDownload(ScpFile scpFile) {
        super();
        this.scpFile = scpFile;
    }


	@Override
	void execute(Session sshSession) throws SshException {
		InputStream in = null;
		OutputStream out = null;
		OutputStream outStream = null;
		ChannelExec channel = null;
		long fileSize = 0L;
		try {
			try {
				boolean foundFile = false;
				int counter = 0;
				String path = this.scpFile.getRemoteDirectory()
						+ this.scpFile.getRemoteFilename();
				while (!foundFile && counter < 2) {

					String cmd = SCP_DOWNLOAD_COMMAND + path;

					channel = setUpChannel(sshSession, cmd);
					// TODO.. this is where I think the refactoring should come
					// in..
					// Maybe ScpOutputStream should extend ScpDownload adn this
					// method Overriden accordingly?
					// Maybe the following should be encapsulated in the
					// ScpFile, instead of place here?
					if (scpFile.getLocalFile() == null) {
						outStream = new ByteArrayOutputStream();
					} else {
						outStream = new FileOutputStream(scpFile.getLocalFile());
					}

					in = channel.getInputStream();
					out = channel.getOutputStream();

					channel.connect();

					sendAck(out);

					try {
						fileSize = getFileSizeFromStream(in);
						foundFile = true;
					} catch (Exception e) {

						e.printStackTrace();
						path = this.scpFile.getRemoteDirectory()
								+ this.scpFile.getRemoteFilename()
										.toLowerCase();
						counter++;
						if (out != null)
							out.close();
						if (in != null)
							in.close();
						if (outStream != null)
							outStream.close();
						if (channel != null)
							channel.disconnect();
					}
				}
				skipFileName(in);

				sendAck(out);

				writePayloadToFile(in, out, outStream, fileSize);

			} finally {
				if (out != null)
					out.close();
				if (in != null)
					in.close();
				if (outStream != null)
					outStream.close();
				if (channel != null)
					channel.disconnect();
			}
		} catch (Exception e) {
			throw new SshException(e);
		}
	}

    private long getFileSizeFromStream(InputStream in)
            throws SshException, IOException {
        long filesize = 0L;

        if (checkAck(in) != 'C') {
            throw new SshException("Scp download from failed. Reason: Initializing "
                    + " size response returned a status that is not 'C'");
        }

        in.skip(5); //receive the expected '0644 '

        while (true) {
            int b = in.read();
            if (b < 0) {
                throw new SshException("Scp download from failed. Reason: reading "
                        + "file size returned a response of less than 0.");
            }
            if (b == ' ')
                break;
            filesize = filesize * 10L + (long) (b - '0');
        }
        return filesize;
    }

    private void skipFileName(InputStream in) throws IOException {
        for (int b = in.read(); b != '\n'; b = in.read()) {
            continue;
        }
    }

    private void writePayloadToFile(InputStream in, OutputStream out,
                                    OutputStream outputStream, long fileSize) throws SshException, IOException {
        byte[] inBuffer = new byte[1024];
        int readSize;
        while (true) {
            int bytesRead;

            if (inBuffer.length < fileSize)
                readSize = inBuffer.length;
            else
                readSize = (int) fileSize;

            bytesRead = in.read(inBuffer, 0, readSize);

            if (bytesRead < 0) {
                throw new SshException("Scp download from failed.  Reason: Unable to "
                        + "download payload of file.");
            }

            outputStream.write(inBuffer, 0, bytesRead);
            scpFile.setOutputStream(outputStream);
            fileSize -= bytesRead;

            if (fileSize == 0L) break;
        }
    }

    public void setScpFile(ScpFile scpFile) {
        if (scpFile == null) {
            throw new IllegalArgumentException("scpFile can't be null");
        }
        this.scpFile = scpFile;
    }
}