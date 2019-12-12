import java.awt.*;
import java.io.Serializable;

public class Message implements Serializable {
    public int player;

    public enum Action {PlayerLostHealth, PlayerMoved, PlayerGotItem}

    public Action action;

    // Only used for PlayerMoved
    public Point newLocation;

    // Only used for PlayerGotItem
    public Point mapCoord; // r, c index of map location from where the item was taken.

    public static Message createMessage(int player, Action action, Point newLocation, Point mapCoord) {
        Message msg = new Message();
        msg.player = player;
        msg.action = action;

        if (action == Action.PlayerMoved) msg.newLocation = newLocation;
        else msg.newLocation = null;

        if (action == Action.PlayerGotItem) msg.mapCoord = mapCoord;
        else msg.mapCoord = null;

        return msg;
    }
}