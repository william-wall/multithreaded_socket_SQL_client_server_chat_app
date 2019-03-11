package client;

import java.net.*;
import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;

// William Wall @ williamwall.ie

public class ClientA2 extends javax.swing.JFrame {

	// declare strings
	String username, settingName, address = "localhost";

	// Instantiate arraylist of users type strings
	ArrayList<String> users = new ArrayList();

	// assign port
	int port = 8000;

	// is connected check
	Boolean isConnected = false;

	// socket var
	Socket sock;

	// reader
	BufferedReader reader;

	// writer
	PrintWriter writer;

	public void ListenThread() {

		// instantiate thread class
		Thread IncomingReader = new Thread(new IncomingReader());
		// start
		IncomingReader.start();

	}

	public void userAdd(String data) {
		// add user
		users.add(data);

	}

	public void userRemove(String data) {

		// remove user
		textAreaChat.append(data + " is now offline.\n");

	}

	public void writeUsers() {

		// array for users
		String[] tempList = new String[(users.size())];

		// add array
		users.toArray(tempList);

	}

	public void sendDisconnect() {

		// string for user disconnect
		String bye = (username + ": :Disconnect");

		try {

			// print string
			writer.println(bye);

			// flush the stream
			writer.flush();

		} catch (Exception e) {
			// print if exception is caught
			textAreaChat.append("Could not send Disconnect message.\n");

		}
	}

	public void NewClientInstance() {
		// new client window
		new ClientA2().setVisible(true);

	}

	public void Disconnect() {

		try {
			// append disconnect message
			textAreaChat.append("Disconnected.\n");
			textAreaChat.setText("");
			// close socket
			sock.close();

		} catch (Exception ex) {
			// print if exception is caught
			textAreaChat.append("Failed to disconnect. \n");
		}

		// change connected state to false
		isConnected = false;
		// set text field editable again for a new user to enter student no
		textfieldUsername.setEditable(true);

	}

	public ClientA2() {
		// initiate components of client
		initComponents();
	}

	public class IncomingReader implements Runnable {
		@Override
		public void run() {

			// string array for data
			String[] data;

			// declare strings and assign
			String stream, done = "Done", connect = "Connect", disconnect = "Disconnect", chat = "Chat";

			try {
				// while stream is not empty read
				while ((stream = reader.readLine()) != null) {

					// conditional to check if user is registered based on the stream coming in from
					// the server
					if (stream.contains("Not a registered user!") && textAreaChat.getText().isEmpty()) {

						// get unregistered student number
						String unregisterdUser = textfieldUsername.getText();

						// append the client confirming the user is unregistered
						textAreaChat.append(unregisterdUser + ": is not a registered user\n");

						// display a popup to notify user that they are not registered
						JOptionPane.showMessageDialog(null,
								"You are not a registered user, please open another client!");

						// end that instance of client gui as they are not registered
						sock.close();
						dispose();
					}	else {
						// split the data stream
						data = stream.split(":");
						// if index of data split 2 is = to chat
						if (data[2].equals(chat)) {
							// username has connected
							textAreaChat.append(data[0] + ": " + data[1] + "\n");
							// sets the username
							settingName = data[0];
							// sets the position of the text insertion caret for this text component
							textAreaChat.setCaretPosition(textAreaChat.getDocument().getLength());
							// if index of data split 2 is = to connect
						} else if (data[2].equals(connect)) {
							// run remove all
							textAreaChat.removeAll();
							// add user
							userAdd(data[0]);
							// if index of data split 2 is = to disconnect
						} else if (data[2].equals(disconnect)) {
							// remove the user
							userRemove(data[0]);
							// if index of data split 2 is = to done
						} else if (data[2].equals(done)) {
							// write the users
							writeUsers();
							// clear
							users.clear();
						}
					}
				}
			} catch (Exception ex) {

			}
		}
	}

