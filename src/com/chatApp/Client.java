package com.chatApp;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Client {
	Socket clientSocket;
	Scanner input = new Scanner(System.in);
	OutputStream clientOutputStream;
	PrintWriter clientWriter;
	InputStream clientInputStream;
	InputStreamReader streamReader;
	BufferedReader reader;
	JFrame frame;
	JPanel mainArea;
	JTextArea messageArea;
	JTextField outgoingMessage;
	JButton button;

	public static void main(String[] args) {
		Client client = new Client();
		client.drawGui();
		client.clientConnectAndWrite();
	}

	public void drawGui() {
		frame = new JFrame();
		mainArea = new JPanel();
		messageArea = new JTextArea(15, 30);
		outgoingMessage = new JTextField(10);
		button = new JButton("Send");
		button.addActionListener(new SendEvent());
		messageArea.setEditable(false);
		mainArea.add(messageArea);
		mainArea.add(outgoingMessage);
		mainArea.add(button);
		frame.getContentPane().add(mainArea, BorderLayout.CENTER);
		frame.setSize(400, 400);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	public void clientConnectAndWrite() {

		System.out.println("Welcome from the Client!");
		String ip = "127.0.0.1";
		int port = 3000;

		try {
			clientSocket = new Socket(ip, port);
			clientOutputStream = clientSocket.getOutputStream();
			clientWriter = new PrintWriter(clientOutputStream);
			clientInputStream = clientSocket.getInputStream();
			streamReader = new InputStreamReader(clientInputStream);
			reader = new BufferedReader(streamReader);
			Thread clientReaderThread = new ClientReader(clientSocket, reader);
			clientReaderThread.start();
			
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
					messageArea.append(inMessage + "\n");
				}
			} catch(IOException except) {
				except.printStackTrace();				
			}
		}
	}

	public class SendEvent implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			try {
				clientWriter.println(outgoingMessage.getText());
				clientWriter.flush();
			} catch (Exception except) {

			}
		}
	}
}
