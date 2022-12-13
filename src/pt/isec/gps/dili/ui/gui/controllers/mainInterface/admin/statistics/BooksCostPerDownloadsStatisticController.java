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
import pt.isec.gps.dili.model.data.book.BookPrice;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.model.fsm.DiliState;
import pt.isec.gps.dili.ui.gui.resources.ImageManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BooksCostPerDownloadsStatisticController implements Initializable {

    public BorderPane booksCostPerDownloadsStatistic;
    public BarChart bcHighestCost;
    public CategoryAxis categoryAxis;
    public NumberAxis numberAxis;
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


            List<BookPrice> bookHighestDebt = fsm.getData().statisticCostPerEachBook(5, "DESC");

            numberAxis.setLabel("Amount owed");
            categoryAxis.setLabel("Book Title");

            bcHighestCost.getData().clear();

            XYChart.Series<String, Number> series1 = new XYChart.Series<>();

            categoryAxis.getCategories().clear();

            List<String> booksNames = new ArrayList<>();

            for (var book : bookHighestDebt) {
                booksNames.add(book.getBook());
                series1.getData().add(new XYChart.Data(book.getBook(), book.getPrice()));
            }

            categoryAxis.setCategories(FXCollections.observableArrayList(booksNames));

            series1.setName("Amount owed");
            bcHighestCost.getData().add(series1);

            bcHighestCost.setOnMouseEntered(e -> {
                for (Object series : bcHighestCost.getData()) {
                    for (final XYChart.Data<String, Number> data : ((XYChart.Series<String, Number>) series).getData()) {
                        data.getNode().setOnMouseEntered(ev -> {
                            caption.setTranslateX(ev.getSceneX() - ev.getSceneX() / 4 - caption.getLayoutX());
                            caption.setTranslateY(ev.getSceneY() - ev.getSceneY() / 4 - caption.getLayoutY());
                            caption.setText(Math.round((double) ((XYChart.Data<?, ?>) data).getYValue()) + "€ owed");
                        });
                    }
                }
            });
        }
    }
}
