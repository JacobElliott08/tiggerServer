all: Server
	java -cp .:build:**/*.class serverConnection.ServerConnect
Server:	
	javac -d build -sourcepath src src/**/*.java
