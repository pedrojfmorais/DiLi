package pt.isec.gps.dili.ui.gui.controllers.mainInterface.admin.statistics;

import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.model.fsm.DiliState;
import pt.isec.gps.dili.ui.gui.resources.ImageManager;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class BooksDownloadsTableStatisticController implements Initializable {
    public BorderPane booksDownloadsTableStatistic;
    public TableView tvBooks;
    @FXML
    private Button btnVoltar;
    @FXML
    private DiliContext fsm;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        booksDownloadsTableStatistic.sceneProperty().addListener((observableValue, oldScene, newScene) -> {
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

    }

    private void registerHandlers() {
        fsm.addPropertyChangeListener(DiliContext.PROP_FASE, evt -> update());
        fsm.addPropertyChangeListener(DiliContext.PROP_BOOK, evt -> update());

        btnVoltar.setOnAction(ev -> fsm.voltar());
    }

    private void update() {

        booksDownloadsTableStatistic.setVisible(fsm != null && fsm.getState() == DiliState.STATISTICS);

        if(fsm.getState() == DiliState.STATISTICS){

            tvBooks.getColumns().clear();

            HashMap<String, Integer> bookDownloads = fsm.getData().statisticAllDownloadedBooks();

            for (int i = 0; i < bookDownloads.size(); i++) {
                tvBooks.getItems().add(i);
            }

            TableColumn<Integer, String> tcBookTitle = new TableColumn<>("Book Title");
            tcBookTitle.setCellValueFactory(cellData -> {
                Integer rowIndex = cellData.getValue();
                return new ReadOnlyStringWrapper((String) bookDownloads.keySet().toArray()[rowIndex]);
            });

            TableColumn<Integer, Number> tcBookDownloads = new TableColumn<>("Book Title");
            tcBookDownloads.setCellValueFactory(cellData -> {
                Integer rowIndex = cellData.getValue();
                return new ReadOnlyIntegerWrapper(bookDownloads.get(bookDownloads.keySet().toArray()[rowIndex]));
            });

            tcBookTitle.setStyle("-fx-alignment: CENTER");
            tcBookDownloads.setStyle("-fx-alignment: CENTER");

            tvBooks.getColumns().addAll(tcBookTitle, tcBookDownloads);
        }


    }
}
