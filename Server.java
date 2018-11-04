import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
	private ServerSocket server;
	private List<PrintWriter> clientsList = new ArrayList<>();

	public static void main(String[] args) throws Exception {
		new Server().initialize();
	}
	
	private void initialize() {
		System.out.print("Initializing server...");
		try {
			server = new ServerSocket(4040);

			while(true) {
				System.out.println("OK");
				Socket client = server.accept();
				PrintWriter writer = new PrintWriter(client.getOutputStream());
				clientsList.add(writer);
				ClientHandler handler = new ClientHandler(client);
				new Thread(handler).start();
				System.out.println(clientsList.size());
			}
		} catch(IOException e) {
			System.out.println("FAILED");
			e.printStackTrace();
		}
	}

	private class ClientHandler implements Runnable {
		Socket socket;
		BufferedReader in;
		String nickName;

		ClientHandler(Socket socket) {
			this.socket = socket;
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				String message;
				while(!(message = in.readLine()).equals("Exit")) {
					System.out.println(message);
					share(message);
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void share(String message) {
		for (PrintWriter w : clientsList) {
			try {
				w.println(message);
				w.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}

