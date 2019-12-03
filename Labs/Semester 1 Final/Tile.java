public class Tile {
    public enum Type {LAND, MOUNTAIN, WATER}

    public Type type;

    public boolean hasItem;

    public static Tile createTile(Type type, boolean hasItem) {
        Tile tile = new Tile();
        tile.type = type;
        tile.hasItem = hasItem;
        return tile;
    }
}
