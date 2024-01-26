package org.example;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Введите имя: ");
            String name = scanner.nextLine();

            InetAddress address = InetAddress.getLocalHost();
            System.out.println("InetAddress: " + address);
            Socket socket = new Socket(address, 5000);
            String remoteIP = address.getHostAddress();
            System.out.println("Remote IP: " + remoteIP);
            Client client = new Client(socket,name);
            System.out.println("LocalPort: " + socket.getLocalPort());

            client.listenForMessage();
            client.sendMessage();

        }catch (UnknownHostException ex){
            throw new RuntimeException(ex);
        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }
}