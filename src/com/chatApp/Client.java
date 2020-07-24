package com.chatApp;

import java.net.*;
import java.io.*;
import java.util.Scanner;

class Client {

	final String CLIENT_DISCONNECT_MESSAGE = "FIN";
	final String SERVER_ACK_MESSAGE = "ACK";
	ChatView view;
	Scanner input = new Scanner(System.in);
	String name;
	Socket clientSocket;
	PrintWriter clientWriter;
	BufferedReader clientReader;
	ClientReader clientReaderThread;
	String clientName;

	public static void main(String[] args) {
		Client client = new Client();
		client.drawGui();
		client.clientConnect();
	}

	public Client() {
		this.name = getNameInput();
		this.view = new ChatView(this);
	}

	public void drawGui() {
		this.view.drawGui();
	}

	public String getNameInput() {
		System.out.println("Please Enter Your Name: ");
		String name = input.nextLine();
		return name;
	}

	public void clientConnect() {

		System.out.println("Welcome from the Client!");
		String ip = "127.0.0.1";
		int port = 3000;

		try {
			clientSocket = new Socket(ip, port);
			clientWriter = new PrintWriter(clientSocket.getOutputStream());
			clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			clientWriter.println(this.name);
			clientWriter.flush();
			clientReaderThread = new ClientReader(clientSocket, clientReader);
			clientReaderThread.start();
			
		} catch(IOException except) {
			except.printStackTrace();
		}
	}

	//CLient Read thread

	public class ClientReader extends Thread {

		Socket socket;
		BufferedReader reader;
		boolean connected;

		public ClientReader(Socket socket, BufferedReader reader) {
			this.socket = socket;
			this.reader = reader;
		}

		public void run() {
			checkForRead();
		}

		public void checkForRead() {
			try {
				while (true) {
					String inMessage = this.reader.readLine();
					if (inMessage.equals(SERVER_ACK_MESSAGE)) {
						disconnect();
						break;
					}
					view.messageArea.append(inMessage + "\n");
				}
			} catch(IOException exception) {
				exception.printStackTrace();	
			}
		}

		public void disconnect() {
			try {
				this.reader.close();
				this.socket.close();
				view.frame.dispose();
				System.out.println("Socket Closed..");
			} catch(IOException exception) {
				exception.printStackTrace();
			}
		}
	}
}
