# SocketPlayer
A media player controlled by socket.

## Usage
- Connect to socket via any tools you like, example: `telnet [ip] 2323`.
- Send full file name to socket, example:
```````python
# https://pythontic.com/modules/socket/send
import socket

clientSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
clientSocket.connect(("192.168.110.202", 2323))

data = "/Pictures/WeChat/mmexport1631700069061.mp4"
clientSocket.send((data + "\r\n").encode())
```````

## Note
Auto start on Android 10 needs do extra work.  
See https://stackoverflow.com/questions/6391902/how-do-i-start-my-app-on-startup.
