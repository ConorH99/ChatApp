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
			InputStreamReader streamReader = new InputStreamReader(clientInputStream);
			BufferedReader reader = new BufferedReader(streamReader);
			Thread clientReaderThread = new ClientReader(clientSocket, reader);
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
		BufferedReader reader;

		public ClientReader(Socket socket, BufferedReader reader) {
			this.socket = socket;
			this.reader = reader;
		}

		public void run() {
			try {
				while (true) {
					String inMessage = this.reader.readLine();
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
