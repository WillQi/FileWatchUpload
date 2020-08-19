package io.github.willqi;

import org.apache.commons.cli.*;

public class FileWatchUpload {

    public static void main (String[] args) {

        Options cliOptions = new Options();

        cliOptions.addRequiredOption("w", "watch", true, "Path to file/directory to watch");
        cliOptions.addRequiredOption("o", "outdir", true, "Output directory on remote server");
        cliOptions.addRequiredOption("i", "ip", true, "IP of target server to connect to");
        cliOptions.addRequiredOption("port", "port", true, "Port of target server to connect to");
        cliOptions.addRequiredOption("user", "username", true, "Username to use to login to remote server");
        cliOptions.addOption("pass", "password", true, "Password to use to login to remote server");
        cliOptions.addOption("pk", "privatekey", true, "Path to private key file");

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine;

        try {
            commandLine = parser.parse(cliOptions, args);
        } catch (ParseException exception) {
            new HelpFormatter()
                    .printHelp("filewatchupload", cliOptions);
            return;
        }

    }

}