import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.socketio.client.IO;

public class GuiCommunication {
	
	public static final String URL = "http://localhost";
	public static final String GUI_UPDATE = "update";
	private static Socket _socket;
	
	
	static {
		InitSocket();
	}

	private static void InitSocket()
	{
		try {
			// attempt to create socket
			_socket = IO.socket("http://localhost");
			
			// chain new events here
			_socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
				
				@Override
				public void call(Object... arg0) {
					_socket.emit("client-connect", "I are client!");
				}
			});
			
		}
		catch(Exception e) { e.printStackTrace(); }
	}
	
	public static void Send(String event, Object obj)
	{
		_socket.emit(event, obj);
	}
}
