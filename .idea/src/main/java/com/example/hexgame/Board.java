package com.example.hexgame;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class Board {

    private final AnchorPane tileMap;
    private final int row, col;
    private final Player player1, player2;
    private final Label turnLabel;
    private boolean playerTurn = true;

    public Board(AnchorPane tileMap, int row, int col, Player player1, Player player2, Label turnLabel) {
        this.tileMap = tileMap;
        this.row = row;
        this.col = col;
        this.player1 = player1;
        this.player2 = player2;
        this.turnLabel = turnLabel;
        createTileMap();
    }

    private void createTileMap() {
        tileMap.getChildren().clear();

        double xBaslangicKaydirma = 40;
        double yBaslangicKaydirma = 40;

        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x++) {
                double xKoord = x * Utils.TILE_GENISLIGI + y * Utils.n + xBaslangicKaydirma;
                double yKoord = y * Utils.TILE_YUKSEKLIGI * 0.75 + yBaslangicKaydirma;

                Tile tile = new Tile(x, y, xKoord, yKoord, this);
                tileMap.getChildren().add(tile);
            }
        }
    }

    public void onTileClicked(Tile tile) {
        if (tile.isColored()) return;

        if (playerTurn) {
            tile.setFill(player1.getColor());
            tile.setPlayer(player1);
        } else {
            tile.setFill(player2.getColor());
            tile.setPlayer(player2);
        }
        playerTurn = !playerTurn;
        tile.setColored(true);
        tile.setDisable(true);

        String playerTurnText = playerTurn ? "Sıra: " + player1.getName() : "Sıra: " + player2.getName();
        turnLabel.setText(playerTurnText);

        if (checkWinCondition(tile.getPlayer())) {
            String winner = tile.getPlayer().getName() + " Kazandı!";
            turnLabel.setText(winner);
            disableAllTiles();
        }
    }

    private boolean checkWinCondition(Player player) {
        boolean[][] visited = new boolean[row][col];
        if (player == player1) {
            for (int x = 0; x < col; x++) {
                if (dfs(player, visited, x, 0)) {
                    Utils.showWinnerAnimation(tileMap, player.getName(), player.getColor());
                    return true;
                }
            }
        } else {
            for (int y = 0; y < row; y++) {
                if (dfs(player, visited, 0, y)) {
                    Utils.showWinnerAnimation(tileMap, player.getName(), player.getColor());
                    return true;
                }
            }
        }
        return false;
    }

    private boolean dfs(Player player, boolean[][] visited, int x, int y) {
        if (x < 0 || y < 0 || x >= col || y >= row || visited[y][x]) return false;

        Tile tile = getTile(x, y);
        if (tile == null || tile.getPlayer() != player) return false;

        visited[y][x] = true;

        if ((player == player1 && y == row - 1) || (player == player2 && x == col - 1)) {
            return true;
        }

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, -1}};
        for (int[] dir : directions) {
            if (dfs(player, visited, x + dir[0], y + dir[1])) {
                return true;
            }
        }
        return false;
    }

    private Tile getTile(int x, int y) {
        for (var node : tileMap.getChildren()) {
            if (node instanceof Tile) {
                Tile tile = (Tile) node;
                if (tile.getXIndex() == x && tile.getYIndex() == y) {
                    return tile;
                }
            }
        }
        return null;
    }

    private void disableAllTiles() {
        for (var node : tileMap.getChildren()) {
            if (node instanceof Tile) {
                node.setDisable(true);
            }
        }
    }
}
