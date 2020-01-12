package fx;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

public class Window {
    @FXML
    private BarChart<String, Integer> graph;

    @FXML
    private void initialize() {
        graph.getYAxis().setLabel("Number of occurrences");
        graph.getXAxis().setLabel("Word");
    }

    @FXML
    private void chooseFile() {
        FileChooser dialog = new FileChooser();
        dialog.setInitialDirectory(new File(System.getProperty("user.home")));
        dialog.setTitle("Choose a file");
        Stage stage = (Stage) graph.getScene().getWindow();
        File file = dialog.showOpenDialog(stage);
        try {
            Map<String, Integer> words = WordCounter.getWordCount(file);
            showTopTen(words);
        } catch (ExecutionException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private void showTopTen(Map<String, Integer> in) {
        graph.getData().clear();
        ArrayList<Pair<String, Integer>> ls = new ArrayList<>();
        for (String key : in.keySet())
            ls.add(new Pair<>(key, in.get(key)));
        Comparator<Pair<String, Integer>> c = Comparator.comparingInt(Pair::getValue);
        ls.sort(c.reversed());
        for (int i = 0; i < Math.min(100, ls.size()); i++) {
            WordCounter.LOG.log(Level.FINE, ls.get(i).getKey() + ": " + ls.get(i).getValue());
            XYChart.Series<String, Integer> series = new XYChart.Series<>();
            series.getData().add(new XYChart.Data<>("", ls.get(i).getValue()));
            series.setName(ls.get(i).getKey());
            graph.getData().add(series);
        }
    }
}
