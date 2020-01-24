public class Song {
    private String song;
    private String artist;
    private int time;

    public Song(String song, String artist, int time) {
        this.song = song;
        this.artist = artist;
        this.time = time;
    }

    public String toString() {
        return song + " - " + artist;
    }

    public int getTime() {
        return time;
    }

    public String getSong() {
        return song;
    }

    public String getArtist() {
        return artist;
    }

    public boolean equals(Object o) {
        Song tmp = (Song) o;
        return tmp.getArtist().equals(artist) && tmp.getSong().equals(song);
    }
}