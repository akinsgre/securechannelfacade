package org.rev6.scf;

import java.io.File;

public class ScpFile {
    private final File localFile;
    private final String remoteDirectory;
    private final String remoteFilename;

    public ScpFile(final File localFile, final String remotePath) {
        if (localFile == null || remotePath == null) {
            throw new IllegalArgumentException("File reference and path must " +
                    "be non-null");
        }

        this.localFile = localFile;

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

    public ScpFile(final File file) {
        this(file, file.getName());
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
        return this.localFile.length();
    }
}
