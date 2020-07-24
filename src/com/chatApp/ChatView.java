package com.chatApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatView {

	Client model;
	JFrame frame;
	JPanel mainArea;
	JTextArea messageArea;
	JTextField outgoingMessage;
	JButton sendButton;
	JButton disconnectButton;

	public ChatView(Client model) {
		this.model = model;
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
			model.clientWriter.println(model.CLIENT_DISCONNECT_MESSAGE);
			model.clientWriter.flush();
		}
	}


}