import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DLList<Task> tasks = new DLList<>();
        tasks.add(new Task("Complete HW", 1));
        tasks.add(new Task("Take out trash", 2));
        tasks.add(new Task("Meet up with friends", 1));
        tasks.add(new Task("Make lots of little holes", 3));
        tasks.add(new Task("Plan trip abroad", 2));

        while (true) {
            System.out.print("Choice:" +
                    "\n\t1. List tasks" +
                    "\n\t2. Add a task" +
                    "\n\t3. Remove a task" +
                    "\n\t4. Update a task" +
                    "\n\t5. Quit" +
                    "\n: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: // List tasks
                    System.out.println("Task list:");
                    System.out.println(tasks);
                    break;

                case 2: { // Add task
                    System.out.print("Let's add a new task! Name: ");
                    String name = sc.nextLine();
                    System.out.print("Rank: (number > 0) ");
                    int rank = sc.nextInt();
                    sc.nextLine();
                    Task newTask = new Task(name, rank);
                    if (tasks.contains(newTask))
                        System.out.println("Oops! This task already exists!");
                    else tasks.add(newTask);

                    System.out.println();
                    break;
                }

                case 3: { // Remove task
                    System.out.print("Let's remove a task! Name: ");
                    String name = sc.nextLine();
                    System.out.print("Rank: (number > 0) ");
                    int rank = sc.nextInt();
                    sc.nextLine();
                    Task query = new Task(name, rank);
                    tasks.remove(query);

                    System.out.println();
                    break;
                }

                case 4: { // Update task
                    System.out.print("Let's update a task! Name: ");
                    String name_q = sc.nextLine();
                    System.out.print("Rank: (number > 0) ");
                    int rank_q = sc.nextInt();
                    sc.nextLine();
                    Task query = new Task(name_q, rank_q);

                    if (!tasks.contains(query))
                        System.out.println("Oops! This task doesn't exist!");
                    else {
                        System.out.print("New name: ");
                        String name_n = sc.nextLine();
                        System.out.print("New rank: (number > 0) ");
                        int rank_n = sc.nextInt();
                        sc.nextLine();
                        Task newTask = new Task(name_n, rank_n);

                        if (tasks.contains(newTask))
                            System.out.println("Oops! This task already exists!");
                        else tasks.set(tasks.indexOf(query), newTask);
                    }

                    System.out.println();
                    break;
                }

                case 5: // Quit
                    return;
            }
        }
    }
}