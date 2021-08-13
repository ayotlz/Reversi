package clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.chip.Color;
import rooms.Room;
import rooms.RoomType;
import server.Server;

import java.io.*;
import java.net.Socket;

public class RealClient extends AbstractPlayer {
    public RealClient(final Server server, final Socket socket) throws IOException {
        super(server, socket);
    }

    @Override
    public void run() {
        try {
            nickName = in.readLine();

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
}
