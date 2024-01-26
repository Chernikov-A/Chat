package org.example;

import javax.imageio.IIOException;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientManager implements Runnable {
    private  Socket socket;
    private  String name;

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public static ArrayList<ClientManager> clients = new ArrayList<>();

    public ClientManager(Socket socket) {
        try {
            this.socket = socket;
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            name = bufferedReader.readLine();
            clients.add(this);
            System.out.println(name + " подключился к чату.");
            broadcastMassage("Server: " + name + " подключился к чату.");
        }catch (IOException e){
            closeEverything(socket,bufferedReader,bufferedWriter);
        }

    }
    private void closeEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter){
        removeClient();
        try {
            if (bufferedReader != null){
                bufferedReader.close();
            }
            if (bufferedWriter !=null){
                bufferedWriter.close();
            }
            if (socket != null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void removeClient(){
        clients.remove(this);
        System.out.println(name + " покинул чат.");
        broadcastMassage("Server: " + name + " покинул чат.");

    }

    @Override
    public void run() {
        String massageFromClient;

            while (socket.isConnected()) {
                try {
                    // Чтение данных
                    massageFromClient = bufferedReader.readLine();
                    if (massageFromClient == null){
                        // для macOS
                        closeEverything(socket,bufferedReader,bufferedWriter);
                        break;
                    }
                    broadcastMassage(massageFromClient);
                }catch (IOException e){
                    closeEverything(socket,bufferedReader,bufferedWriter);
                    break;
                }

            }

    }

    private void broadcastMassage(String massage) {
        for (ClientManager clientManager : clients){
            try {
                if (!clientManager.name.equals(name) && massage != null){
                    clientManager.bufferedWriter.write(massage);
                    clientManager.bufferedWriter.newLine();
                    clientManager.bufferedWriter.flush();
                }
            }catch (IOException e){
                closeEverything(socket,bufferedReader,bufferedWriter);
            }
        }
    }
}
