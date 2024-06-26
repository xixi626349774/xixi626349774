import javafx.beans.property.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Level {
    private int number;
    public List<Coin> treasure = new ArrayList<>();
    public boolean isReward = false;

    public Level(int number) {
        Random random = new Random();
        double rand = random.nextDouble();
        if (number % 3 == 0) {                       // reward level
            if (rand <= 0.5) {
                treasure.add(new Coin(2, true));
            } else {
                treasure.add(new Coin(1, true));
                treasure.add(new Coin(1, true));
            }
            this.isReward = true;
        } else {                                     // ordinary level
            if (rand <= 0.7) {
                treasure.add(new Coin(1, true));
            } else {
                treasure.add(new Coin(1, false));
            }
        }
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

}

class Coin {
    public int faceValue;
    public boolean isReal;

    public Coin(int faceValue, boolean isReal) {
        this.faceValue = faceValue;
        this.isReal = isReal;
    }

    public int getFaceValue() {
        return faceValue;
    }

    public boolean isReal() {
        return isReal;
    }

    @Override
    public String toString() {
        String output = "You have a coin whose facevalue is " + this.faceValue + ",and it's a " + this.isReal + " one.";
        return output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Coin coin = (Coin) o;
        return faceValue == coin.faceValue && isReal == coin.isReal;
    }
}

class Creature {
    private String name;
    private int health;
    protected List<Coin> coins = new ArrayList<>();
    protected double winOdds;

    public Creature(String name, int health) {
        this.name = name;
        this.health = health;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void getCoins() {
        for (Coin coin : this.coins) {
            coin.toString();
        }
    }

    public void addCoins(List<Coin> newCoin) {
        this.coins.addAll(newCoin);
    }

    public int getValue() {
        int value = 0;
        for (Coin coin : this.coins) {
            if (coin.isReal)
                value += coin.getFaceValue();
        }
        return value;
    }

    // Method to check if the creature is alive
    public boolean isAlive() {
        return health > 0;
    }
}

class Hobbit extends Creature {
    public List<Item> items = new ArrayList<>();
    public boolean useItem2 = false;
    public boolean useItem3 = false;
    public boolean useItem1 = false;

    public Hobbit() {
        super("Hobbit", randomLifeValue());
    }

    private static int randomLifeValue() {
        Random rand = new Random();
        return rand.nextInt(5) + 5; // Random life value 5-9
    }

    public boolean equipItem(int itemID) {
        for (Item item : items) {
            if (item.getId() == itemID)
                return true;
        }
        return false;
    }

    public void removeItem(int itemID) {
        for (Item item : items) {
            if (item.getId() == itemID)
                items.remove(item);
        }
    }

    public void getState() {
        int realbig = 0;
        int fakebig = 0;
        int realsmall = 0;
        int fakesmall = 0;
        for (Coin coin : coins) {
            if (coin.isReal && coin.getFaceValue() == 2)
                realbig++;
            else if (coin.getFaceValue() == 2)
                fakebig++;
            else if (coin.isReal)
                realsmall++;
            else
                fakesmall++;
        }
        Label lbLi = new Label("Life:");
        Label lbCo = new Label("Coins:");
        Label lbIt = new Label("Items:");
        Font font = new Font("Arial", 50);
        lbLi.setFont(font);
        lbCo.setFont(font);
        lbIt.setFont(font);
        lbLi.setTextFill(Color.YELLOWGREEN);
        lbCo.setTextFill(Color.YELLOWGREEN);
        lbIt.setTextFill(Color.YELLOWGREEN);
        lbLi.setLayoutX(20);
        lbCo.setLayoutX(20);
        lbIt.setLayoutX(20);
        lbLi.setLayoutY(25);
        lbCo.setLayoutY(230);
        lbIt.setLayoutY(350);
        HBox redHeartHbox = new HBox(10);
        HBox redHeartHbox1 = new HBox(10);
        HBox AdventurerCoins = new HBox(10);
        redHeartHbox.setLayoutX(120);
        redHeartHbox.setLayoutY(25);
        redHeartHbox1.setLayoutX(120);
        redHeartHbox1.setLayoutY(100);
        AdventurerCoins.setLayoutX(180);
        AdventurerCoins.setLayoutY(236);
        AnchorPane pane = new AnchorPane();
        pane.setOpacity(1);
        pane.setPrefSize(1200, 820);
        Stage stage = new Stage();
        if (getHealth() <= 15) {
            for (int i = 0; i < getHealth(); i++) {
                ImageView heart = new ImageView(new javafx.scene.image.Image("resources/Red Heart.png"));
                heart.setFitHeight(70);
                heart.setFitWidth(70);
                redHeartHbox.getChildren().add(heart);
            }
        } else {
            for (int i = 0; i < 15; i++) {
                ImageView heart = new ImageView(new javafx.scene.image.Image("resources/Red Heart.png"));
                heart.setFitHeight(70);
                heart.setFitWidth(70);
                redHeartHbox.getChildren().add(heart);
            }
            for (int i = 0; i < getHealth() - 15; i++) {
                ImageView heart = new ImageView(new javafx.scene.image.Image("resources/Red Heart.png"));
                heart.setFitHeight(70);
                heart.setFitWidth(70);
                redHeartHbox1.getChildren().add(heart);
            }
        }
        for (int i = 0; i < realbig; i++) {
            ImageView realCoin = new ImageView(new javafx.scene.image.Image("resources/Real Cin.png"));
            realCoin.setFitHeight(70);
            realCoin.setFitWidth(70);
            AdventurerCoins.getChildren().add(realCoin);
        }

        for (int i = 0; i < fakebig; i++) {
            ImageView fakeCoin = new ImageView(new javafx.scene.image.Image("resources/Fake Coin.png"));
            fakeCoin.setFitHeight(70);
            fakeCoin.setFitWidth(70);
            AdventurerCoins.getChildren().add(fakeCoin);
        }

        for (int i = 0; i < realsmall; i++) {
            ImageView realCoin = new ImageView(new javafx.scene.image.Image("resources/Real Cin.png"));
            realCoin.setFitHeight(50);
            realCoin.setFitWidth(50);
            AdventurerCoins.getChildren().add(realCoin);
        }

        for (int i = 0; i < fakesmall; i++) {
            ImageView fakeCoin = new ImageView(new javafx.scene.image.Image("resources/Fake Coin.png"));
            fakeCoin.setFitHeight(50);
            fakeCoin.setFitWidth(50);
            AdventurerCoins.getChildren().add(fakeCoin);
        }

        for (Item item : items) {
            if (item.getId() == 1) {
                ImageView lifePOtion = new ImageView(new javafx.scene.image.Image("resources/Life Potion.png"));
                lifePOtion.setFitHeight(250);
                lifePOtion.setFitWidth(250);
                lifePOtion.setX(120);
                lifePOtion.setY(400);
                Font font1 = new Font("Arial", 20);
                Button btLi = new Button();
                btLi.setText("Life Potion");
                btLi.setFont(font1);
                btLi.setTextFill(Color.ORANGERED);
                btLi.setPrefSize(230, 70);
                btLi.setLayoutX(130);
                btLi.setLayoutY(700);
                pane.getChildren().addAll(lifePOtion, btLi);
                btLi.setOnAction(event -> {
                    try {
                        useLi();
                        if (useItem1)
                            pane.getChildren().removeAll(lifePOtion, btLi);
                            useItem1 = false;
                            Scene scene1 = new Scene(pane);
                            stage.setScene(scene1);
                            stage.show();
                    } catch (IOException e) {
                    }
                });
            } else if (item.getId() == 2) {
                ImageView critPotion = new ImageView(new javafx.scene.image.Image("resources/Crit Potion.png"));
                critPotion.setFitHeight(250);
                critPotion.setFitWidth(250);
                critPotion.setX(490);
                critPotion.setY(400);
                Font font1 = new Font("Arial", 20);
                Button btCr = new Button();
                btCr.setText("Crit Potion");
                btCr.setFont(font1);
                btCr.setTextFill(Color.ORANGERED);
                btCr.setPrefSize(230, 70);
                btCr.setLayoutX(500);
                btCr.setLayoutY(700);
                pane.getChildren().addAll(critPotion, btCr);
                btCr.setOnAction(event1 -> {
                    try {
                        useCr();
                        if (useItem2)
                            pane.getChildren().removeAll(critPotion, btCr);
                            Scene scene1 = new Scene(pane);
                            stage.setScene(scene1);
                            stage.show();
                    } catch (IOException e) {
                    }
                });
            } else if (item.getId() == 3) {
                ImageView charm = new ImageView(new Image("resources/curse.png"));
                charm.setFitHeight(250);
                charm.setFitWidth(250);
                charm.setX(840);
                charm.setY(400);
                Font font1 = new Font("Arial", 20);
                Button btCh = new Button();
                btCh.setText("Evasion Charm");
                btCh.setFont(font1);
                btCh.setTextFill(Color.ORANGERED);
                btCh.setPrefSize(230, 70);
                btCh.setLayoutX(847);
                btCh.setLayoutY(700);
                pane.getChildren().addAll(charm, btCh);
                btCh.setOnAction(event1 -> {
                    try {
                        useCh();
                        if (useItem3)
                            pane.getChildren().removeAll(charm, btCh);
                        Scene scene1 = new Scene(pane);
                        stage.setScene(scene1);
                        stage.show();
                    } catch (IOException e) {
                    }
                });
            }
        }
        pane.getChildren().addAll(lbLi, lbCo, lbIt, redHeartHbox, redHeartHbox1, AdventurerCoins);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    private void useLi() throws IOException {
        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(600, 300);
        Label lbEf = new Label("Life Potion increases adventurer life by 2");
        Label lbSu = new Label("Are you sure to use?");
        Font font = new Font(40);
        Font font1 = new Font(20);
        lbEf.setFont(font);
        lbSu.setFont(font1);
        lbEf.setTextFill(Color.YELLOWGREEN);
        lbSu.setTextFill(Color.BLACK);
        Button btSu = new Button("sure to use");
        btSu.setFont(font1);
        lbEf.setLayoutX(10);
        lbEf.setLayoutY(10);
        lbSu.setLayoutX(10);
        lbSu.setLayoutY(100);
        btSu.setPrefSize(250, 50);
        btSu.setLayoutX(350);
        btSu.setLayoutY(230);
        Stage stage = new Stage();
        btSu.setOnAction(event -> {
            setHealth(getHealth() + 2);
            removeItem(1);
            useItem1 = true;
            stage.close();
        });
        pane.getChildren().addAll(lbEf, lbSu, btSu);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    private void useCr() throws IOException {
        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(700, 300);
        Label lbEf = new Label("Crit Potion increases the injury for opponent to 3");
        Label lbSu = new Label("Are you sure to use?");
        Font font = new Font(40);
        Font font1 = new Font(20);
        lbEf.setFont(font);
        lbSu.setFont(font1);
        lbEf.setTextFill(Color.YELLOWGREEN);
        lbSu.setTextFill(Color.BLACK);
        Button btSu = new Button("sure to use");
        btSu.setFont(font1);
        lbEf.setLayoutX(10);
        lbEf.setLayoutY(10);
        lbSu.setLayoutX(10);
        lbSu.setLayoutY(100);
        btSu.setPrefSize(250, 50);
        btSu.setLayoutX(400);
        btSu.setLayoutY(230);
        Stage stage = new Stage();
        btSu.setOnAction(event -> {
            useItem2 = true;
            stage.close();
        });
        pane.getChildren().addAll(lbEf, lbSu, btSu);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    private void useCh() throws IOException {
        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(600, 300);
        Label lbEf = new Label("Evasion Charm prevents adventurer from injury");
        Label lbSu = new Label("Are you sure to use?");
        Font font = new Font(40);
        Font font1 = new Font(20);
        lbEf.setFont(font);
        lbSu.setFont(font1);
        lbEf.setTextFill(Color.YELLOWGREEN);
        lbSu.setTextFill(Color.BLACK);
        Button btSu = new Button("sure to use");
        btSu.setFont(font1);
        lbEf.setLayoutX(10);
        lbEf.setLayoutY(10);
        lbSu.setLayoutX(10);
        lbSu.setLayoutY(100);
        btSu.setPrefSize(250, 50);
        btSu.setLayoutX(350);
        btSu.setLayoutY(230);
        Stage stage = new Stage();
        btSu.setOnAction(event -> {
            useItem3 = true;
            stage.close();
        });
        pane.getChildren().addAll(lbEf, lbSu, btSu);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }
}

interface Opponent {
    int shouldFight(Hobbit adventurer, Level level);

    boolean fight(Hobbit adventurer, Level level);

    void getState();
}

class Elf extends Creature implements Opponent {
    public Elf() {
        super("Elf", 7);
        winOdds = 0.8;
    }

    @Override
    public int shouldFight(Hobbit adventurer, Level level) {
        // when opponent is dead
        if (this.getHealth() == 0) {
            adventurer.setHealth(adventurer.getHealth() + (adventurer.getHealth() <= 2 ? -1 : 1));
            adventurer.addCoins(level.treasure);
            if (adventurer.equipItem(3) && adventurer.useItem3) {
                adventurer.removeItem(3);
                adventurer.useItem3 = false;
            }
            if (adventurer.equipItem(2) && adventurer.useItem2) {
                adventurer.removeItem(2);
                adventurer.useItem2 = false;
            }//clear the adventurer's items
            return 0;
        } else if (adventurer.getHealth() >= 2) {
            return 1;
        } else {
            this.addCoins(level.treasure);
            // due to the injury
            adventurer.setHealth(adventurer.getHealth() - 1);
            this.setHealth(this.getHealth() + 1);
            if (adventurer.equipItem(3) && adventurer.useItem3) {
                adventurer.removeItem(3);
                adventurer.useItem3 = false;
            }
            if (adventurer.equipItem(2) && adventurer.useItem2) {
                adventurer.removeItem(2);
                adventurer.useItem2 = false;
            }//clear the adventurer's items
            return -1;
        }
    }

    @Override
    public boolean fight(Hobbit adventurer, Level level) {
        Random random = new Random();
        double rand = random.nextDouble();
        if (rand <= winOdds) {
            // realise the effect of Evasion Charm
            if (adventurer.equipItem(3) && adventurer.useItem3) {
                adventurer.removeItem(3);
                adventurer.useItem3 = false;
            } else {
                adventurer.setHealth(adventurer.getHealth() - 2);
            }
            this.addCoins(level.treasure);
            return false;
        } else {
            // realise the effect of Crit Potion
            if (adventurer.equipItem(2) && adventurer.useItem2) {
                this.setHealth(this.getHealth() - 3);
                adventurer.removeItem(2);
                adventurer.useItem2 = false;
            } else {
                this.setHealth(this.getHealth() - 2);
            }
            adventurer.addCoins(level.treasure);
            return true;
        }
    }

    public void getState() {
        int realbig = 0;
        int fakebig = 0;
        int realsmall = 0;
        int fakesmall = 0;
        for (Coin coin : coins) {
            if (coin.isReal && coin.getFaceValue() == 2)
                realbig++;
            else if (coin.getFaceValue() == 2)
                fakebig++;
            else if (coin.isReal)
                realsmall++;
            else
                fakesmall++;
        }
        AnchorPane pane = new AnchorPane();
        pane.setOpacity(1);
        pane.setPrefSize(1200, 820);
        HBox greyHeartHbox = new HBox(10);
        HBox greyHeartHbox1 = new HBox(10);
        HBox OpponentCoins = new HBox(10);
        Label lbLi = new Label("Life:");
        Label lbCo = new Label("Coins:");
        Font font = new Font("Arial", 50);
        lbLi.setFont(font);
        lbCo.setFont(font);
        lbLi.setTextFill(Color.YELLOWGREEN);
        lbCo.setTextFill(Color.YELLOWGREEN);
        lbLi.setLayoutX(20);
        lbLi.setLayoutY(100);
        lbCo.setLayoutX(20);
        lbCo.setLayoutY(450);
        greyHeartHbox.setLayoutX(120);
        greyHeartHbox.setLayoutY(100);
        greyHeartHbox1.setLayoutX(120);
        greyHeartHbox1.setLayoutY(180);
        OpponentCoins.setLayoutX(180);
        OpponentCoins.setLayoutY(459);
        if (getHealth() <= 15) {
            for (int i = 0; i < getHealth(); i++) {
                ImageView heart = new ImageView(new Image("resources/Grey Heart.png"));
                heart.setFitHeight(70);
                heart.setFitWidth(70);
                greyHeartHbox.getChildren().add(heart);
            }
        } else {
            for (int i = 0; i < 15; i++) {
                ImageView heart = new ImageView(new Image("resources/Grey Heart.png"));
                heart.setFitHeight(70);
                heart.setFitWidth(70);
                greyHeartHbox.getChildren().add(heart);
            }
            for (int i = 0; i < getHealth() - 15; i++) {
                ImageView heart = new ImageView(new Image("resources/Grey Heart.png"));
                heart.setFitHeight(70);
                heart.setFitWidth(70);
                greyHeartHbox1.getChildren().add(heart);
            }
        }
        for (int i = 0; i < realbig; i++) {
            ImageView realCoin = new ImageView(new Image("resources/Real Cin.png"));
            realCoin.setFitHeight(70);
            realCoin.setFitWidth(70);
            OpponentCoins.getChildren().add(realCoin);
        }

        for (int i = 0; i < fakebig; i++) {
            ImageView fakeCoin = new ImageView(new Image("resources/Fake Coin.png"));
            fakeCoin.setFitHeight(70);
            fakeCoin.setFitWidth(70);
            OpponentCoins.getChildren().add(fakeCoin);
        }

        for (int i = 0; i < realsmall; i++) {
            ImageView realCoin = new ImageView(new Image("resources/Real Cin.png"));
            realCoin.setFitHeight(50);
            realCoin.setFitWidth(50);
            OpponentCoins.getChildren().add(realCoin);
        }

        for (int i = 0; i < fakesmall; i++) {
            ImageView fakeCoin = new ImageView(new Image("resources/Fake Coin.png"));
            fakeCoin.setFitHeight(50);
            fakeCoin.setFitWidth(50);
            OpponentCoins.getChildren().add(fakeCoin);
        }
        pane.getChildren().addAll(lbLi, lbCo, greyHeartHbox, greyHeartHbox1, OpponentCoins);
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}

class Dwarf extends Creature implements Opponent {
    public Dwarf() {
        super("Dwarf", 9);
        winOdds = 0.5;
    }

    @Override
    public int shouldFight(Hobbit adventurer, Level level) {
        // when opponent is dead
        if (this.getHealth() == 0) {
            adventurer.setHealth(adventurer.getHealth() + (adventurer.getHealth() <= 2 ? -1 : 1));
            adventurer.addCoins(level.treasure);
            return 0;
        } else if (adventurer.getHealth() >= 2 && this.getHealth() > 2) {
            return 1;
        } else if (this.getHealth() > 2) {
            this.addCoins(level.treasure);
            // due to the injury
            adventurer.setHealth(adventurer.getHealth() - 1);
            this.setHealth(this.getHealth() + 1);
            return -1;
        } else {
            adventurer.addCoins(level.treasure);
            //check adventurer's injury
            adventurer.setHealth(adventurer.getHealth() + (adventurer.getHealth() <= 2 ? -1 : 1));
            this.setHealth(this.getHealth() + 1);
            return 0;
        }
    }

    @Override
    public boolean fight(Hobbit adventurer, Level level) {
        Random random = new Random();
        double rand = random.nextDouble();
        if (rand <= winOdds) {
            // realise the effect of Evasion Charm
            if (adventurer.equipItem(3) && adventurer.useItem3) {
                adventurer.removeItem(3);
                adventurer.useItem3 = false;
            } else {
                adventurer.setHealth(adventurer.getHealth() - 2);
            }
            this.addCoins(level.treasure);
            return false;
        } else {
            // realise the effect of Crit Potion
            if (adventurer.equipItem(2) && adventurer.useItem2) {
                this.setHealth(this.getHealth() - 3);
                adventurer.removeItem(2);
                adventurer.useItem2 = false;
            } else {
                this.setHealth(this.getHealth() - 2);
            }
            adventurer.addCoins(level.treasure);
            return true;
        }
    }

    public void getState() {
        int realbig = 0;
        int fakebig = 0;
        int realsmall = 0;
        int fakesmall = 0;
        for (Coin coin : coins) {
            if (coin.isReal && coin.getFaceValue() == 2)
                realbig++;
            else if (coin.getFaceValue() == 2)
                fakebig++;
            else if (coin.isReal)
                realsmall++;
            else
                fakesmall++;
        }
        AnchorPane pane = new AnchorPane();
        pane.setOpacity(1);
        pane.setPrefSize(1200, 820);
        HBox greyHeartHbox = new HBox(10);
        HBox greyHeartHbox1 = new HBox(10);
        HBox OpponentCoins = new HBox(10);
        Label lbLi = new Label("Life:");
        Label lbCo = new Label("Coins:");
        Font font = new Font("Arial", 50);
        lbLi.setFont(font);
        lbCo.setFont(font);
        lbLi.setTextFill(Color.YELLOWGREEN);
        lbCo.setTextFill(Color.YELLOWGREEN);
        lbLi.setLayoutX(20);
        lbLi.setLayoutY(100);
        lbCo.setLayoutX(20);
        lbCo.setLayoutY(450);
        greyHeartHbox.setLayoutX(120);
        greyHeartHbox.setLayoutY(100);
        greyHeartHbox1.setLayoutX(120);
        greyHeartHbox1.setLayoutY(180);
        OpponentCoins.setLayoutX(180);
        OpponentCoins.setLayoutY(459);
        if (getHealth() <= 15) {
            for (int i = 0; i < getHealth(); i++) {
                ImageView heart = new ImageView(new Image("resources/Grey Heart.png"));
                heart.setFitHeight(70);
                heart.setFitWidth(70);
                greyHeartHbox.getChildren().add(heart);
            }
        } else {
            for (int i = 0; i < 15; i++) {
                ImageView heart = new ImageView(new Image("resources/Grey Heart.png"));
                heart.setFitHeight(70);
                heart.setFitWidth(70);
                greyHeartHbox.getChildren().add(heart);
            }
            for (int i = 0; i < getHealth() - 15; i++) {
                ImageView heart = new ImageView(new Image("resources/Grey Heart.png"));
                heart.setFitHeight(70);
                heart.setFitWidth(70);
                greyHeartHbox1.getChildren().add(heart);
            }
        }
        for (int i = 0; i < realbig; i++) {
            ImageView realCoin = new ImageView(new Image("resources/Real Cin.png"));
            realCoin.setFitHeight(70);
            realCoin.setFitWidth(70);
            OpponentCoins.getChildren().add(realCoin);
        }

        for (int i = 0; i < fakebig; i++) {
            ImageView fakeCoin = new ImageView(new Image("resources/Fake Coin.png"));
            fakeCoin.setFitHeight(70);
            fakeCoin.setFitWidth(70);
            OpponentCoins.getChildren().add(fakeCoin);
        }

        for (int i = 0; i < realsmall; i++) {
            ImageView realCoin = new ImageView(new Image("resources/Real Cin.png"));
            realCoin.setFitHeight(50);
            realCoin.setFitWidth(50);
            OpponentCoins.getChildren().add(realCoin);
        }

        for (int i = 0; i < fakesmall; i++) {
            ImageView fakeCoin = new ImageView(new Image("resources/Fake Coin.png"));
            fakeCoin.setFitHeight(50);
            fakeCoin.setFitWidth(50);
            OpponentCoins.getChildren().add(fakeCoin);
        }
        pane.getChildren().addAll(lbLi, lbCo, greyHeartHbox, greyHeartHbox1, OpponentCoins);
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}

class Orc extends Creature implements Opponent {
    public Orc() {
        super("Orc", 8);
    }

    @Override
    public int shouldFight(Hobbit adventurer, Level level) {
        // when opponent is dead
        if (this.getHealth() == 0) {
            adventurer.setHealth(adventurer.getHealth() + (adventurer.getHealth() <= 2 ? -1 : 1));
            adventurer.addCoins(level.treasure);
            return 0;
        } else if (adventurer.getHealth() >= 2) {
            return 1;
        } else {
            this.addCoins(level.treasure);
            // due to the injury
            adventurer.setHealth(adventurer.getHealth() - 1);
            this.setHealth(this.getHealth() + 1);
            return -1;
        }
    }

    @Override
    public boolean fight(Hobbit adventurer, Level level) {
        Random random = new Random();
        double rand = random.nextDouble();
        if (this.getHealth() > adventurer.getHealth()) {
            winOdds = 0.6;
        } else if (this.getHealth() == adventurer.getHealth()) {
            winOdds = 0.4;
        } else {
            winOdds = 0.3;
        }
        if (rand <= winOdds) {
            // realise the effect of Evasion Charm
            if (adventurer.equipItem(3) && adventurer.useItem3) {
                adventurer.removeItem(3);
                adventurer.useItem3 = false;
            } else {
                adventurer.setHealth(adventurer.getHealth() - 2);
            }
            this.addCoins(level.treasure);
            return false;
        } else {
            // realise the effect of Crit Potion
            if (adventurer.equipItem(2) && adventurer.useItem2) {
                this.setHealth(this.getHealth() - 3);
                adventurer.removeItem(2);
                adventurer.useItem2 = false;
            } else {
                this.setHealth(this.getHealth() - 2);
            }
            adventurer.addCoins(level.treasure);
            return true;
        }
    }

    public void getState() {
        int realbig = 0;
        int fakebig = 0;
        int realsmall = 0;
        int fakesmall = 0;
        for (Coin coin : coins) {
            if (coin.isReal && coin.getFaceValue() == 2)
                realbig++;
            else if (coin.getFaceValue() == 2)
                fakebig++;
            else if (coin.isReal)
                realsmall++;
            else
                fakesmall++;
        }
        AnchorPane pane = new AnchorPane();
        pane.setOpacity(1);
        pane.setPrefSize(1200, 820);
        HBox greyHeartHbox = new HBox(10);
        HBox greyHeartHbox1 = new HBox(10);
        HBox OpponentCoins = new HBox(10);
        Label lbLi = new Label("Life:");
        Label lbCo = new Label("Coins:");
        Font font = new Font("Arial", 50);
        lbLi.setFont(font);
        lbCo.setFont(font);
        lbLi.setTextFill(Color.YELLOWGREEN);
        lbCo.setTextFill(Color.YELLOWGREEN);
        lbLi.setLayoutX(20);
        lbLi.setLayoutY(100);
        lbCo.setLayoutX(20);
        lbCo.setLayoutY(450);
        greyHeartHbox.setLayoutX(120);
        greyHeartHbox.setLayoutY(100);
        greyHeartHbox1.setLayoutX(120);
        greyHeartHbox1.setLayoutY(180);
        OpponentCoins.setLayoutX(180);
        OpponentCoins.setLayoutY(459);

        if (getHealth() <= 15) {
            for (int i = 0; i < getHealth(); i++) {
                ImageView heart = new ImageView(new Image("resources/Grey Heart.png"));
                heart.setFitHeight(70);
                heart.setFitWidth(70);
                greyHeartHbox.getChildren().add(heart);
            }
        } else {
            for (int i = 0; i < 15; i++) {
                ImageView heart = new ImageView(new Image("resources/Grey Heart.png"));
                heart.setFitHeight(70);
                heart.setFitWidth(70);
                greyHeartHbox.getChildren().add(heart);
            }
            for (int i = 0; i < getHealth() - 15; i++) {
                ImageView heart = new ImageView(new Image("resources/Grey Heart.png"));
                heart.setFitHeight(70);
                heart.setFitWidth(70);
                greyHeartHbox1.getChildren().add(heart);
            }
        }
        for (int i = 0; i < realbig; i++) {
            ImageView realCoin = new ImageView(new Image("resources/Real Cin.png"));
            realCoin.setFitHeight(70);
            realCoin.setFitWidth(70);
            OpponentCoins.getChildren().add(realCoin);
        }

        for (int i = 0; i < fakebig; i++) {
            ImageView fakeCoin = new ImageView(new Image("resources/Fake Coin.png"));
            fakeCoin.setFitHeight(70);
            fakeCoin.setFitWidth(70);
            OpponentCoins.getChildren().add(fakeCoin);
        }

        for (int i = 0; i < realsmall; i++) {
            ImageView realCoin = new ImageView(new Image("resources/Real Cin.png"));
            realCoin.setFitHeight(50);
            realCoin.setFitWidth(50);
            OpponentCoins.getChildren().add(realCoin);
        }

        for (int i = 0; i < fakesmall; i++) {
            ImageView fakeCoin = new ImageView(new Image("resources/Fake Coin.png"));
            fakeCoin.setFitHeight(50);
            fakeCoin.setFitWidth(50);
            OpponentCoins.getChildren().add(fakeCoin);
        }
        pane.getChildren().addAll(lbLi, lbCo, greyHeartHbox, greyHeartHbox1, OpponentCoins);
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}

class Item {
    private int id;
    private String name;
    private int price;
    private String effect;
    public boolean isAvailable;

    public Item(int id, String name, int price, String effect) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.effect = effect;
        this.isAvailable = true;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEffect() {
        return effect;
    }

    public int getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setIs(boolean value) {
        isAvailable = value;
    }

    @Override
    public String toString() {
        String output = "Item: " + this.getName() + ",ID: " + this.getId() + " ,price: " + this.getPrice()
                + " ,Is available: " + this.isAvailable;
        return output;
    }
}

class Shop {
    public List<Item> items = new ArrayList<>();

    public Shop() {
        items.add(new Item(1, "Life Potion", 2, "Increase 2 life"));
        items.add(new Item(2, "Crit Potion", 2, "Increase injury to 3"));
        items.add(new Item(3, "Evasion Charm", 3, "Avoid injury"));
    }
}
