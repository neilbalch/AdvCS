public class ToDo {
	private String task;
	private int rank;
	
	public ToDo(String task, int rank) {
		this.task = task;
		this.rank = rank;
	}
	
	public String toString() { return rank + " : " + task; }
}