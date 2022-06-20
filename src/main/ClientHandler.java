package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
	private static int counter;

	private final Socket clientSocket;

	public static String clientRole(int role) {
		if (role == 1) {
			return "Shop";
		} else {
			return "Courier";
		}
	}

	public ClientHandler(Socket socket) {
		this.clientSocket = socket;
	}

	public void run() {
		String clientPhrase;
		String response;
		String orderDetails;
		String[] clientArray;
		int port = 4444;
		String filename = "";

		PrintWriter out = null;
		BufferedReader in = null;

		try {
			// Input text stream to get data from the client.
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			// Output text stream to send data to the client.
			out = new PrintWriter(clientSocket.getOutputStream());

			System.out.println("Defined input text stream from the client and output text stream to the client");

			System.out.println("Reading data from the client");

			// Consume the input from the client.
			// Read a text string until the new line character.
			clientPhrase = in.readLine();

			clientArray = clientPhrase.split(",");

			if (clientRole(Integer.parseInt(clientArray[0])) == "Shop") {

				// Path, change it
				String path = "D:\\University\\3rdYear\\2ndSemester\\NetworkComputing\\Orders\\";

				// Gets the "Orders" directory in order to set the counter
				File ordersDir = new File(path);
				counter = ordersDir.listFiles().length + 1;

				filename = path + clientArray[1] + "_Order_" + counter + ".txt";

				orderDetails = "Order ID: " + counter + "\n" + clientRole(Integer.parseInt(clientArray[0])) + ": "
						+ clientArray[1] + "\nCity: " + clientArray[2] + "\nAddress: " + clientArray[3]
						+ "\nCustomer Address: " + clientArray[5] + "\nOrder details are: " + clientArray[4];

				// Write to file all the data that has been gathered in the response string
				try {
					PrintWriter file = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
					file.println(orderDetails);
					file.close();
					System.out.println("Order request added to the system.");
				} catch (IOException e) {
					System.out.println("Error: Cannot write to log file.");
					return;
				}

			} else {
				response = "We received from you this information - You are a "
						+ clientRole(Integer.parseInt(clientArray[0])) + " | Name: " + clientArray[1] + " | Address: "
						+ clientArray[2] + " | Phone Number: " + clientArray[3];

			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
					clientSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
