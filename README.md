# FileWatchUpload
A utility java program (For Windows ATM) I built when I was getting frustrated having to manually upload my jar file to a remote test server over and over.

> **Note**: At the moment, FileWatchUpload will only authenticate with a username and password.

## Purpose
This program uploads a file of your choosing to a remote server whenever a change has occurred to that file.

## Usage

- Download the latest [release](https://github.com/WillQi/FileWatchUpload/releases) and add place it in a folder.
- In the same folder, create a file called `filewatchupload.bat` and set the contents to the script found further below.
- Create a file somewhere with the configuration structure you need. (See further down)
- Register it `filewatchupload --register pathToConfigFile.json`
- Now you can watch files and upload it using the configuration. `filewatchupload --config id --file ./fileToWatch` (the `id` can be found in the configuration JSON)
- Any changes that happen to your file/directory will be uploaded to your server automatically when made.

## `filewatchupload.bat` Script
```bash
@ECHO OFF
java -jar %~dp0/filewatchupload-1.0-SNAPSHOT.jar %*
```

## Configuration Types

### Simple SSH
```json
{
  "id": "example-id",
  "type": "simple_ssh",
  "ip": "0.0.0.0",
  "port": 22,
  "username": "username",
  "password": "password",
  "output": [
    "array of remote directories to upload file to"
  ]
}
```

## Future Plans
- Implement SSH connections with keys
- Implement connection that just moves files to another directory