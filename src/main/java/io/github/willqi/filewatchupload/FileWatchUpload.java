package io.github.willqi.filewatchupload;

import io.github.willqi.filewatchupload.config.SimpleSSHConfig;
import io.github.willqi.filewatchupload.connection.SimpleSSHConnection;
import io.github.willqi.filewatchupload.listener.VerboseListener;
import org.apache.commons.cli.*;

import java.io.FileNotFoundException;
import java.nio.file.Paths;

public class FileWatchUpload {

    public static void main (String[] args) throws FileNotFoundException {

        final Options cliOptions = new Options();

        cliOptions.addOption("w", "watch", true, "Path to file/directory to watch");
        cliOptions.addOption("o", "outdir", true, "Output directory on remote server");
        cliOptions.addOption("c", "config", true, "Path to configuration file");

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine;

        try {
            commandLine = parser.parse(cliOptions, args);
        } catch (ParseException exception) {
            new HelpFormatter()
                    .printHelp("filewatchupload", cliOptions);
            return;
        }

        String configPath;

        if (commandLine.hasOption("c")) {
            configPath = commandLine.getOptionValue("c");
        } else {
            configPath = Paths.get(System.getProperty("user.dir"), "filewatchupload.config").toString();
        }

        SimpleSSHConfig config = new SimpleSSHConfig(configPath);

        if (commandLine.hasOption("w")) {
            config.setWatchPath(commandLine.getOptionValue("w"));
        }
        if (commandLine.hasOption("o")) {
            config.setOutputPath(commandLine.getOptionValue("o"));
        }

        WatchManager watchManager = new WatchManager(
                new WatchManager.WatchPathConfig(config.getWatchPath(), config.getOutputPath()),
                new SimpleSSHConnection(config.getIP(), config.getPort(), config.getUsername(), config.getPassword()),
                new VerboseListener()
        );

        System.out.println("Watching...");
        Thread watchThread = watchManager.watch();

        try {
            watchThread.join();
        } catch (InterruptedException exception) {
            System.out.println("Watch thread interrupted. No longer watching.");
            exception.printStackTrace();
        }

    }

}