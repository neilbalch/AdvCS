public class Task implements Comparable<Task> {
    private String name;
    private int rank;

    public Task(String name, int rank) {
        this.name = name;
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String toString() {
        return rank + " : " + name;
    }

    public int compareTo(Task o) {
        if (rank != o.getRank()) return rank - o.getRank();
        else return name.compareToIgnoreCase(o.getName());
    }
}
