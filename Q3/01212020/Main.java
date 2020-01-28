import java.util.Scanner;

class Main {
    private enum SortMethod {song, artist, time}

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        if (false) {
            String[] data = {"Cat", "Dog", "Bird", "Bear", "Pig"};
            DLList<String> list = new DLList<String>();

            for (String item : data) {
                list.add(item);
            }

            System.out.println(list);
            System.out.println();

            System.out.print("Adding animal... name? ");
            list.add(input.next());
            System.out.println(list);
            System.out.println();

            System.out.print("Adding animal... name? ");
            String animal_name = input.next();
            System.out.print("Adding animal... index? ");
            int animal_index = input.nextInt();
            list.add(animal_index, animal_name);
            System.out.println(list);
            System.out.println();

            System.out.print("Getting animal... index? ");
            System.out.println(list.get(input.nextInt()));
            System.out.println();
        }

        System.out.println();
        System.out.println("------------ PART 2 ------------");

        DLList<Song> list = new DLList<Song>();

        int time = 0;
        boolean dontBreak = true;
        SortMethod currentMethod = SortMethod.time;

        list.add(new Song("b", "b", 0));
        list.add(new Song("a", "a", 1));
        list.add(new Song("c", "c", 2));
        time = 3;

        while (dontBreak) {
            System.out.println();
            System.out.println(
                    "Choose an option:" + "\n" +
                            "1. Add a new song" + "\n" +
                            "2. Display song list" + "\n" +
                            "3. Delete song by artist and song name" + "\n" +
                            "4. Delete song by number on playlist" + "\n" +
                            "5. Delete song by artist" + "\n" +
                            "6. Delete song by name" + "\n" +
                            "7. Sort by artist name" + "\n" +
                            "8. Sort by song name" + "\n" +
                            "9. Sort by time added" + "\n" +
                            "10. Search by artist" + "\n" +
                            "11. Quit"
            );

            System.out.println();
            System.out.print("Selection? ");
            int selection = input.nextInt();
            System.out.println();

            switch (selection) {
                case 1: // Add song
                    System.out.print("Enter New Song: ");
                    String newSong = input.next();
                    System.out.print("Enter the Artist: ");
                    String artist = input.next();
                    if (!list.contains(new Song(newSong, artist, time))) {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getSong().compareTo(newSong) < 0 && currentMethod == SortMethod.song) {
                                list.add(i, new Song(newSong, artist, time));
                                time++;
                            } else if (list.get(i).getArtist().compareTo(artist) < 0 && currentMethod == SortMethod.artist) {
                                list.add(i, new Song(newSong, artist, time));
                                time++;
                            }
                        }
                        if (currentMethod == SortMethod.time) {
                            if (list.size() > 0) {
                                list.add(list.size(), new Song(newSong, artist, time));
                            } else {
                                list.add(0, new Song(newSong, artist, time));
                            }
                            time++;
                        }
                    } else System.out.println("Oops, that song already exists!");
                    break;
                case 2: // Display song list
                    System.out.println(list.toString());
                    break;
                case 3: // Delete song by artist and song name
                    System.out.print("Enter Song: ");
                    String songName = input.next();
                    System.out.print("Enter Artist: ");
                    artist = input.next();
                    Song query = new Song(songName, artist, 0);

                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).equals(query)) list.remove(i);
                    }
                    break;
                case 4: // Delete song by number on playlist
                    System.out.print("Enter number on playlist: ");
                    int index = input.nextInt();

                    list.remove(index);
                    break;
                case 5: // Delete song by artist
                    System.out.print("Enter Artist: ");
                    artist = input.next();

                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getArtist().equalsIgnoreCase(artist)) list.remove(i);
                    }
                    break;
                case 6: // Delete song by name
                    System.out.print("Enter Song Name: ");
                    songName = input.next();

                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getSong().equalsIgnoreCase(songName)) list.remove(i);
                    }
                    break;
                case 7: // Sort by artist name
                    for (int i = 0; i < list.size() - 1; i++) {
                        for (int j = i + 1; j < list.size(); j++) {
                            if (list.get(i).getArtist().compareTo(list.get(j).getArtist()) > 0) {
                                Song temp = list.get(i);
                                Song temp2 = list.get(j);
                                list.set(i, temp2);
                                list.set(j, temp);
                            }
                        }
                    }

                    currentMethod = SortMethod.artist;
                    break;
                case 8: // Sort by song name
                    for (int i = 0; i < list.size() - 1; i++) {
                        for (int j = i + 1; j < list.size(); j++) {
                            if (list.get(i).getSong().compareTo(list.get(j).getSong()) > 0) {
                                Song temp = list.get(i);
                                Song temp2 = list.get(j);
                                list.set(i, temp2);
                                list.set(j, temp);
                            }
                        }
                    }

                    currentMethod = SortMethod.song;
                    break;
                case 9: // Sort by time added
                    for (int i = 0; i < list.size() - 1; i++) {
                        for (int j = i + 1; j < list.size(); j++) {
                            if (list.get(i).getTime() > list.get(j).getTime()) {
                                Song temp = list.get(i);
                                Song temp2 = list.get(j);
                                list.set(i, temp2);
                                list.set(j, temp);
                            }
                        }
                    }

                    currentMethod = SortMethod.time;
                    break;
                case 10: // Search by artist
                    System.out.print("Enter Artist: ");
                    artist = input.next();

                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getArtist().equalsIgnoreCase(artist)) {
                            System.out.println(list.get(i));
                        }
                    }
                    break;
                case 11: // Quit
                    dontBreak = false;
                    break;
            }
        }
    }
}