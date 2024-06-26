import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Director {
    private List<Level> levels = new ArrayList<Level>();
    private int levelNumber;
    private int currentLevel = 1;
    private Hobbit adventurer = new Hobbit();
    private Opponent opponent = getRandomOpponent();
    private Creature oppo = (Creature) opponent;
    private int totalValue;
    private String destination;
    private Shop shop = new Shop();
    private Stage stage = new Stage();

    public Director() {
    }

    public Director(int levelNumber, String destination) {
        initLevel(levelNumber);
        getTotalValue(levels);
        this.destination = destination;
        this.levelNumber = levelNumber;
    }

    // initialise the total levels
    private void initLevel(int levelnumber) {
        for (int i = 1; i <= levelnumber; i++) {
            levels.add(new Level(i));
        }
    }

    // get total value set in 12 levels
    private void getTotalValue(List<Level> levels) {
        for (Level level : levels) {
            for (Coin coin : level.treasure) {
                if (coin.isReal && level.isReward) {
                    totalValue += (2 * coin.faceValue);
                } else if (coin.isReal) {
                    totalValue += coin.faceValue;
                }
            }
        }
    }

    private static Opponent getRandomOpponent() {
        Random random = new Random();
        int opponentType = random.nextInt(3); // 0 for Dwarf, 1 for Elf, 2 for Orc

        switch (opponentType) {
            case 0:
                return new Dwarf();
            case 1:
                return new Elf();
            default:
                return new Orc();
        }
    }

    public void Gaming(Stage stage) throws IOException {
        this.stage = stage;
        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(1200, 820);
        pane.setOpacity(1);
        Button btAdSta = new Button("Adventurer State");
        Button btOpSta = new Button("Opponent State");
        Button btSho = new Button("Shop");
        Button btBa = new Button("Battle");
        Button btNe = new Button("Next");
        Font font = new Font("Arial", 30);
        Font font1 = new Font("Arial", 50);
        btAdSta.setFont(font);
        btOpSta.setFont(font);
        btSho.setFont(font);
        btBa.setFont(font);
        btNe.setFont(font);
        btAdSta.setTextFill(Color.ORANGERED);
        btOpSta.setTextFill(Color.ORANGERED);
        btSho.setTextFill(Color.ORANGERED);
        btBa.setTextFill(Color.ORANGERED);
        btNe.setTextFill(Color.ORANGERED);
        btAdSta.setPrefSize(350, 200);
        btOpSta.setPrefSize(350, 200);
        btSho.setPrefSize(300, 100);
        btBa.setPrefSize(200, 70);
        btNe.setPrefSize(200, 70);
        btAdSta.setLayoutX(25);
        btAdSta.setLayoutY(50);
        btOpSta.setLayoutX(780);
        btOpSta.setLayoutY(50);
        btSho.setLayoutX(430);
        btSho.setLayoutY(250);
        btBa.setLayoutX(100);
        btBa.setLayoutY(590);
        btNe.setLayoutX(880);
        btNe.setLayoutY(590);
        btNe.setDisable(true);
        btAdSta.setOnAction(event -> adventurer.getState());
        btOpSta.setOnAction(event -> opponent.getState());
        btSho.setOnAction(event -> {
            try {
                getShop();
            } catch (IOException e) {
            }
        });
        btBa.setOnAction(event -> {
            try {
                battle();
                btBa.setDisable(true);
                btNe.setDisable(false);
            } catch (IOException e) {
            }
        });
        btNe.setOnAction(event -> {
            try {
                next();
            } catch (IOException e) {
            }
        });
        Label levelLabel = new Label();
        Label destinationLabel = new Label();
        levelLabel.setFont(font1);
        destinationLabel.setFont(font1);
        levelLabel.setTextFill(Color.ORANGERED);
        destinationLabel.setTextFill(Color.ORANGERED);
        ImageView treasure = new ImageView("resources/Treasure Box.png");
        ImageView coin = new ImageView("resources/Real Cin.png");
        ImageView imAd = new ImageView("resources/Adventurer.png");
        ImageView imOp = new ImageView("resources/Opponent.png");
        treasure.setFitHeight(150);
        treasure.setFitWidth(200);
        coin.setFitHeight(150);
        coin.setFitWidth(150);
        imAd.setFitHeight(300);
        imAd.setFitWidth(350);
        imOp.setFitHeight(250);
        imOp.setFitWidth(250);
        imAd.setLayoutX(30);
        imAd.setLayoutY(280);
        imOp.setLayoutX(850);
        imOp.setLayoutY(280);
        treasure.setLayoutX(490);
        treasure.setLayoutY(390);
        coin.setLayoutX(510);
        coin.setLayoutY(395);
        levelLabel.setLayoutX(400);
        levelLabel.setLayoutY(50);
        destinationLabel.setLayoutX(311);
        destinationLabel.setLayoutY(590);

        if (currentLevel % 3 == 0) {
            btBa.setDisable(true);
            levelLabel.setText("Level " + currentLevel + "[Reward Level]");
            destinationLabel.setText("Destination: " + destination + "\n Levels remaining: "
                    + (levelNumber - currentLevel));
            pane.getChildren().addAll(btAdSta, btOpSta, btSho, btBa, btNe, imAd, imOp, treasure, levelLabel, destinationLabel);
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            stage.show();
            adventurer.setHealth(adventurer.getHealth() + 2);
            adventurer.addCoins(levels.get(currentLevel - 1).treasure);
            // the opponent can't come back to life
            if (oppo.getHealth() != 0) {
                oppo.setHealth(oppo.getHealth() + 2);
                oppo.addCoins(levels.get(currentLevel - 1).treasure);
            } // adventurer gets all the coins when opponent is dead
            else {
                adventurer.addCoins(levels.get(currentLevel - 1).treasure);
            }
            Parent root = FXMLLoader.load(getClass().getResource("AfterReward.fxml"));
            Scene scene1 = new Scene(root);
            Stage stage1 = new Stage();
            stage1.setScene(scene1);
            stage1.show();
            btNe.setDisable(false);
            if (adventurer.useItem2) {
                adventurer.removeItem(2);
                adventurer.useItem2 = false;
            }
            if (adventurer.useItem3) {
                adventurer.removeItem(3);
                adventurer.useItem3 = false;
            }// clear adventurer's item
        } else {
            levelLabel.setText("Level " + currentLevel);
            destinationLabel.setText("Destination: " + destination + "\n Levels remaining: "
                    + (levelNumber - currentLevel));
            pane.getChildren().addAll(btAdSta, btOpSta, btSho, btBa, btNe, imAd, imOp, coin, levelLabel, destinationLabel);
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            stage.show();
        }
    }

    private void battle() throws IOException {
        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(500, 200);
        Label lbWi = new Label();
        Label lbCo = new Label();
        Font font = new Font(40);
        Font font1 = new Font(20);
        lbWi.setFont(font);
        lbCo.setFont(font1);
        lbWi.setTextFill(Color.RED);
        lbCo.setTextFill(Color.BLACK);

        if ((opponent.shouldFight(adventurer, levels.get(currentLevel - 1))) == 1) {
            boolean Win = opponent.fight(adventurer, levels.get(currentLevel - 1));
            if (Win)
                lbWi.setText("Winner is:Adventurer");
            else
                lbWi.setText("Winner:" + oppo.getName());
            if (currentLevel != 1) {
                // check adventurer's injury
                adventurer.setHealth(adventurer.getHealth() + (adventurer.getHealth() <= 2 ? -1 : 1));
                oppo.setHealth(oppo.getHealth() + 1);
            }
        } else if (opponent.shouldFight(adventurer, levels.get(currentLevel - 1)) == 0) {
            lbWi.setText("Peace.Adventurer gets the coin.");
        } else
            lbWi.setText("Peace." + oppo.getName() + " gets the coin.");
        lbCo.setText("The coin is " + levels.get(currentLevel - 1).treasure.get(0).isReal);
        lbWi.setLayoutX(10);
        lbWi.setLayoutY(10);
        lbCo.setLayoutX(10);
        lbCo.setLayoutY(100);
        pane.getChildren().addAll(lbWi, lbCo);
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    private void next() throws IOException {
        if (adventurer.isAlive() && currentLevel < levelNumber) {
            currentLevel++;
            Gaming(stage);
        } else {
            AnchorPane pane = new AnchorPane();
            pane.setPrefSize(1200, 820);
            Font font = new Font(30);
            Font font1 = new Font(50);
            Button btAdSta = new Button("Adventurer State");
            Button btOpSta = new Button("Opponent State");
            Button btExit = new Button("Exit");
            btAdSta.setFont(font);
            btOpSta.setFont(font);
            btExit.setFont(font);
            btAdSta.setTextFill(Color.RED);
            btOpSta.setTextFill(Color.RED);
            btExit.setTextFill(Color.RED);
            btAdSta.setPrefSize(355, 293);
            btOpSta.setPrefSize(355, 293);
            btExit.setPrefSize(200, 100);
            btAdSta.setLayoutX(14);
            btAdSta.setLayoutY(264);
            btOpSta.setLayoutX(831);
            btOpSta.setLayoutY(260);
            btExit.setLayoutX(514);
            btExit.setLayoutY(651);
            btAdSta.setOnAction(event -> adventurer.getState());
            btOpSta.setOnAction(event -> opponent.getState());
            btExit.setOnAction(event -> System.exit(0));
            Label FinalWinner = new Label();
            Label lbSco = new Label("Score");
            Label Score = new Label();
            FinalWinner.setFont(font1);
            FinalWinner.setTextFill(Color.ORANGERED);
            lbSco.setFont(font1);
            lbSco.setTextFill(Color.ORANGERED);
            Score.setFont(font1);
            Score.setTextFill(Color.ORANGERED);
            FinalWinner.setLayoutX(180);
            FinalWinner.setLayoutY(50);
            lbSco.setLayoutX(540);
            lbSco.setLayoutY(160);
            Score.setLayoutX(540);
            Score.setLayoutY(290);
            if (adventurer.isAlive())
                FinalWinner.setText("Winner:Adventurer");
            else
                FinalWinner.setText("Winner:" + oppo.getName());
            if (totalValue == 0)
                Score.setText("0%");
            else {
                double adValue = (double) adventurer.getValue();
                double score = (adValue / totalValue) * 100;
                Score.setText(String.format("%.2f", score) + "%");
            }
            pane.getChildren().addAll(FinalWinner, lbSco, Score, btAdSta, btOpSta, btExit);
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void getShop() throws IOException {
        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(1000, 500);
        VBox btVbox = new VBox(7);
        btVbox.setPrefSize(200, 300);
        btVbox.setLayoutX(461);
        btVbox.setLayoutY(100);
        Label lbShop = new Label("Shop System");
        Font font1 = new Font(30);
        lbShop.setFont(font1);
        lbShop.setLayoutX(430);
        lbShop.setLayoutY(25);
        ObservableList<Item> list = FXCollections.observableArrayList();
        List<Item> list1 = new ArrayList<>();
        list1.add(new Item(1, "Life Potion", "2", "Increases 2 life"));
        list1.add(new Item(2, "Crit Potion", "2", "Increases injury to 3"));
        list1.add(new Item(3, "Evasion Charm", "3", "Avoid injury"));
        list.addAll(list1);
        TableView<Item> tableView = new TableView<>();
        tableView.setItems(list);
        TableColumn<Item, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Item, String> effCol = new TableColumn<>("Effect");
        effCol.setCellValueFactory(new PropertyValueFactory<>("effect"));

        TableColumn<Item, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Item, Item> buttonColumn = new TableColumn<>("Button");
        Button btLi = new Button("Buy it");
        Button btCr = new Button("Buy it");
        Button btCh = new Button("Buy it");
        Font font = new Font(9);
        btLi.setFont(font);
        btCr.setFont(font);
        btCh.setFont(font);
        btLi.setTextFill(Color.RED);
        btCr.setTextFill(Color.RED);
        btCh.setTextFill(Color.RED);
        btLi.setOnAction(event -> {
            try {
                if (buyLife()) {
                    btCh.setDisable(true);
                    btCr.setDisable(true);
                }
            } catch (IOException e) {
            }
        });
        btCr.setOnAction(event -> {
            try {
                if (buyCrit()) {
                    btLi.setDisable(true);
                    btCh.setDisable(true);
                }
            } catch (IOException e) {
            }
        });
        btCh.setOnAction(event -> {
            try {
                if (buyCharm()) {
                    btLi.setDisable(true);
                    btCr.setDisable(true);
                }
            } catch (IOException e) {
            }
        });
        tableView.setEditable(true);
        tableView.getColumns().addAll(nameCol, priceCol, effCol,buttonColumn);
        btVbox.getChildren().addAll(btLi, btCr, btCh);
        tableView.setPrefSize(700, 350);
        tableView.setLayoutX(170);
        tableView.setLayoutY(75);
        pane.getChildren().addAll(tableView, btVbox, lbShop);
        Scene scene = new Scene(pane);
        Stage stage1 = new Stage();
        stage1.setScene(scene);
        stage1.show();
    }

    public static class Item {
        private int id;
        private StringProperty name;
        private StringProperty price;
        private StringProperty effect;
        private BooleanProperty isAvailable;

        public Item(int id, String name, String price, String effect) {
            this.id = id;
            this.name = new SimpleStringProperty(name);
            this.price = new SimpleStringProperty(price);
            this.effect = new SimpleStringProperty(effect);
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name.get();
        }

        public String getEffect() {
            return effect.get();
        }

        public String getPrice() {
            return price.get();
        }

        @Override
        public String toString() {
            String output = "Item: " + this.getName() + ",ID: " + this.getId() + " ,price: " + this.getPrice()
                    + " ,Is available: " + this.isAvailable;
            return output;
        }
    }

    public boolean buyLife() throws IOException {
        List<Coin> payway1 = new ArrayList<>();
        List<Coin> payway2 = new ArrayList<>();
        payway1.add(new Coin(2, true));
        payway2.add(new Coin(1, true));
        payway2.add(new Coin(1, true));
        if (!shop.items.get(0).isAvailable()) {
            Parent root = FXMLLoader.load(getClass().getResource("noItemAlert.fxml"));
            Scene scene = new Scene(root);
            Stage stage1 = new Stage();
            stage1.setScene(scene);
            stage1.show();
            return false;
        } else if (adventurer.getValue() < 2) {
            Parent root = FXMLLoader.load(getClass().getResource("noMoneyAlert.fxml"));
            Scene scene = new Scene(root);
            Stage stage1 = new Stage();
            stage1.setScene(scene);
            stage1.show();
            return false;
        } // adventurer could buy it successfully
        else {
            if (adventurer.coins.containsAll(payway1)) {
                adventurer.coins.removeAll(payway1);
            } else {
                adventurer.coins.removeAll(payway2);
            }
            // add certain item to adventurer
            adventurer.items.add(shop.items.get(0));
            shop.items.get(0).setIs(false);
            return true;
        }
    }

    public boolean buyCrit() throws IOException {
        List<Coin> payway1 = new ArrayList<>();
        List<Coin> payway2 = new ArrayList<>();
        List<Coin> change = new ArrayList<>();
        payway1.add(new Coin(2, true));
        payway2.add(new Coin(1, true));
        payway2.add(new Coin(1, true));
        change.add(new Coin(1, true));
        if (!shop.items.get(1).isAvailable()) {
            Parent root = FXMLLoader.load(getClass().getResource("noItemAlert.fxml"));
            Scene scene = new Scene(root);
            Stage stage1 = new Stage();
            stage1.setScene(scene);
            stage1.show();
            return false;
        } else if (adventurer.getValue() < 2) {
            Parent root = FXMLLoader.load(getClass().getResource("noMoneyAlert.fxml"));
            Scene scene = new Scene(root);
            Stage stage1 = new Stage();
            stage1.setScene(scene);
            stage1.show();
            return false;
        } // adventurer could buy it successfully
        else {
            if (adventurer.coins.containsAll(payway1)) {
                adventurer.coins.removeAll(payway1);
            } else {
                adventurer.coins.removeAll(payway2);
            }
            // add certain item to adventurer
            adventurer.items.add(shop.items.get(1));
            shop.items.get(1).setIs(false);
            return true;
        }
    }

    public boolean buyCharm() throws IOException {
        List<Coin> payway1 = new ArrayList<>();
        List<Coin> payway2 = new ArrayList<>();
        List<Coin> payway3 = new ArrayList<>();
        List<Coin> change = new ArrayList<>();
        payway1.add(new Coin(2, true));
        payway1.add(new Coin(1, true));
        payway2.add(new Coin(1, true));
        payway2.add(new Coin(1, true));
        payway2.add(new Coin(1, true));
        payway3.add(new Coin(2, true));
        payway3.add(new Coin(2, true));
        change.add(new Coin(1, true));
        if (!shop.items.get(2).isAvailable()) {
            Parent root = FXMLLoader.load(getClass().getResource("noItemAlert.fxml"));
            Scene scene = new Scene(root);
            Stage stage1 = new Stage();
            stage1.setScene(scene);
            stage1.show();
            return false;
        } else if (adventurer.getValue() < 3) {
            Parent root = FXMLLoader.load(getClass().getResource("noMoneyAlert.fxml"));
            Scene scene = new Scene(root);
            Stage stage1 = new Stage();
            stage1.setScene(scene);
            stage1.show();
            return false;
        } // adventurer could buy it successfully
        else {
            if (adventurer.coins.containsAll(payway1)) {
                adventurer.coins.removeAll(payway1);
            } else if (adventurer.coins.containsAll(payway2)) {
                adventurer.coins.removeAll(payway2);
            } else {
                adventurer.coins.removeAll(payway3);
                adventurer.addCoins(change);
            }
            // add certain item to adventurer
            adventurer.items.add(shop.items.get(2));
            shop.items.get(2).setIs(false);
            return true;
        }
    }

}

