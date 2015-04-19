all: NBodies 

NBodies:
	javac -cp ".:src/json-20090211.jar:src/engine.io-client-0.4.1.jar:src/socket.io-client-0.4.2.jar:src/Java-WebSocket-1.3.0.jar" -g src/*.java


clean:
	rm -f src/*.class
