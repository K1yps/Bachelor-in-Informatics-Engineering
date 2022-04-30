package business;

import UI.TextUI;
import business.Model.User;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;

public class Client implements ClientI{

    public User utilizador;


    public Client(){
        this.utilizador = new User();
    }

    public Client(User user){
        this.utilizador = user;
    }

    public static void main(String[] args) {
        try {
            new TextUI().run();
            Socket socket = new Socket("localhost", 12345);
            TaggedConnection tc = new TaggedConnection(socket);
            BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));
            Client client = new Client();


            System.out.println("Registo.");
            String username_registered = insertCredentials(systemIn,tc); //para o registo


            System.out.println("Login.");
            String username_loggedIn = insertCredentials(systemIn,tc); //para o login
            if(!username_loggedIn.equals("")){  // se o login foi bem sucedido cria o novo cliente
                tc.send(9,username_loggedIn.getBytes());
                User user = tc.deserializeUser();
                client = new Client(user);
            }


            client.sendCurrentLocation(tc); // manda a localizaçao do user



           tc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void login(String username, String password) {

    };

    public void registar(String username, String password) {

    };

    public void atualizarLocalizacao(int x, int y) {

    };

    public void getPessoasZona(int x){

    };

    public void alarmeZonaVazia(int x){

    };

    public void informarDoenca(){

    };

    public void mapaLocalizacoes(){

    };



    public static String  insertCredentials(BufferedReader systemIn, TaggedConnection tc){
        String username= "";
        String password= "";

        System.out.println("Inserir username desejado: ");

        try{
            String line;

            while ((line = systemIn.readLine()) != null) {
                if(username.equals("")){
                    username = line;
                    System.out.println("Inserir password desejada: ");
                }
                else{
                    password = line;
                    break;
                }
            }

             // para ja deixei tag 0, temos de pensar nisto
            tc.send(0,username.getBytes());
            // para ja deixei tag 0, temos de pensar nisto
            tc.send(0,password.getBytes());

            TaggedConnection.Frame recebida = tc.receive();
            String msg =  new String(recebida.data);
            System.out.println(msg);
            int res = recebida.tag;
            if (res == 1){
                return username;  //return username if login or register successful
            }


        }
        catch (IOException e){
            e.printStackTrace();
        }
        return "";
    }


    public void sendCurrentLocation(TaggedConnection tc){
        User user = this.utilizador;
        byte [] toSend = this.utilizador.getUsername().getBytes();

        try{
            tc.send(10,toSend); // falta mandar a sua nova posicao
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private byte[] bigIntToByteArray( final int i ) {
        BigInteger bigInt = BigInteger.valueOf(i);
        return bigInt.toByteArray();
    }


}