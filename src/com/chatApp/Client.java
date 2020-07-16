package com.chatApp;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Client {

	//Socket Variables
	final String CLIENT_DISCONNECT_MESSAGE = "FIN";
	final String SERVER_ACK_MESSAGE = "ACK";
	Scanner input = new Scanner(System.in);
	Socket clientSocket;
	PrintWriter clientWriter;
	BufferedReader clientReader;
	ClientReader clientReaderThread;

	//GUI Variables
	JFrame frame;
	JPanel mainArea;
	JTextArea messageArea;
	JTextField outgoingMessage;
	JButton sendButton;
	JButton disconnectButton;
	

	public static void main(String[] args) {
		Client client = new Client();
		client.clientConnect();
	}

	//Client Write Thread

	public void clientConnect() {

		System.out.println("Welcome from the Client!");
		String ip = "127.0.0.1";
		int port = 3000;
		drawGui();

		try {
			clientSocket = new Socket(ip, port);
			clientWriter = new PrintWriter(clientSocket.getOutputStream());
			clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			clientReaderThread = new ClientReader(clientSocket, clientReader);
			clientReaderThread.start();
			
		} catch(IOException except) {
			except.printStackTrace();
		}
	}

	public void drawGui() {
		frame = new JFrame();
		mainArea = new JPanel();
		messageArea = new JTextArea(15, 30);
		outgoingMessage = new JTextField(10);
		sendButton = new JButton("Send");
		sendButton.addActionListener(new SendEvent());
		disconnectButton = new JButton("Disconnect");
		disconnectButton.addActionListener(new DisconnectEvent());
		messageArea.setEditable(false);
		mainArea.add(messageArea);
		mainArea.add(outgoingMessage);
		mainArea.add(sendButton);
		mainArea.add(disconnectButton);
		frame.getContentPane().add(mainArea, BorderLayout.CENTER);
		frame.setSize(400, 400);
		frame.setResizable(false);
		frame.setVisible(true);
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

		public void disconnect() {
			try {
				this.reader.close();
				this.socket.close();
				messageArea.append("You are disconnected\n");
				System.out.println("Socket Closed..");
			} catch(IOException exception) {

			}
		}

		public void checkForRead() {
			try {
				while (true) {
					String inMessage = this.reader.readLine();
					if (inMessage.equals(SERVER_ACK_MESSAGE)) {
						disconnect();
						break;
					}
					messageArea.append("Client: " + inMessage + "\n");
				}
			} catch(IOException except) {
				except.printStackTrace();	
			}
		}
	}

	public class SendEvent implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			try {
				String message = outgoingMessage.getText();
				clientWriter.println(message);
				clientWriter.flush();
				messageArea.append("Me: " + message + "\n");
				outgoingMessage.setText("");
			} catch (Exception except) {

			}
		}
	}

	public class DisconnectEvent implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			clientWriter.println(CLIENT_DISCONNECT_MESSAGE);
			clientWriter.flush();
		}
	}
}
