package business;

import business.Model.Facade;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private Socket socket;
    private Facade registo; //o register tem as informaçoes do servidor

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(12345);
            Facade registo = new Facade();

            while (true) { //esta continuamente a aceitar conexoes
                Socket socket = ss.accept(); //por cada accept que fazemos, estamos a criar um novo worker, conseguimos assim ter mts clientes ao mms tempo
                TaggedConnection tc = new TaggedConnection(socket);
                System.out.println("Client connected: " + socket.getRemoteSocketAddress());

                Thread serverWorker = new Thread(new ServerWorker(tc,registo)); //esta thread é quem interage com o client
                serverWorker.start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}


