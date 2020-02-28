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
//        System.out.println("Char 0: " + abbrev.charAt(0) + ", hashed: " + (((int)abbrev.charAt(0)) - 97) * 100);
//        System.out.println("Char 1: " + abbrev.charAt(1) + ", hashed: " + (((int)abbrev.charAt(1)) - 97));
//        System.out.println("Combined: " + ((((int)abbrev.charAt(0)) - 97) * 100 + (((int)abbrev.charAt(1)) - 97)));
        return (((int) abbrev.charAt(0)) - 97) * 100 + (((int) abbrev.charAt(1)) - 97);
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
