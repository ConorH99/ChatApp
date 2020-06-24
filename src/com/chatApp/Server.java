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
				OutputStream serverOutputStream = clientSocket.getOutputStream();
				System.out.println("Connection from address " + clientIP);
				ThreadedClient clientThread = new ThreadedClient(clientSocket, serverInputStream, serverOutputStream, clientList);
				clientList.add(clientThread);
				clientThread.start();
			}

			// serverSocket.close();
			// serverInputStream.close();
			// serverOutputStream.close();

		} catch (IOException except) {
			except.printStackTrace();
		}
	}

	public class ThreadedClient extends Thread {

		Socket socket;
		InputStream reader;
		OutputStream outputStream;
		PrintWriter writer;
		ArrayList<ThreadedClient> clientList;

		public ThreadedClient(Socket socket, InputStream input, OutputStream outputStream, ArrayList<ThreadedClient> clientList) {
			this.socket = socket;
			this.reader = input;
			this.outputStream = outputStream;
			this.writer = new PrintWriter(this.outputStream);
			this.clientList = clientList;
		}

		public void run() {
			try {
				Reader inputReader = new InputStreamReader(this.reader);
				BufferedReader bufferedReader = new BufferedReader(inputReader);
				while (true) {
					this.getWriter().println("Enter Message: ");
					this.getWriter().flush();
					String inMessage = bufferedReader.readLine();
					for (ThreadedClient client : clientList) {
						if (client != this) {
							client.getWriter().println(inMessage);
							client.getWriter().flush();
						}
					}
				} 

				// this.socket.close();
				// this.reader.close();
				// this.writer.close();
				// inputReader.close();
				// bufferedReader.close();
				// outputWriter.close();

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