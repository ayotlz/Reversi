package server;

import clients.BotClient;
import clients.AbstractPlayer;
import clients.RealClient;
import models.chip.Color;
import property.Property;
import rooms.Room;
import rooms.RoomType;
import service.Service;

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
    private final Service service = new Service();
    private final List<Room> roomList = new ArrayList<>();
    private int countOfMadeRooms = 0;

    @SuppressWarnings("InfiniteLoopStatement")
    private void startServer() throws IOException {
        System.out.printf("Сервер запущен [port: %d]%n", PORT);
        try (final ServerSocket serverSocket = new ServerSocket(PORT)) {
            for (int i = 0; i < 2; i++) {
                final Socket socket = serverSocket.accept();
                try {
                    final BotClient bc = new BotClient(this, socket);
                    service.addBot(bc);
                    bc.start();
                } catch (final IOException e) {
                    socket.close();
                }
            }

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
            if (player.getClass() != BotClient.class) {
                player.downService();
            } else {
                service.addBot((BotClient) player);
            }
        }
        System.out.printf("Закрыта комната %d%n", room.getID());
        roomList.remove(room);
    }

    public BotClient getBot(final Color color) {
        try {
            final BotClient botClient = service.getBot();
            botClient.setColor(color);
            return botClient;
        } catch (IOException e) {
            return null;
        }
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void removeClient(final AbstractPlayer ap) {
        serverList.remove(ap);
    }

    public static void main(final String[] args) throws IOException {
        final Server server = new Server();
        server.startServer();
    }
}
