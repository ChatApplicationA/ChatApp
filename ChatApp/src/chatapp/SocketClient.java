/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp;

import java.io.*;
import java.net.*;
import java.util.Date;
import ui.ChatFrame;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Agit
 */
public class SocketClient implements Runnable {

	public int port;
	public String serverAddr;
	public Socket socket;
	public ChatFrame ui;
	public ObjectInputStream In;
	public ObjectOutputStream Out;
	public History hist;

	public SocketClient(ChatFrame frame) throws IOException {
		ui = frame;
		this.serverAddr = ui.serverAddr;
		this.port = ui.port;
		socket = new Socket(InetAddress.getByName(serverAddr), port);
		Out = new ObjectOutputStream(socket.getOutputStream());
		Out.flush();
		In = new ObjectInputStream(socket.getInputStream());
		hist = ui.hist;
	}

	@Override
	public void run() {
		boolean keepRunning = true;
		while (keepRunning) {
			try {
				Message msg = (Message) In.readObject();
				System.out.println("Incoming : " + msg.toString());

				if (msg.type.equals("message")) {
					if (msg.recipient.equals(ui.username)) {
						ui.jTextArea1.append("[" + msg.sender + " > Me ] : " + msg.content + "\n");
					} else {
						ui.jTextArea1.append("[" + msg.sender + " > " + msg.recipient + "] : " + msg.content + "\n");
					}
					if (!msg.content.equals(".bye") && !msg.sender.equals(ui.username)) {
						String msgTime = (new Date()).toString();

						try {
							hist.addMessage(msg, msgTime);
							DefaultTableModel table = (DefaultTableModel) ui.historyFrame.jTable1.getModel();
							table.addRow(new Object[]{msg.sneder, msg.content, "Me", msgTime});
						} catch (Exception ex) {
						}
					}
				} else if (msg.type.equals("login")) {
					if (msg.content.equals("TRUE")) {
						ui.jButton2.setEnabled(false);
						ui.jButton3.setEnabled(false);
						ui.jButton4.setEnabled(true);
						ui.jButton5.setEnabled(true);
						ui.jTextArea1.append("[SERVER > Me ] : Login Successful\n");
						ui.jTextField3.setEnabled(false);
						ui.jPasswordField1.setEnabled(false);
					} else {
						ui.jTextArea1.append("[SERVER > Me ] : Login Failed \n");
					}
				} else if (msg.type.equals("test")) {
					ui.jButton1.setEnabled(false);
					ui.jButton2.setEnabled(true);
					ui.jButton3.setEnabled(true);
					ui.jTextField3.setEnabled(true);
					ui.jPasswordField1.setEnabled(true);
					ui.jTextField1.setEditable(false);
					ui.jTextField2.setEditable(false);
					ui.jButton7.setEnabled(true);
				} else if (msg.type.equals("newuser")) {
					if (!msg.content.equals(ui.username)) {
						boolean exist = false;
						for (int i = 0; i < ui.model.getSize(); i++) {
							if (ui.model.getElementAt(i).equals(msg.content)) {
								exists = true;
								break;
							}
						}
						if (!exists) {
							ui.model.addElement(msg.content);
						}
					} else if (msg.type.equals("signup")) {
						if (msg.content.equals("TRUE")) {
						}
					}
				}
			}
		}
	}
