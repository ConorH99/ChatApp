package com.chatApp;

import java.net.*;
import java.io.*;

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

			while (true) {
				clientSocket = serverSocket.accept();
				clientIP = clientSocket.getRemoteSocketAddress().toString();
				InputStream serverInputStream = clientSocket.getInputStream();
				OutputStream serverOutputStream = clientSocket.getOutputStream();
				System.out.println("Connection from address " + clientIP);
				Thread clientThread = new ThreadedClient(clientSocket, serverInputStream, serverOutputStream);
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
		OutputStream writer;

		public ThreadedClient(Socket socket, InputStream input, OutputStream output) {
			this.socket = socket;
			this.reader = input;
			this.writer = output;
		}

		public void run() {
			try {
				Reader inputReader = new InputStreamReader(this.reader);
				BufferedReader bufferedReader = new BufferedReader(inputReader);
				PrintWriter outputWriter = new PrintWriter(this.writer);
				while (true) {
					outputWriter.println("Enter Message: ");
					outputWriter.flush();
					String inMessage = bufferedReader.readLine();
					System.out.println(inMessage);
					outputWriter.println(inMessage);
					outputWriter.flush();
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
	}

	public static void main(String[] args) {
		Server server = new Server();
		server.serverConnect();
	}
}