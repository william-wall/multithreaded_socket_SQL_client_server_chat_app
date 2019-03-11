package server;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

// William Wall @ williamwall.ie


public class MultiThreadedServerA2 extends javax.swing.JFrame {

	// declare arraylist of output stream
	ArrayList clientOutputStreams;

	// declare arraylist of users of type string
	ArrayList<String> users;

	public class ClientHandler implements Runnable {

		// declare reader
		BufferedReader reader;

		// declare socket
		Socket sock;

		// declare print writer
		PrintWriter client;

		// declare inet address
		private InetAddress address;

		public ClientHandler(Socket clientSocket, PrintWriter user) {

			// client is = user in parameters
			client = user;

			try {
				// socket = client socket in
				sock = clientSocket;
				// address = get inet address
				address = sock.getInetAddress();
				// input stream reader instance
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				// buff reader instance
				reader = new BufferedReader(isReader);

			} catch (Exception ex) {
				// append error message if exception thrown
				textAreaChat.append("Unexpected error... \n");
			}

		}

		@Override
		public void run() {
			// declare strings and assign
			String message, connect = "Connect", disconnect = "Disconnect", chat = "Chat";
			// string array for data
			String[] data;

			try {

				// while message is not empty read
				while ((message = reader.readLine()) != null) {
					// new instance of sql driver
					Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
					// connection string from database with database credentials
					Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Assign2", "root", "");
					// create the statement based on the connection
					Statement smt = cn.createStatement();
					// sql query string
					String q = "Select * from myStudents where STUD_ID ='" + message + "'";
					// data array assigned to message split
					data = message.split(":");
					// result set is equal to the query results above
					ResultSet rs = smt.executeQuery(q);

					if (rs.next()) {
						do {
							// get the student name
							String studentName = rs.getString(3)+" "+rs.getString(4);
							// append the host name
							textAreaChat.append("Client's host name is " + address.getHostName() + '\n');
							// append the host address
							textAreaChat.append("Client's IP Address is " + address.getHostAddress() + '\n');
							// append the student name with a welcome
							textAreaChat.append(studentName + " is a registered user. \n");
							// append message received
							textAreaChat.append("Received: " + message + "\n");

							// split message into data array
							data = message.split(":");

							// append data
							for (String token : data) {
								textAreaChat.append(token + "\n");
							}

							// if data array index 2 = connect
							if (data[2].equals(connect)) {
								// tell everyone student name has connected
								notifyClient((studentName + ":" + data[1] + ":" + chat));
								// add user
								addUser(data[0]);
								// if data array index 2 = disconnect
							} else if (data[2].equals(disconnect)) {
								// tell everyone has disconnected
								notifyClient((data[0] + ":has disconnected." + ":" + chat));
								// remove suer
								removeUser(data[0]);
								// if data array index 2 = chat
							} else if (data[2].equals(chat)) {
								// tell everyone the message
								notifyClient(message);
							} else {
								// append no conditions were met
								textAreaChat.append("No Conditions were met. \n");
							}

						} while (rs.next());
					}
					else {
						// if the user is not registered in the database
						textAreaChat.append(data[0] + " is not a registered user \n");
						// tell client not a registered users
						notifyClient("Not a registered user!");
					}
					// close connection
					cn.close();
				}
			} catch (Exception ex) {
				// append lost a connection
				textAreaChat.append("Lost a connection. \n");
				// print what has happened
				ex.printStackTrace();
				// remove client
				clientOutputStreams.remove(client);
			}
		}
	}

	public MultiThreadedServerA2() {
		// initialize components
		initComponents();
	}
	
	// verify java generics
	@SuppressWarnings("unchecked")

