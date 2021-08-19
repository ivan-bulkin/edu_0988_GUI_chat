package Server;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class User {
    private Socket socket;
    private String userName;
    private ObjectOutputStream oos;

    public User(Socket socket) {
        this.socket = socket;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
