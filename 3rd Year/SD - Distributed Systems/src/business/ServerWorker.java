package business;

import business.Model.Facade;
import business.Model.User;


public class ServerWorker implements Runnable { // instanciamos um a cada socket

    private TaggedConnection tc;
    private Facade registo; //o register tem as informaçoes do servidor


    public ServerWorker(TaggedConnection tc, Facade registo){
        this.tc = tc;
        this.registo = registo;
    }


    @Override
    public void run() {
        try{
            //registerAccount(tc);
            //loginAccount(tc);

            User target =null; //TODO

            System.out.println("Estado das contas no servidor: " + registo.toString());


            /*TaggedConnection.Frame frame = tc.receive();
            if(frame.tag==9){ //esta a pedir a conta a que deu login
                String username = new String (frame.data);
                User user = registo.getUser(username);
                tc.serializeUser(user);
            }

            TaggedConnection.Frame frame1 = tc.receive();
            if(frame1.tag==10){ //esta a receber a localização do user
                String username = new String (frame.data);
                User user = registo.getUser(username);
                tc.serializeUser(user);
            }*/

            while(true){
                TaggedConnection.Frame frame = tc.receive();
                System.out.println("recebeu trama");
                if(frame.tag==9){ //esta a pedir a conta a que deu login
                    System.out.println("trama tipo 9");
                    String username = new String (frame.data);
                    User user = registo.getUser(username);
                    tc.serializeUser(user);
                }
                if(frame.tag==10){ //esta a receber a localização do user
                    String username = new String (frame.data);
                    User user = registo.getUser(username);
                    int localizacao = 0;
                    user.setLocalizacao_atual(localizacao);
                    System.out.println(user.toString());
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    public void registerAccount(TaggedConnection tc){ //deve dizer ao client se correu bem ou nao
        try{
            String username = new String (tc.receive().data);
            String password = new String (tc.receive().data);

            if(!registo.containsUser(username)){
                User newUser = new User(username,password);
                //registo.setUser(username,newUser);

                 // para ja deixei tag 1, temos de pensar nisto
                tc.send(1,"Conta criada com sucesso! ".getBytes());

            }
            else{
                // para ja deixei tag 0, temos de pensar nisto
                tc.send(0,"Username já está utilizado, conta não foi criada. ".getBytes());

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loginAccount(TaggedConnection tc){ //deve devolver o objeto business.Model.User ou nada
        try{
            String username = new String (tc.receive().data);
            String password = new String (tc.receive().data);
            TaggedConnection.Frame frame;

            if(registo.userExists(username)){
                if(registo.checkUserPassword(username,password)){
                    frame = new TaggedConnection.Frame(1, "O login foi bem sucedido! ".getBytes());
                }
                else{
                    frame = new TaggedConnection.Frame(0, "A password inserida está incorreta. ".getBytes());
                }

            }
            else{
                frame = new TaggedConnection.Frame(0,"Username não existe. ".getBytes());
            }
            tc.send(frame);

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }



    /*public boolean login(BufferedReader in, PrintWriter out){
        String username= "";
        String password= "";

        out.println("Inserir username: ");
        out.flush();


        try{
            String line;


            while ((line = in.readLine()) != null) {
                if(username.equals("")){
                    username = line;
                    out.println("Inserir password: ");
                    out.flush();
                }
                else{
                    password = line;
                    break;
                }
            }
            if(registo.accounts.containsKey(username)){
                if(registo.accounts.containsValue(password)){
                    out.println("O login foi bem sucedido! ");
                    out.flush();
                    return true;
                }
                else{
                    out.println("A password inserida está incorreta ");
                    out.flush();
                    return false;
                }

            }
            else{
                out.println("Username não existe.");
                out.flush();
                return false;
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }*/


    /*public boolean register(BufferedReader in, PrintWriter out){
        String username= "";
        String password= "";

        out.println("Inserir username desejado: ");
        out.flush();

        try{
            String line;


            while ((line = in.readLine()) != null) {
                if(username.equals("")){
                    username = line;
                    out.println("Inserir password desejada: ");
                    out.flush();
                }
                else{
                    password = line;
                    break;
                }
            }
            if(!registo.accounts.containsKey(username)){
                registo.addUser(username,password);
                out.println("Conta criada com sucesso! ");
                out.flush();
                return true;
            }
            else{
                out.println("Username já está utilizado, conta não foi criada. ");
                out.flush();
                return false;
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }*/
}