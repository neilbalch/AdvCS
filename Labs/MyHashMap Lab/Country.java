import java.io.Serializable;

public class Country implements Comparable<Country>, Serializable {
    private String abbrev;

    public Country(String abbrev) {
        this.abbrev = abbrev;
    }

    public String getAbbrev() {
        return abbrev;
    }

    public String toString() {
        return abbrev;
    }

    @Override
    public int hashCode() {
        return (int) abbrev.charAt(0) * 11 + (int) abbrev.charAt(1) * 7;
    }

    @Override
    public boolean equals(Object oth) {
        Country o = (Country) oth;
        return o.getAbbrev().equalsIgnoreCase(abbrev);
    }

    @Override
    public int compareTo(Country o) {
        return abbrev.compareToIgnoreCase(o.getAbbrev());
    }
}
