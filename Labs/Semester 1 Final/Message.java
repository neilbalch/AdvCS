import java.awt.*;
import java.io.Serializable;

public class Message implements Serializable {
    public int player;

    public enum Action {PlayerLostHealth, PlayerMoved, PlayerGotItem}

    public Action action;

    // Only used for PlayerMoved
    public Point direction;

    public static Message createMessage(int player, Action action, Point direction) {
        Message msg = new Message();
        msg.player = player;
        msg.action = action;

        if (action == Action.PlayerMoved) msg.direction = direction;
        else msg.direction = null;

        return msg;
    }
}