	// verify java generics
	@SuppressWarnings("unchecked")
	private void initComponents() {

		// component instances
		labelAddress = new javax.swing.JLabel();
		textfieldAddress = new javax.swing.JTextField();
		labelPort = new javax.swing.JLabel();
		textfieldPort = new javax.swing.JTextField();
		labelUsername = new javax.swing.JLabel();
		textfieldUsername = new javax.swing.JTextField();
		buttonConnect = new javax.swing.JButton();
		buttonDisconnect = new javax.swing.JButton();
		buttonNewClient = new javax.swing.JButton();
		jScrollPane = new javax.swing.JScrollPane();
		textAreaChat = new javax.swing.JTextArea();
		textFieldChat = new javax.swing.JTextField();
		buttonSendMessage = new javax.swing.JButton();
		labelName = new javax.swing.JLabel();

		// dispose on close is most efficient, using EXIT_ON_CLOSE is buggy and closes
		// all windows
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Chat - Client's frame");
		setName("client");
		setResizable(false);

		// set label text
		labelAddress.setText("Address : ");

		// set field address
		textfieldAddress.setText("localhost");

		// disable text field for address
		textfieldAddress.disable();

		// set label for port
		labelPort.setText("Port :");

		// set text for port
		textfieldPort.setText("8000");

		// disable port field
		textfieldPort.disable();

		// set text of label
		labelUsername.setText("Student No :");

		// set text for button connect
		buttonConnect.setText("Connect");

		// action listener for connect
		buttonConnect.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(java.awt.event.ActionEvent evt) {

				buttonConnectActionPerformed(evt);
				
			}
		});

		// set text for disconnect button
		buttonDisconnect.setText("Disconnect");

		// action listener for disconnect
		buttonDisconnect.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(java.awt.event.ActionEvent evt) {

				buttonDisconnectActionPerformed(evt);

			}
		});

		// set text for client button
		buttonNewClient.setText("New Client");

		buttonNewClient.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(java.awt.event.ActionEvent evt) {

				buttonNewClientActionPerformed(evt);
			}
		});

		// set text area columns
		textAreaChat.setColumns(20);

		// set text area rows
		textAreaChat.setRows(5);

		// text area to scrollpane
		jScrollPane.setViewportView(textAreaChat);

		// set button text for send
		buttonSendMessage.setText("SEND");

		// action listener for send button
		buttonSendMessage.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(java.awt.event.ActionEvent evt) {

				buttonSendMessageActionPerformed(evt);
			}
		});

		// set label of client gui
		labelName.setText("CLIENT @ William Wall");

		// set border of label name
		labelName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

		// group layout instantiation
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());

		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addComponent(textFieldChat, javax.swing.GroupLayout.PREFERRED_SIZE, 352,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
										buttonSendMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE))
						.addComponent(jScrollPane)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
										.addComponent(labelUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 62,
												Short.MAX_VALUE)
										.addComponent(labelAddress, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addGap(18, 18, 18)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
										.addComponent(textfieldAddress, javax.swing.GroupLayout.DEFAULT_SIZE, 89,
												Short.MAX_VALUE)
										.addComponent(textfieldUsername))
								.addGap(18, 18, 18)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
										.addComponent(labelPort, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
										.addComponent(textfieldPort, javax.swing.GroupLayout.DEFAULT_SIZE, 50,
												Short.MAX_VALUE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(layout.createSequentialGroup().addComponent(buttonConnect)
												.addGap(2, 2, 2).addComponent(buttonDisconnect)
												.addGap(0, 0, Short.MAX_VALUE))
										.addComponent(buttonNewClient, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
				.addContainerGap())
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup()
								.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(labelName).addGap(201, 201, 201)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(labelAddress)
								.addComponent(textfieldAddress, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(labelPort)
								.addComponent(textfieldPort, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(buttonNewClient))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(textfieldUsername)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(labelUsername).addComponent(buttonConnect)
										.addComponent(buttonDisconnect)))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 310,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(textFieldChat).addComponent(buttonSendMessage,
										javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(labelName)));

		// sizes the frame so that all its contents are at or above their preferred
		// sizes
		pack();
	}
	
	private void buttonConnectActionPerformed(java.awt.event.ActionEvent evt) {

		// if not connected
		if (isConnected == false) {

			// assign username to correctly authenticated user
			username = textfieldUsername.getText();

			// if successful disable text field
			textfieldUsername.setEditable(false);

			try {
				// new socket based on port and address
				sock = new Socket(address, port);

				// stream reader instantiation
				InputStreamReader streamreader = new InputStreamReader(sock.getInputStream());

				// assign reader to stream reader
				reader = new BufferedReader(streamreader);

				// assign writer to socket output stream
				writer = new PrintWriter(sock.getOutputStream());

				// print username has connected
				writer.println(username + ":has connected.:Connect");

				// flush the stream
				writer.flush();

				// render instance as connected setting boolean to true
				isConnected = true;

			} catch (Exception ex) {

				// append exception to cannot count
				textAreaChat.append("Cannot Connect! Try Again. \n");

				// set editable to true
				textfieldUsername.setEditable(true);
			}

			// listen thread call
			ListenThread();
		} else if (isConnected == true) {
			// if user is connect display message
			textAreaChat.append("You are already connected. \n");
		}

	}

	private void buttonDisconnectActionPerformed(java.awt.event.ActionEvent evt) {
		// send disconnect
		sendDisconnect();
		// disconnect user
		Disconnect();

	}

	private void buttonNewClientActionPerformed(java.awt.event.ActionEvent evt) {
		// new client
		NewClientInstance();
	}

	private void buttonSendMessageActionPerformed(java.awt.event.ActionEvent evt) {
		// declare empty string
		String nothing = "";
		// if text field is empty
		if ((textFieldChat.getText()).equals(nothing)) {
			// set text to nothing
			textFieldChat.setText("");
			// requests that the component gets the input focus
			textFieldChat.requestFocus();

		} else {
			try {
				// send text based on username
				writer.println(username + ":" + textFieldChat.getText() + ":" + "Chat");
				// flush the stream
				writer.flush();

			} catch (Exception ex) {
				// print message not sent on exception
				textAreaChat.append("Message was not sent. \n");
				JOptionPane.showMessageDialog(null, "You are not logged in, you cannot sent a message!");
				textAreaChat.setText("");

			}
			// set text field to black
			textFieldChat.setText("");
			// requests that the component gets the input focus
			textFieldChat.requestFocus();
		}

		// set text field to black
		textFieldChat.setText("");
		// requests that the component gets the input focus
		textFieldChat.requestFocus();
	}

	public static void main(String args[]) {

		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {

				new ClientA2().setVisible(true);

			}
		});
	}

	private javax.swing.JButton buttonNewClient;
	private javax.swing.JButton buttonConnect;
	private javax.swing.JButton buttonDisconnect;
	private javax.swing.JButton buttonSendMessage;
	private javax.swing.JScrollPane jScrollPane;
	private javax.swing.JLabel labelAddress;
	private javax.swing.JLabel labelName;
	private javax.swing.JLabel labelPort;
	private javax.swing.JLabel labelUsername;
	private javax.swing.JTextArea textAreaChat;
	private javax.swing.JTextField textfieldAddress;
	private javax.swing.JTextField textFieldChat;
	private javax.swing.JTextField textfieldPort;
	private javax.swing.JTextField textfieldUsername;
}
