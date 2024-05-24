package com.example.hexgame;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HexGame extends Application {

    private Label turnLabel;
    private Board board;
    private Player player1, player2;
    private boolean playerTurn = true;

    @Override
    public void start(Stage primaryStage) {
        // Oyuncu isimlerini alın
        String player1Name = getPlayerName("Oyuncu 1");
        String player2Name = getPlayerName("Oyuncu 2");

        // Oyuncu renklerini alın
        List<javafx.scene.paint.Color> availableColors = new ArrayList<>(Utils.getAvailableColors());
        player1 = new Player(player1Name, Utils.getPlayerColor(player1Name, availableColors));
        availableColors.remove(player1.getColor());
        player2 = new Player(player2Name, Utils.getPlayerColor(player2Name, availableColors));

        AnchorPane tileMap = new AnchorPane();
        VBox root = new VBox();
        Scene scene = new Scene(root, 800, 600);

        // Hamle sırasını gösteren etiket
        turnLabel = new Label("Sıra: " + player1.getName());
        turnLabel.setAlignment(Pos.CENTER);

        Button btn5x5 = new Button("5x5");
        Button btn11x11 = new Button("11x11");
        Button btn17x17 = new Button("17x17");

        btn5x5.setOnAction(event -> createBoard(tileMap, 5, 5));
        btn11x11.setOnAction(event -> createBoard(tileMap, 11, 11));
        btn17x17.setOnAction(event -> createBoard(tileMap, 17, 17));

        Button howToPlayButton = new Button("Nasıl Oynanır?");
        howToPlayButton.setOnAction(event -> showHowToPlayDialog());

        root.getChildren().addAll(turnLabel, tileMap, btn5x5, btn11x11, btn17x17, howToPlayButton);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Hex Game");
        primaryStage.show();
    }

    private String getPlayerName(String player) {
        TextInputDialog dialog = new TextInputDialog(player);
        dialog.setTitle("Oyuncu Adı");
        dialog.setHeaderText(null);
        dialog.setContentText(player + " adını girin:");
        Optional<String> result = dialog.showAndWait();
        return result.orElse(player);
    }

    private void createBoard(AnchorPane tileMap, int row, int col) {
        board = new Board(tileMap, row, col, player1, player2, turnLabel);
        turnLabel.setText("Sıra: " + player1.getName());
    }

    private void showHowToPlayDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Nasıl Oynanır?");
        alert.setHeaderText(null);
        alert.setContentText(
                "HexGame, iki oyunculu bir oyundur. Oyunun amacı, altıgenlerden oluşan bir tahtada\n" +
                        "karşılıklı iki kenarı birleştiren bir yol oluşturarak diğer oyuncuya engel olmaktır.\n" +
                        "Sıranız geldiğinde bir altıgen seçin ve kendi renginizle doldurun.\n" +
                        "Bir yolu tamamladığınızda, oyunu kazanırsınız.\n" +
                        "İyi eğlenceler!"
        );
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
