import java.io.*;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        String votesPath = "votes.csv";
        String seniors2019 = "2019-seniors.csv";
        String seniors2020 = "2020-seniors.csv";

        LinkedList<String> seniors2019List = new LinkedList<>();
        LinkedList<String> seniors2020List = new LinkedList<>();

        try {
            BufferedReader reader2019 = new BufferedReader(new FileReader(seniors2019));
            BufferedReader reader2020 = new BufferedReader(new FileReader(seniors2020));

            String line = "";
            while ((line = reader2019.readLine()) != null) seniors2019List.add(line);

            while ((line = reader2020.readLine()) != null) seniors2020List.add(line);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LinkedList<String> acceptedVotes = new LinkedList<>();

        try {
            BufferedReader readerVotes = new BufferedReader(new FileReader(votesPath));

            // First line is garbage...
            readerVotes.readLine();

            String line = "";
            while ((line = readerVotes.readLine()) != null) {
                String idNum = line.substring(0, line.indexOf("@"));
                if (!seniors2019List.contains(idNum) && !seniors2020List.contains(idNum)) {
                    acceptedVotes.add(line);
                    System.out.println("Vote from " + idNum + " admitted");
                } else System.out.println("Vote from " + idNum + " forbidden");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileWriter fileWriter = new FileWriter("filteredVotes.csv");
            for (String line : acceptedVotes) {
                fileWriter.write(line + "\n");
            }
            fileWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}