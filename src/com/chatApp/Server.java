package com.chatApp;

import java.net.*;
import java.io.*;
import java.util.*;

public class Server {

	int port = 3000;
	ServerSocket serverSocket;
	Socket clientSocket;
	String clientIP;

	public void run() {

	}

	public void serverConnect() {

		System.out.println("Starting Server...");

		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Listening on port " + port);
			ArrayList<ThreadedClient> clientList = new ArrayList<>();


			while (true) {
				clientSocket = serverSocket.accept();
				clientIP = clientSocket.getRemoteSocketAddress().toString();
				InputStream serverInputStream = clientSocket.getInputStream();
				InputStreamReader in = new InputStreamReader(serverInputStream);
				BufferedReader bufferedReader = new BufferedReader(in);
				OutputStream serverOutputStream = clientSocket.getOutputStream();
				PrintWriter writer = new PrintWriter(serverOutputStream);
				System.out.println("Connection from address " + clientIP);
				ThreadedClient clientThread = new ThreadedClient(clientSocket, bufferedReader, writer, clientList);
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

		public ThreadedClient(Socket socket, BufferedReader reader, PrintWriter writer, ArrayList<ThreadedClient> clientList) {
			this.socket = socket;
			this.reader = reader;
			this.writer = writer;
			this.clientList = clientList;
		}

		public void run() {
			try {
				while (true) {
					String inMessage = this.reader.readLine();
					if (inMessage.equals("Quit")) {
						break;
					}

					System.out.println(inMessage);
					for (ThreadedClient client : clientList) {
						if (client != this) {
							client.writer.println(inMessage);
							client.writer.flush();
						}
					}
				}

			} catch (IOException except) {
					except.printStackTrace();
				}
		}
		public PrintWriter getWriter() {
			return this.writer;
		}
	}

	public static void main(String[] args) {
		Server server = new Server();
		server.serverConnect();
	}
}