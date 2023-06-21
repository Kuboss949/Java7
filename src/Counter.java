import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Counter implements Runnable {

    private final int id;
    private int noOfLines;
    private final BufferedReader file;
    private Map<String, Long> map;

    public Counter(int id, BufferedReader file, Map<String, Long> map) {
        this.id = id;
        this.noOfLines = 0;
        this.file = file;

        this.map = map;
    }

    @Override
    public void run() {

        try {
            String line;
            synchronized(file){
                line = file.readLine();
            }
            while (line != null) {
                this.noOfLines++;
                Arrays.stream(line.split("[^a-zA-Z]+")).filter(x -> x.length() > 1).map(String::toLowerCase)
                        .forEach(word -> {
                            synchronized (map){
                                    if (map.containsKey(word)) {
                                        long count = map.get(word);
                                        map.put(word, count + 1);
                                    } else {
                                        map.put(word, 1L);
                                    }
                            }
                                }
                        );

                synchronized(file){
                    line = file.readLine();
                }
            }
            System.out.println("Thread " + id + " : " + noOfLines);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
