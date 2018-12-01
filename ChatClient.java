package assignment7;

import java.io.*;
import java.net.*;
import javax.swing.*;

import javafx.application.Application;
import javafx.scene.control.TextField;

import java.awt.*;
import java.awt.event.*;

public class ChatClient {
	private JTextArea incoming;
	private JTextField outgoing;
	private JTextField newserver;
	private BufferedReader reader;
	private PrintWriter writer;
	public int serverport = 0;
	

	public void run() throws Exception {
		serverport = Login.serverport;
		initView();
		setUpNetworking(serverport);
	}

	private void initView() {
		JFrame frame = new JFrame(Login.user);
		JPanel mainPanel = new JPanel();
		incoming = new JTextArea(15, 50);
		incoming.setLineWrap(true);
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);
		JScrollPane qScroller = new JScrollPane(incoming);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		outgoing = new JTextField(20);
		newserver = new JTextField(20);
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new SendButtonListener());
		mainPanel.add(qScroller);
		
		JButton serverButton = new JButton("Change Server");
		sendButton.addActionListener(new ServerButtonListener());
		
		
		mainPanel.add(outgoing);
		mainPanel.add(serverButton);
		mainPanel.add(sendButton);
		
		mainPanel.add(newserver);
		
		mainPanel.add(serverButton);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.setSize(650, 500);
		frame.setVisible(true);

	}

	private void setUpNetworking(int i) throws Exception {
		@SuppressWarnings("resource")
		Socket sock = new Socket("127.0.0.1", i);
		InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
		reader = new BufferedReader(streamReader);
		writer = new PrintWriter(sock.getOutputStream());
		System.out.println("networking established");
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();
	}
	
	
	

	class SendButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			writer.println(Login.user + ": " + outgoing.getText());
			writer.flush();
			outgoing.setText("");
			outgoing.requestFocus();
		}
	}
	
	class ServerButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			//writer.println(Login.user + " is leaving the chat");
			try {
				setUpNetworking(Integer.parseInt(newserver.getText()));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			Application.launch(Login.class);
			new ChatClient().run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class IncomingReader implements Runnable {
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					
						incoming.append(message + "\n");
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
