import java.util.ArrayList;

public class Schedule {
    private ArrayList<Pair<Integer, String>> schedule;

    public Schedule() {
        schedule = new ArrayList<>(8);
        for (int i = 0; i < 8; i++) {
            schedule.add(null);
        }
    }

    public void addPeriod(int period, String courseName) {
        if (period >= 8) return;

        schedule.set(period, new Pair<Integer, String>(period, courseName, "Period: ", " Course: "));
    }

    public String[] formatSchedule() {
        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < schedule.size(); i++) {
            if (schedule.get(i) != null) list.add(schedule.get(i).toString());
        }

        // https://stackoverflow.com/a/4042464/3339274
        return list.toArray(new String[0]);
    }
}
