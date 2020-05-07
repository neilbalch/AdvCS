import java.io.Serializable;
import java.util.ArrayList;

public class GameData implements Serializable {
    private final ArrayList<Player> playerList;

    public GameData() {
        playerList = new ArrayList<Player>();
    }

    public void addNewPlayer(Player p) {
        playerList.add(p);
    }

    public void removePlayer(int id) {
        for (int i = 0; i < playerList.size(); i++) {
            if (id == playerList.get(i).getID()) {
                playerList.remove(i);
                i--;
            }
        }
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }
}
