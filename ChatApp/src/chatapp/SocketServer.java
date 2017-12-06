package chatapp;

import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hydrolyze
 */
public class ServerThread extends Thread{
    public SocketServer server = null;
    public Socket socket = null;
    private int ID = -1;
    public ObjectInputStream streamIn = null;
    public ObjectOutputStream streamOut = null;
    public ServerFrame ui;    
    
    public ServerThread(SocketServer _server, Socket _socket) {
        super();
        server = _server;
        socket = _socket;
        ID = _socket.getPort();
        ui = _server.ui;
    }
    
    public void send(Message msg) {
        try {
            streamOut.writeObject(msg);
        
            streamOut.flush();
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the ID
     */
    public int getID() {
        return ID;
    }
    
    @SuppressWarnings("deprecation")
    public void run(){
        ui.jTextAreal.append("\nServer Thread " + ID + "  running.");
        while(true){
            try {
                Message msg = (Message) streamIn.readObject();
                server.handle(ID, msg);
            } catch (IOException ioe) {
                System.out.println(ID + " ERROR reading: " + ioe.getMessage());
                server.remove(ID);
                stop();
            }
        }
    }
    
    public void open() throws IOException {
        streamOut = new ObjectOutputStream(socket.getOutputStream());
        streamOut.flush();
        streamIn = new ObjectInputStream(socket.getInputStream());
    }
    
    public void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
        if (streamIn != null) {
            streamIn.close();
        }
        if (streamOut != null) {
            streamOut.close();
        }
    }
}

public class SocketServer implements Runnable{
    public ServerThread clients[];
    public ServerSocket server = null;
    public Thread thread = null;
    public int clientCount = 0, port = 8080;
    public ServerFrame ui;
    public Database db;
    
    public SocketServer(ServerFrame frame){
        clients = new ServerThread[50];
        ui = frame;
        db = new Database(ui.filePath);

        try {
            server = new ServerSocket(port);
            port = server.getLocalPort();
            ui.jTextArea1.append("Server startet. IP : " + InetAddress.getLocalHost() + ", Port : " + server.getLocalPort());
            start();
        } catch (IOException ioe) {
            ui.jTextArea1.append("Can not bind to port : " + port + "\nRetrying");
            ui.RetryStart(0);
        }
    }
    
    public SocketServer(ServerFrame frame, int Port){
        clients = new ServerThread[50];
        ui = frame;
        port = Port;
        db = new Database(ui.filePath);

        try {
            server = new ServerSocket(port);
            port = server.getLocalPort();
            ui.jTextArea1.append("Server startet. IP : " + InetAddress.getLocalHost() + ", Port : " + server.getLocalPort());
            start();
        } catch (IOException ioe) {
            ui.jTextArea1.append("Can not bind to port : " + port + " : " + ioe.getMessage());
        }
    }
    
    public void run() {
        while(thread != null){
            try {
                ui.jTextArea.append("\nWaiting for a client ...");
                addTread(server.accept());
            } catch (IOException ioe) {
                ui.jTextArea1.append("\nServer accept error: \n");
                ui.RetryStart(0);
            }
        }
    }
    
    public void start(){
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }
    
    @SuppressWarnings("deprecation")
    public void stop(){
        if (thread != null) {
            thread.stop();
            thread = null;
        }
    }
    
    private int findClient(int ID){
        for (int i = 0; i < clientCount; i++) {
            if (clients[i].getID() == ID) {
                return i;
            }
        }
        return -1;
    }
    
    
}