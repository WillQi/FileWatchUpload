package io.github.willqi.filewatchupload;

import io.github.willqi.filewatchupload.config.ConfigRegistry;
import io.github.willqi.filewatchupload.config.data.Config;
import io.github.willqi.filewatchupload.connection.Connection;
import io.github.willqi.filewatchupload.connection.ConnectionResolver;
import io.github.willqi.filewatchupload.listener.VerboseListener;
import org.apache.commons.cli.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

public class FileWatchUpload {

    private static final HelpFormatter HELP_FORMATTER = new HelpFormatter();


    public static void main(String[] args) {
        Options cliOptions = new Options();
        cliOptions.addOption("f", "file", true, "Path to file to upload");
        cliOptions.addOption("c", "config", true, "configuration file name");
        cliOptions.addOption("r", "register", true, "Relative path to the configuration file you are registering");

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine;
        try {
            commandLine = parser.parse(cliOptions, args);
        } catch (ParseException exception) {
            HELP_FORMATTER.printHelp("filewatchupload", cliOptions);
            return;
        }

        if (commandLine.hasOption("r")) {
            // Register configuration file
            doRegister(commandLine.getOptionValue("r"));
        } else if (commandLine.hasOption("f") && commandLine.hasOption("c")) {
            // Watch a file
            doWatch(commandLine.getOptionValue("c"), commandLine.getOptionValue("f"));
        } else {
            // Invalid
            HELP_FORMATTER.printHelp("filewatchupload", cliOptions);
        }

    }

    /**
     * Register a configuration file
     * @param relativePath
     */
    private static void doRegister(String relativePath) {
        try {
            ConfigRegistry.register(Paths.get(System.getProperty("user.dir"), relativePath));
        } catch (FileNotFoundException exception) {
            System.out.println("Could not find configuration file to register.");
            return;
        } catch (IOException exception) {
            System.out.println("Failed to register configuration file.");
            exception.printStackTrace();
            return;
        }
        System.out.println("Registered!");
    }

    /**
     * Watch a file using a configuration id
     * @param configId
     * @param watchFileRelativePath
     */
    private static void doWatch(String configId, String watchFileRelativePath) {
        Config config;
        try {
            config = ConfigRegistry.get(configId);
        } catch (FileNotFoundException exception) {
            System.out.println("Could not find configuration file.");
            return;
        } catch (IOException exception) {
            System.out.println("Failed to parse configuration file.");
            exception.printStackTrace();
            return;
        }
        Connection connection = ConnectionResolver.resolve(config);

        WatchManager watchManager = new WatchManager(connection, new VerboseListener());

        Thread watchThread;
        try {
            watchThread = watchManager.watch(Paths.get(System.getProperty("user.dir"), watchFileRelativePath));
        } catch (FileNotFoundException exception) {
            System.out.println("Could not file to watch");
            return;
        } catch (IOException exception) {
            System.out.println("Failed to start watching file.");
            exception.printStackTrace();
            return;
        }
        System.out.println("Watching file!");
        try {
            watchThread.join();
        } catch (InterruptedException exception) {
            System.out.println("Stopped watching file.");
            exception.printStackTrace();
        }
    }

}