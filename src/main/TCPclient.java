package main;

import java.io.*;
import java.net.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class TCPclient {

	public static void main(String[] args) throws Exception {

		int clientType = 0;
		String clientInfo;

		String ip = "192.168.1.5"; // Change according to your local IP
		int port = 4444;

		String pathStr = "D:\\University\\3rdYear\\2ndSemester\\NetworkComputing\\NetworkComputingCoursework\\Orders\\";

		// Creating a File object for directory
		File directoryPath = new File(pathStr);
		// List of all files and directories
		FilenameFilter textFilefilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String lowercaseName = name.toLowerCase();
				if (lowercaseName.endsWith(".txt")) {
					return true;
				} else {
					return false;
				}
			}
		};

		// List of all the text files
		String filesList[] = directoryPath.list(textFilefilter);

		try {

			System.out.println("Starting the client application");

			// Input text stream to get data from the user via the keyboard.
			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("Defined input text stream from the keyboard");

			// Create a new socket named clientSocket for port 6789 of the current IP
			// 127.0.0.1 (current system).
			// This socket will be used to send a connection request to the server running
			// at the provided IP on port 6789.
			// The hostname may be used instead of the loop IP address, or any IP or
			// hostname.
			Socket clientSocket = new Socket(ip, port);

			// Output text stream to send data to the server.
			// The output text stream connects to the socket of the server to send
			// characters.
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

			System.out.println(
					"Connection established. Are you a shop here to request a delivery or a courier looking to make a delivery? Press 1 for shop or 2 for courier.");

			do {
				try {
					clientType = Integer.parseInt(inFromUser.readLine());
					switch (clientType) {
						case 1:
							System.out.print("Welcome shop! Please enter your name: ");
							clientInfo = inFromUser.readLine();

							System.out.print("Please enter your city: ");
							clientInfo += "," + inFromUser.readLine();

							System.out.print("Please enter your address: ");
							clientInfo += "," + inFromUser.readLine();

							System.out.print("Please enter your order details: ");
							clientInfo += "," + inFromUser.readLine();

							System.out.print("Please enter the customers address: ");
							clientInfo += "," + inFromUser.readLine();

							outToServer.writeBytes(clientType + "," + clientInfo + "\n");
							break;
						case 2:
							System.out.print("Welcome courier! Please enter your name:");
							clientInfo = inFromUser.readLine();

							System.out.print("Please enter your address:");
							clientInfo += "," + inFromUser.readLine();

							System.out.print("Please enter your phone number:");
							clientInfo += "," + inFromUser.readLine();

							outToServer.writeBytes(clientType + "," + clientInfo + "\n");

							System.out.println("List of all the available orders:");
							for (String fileName : filesList) {
								System.out.println(fileName);
							}
							if (filesList.length > 0) {
								System.out.print("Please enter the order name which you would like to see:");
								String filename = inFromUser.readLine();

								try (BufferedReader br = new BufferedReader(new FileReader(pathStr + filename))) {
									String line;
									while ((line = br.readLine()) != null) {
										System.out.println(line);

									}
								}
								System.out.println("Would you like to accept this order? Type yes or no");
								String answer = inFromUser.readLine();
								while (filesList.length > 0) {
									if (answer.contains("yes")) {
										Path filepath = FileSystems.getDefault()
												.getPath("./Orders/" + filename);
										try {
											Files.delete(filepath);
										} catch (NoSuchFileException x) {
											System.err.format("%s: no such" + " file or directory%n", filepath);
										} catch (IOException x) {
											System.err.println(x);
										}
										break;
									} else {
										System.out.println("List of all the available orders:");
										for (String fileName : filesList) {
											System.out.println(fileName);
										}
										System.out.print("Please enter the order name which you would like to see:");
										filename = inFromUser.readLine();

										try (BufferedReader br = new BufferedReader(
												new FileReader(pathStr + filename))) {
											String line;
											while ((line = br.readLine()) != null) {
												System.out.println(line);

											}

										}
										System.out.println("Would you like to accept this order? Type yes or no");
										answer = inFromUser.readLine();

									}
								}
							} else {
								System.out.println("No orders to show!");
							}

							break;
						default:
							System.out.println("Wrong input, try again!");
							break;
					}
				} catch (NumberFormatException e) {
					System.out.println("Wrong input, try again!");
				}
			} while (clientType != 1 && clientType != 2);

			System.out.println("Your request has been received. Thank you!");

			// The connection socket and the TCP connection are terminated by the client
			clientSocket.close();

		} catch (

		ConnectException e) {
			System.out.println("A connection could not be established to the server. Please try again later.");
		}
	}
}