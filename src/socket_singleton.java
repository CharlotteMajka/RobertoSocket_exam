import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.AutoCloseable;

public class socket_singleton implements AutoCloseable {
	
	
	private static socket_singleton socket_instance = null;
	
	public ServerSocket serverSocket = null; 
	public Socket socket = null; 
	public DataInputStream dataIn = null; 
	public DataOutputStream dataOut = null;
	
	
	private socket_singleton() throws Exception {
	
		serverSocket = new ServerSocket(5000);
		socket = serverSocket.accept();
		dataIn = new DataInputStream(socket.getInputStream());	
		dataOut = new DataOutputStream(socket.getOutputStream());
		

	}
	
	public static socket_singleton getSocketInstance() throws Exception {
		if( socket_instance == null) 
		socket_instance = new socket_singleton();
		
	 return socket_instance;
	}

	
	@Override
	public void close() throws Exception {
		serverSocket.close();
		
	}

}
