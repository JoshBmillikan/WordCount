package fx;

import data.WordCounterThread;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WordCounter extends Application {
    public static final Logger LOG = Logger.getGlobal();

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        AnchorPane root = loader.load(this.getClass().getResourceAsStream("/window.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Word Count");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private static String[] getTxt(final int arraySize, final File file) throws IOException {
        if (!file.exists() || file.isDirectory())
            throw new IOException("Target is not a valid file to read");
        String[] out = new String[arraySize];
        Arrays.fill(out, "");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int i = 0;
        while (reader.ready()) {
            if (i >= out.length)
                i = 0;
            out[i] = out[i].concat(reader.readLine() + "\n");
            i++;
        }
        return out;
    }

    public static Map<String, Integer> getWordCount(final File file) throws ExecutionException, InterruptedException, IOException {
        final int THREADS = Runtime.getRuntime().availableProcessors();
        LOG.log(Level.INFO, "Starting " + THREADS + " Threads");
        FutureTask<Map<String, Integer>>[] futures = new FutureTask[THREADS];
        String[] txt = getTxt(THREADS, file);
        for (int i = 0; i < THREADS; i++) {
            futures[i] = new FutureTask<>(new WordCounterThread(txt[i]));
            new Thread(futures[i]).start();
        }
        Map<String, Integer> out = new HashMap<>();
        for (FutureTask<Map<String, Integer>> f : futures) {
            Map<String, Integer> result = f.get();
            for (String word : result.keySet()) {
                if (out.containsKey(word)) {
                    int val = out.get(word) + result.get(word);
                    out.replace(word, val);
                } else out.put(word, result.get(word));
            }
        }
        return out;
    }

}
