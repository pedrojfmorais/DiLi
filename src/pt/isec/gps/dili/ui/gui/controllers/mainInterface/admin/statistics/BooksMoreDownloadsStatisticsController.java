package pt.isec.gps.dili.ui.gui.controllers.mainInterface.admin.statistics;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.model.fsm.DiliState;
import pt.isec.gps.dili.ui.gui.resources.ImageManager;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class BooksMoreDownloadsStatisticsController implements Initializable {
    public BorderPane booksMoreDownloads;
    public BarChart<String, Number> bcMoreDownloads;
    public CategoryAxis categoryAxis;
    public NumberAxis numberAxis;
    @FXML
    private Button btnVoltar;
    @FXML
    private DiliContext fsm;

    Label caption;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        booksMoreDownloads.sceneProperty().addListener((observableValue, oldScene, newScene) -> {
            if (newScene != null)
                fsm = (DiliContext) newScene.getUserData();

            if (fsm != null) {
                createViews();
                registerHandlers();
                update();
            }
        });
    }

    private void createViews() {

        btnVoltar.setGraphic(ImageManager.getImageView("left_arrow.png", 20));
        btnVoltar.setText("");

        caption = new Label("");
        caption.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, CornerRadii.EMPTY, Insets.EMPTY)));

        booksMoreDownloads.setBottom(caption);

    }

    private void registerHandlers() {
        fsm.addPropertyChangeListener(DiliContext.PROP_FASE, evt -> update());
        fsm.addPropertyChangeListener(DiliContext.PROP_BOOK, evt -> update());

        btnVoltar.setOnAction(ev -> fsm.voltar());
    }

    private void update() {

        booksMoreDownloads.setVisible(fsm != null && fsm.getState() == DiliState.STATISTICS);

        if(fsm.getState() == DiliState.STATISTICS){


            HashMap<String, Integer> bookMostDownloads = fsm.getData().statisticMostDownloadedBooks(5);

            numberAxis.setLabel("Downloads");
            categoryAxis.setLabel("Book Title");

            bcMoreDownloads.getData().clear();

            XYChart.Series<String, Number> series1 = new XYChart.Series<>();

            categoryAxis.getCategories().clear();
            categoryAxis.setCategories(FXCollections.observableArrayList(bookMostDownloads.keySet()));

            for (int i = 0; i < bookMostDownloads.size(); i++) {
                series1.getData().add(new XYChart.Data(bookMostDownloads.keySet().toArray()[i], bookMostDownloads.get(bookMostDownloads.keySet().toArray()[i])));
            }

            series1.setName("Number of Downloads");
            bcMoreDownloads.getData().add(series1);

            bcMoreDownloads.setOnMouseEntered(e -> {
                for (XYChart.Series<String, Number> series : bcMoreDownloads.getData()) {
                    for (final XYChart.Data<String, Number> data : series.getData()) {
                        data.getNode().setOnMouseEntered(ev -> {
                            caption.setTranslateX(ev.getSceneX() - ev.getSceneX() / 4 - caption.getLayoutX());
                            caption.setTranslateY(ev.getSceneY() - ev.getSceneY() / 4 - caption.getLayoutY());
                            caption.setText(Math.round((double) ((XYChart.Data<?, ?>) data).getYValue()) + " downloads");
                        });
                    }
                }
            });

        }


    }
}
