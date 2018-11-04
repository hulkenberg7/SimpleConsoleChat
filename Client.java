import java.net.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Client {
	private Socket client;
	private BufferedReader reader;
	private BufferedReader in;
	private BufferedWriter out;

	public static void main(String[] args) throws Exception {
		new Client().initialize();
	}

	private void initialize() {
		try {
			client = new Socket("localhost", 4040);
			reader = new BufferedReader(new InputStreamReader(System.in));
			
			out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			new Thread(new ServerListener()).start(); 

			System.out.println("Enter Your Name:");
			String message;
			String nickName = reader.readLine();
			LocalDateTime now;
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
			while(!(message = reader.readLine()).equals("Exit")) {
				now = LocalDateTime.now();
				out.write("(" + dtf.format(now) + ") " + nickName + ": " + message + "\n");
				out.flush();

			}

			out.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private class ServerListener implements Runnable {

		@Override
		public void run() {
			try {
				
				try {
					String message;

					while (!(message = in.readLine()).equals("Exit")) {
						System.out.println(message);
					}
				} catch (IOException e) {
					e.printStackTrace();
					in.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
}