	private void initComponents() {

		jScrollPane = new javax.swing.JScrollPane();
		textAreaChat = new javax.swing.JTextArea();
		buttonStart = new javax.swing.JButton();
		buttonEnd = new javax.swing.JButton();
		buttonUsers = new javax.swing.JButton();
		buttonClear = new javax.swing.JButton();
		labelName = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Chat - Server's frame");
		setName("server");
		setResizable(false);

		// set column
		textAreaChat.setColumns(20);
		// set row
		textAreaChat.setRows(5);
		// set chat to scroll
		jScrollPane.setViewportView(textAreaChat);

		// set text button start
		buttonStart.setText("START");
		// action listener for start
		buttonStart.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				startServerActionPerformed(evt);
			}
		});
		// set text button end
		buttonEnd.setText("END");
		// action listener for end
		buttonEnd.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				stopServerActionPerformed(evt);
			}
		});
		// set text button online users
		buttonUsers.setText("Online Users");
		// action listener for online users
		buttonUsers.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				showOnlineUsersActionPerformed(evt);
			}
		});
		// set text button clear
		buttonClear.setText("Clear");
		// action listener for clear
		buttonClear.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				clearTextActionPerformed(evt);
			}
		});
		// set label for server
		labelName.setText("SERVER @ William Wall");

		labelName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
										.addComponent(buttonEnd, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(buttonStart, javax.swing.GroupLayout.DEFAULT_SIZE, 75,
												Short.MAX_VALUE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 291,
										Short.MAX_VALUE)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
										.addComponent(buttonClear, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(buttonUsers, javax.swing.GroupLayout.DEFAULT_SIZE, 103,
												Short.MAX_VALUE))))
						.addContainerGap())
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup()
								.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(labelName).addGap(209, 209, 209)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
						.addGap(18, 18, 18)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(buttonStart).addComponent(buttonUsers))
						.addGap(18, 18, 18)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(buttonClear).addComponent(buttonEnd))
						.addGap(4, 4, 4).addComponent(labelName)));

		// sizes the frame so that all its contents are at or above their preferred
		// sizes
		pack();
	}

	private void stopServerActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			// sleep for 3 seconds before ending
			Thread.sleep(3000);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		// tell everyone server is stopping
		notifyClient("Server:is stopping and all users will be disconnected.\n:Chat");
		// append that the server is stopping
		textAreaChat.append("Server stopping... \n");
		// set the text area to blank
		textAreaChat.setText("");
	}

	private void startServerActionPerformed(java.awt.event.ActionEvent evt) {
		// instantiate new thread
		Thread starter = new Thread(new ServerStart());
		// start the server
		starter.start();
		// append that the server has started
		textAreaChat.append("Server started...\n");
	}

	private void showOnlineUsersActionPerformed(java.awt.event.ActionEvent evt) {
		// append message before display users
		textAreaChat.append("\n Online users : \n");
		// loop through users
		for (String current_user : users) {
			// append the users
			textAreaChat.append(current_user);
			// new line
			textAreaChat.append("\n");
		}

	}

	private void clearTextActionPerformed(java.awt.event.ActionEvent evt) {
		// set chat area to blank
		textAreaChat.setText("");
	}

	public static void main(String args[]) {

		java.awt.EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				// start new server instance
				new MultiThreadedServerA2().setVisible(true);
			}
		});
	}

	public class ServerStart implements Runnable {
		@Override
		public void run() {
			// assign client output stream to a new arraylist
			clientOutputStreams = new ArrayList();
			// assign users to new arraylist
			users = new ArrayList();

			try {
				// instantiate new server socket
				ServerSocket serverSock = new ServerSocket(8000);

				while (true) {
					// accept socket
					Socket clientSock = serverSock.accept();
					// instantiate print writer
					PrintWriter writer = new PrintWriter(clientSock.getOutputStream());
					// add writer to output stream
					clientOutputStreams.add(writer);
					// instantiate thread
					Thread listener = new Thread(new ClientHandler(clientSock, writer));
					// start listener
					listener.start();
					// append got a connection
					textAreaChat.append("Got a connection. \n");
				}
			} catch (Exception ex) {
				// append exception error message
				textAreaChat.append("Error making a connection. \n");
			}
		}
	}

	public void addUser(String data) {
		// declare strings and assign
		String message, add = ": :Connect", done = "Server: :Done", name = data;
		// before add append
		textAreaChat.append("Before " + name + " added. \n");
		// add user
		users.add(name);
		// append after added
		textAreaChat.append("After " + name + " added. \n");
		// string array
		String[] tempList = new String[(users.size())];
		// add array to users
		users.toArray(tempList);
		// loop through array
		for (String token : tempList) {
			message = (token + add);
			// tell everyone that a user has been added
			notifyClient(message);
		}
		// tell everyone it is done
		notifyClient(done);
	}

	public void removeUser(String data) {
		// declare strings and assign
		String message, add = ": :Connect", done = "Server: :Done", name = data;
		// remove user
		users.remove(name);
		// string array
		String[] tempList = new String[(users.size())];
		// add array to users
		users.toArray(tempList);
		// loop through the array
		for (String token : tempList) {
			message = (token + add);
			// tell everyone user has been removed
			notifyClient(message);
		}
		// tell everyone it is done
		notifyClient(done);
	}

	public void notifyClient(String message) {
		// instantiate an iterator
		Iterator it = clientOutputStreams.iterator();

		while (it.hasNext()) {
			try {
				// instantiate and cast print writer
				PrintWriter writer = (PrintWriter) it.next();
				// print writer message
				writer.println(message);
				// append that sending message
				textAreaChat.append("Sending: " + message + "\n");
				// flush stream
				writer.flush();
				// sets the position of the text insertion caret for this text component
				textAreaChat.setCaretPosition(textAreaChat.getDocument().getLength());
			} catch (Exception ex) {
				// append error if exception is thrown
				textAreaChat.append("Error telling everyone. \n");
			}
		}
	}

	private javax.swing.JButton buttonClear;
	private javax.swing.JButton buttonEnd;
	private javax.swing.JButton buttonStart;
	private javax.swing.JButton buttonUsers;
	private javax.swing.JScrollPane jScrollPane;
	private javax.swing.JLabel labelName;
	private javax.swing.JTextArea textAreaChat;

}
