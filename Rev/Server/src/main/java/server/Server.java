package server;

import clients.BotClient;
import clients.AbstractPlayer;
import clients.RealClient;
import property.Property;
import rooms.Room;
import rooms.RoomType;

import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Server {
    private static final int PORT = Property.getPort();

    private final ConcurrentLinkedQueue<AbstractPlayer> serverList = new ConcurrentLinkedQueue<>();
    private final List<Room> roomList = new ArrayList<>();
    private int countOfMadeRooms = 0;

    @SuppressWarnings("InfiniteLoopStatement")
    private void startServer() throws IOException {
        System.out.printf("Сервер запущен [port: %d]%n", PORT);
        try (final ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                final Socket socket = serverSocket.accept();
                try {
                    final RealClient rc = new RealClient(this, socket);
                    serverList.add(rc);
                    rc.start();
                } catch (final IOException e) {
                    socket.close();
                }
            }
        } catch (final BindException e) {
            System.out.println(e.getMessage());
        }
    }

    public Room createRoom(final RoomType type) {
        final Room room = new Room(this, type, ++countOfMadeRooms);
        System.out.printf("Создана комната %d%n", room.getID());
        roomList.add(room);
        return room;
    }

    public void closeRoom(final Room room) {
        for (final AbstractPlayer player : room.getPlayers()) {
            try {
                player.send(Command.DOWN_CLIENT.getCommand());
            } catch (IOException ignored) {
            }
            player.downService();
        }
        System.out.printf("Закрыта комната %d%n", room.getID());
        roomList.remove(room);
    }

    public BotClient getBot(String name) {
        return null;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void removeClient(final AbstractPlayer cs) {
        serverList.remove(cs);
    }

    public static void main(final String[] args) throws IOException {
        final Server server = new Server();
        server.startServer();
    }
}
