package main;

import java.net.*;

public class TCPserver {

	public static String clientRole(int role) {
		if (role == 1)
			return "Shop";
		else
			return "Courier";
	}

	public static void main(String[] args) throws Exception {

		int port = 4444;

		System.out.println("Starting the server application");

		try (
				// Create a new socket named welcomeSocket for port 4444 of the current IP.
				// This socket will be waiting for incoming connection request by a client.
				ServerSocket welcomeSocket = new ServerSocket(port)) {
			welcomeSocket.setReuseAddress(true);

			System.out.println("Defined new socket " + port + " for connection from clients");

			while (true) {

				try {

					System.out.println("Waiting for incoming requests");

					// Create a new socket named connectionSocket and assigning to it an incoming
					// connection request to the welcomeSocket.
					// A tunnel is automatically established between the server and the client using
					// the connectionSocket socket.
					// Next, the welcome socket is released.

					System.out.println("Establishing an incoming request");

					Socket client = welcomeSocket.accept();

					// Displaying that new client is connected
					// to server
					System.out.println("New client connected"
							+ client.getInetAddress()
									.getHostAddress());

					// create a new thread object
					ClientHandler clientSock = new ClientHandler(client);

					// This thread will handle the client
					// separately
					new Thread(clientSock).start();

				} catch (SocketException e) {
					System.out.println("Client terminated connection");
				}

			}
		}

		// The server remains active waiting for incoming connections
		// Each TCP connection originating from a client is terminated by the client
		// itself

	}

}
