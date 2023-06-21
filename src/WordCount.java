import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.nio.file.Files;


public class WordCount {

    public static boolean isInteger(String s) {
        try {
            int p = Integer.parseInt(s);
            return p > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean checkParameters(String[] args) {
        Path p = Paths.get(args[1]);
        return args.length == 2 && isInteger(args[0]) && Files.exists(p);
    }

    public static void main(String[] args) {

        if (!checkParameters(args)) {
            System.out.println("Given parameters are incorrect! Please run program like \"java programName numberOfThreads filePath\"");
            return;
        }

        int numberOfThreads = Integer.parseInt(args[0]);

        Map<String, Long> map = Collections.synchronizedMap(new HashMap<>());
        try {
            BufferedReader file = new BufferedReader(new FileReader("data.txt"));
            Thread[] threads = new Thread[numberOfThreads];
            for (int i = 0; i < numberOfThreads; i++) {
                threads[i] = new Thread(new Counter(i + 1, file, map));
                threads[i].start();
            }
            for (var thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }

            map.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
}
