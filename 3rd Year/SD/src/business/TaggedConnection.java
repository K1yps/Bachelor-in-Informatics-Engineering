package business;

import business.Model.User;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TaggedConnection implements AutoCloseable {

    private final DataInputStream dis;
    private final DataOutputStream dos;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public static class Frame {
        public final int tag;
        public final byte[] data;
        public Frame(int tag, byte[] data) {
            this.tag = tag;
            this.data = data;
        }
    }

    public enum Tag {
        Invalid, Authentication_Request, Authentication_Response,

        ;
        private static final Tag[] tags = Tag.values();
        public static Tag valueOf(int value) { //pq java é estupido
            try {
                return tags[value];
            }catch (Exception e){
                return Invalid;
            }
        }
    }



    public TaggedConnection(Socket socket) throws IOException {
        this.dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        this.dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    public void send(Frame frame) throws IOException {
        try {
            lock.writeLock().lock();
            this.dos.writeInt(frame.tag);
            this.dos.writeInt(frame.data.length);
            this.dos.write(frame.data);
            this.dos.flush();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void send(int tag, byte[] data) throws IOException {
        this.send(new Frame(tag, data));
    }

    public Frame receive() throws IOException {
        int tag;
        byte[] data;
        try {
            lock.readLock().lock();
            tag = this.dis.readInt();
            int n = this.dis.readInt();
            data = new byte[n];
            this.dis.readFully(data);
        } finally {
            lock.readLock().unlock();
        }
        return new Frame(tag, data);
    }

    @Override
    public void close() throws IOException {
        this.dis.close();
        this.dos.close();
    }


    public void serializeUser(User user) throws IOException {
        String username = user.getUsername();
        String password = user.getPassword();
        try {
            lock.writeLock().lock();
            dos.writeUTF(username);
            dos.writeUTF(password);
            dos.writeInt(user.getLocalizacao_atual());
            dos.writeInt(user.getPessoas_em_contacto().size());
            for (String pessoa : user.getPessoas_em_contacto())
                dos.writeUTF(pessoa);
            dos.flush();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public User deserializeUser() throws IOException {
        try {
            lock.readLock().lock();
            String username = dis.readUTF();
            String password = dis.readUTF();
            int localizacao_atual = dis.readInt();

            int num_pessoas_em_contacto = dis.readInt();
            HashSet<String> pessoas_em_contacto = new HashSet<>();
            for (int i = 0; i < num_pessoas_em_contacto; i++) {
                pessoas_em_contacto.add(dis.readUTF());
            }
            return new User(username, password);
        } finally {
            lock.readLock().unlock();

        }
    }

}