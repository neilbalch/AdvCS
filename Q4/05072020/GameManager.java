import java.util.ArrayList;

public class GameManager {
    private final ArrayList<ServerThread> serverThreads;
    private GameData gameData;
    private int id = 0;

    public GameManager() {
        serverThreads = new ArrayList<>();
        gameData = new GameData();
    }

    public void addThread(ServerThread st) {
        serverThreads.add(st);
    }

    public void broadcastGameData() {
        for (int i = 0; i < serverThreads.size(); i++) {
            serverThreads.get(i).sendGameData(gameData);
        }
    }

    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }

    public void remove(ServerThread st) {
        serverThreads.remove(st);
    }

    public int addNewPlayer() {
        id++;

        int x = (int) (Math.random() * 600 + 100);
        int y = (int) (Math.random() * 300 + 100);
        gameData.addNewPlayer(new Player(x, y, id));

        return id;
    }

    public void removePlayer(int id) {
        gameData.removePlayer(id);
    }
}