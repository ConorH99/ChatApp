package com.chatApp;

import java.net.*;
import java.io.*;
import java.util.Scanner;

class Client {
	Socket clientSocket;
	Scanner input = new Scanner(System.in);

	public void clientConnect() {

		System.out.println("Welcome from the Client!");
		String ip = "127.0.0.1";
		int port = 3000;

		try {
			clientSocket = new Socket(ip, port);
			OutputStream clientOutputStream = clientSocket.getOutputStream();
			InputStream clientInputStream = clientSocket.getInputStream();
			PrintWriter clientWriter = new PrintWriter(clientOutputStream);
			Reader clientReader = new InputStreamReader(clientInputStream);
			BufferedReader bufferedReader = new BufferedReader(clientReader);
			while(true){
				System.out.println(bufferedReader.readLine());
				String outMessage = input.nextLine();
				clientWriter.println(outMessage);
				clientWriter.flush();
				String inMessage = bufferedReader.readLine();
				System.out.println(inMessage);
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
	public static void main(String[] args) {
		Client client = new Client();
		client.clientConnect();
	}
}
