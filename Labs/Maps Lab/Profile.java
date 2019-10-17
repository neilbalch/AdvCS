import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.Arrays;

class Profile implements Comparable<Profile> {
    private String nameFirst;
    private String nameLast;
    private int dob;
    private ArrayList<String> periods;

    public Profile(String nameFirst, String nameLast, int dob) {
        this.nameFirst = nameFirst;
        this.nameLast = nameLast;
        this.dob = dob;
        this.periods = new ArrayList<>();
    }

    public int getDob() {
        return dob;
    }

    public String getNameFirst() {
        return nameFirst;
    }

    public String getNameLast() {
        return nameLast;
    }

    public String toString() {
        return nameLast + ", " + nameFirst + " : " + dob;
    }

    public boolean removePeriod(String period) {
        for (int i = 0; i < periods.size(); i++) {
            if (periods.get(i).equalsIgnoreCase(period)) {
                periods.remove(i);
                return true;
            }
        }

        return false;
    }

    public String[] formatAllAsStrings() {
        ArrayList<String> temp = new ArrayList<>();
        temp.add(this.toString());

        if (periods.size() > 0) {
            temp.add("");
            temp.add("Periods: ");
            for (String period : periods)
                temp.add(" - " + period);
        }

        return temp.toArray(new String[1]);
    }

    public void addPeriods(String[] periods) {
        this.periods.addAll(Arrays.asList(periods));
    }

    public int hashCode() {
        int hashCode = nameFirst.hashCode() * 11;
        hashCode += nameLast.hashCode() * 17;
        hashCode += dob * 19;

        return 23 * hashCode;
    }

    public boolean equals(Object item) {
        Profile i = (Profile) item;
        return hashCode() == i.hashCode();
    }

    public int compareTo(Profile i) {
        int lastComparison = nameLast.compareToIgnoreCase(i.getNameLast());
        if (lastComparison > 0) return 1;
        else if (lastComparison < 0) return -1;
        else {
            int firstComparison = nameFirst.compareToIgnoreCase(i.getNameFirst());
            if (firstComparison > 0) return 1;
            else if (firstComparison < 0) return -1;
            else {
                if (dob > i.getDob()) return 1;
                else if (dob < i.getDob()) return -1;
                else return 0;
            }
        }
    }
}

