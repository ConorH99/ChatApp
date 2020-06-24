package com.chatApp;

import java.net.*;
import java.io.*;
import java.util.Scanner;

class Client {
	Socket clientSocket;
	Scanner input = new Scanner(System.in);

	public void clientConnectAndWrite() {

		System.out.println("Welcome from the Client!");
		String ip = "127.0.0.1";
		int port = 3000;

		try {
			clientSocket = new Socket(ip, port);
			OutputStream clientOutputStream = clientSocket.getOutputStream();
			PrintWriter clientWriter = new PrintWriter(clientOutputStream);
			InputStream clientInputStream = clientSocket.getInputStream();
			Thread clientReaderThread = new ClientReader(clientSocket, clientInputStream);
			clientReaderThread.start();
			while(true){
				String outMessage = input.nextLine();
				clientWriter.println(outMessage);
				clientWriter.flush();
			}

			// clientSocket.close();
			// clientOutputStream.close();
			// clientInputStream.close();
			// clientWriter.close();
			// clientReader.close();
			// bufferedReader.close();
			
		} catch(IOException except) {
			except.printStackTrace();
		}


	}

	public class ClientReader extends Thread {

		Socket socket;
		InputStream inputStream;

		public ClientReader(Socket socket, InputStream inputStream) {
			this.socket = socket;
			this.inputStream = inputStream;
		}

		public void run() {
			try {
				Reader clientReader = new InputStreamReader(this.inputStream);
				BufferedReader bufferedReader = new BufferedReader(clientReader);
				while (true) {
					String inMessage = bufferedReader.readLine();
					System.out.println(inMessage);
				}
			} catch(IOException except) {
				except.printStackTrace();				
			}
		}
	}

	public static void main(String[] args) {
		Client client = new Client();
		client.clientConnectAndWrite();
	}
}
