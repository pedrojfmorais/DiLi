package pt.isec.gps.dili.ui.gui.controllers.mainInterface.admin.statistics;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class BookFormatStatisticsController implements Initializable {


    public BorderPane booksCostPerDownloadsStatistic;
    public PieChart pieChart;
    @FXML
    private Button btnVoltar;
    @FXML
    private DiliContext fsm;

    Label caption;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        booksCostPerDownloadsStatistic.sceneProperty().addListener((observableValue, oldScene, newScene) -> {
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

        booksCostPerDownloadsStatistic.setBottom(caption);

    }

    private void registerHandlers() {
        fsm.addPropertyChangeListener(DiliContext.PROP_FASE, evt -> update());
        fsm.addPropertyChangeListener(DiliContext.PROP_BOOK, evt -> update());

        btnVoltar.setOnAction(ev -> fsm.voltar());
    }

    private void update() {

        booksCostPerDownloadsStatistic.setVisible(fsm != null && fsm.getState() == DiliState.STATISTICS);

        if (fsm.getState() == DiliState.STATISTICS) {


            HashMap<String, Integer> statisticsFormats = fsm.getData().statisticsFormatDownloads();

            pieChart.getData().clear();

            ArrayList<PieChart.Data> dados = new ArrayList<>();

            for (int i = 0; i < statisticsFormats.size(); i++) {
                dados.add(new PieChart.Data(statisticsFormats.keySet().toArray()[i].toString(), statisticsFormats.get(statisticsFormats.keySet().toArray()[i])));
            }

            pieChart.setData(FXCollections.observableArrayList(dados));

            pieChart.setOnMouseEntered(e -> {
                for (var data : pieChart.getData()) {
                        data.getNode().setOnMouseEntered(ev -> {
                            caption.setTranslateX(ev.getSceneX() - ev.getSceneX() / 4 - caption.getLayoutX());
                            caption.setTranslateY(ev.getSceneY() - ev.getSceneY() / 4 - caption.getLayoutY());
                            caption.setText(Math.round(data.getPieValue()) + " downloads");
                        });
                    }

            });
        }
    }
}
