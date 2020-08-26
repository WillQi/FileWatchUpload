# FileWatchUpload
A utility java program I built when I was getting frustrated having to manually upload my jar file to a remote test server over and over.

> **Note**: At the moment, FileWatchUpload will only authenticate with a username and password.

## Purpose
This program uploads a file/directory of your choosing to a remote server whenever a change has occurred to the file/directory.

## Usage

- Download the latest [release](https://github.com/WillQi/FileWatchUpload/releases) gand copy the jar to any folder of your choosing.
- Run it once.
- A configuration file will be created in the same directory. Edit it to your server and file information.
- Run the jar again
- Any changes that happen to your file/directory will be uploaded to your server automatically when made.

## Future Plans
- Implement SSH connections with keys
- Implement connection that just moves files to another directory
- Add test cases