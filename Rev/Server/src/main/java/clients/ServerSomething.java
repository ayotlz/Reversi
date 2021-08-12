package clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.chip.Color;
import rooms.Room;
import rooms.RoomType;
import server.Server;

import java.io.*;
import java.net.Socket;

public class ServerSomething extends Thread {
    private final Server server;
    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;
    private String nickName;
    private Color color;

    public ServerSomething(final Server server, final Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void run() {
        try {
            nickName = in.readLine();
            System.out.printf("Подключился новый игрок [%s]%n", nickName);

            final RoomType roomType = requestRoomType();
            if (roomType == RoomType.BotVsBot) {
                color = Color.NEUTRAL;
            } else {
                color = requestColor();
            }

            chooseRoom(roomType, color).joinRoom(this);
        } catch (final IOException e) {
            this.downService();
        }
    }

    public String getNickName() {
        return nickName;
    }

    private RoomType requestRoomType() {
        try {
            RoomType roomType = null;
            while (roomType == null) {
                final String message = in.readLine();
                roomType = switch (message) {
                    case "1" -> RoomType.HumanVsBot;
                    case "2" -> RoomType.HumanVsHuman;
                    case "3" -> RoomType.BotVsBot;
                    default -> null;
                };
            }
            return roomType;
        } catch (IOException e) {
            downService();
            return null;
        }
    }

    private Color requestColor() {
        try {
            Color pColor = null;
            while (pColor == null) {
                final String message = in.readLine();
                final StringReader reader = new StringReader(message);
                final ObjectMapper mapper = new ObjectMapper();
                pColor = mapper.readValue(reader, Color.class);
            }
            return pColor;
        } catch (IOException e) {
            downService();
            return null;
        }
    }

    private Room chooseRoom(final RoomType type, final Color pColor) {
        for (final Room room : server.getRoomList()) {
            if (room.getRoomType() == type && room.isRoomHasPlace(pColor)) {
                return room;
            }
        }
        return server.createRoom(type);
    }

    public Color getColor() {
        return color;
    }

    public void send(final String msg) throws IOException {
        out.write(msg + "\n");
        out.flush();
    }

    public String readMessage() throws IOException {
        return in.readLine();
    }

    public void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                server.removeClient(this);
                System.out.printf("%s отключился%n", this.nickName);
            }
        } catch (final IOException ignored) {
        }
    }
}
