package io.github.willqi.filewatchupload;

import io.github.willqi.filewatchupload.config.SimpleSSHConfig;
import io.github.willqi.filewatchupload.connection.SimpleSSHConnection;
import org.apache.commons.cli.*;

import java.io.FileNotFoundException;
import java.nio.file.Paths;

public class FileWatchUpload {

    public static void main (String[] args) throws FileNotFoundException {

        final Options cliOptions = new Options();

        cliOptions.addOption("w", "watch", true, "Path to file/directory to watch");
        cliOptions.addOption("o", "outdir", true, "Output directory on remote server");
        cliOptions.addOption("c", "config", true, "Path to configuration file");

        final CommandLineParser parser = new DefaultParser();
        final CommandLine commandLine;

        try {
            commandLine = parser.parse(cliOptions, args);
        } catch (ParseException exception) {
            new HelpFormatter()
                    .printHelp("filewatchupload", cliOptions);
            return;
        }

        final String configPath;

        if (commandLine.hasOption("c")) {
            configPath = commandLine.getOptionValue("c");
        } else {
            configPath = Paths.get(System.getProperty("user.dir"), "filewatchupload.config").toString();
        }

        final SimpleSSHConfig config = new SimpleSSHConfig(configPath);

        if (commandLine.hasOption("w")) {
            config.setWatchPath(commandLine.getOptionValue("w"));
        }
        if (commandLine.hasOption("o")) {
            config.setOutputPath(commandLine.getOptionValue("o"));
        }

        final WatchManager watchManager = new WatchManager(
                config.getWatchPath(),
                config.getOutputPath(),
                new SimpleSSHConnection(config.getIP(), config.getPort(), config.getUsername(), config.getPassword())
        );

        System.out.println("Watching...");
        watchManager.watch();
        watchManager.join();

    }

}