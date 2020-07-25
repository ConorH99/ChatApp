package com.chatApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatView extends JFrame {

	Client model;
	JPanel mainArea;
	JTextArea messageArea;
	JTextField outgoingMessage;
	JButton sendButton;
	JButton disconnectButton;

	public ChatView(Client model) {
		this.model = model;
	}

	public void drawGui() {
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
		addWindowListener(new WindowClose());
		getContentPane().add(mainArea, BorderLayout.CENTER);
		setSize(400, 400);
		setResizable(false);
		setVisible(true);
	}

	public void beginDisconnect() {
		model.clientWriter.println(model.CLIENT_DISCONNECT_MESSAGE);
		model.clientWriter.flush();
	}

	public class SendEvent implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			try {
				String message = outgoingMessage.getText();
				model.clientWriter.println(message);
				model.clientWriter.flush();
				messageArea.append("Me: " + message + "\n");
				outgoingMessage.setText("");
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}

	public class DisconnectEvent implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			beginDisconnect();
		}
	}

	public class WindowClose extends WindowAdapter {

		public void windowClosing(WindowEvent event) {
			System.out.println("dddd");
			beginDisconnect();
		}
	}


}