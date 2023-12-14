package sockets;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	private String host;
	private int port;
	private String nickname;

	public static void main(String[] args) throws UnknownHostException, IOException {
		new Client("127.0.0.1", 12345).run();
	}

	public Client(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void run() throws UnknownHostException, IOException {
		// connect client to server
		Socket client = new Socket(host, port);
		System.out.println("Client successfully connected to server!");

		// create a new thread for server messages handling
		new Thread(new ReceivedMessagesHandler(client.getInputStream())).start();

		// ask for a nickname
		Scanner sc = new Scanner(System.in);
		System.out.print("Introduce tu nombre de ususario: ");
		nickname = sc.nextLine();

		// read messages from keyboard and send to server
		System.out.print("Envia mensajes:");
		PrintStream output = new PrintStream(client.getOutputStream());
		String msg = "";
		while (sc.hasNextLine()) {
			msg = sc.nextLine();
			if (msg.equals("exit()")) {
				break;
			}else if(msg.equals("logout()")){
				System.out.print("Introduce tu nombre de ususario: ");
				nickname = sc.nextLine();

				// read messages from keyboard and send to server
				System.out.print("Envia mensajes:");
			}else{
				output.println(nickname + ": " + msg);
			}
		}
		
		output.close();
		sc.close();
		client.close();
	}
}

class ReceivedMessagesHandler implements Runnable {

	private InputStream server;

	public ReceivedMessagesHandler(InputStream server) {
		this.server = server;
	}

	@Override
	public void run() {
		// receive server messages and print out to screen
		Scanner s = new Scanner(server);
		while (s.hasNextLine()) {
			System.out.println(s.nextLine());
		}
		s.close();
	}
}
