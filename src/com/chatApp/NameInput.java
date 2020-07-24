package com.chatApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NameInput extends JFrame implements ActionListener {
 
	Client model;
	JPanel mainArea;
	JTextField nameField;
	JButton submitButton;

	public NameInput(Client clientModel) {
		this.model = clientModel;
	}

	public void drawNameGui() {
		mainArea = new JPanel();
		nameField = new JTextField(10);
		submitButton = new JButton("Submit");
		submitButton.addActionListener(this);
		mainArea.add(nameField);
		mainArea.add(submitButton);
		getContentPane().add(mainArea, BorderLayout.CENTER);
		setSize(400, 200);
		setResizable(false);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent event) {
		String name = nameField.getText();
		this.model.setName(name);
		dispose();
		this.model.drawGui();
		this.model.clientConnect();
	}
}