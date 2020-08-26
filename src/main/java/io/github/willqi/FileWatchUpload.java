package io.github.willqi;

import io.github.willqi.config.SimpleSSHConfig;
import io.github.willqi.connection.SimpleSSHConnection;
import org.apache.commons.cli.*;

import java.io.FileNotFoundException;
import java.nio.file.Paths;

public class FileWatchUpload {

    public static void main (String[] args) {

        Options cliOptions = new Options();

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

        WatchManager watchManager;
        try {
            watchManager = new WatchManager(
                    config.getWatchPath(),
                    config.getOutputPath(),
                    new SimpleSSHConnection(config.getIP(), config.getPort(), config.getUsername(), config.getPassword())
            );
        } catch (FileNotFoundException exception) {
            System.out.println("Watch file/directory could not be found");
            return;
        }

        System.out.println("Watching...");
        watchManager.watch();
        watchManager.join();

    }

}