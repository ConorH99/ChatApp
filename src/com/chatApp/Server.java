package com.chatApp;

import java.net.*;
import java.io.*;
import java.util.*;

public class Server {


	final String CLIENT_DISCONNECT_MESSAGE = "FIN";
	final String SERVER_ACK_MESSAGE = "ACK";
	int port = 3000;
	ServerSocket serverSocket;
	Socket clientSocket;
	BufferedReader serverReader;
	PrintWriter serverWriter;
	String clientIP;
	ThreadedClient clientThread;
	String clientName;

	public void serverConnect() {

		System.out.println("Starting Server...");

		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Listening on port " + port);
			ArrayList<ThreadedClient> clientList = new ArrayList<>();


			while (true) {
				clientSocket = serverSocket.accept();
				clientIP = clientSocket.getRemoteSocketAddress().toString();
				serverReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				serverWriter = new PrintWriter(clientSocket.getOutputStream());
				clientName = serverReader.readLine();
				System.out.println("Connection from address " + clientIP);
				System.out.println("User's name is " + clientName);
				clientThread = new ThreadedClient(clientName, clientSocket, serverReader, serverWriter, clientList);
				clientList.add(clientThread);
				clientThread.start();
			}

		} catch (IOException except) {
			except.printStackTrace();
		}
	}

	public class ThreadedClient extends Thread {

		Socket socket;
		BufferedReader reader;
		PrintWriter writer;
		ArrayList<ThreadedClient> clientList;
		String name;

		public ThreadedClient(String name, Socket socket, BufferedReader reader, PrintWriter writer, ArrayList<ThreadedClient> clientList) {
			this.name = name;
			this.socket = socket;
			this.reader = reader;
			this.writer = writer;
			this.clientList = clientList;
		}

		public void run() {
			checkForRead();
		}

		public void disconnect() {
			try {
				this.writer.println(SERVER_ACK_MESSAGE);
				this.writer.flush();
				this.clientList.remove(this);
				this.writer.close();
				this.socket.close();
				System.out.println("Disconnected");
			} catch (IOException exception) {

			}
		}

		public void checkForRead() {
			try {
				while (true) {
					String inMessage = this.reader.readLine();
					if (inMessage.equals(CLIENT_DISCONNECT_MESSAGE)) {
						disconnect();
						break;
					}
					for (ThreadedClient client : clientList) {
						if (client != this) {
							client.writer.println(this.getClientName() + ": " + inMessage);
							client.writer.flush();
						}
					}
				}
			} catch (IOException except) {
					except.printStackTrace();
				}
		}

		public String getClientName() {
			return this.name;
		}
	}

	public static void main(String[] args) {
		Server server = new Server();
		server.serverConnect();
	}
}