package clients;

import models.chip.Color;
import server.Server;

import java.io.*;
import java.net.Socket;

public abstract class AbstractPlayer extends Thread {
    protected final Server server;
    protected final Socket socket;
    protected final BufferedReader in;
    protected final BufferedWriter out;
    protected String nickName;
    protected Color color;

    public AbstractPlayer(final Server server, final Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public String getNickName() {
        return nickName;
    }

    public Color getColor() {
        return color;
    }

    public String readMessage() throws IOException {
        return in.readLine();
    }

    public void send(final String msg) throws IOException {
        out.write(msg + "\n");
        out.flush();
    }

    public void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                server.removeClient(this);
            }
        } catch (final IOException ignored) {
        }
    }
}
