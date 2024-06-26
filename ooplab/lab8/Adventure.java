package lab8;

import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Adventure {

	private static final int NUM_LEVELS = 12;

	public static void main(String[] args) {
		System.out.println("Welcome to Middle-Earth Adventure!");
		Scanner scanner = new Scanner(System.in);
		System.out.print(
				"Enter the destination name (4-16 characters, letters and spaces only.Do not start or end with a blank): ");
		String destination = scanner.nextLine();
		while (!isValidDestination(destination)) {
			System.out.print("Invalid destination name. Please enter again: ");
			destination = scanner.nextLine();
		}

		Hobbit adventurer = new Hobbit();
		Opponent opponent = getRandomOpponent();
		Creature oppo = (Creature) opponent;// in order to use the methods and attributes from parent class

		List<Level> levels = new ArrayList<>();
		levels = initLevel();
		int totalValue = getTotalValue(levels);

		Shop shop = new Shop();

		int level = 1;
		while (adventurer.isAlive() && level <= NUM_LEVELS) {
			System.out.println("\n===== Level " + level + " =====");
			if (levels.get(level - 1).isReward) {
				System.out.println("Congratuations!This is a reward level.");
				adventurer.setHealth(adventurer.getHealth() + 2);
				adventurer.addCoins(levels.get(level - 1).treasure);
				// the opponent can't come back to life
				if (oppo.getHealth() != 0) {
					oppo.setHealth(oppo.getHealth() + 2);
					oppo.addCoins(levels.get(level - 1).treasure);
				} // adventurer gets all the coins when opponent is dead
				else {
					adventurer.addCoins(levels.get(level - 1).treasure);
				}
				System.out.println("Adventurer: " + adventurer.getName() + " (Health: " + adventurer.getHealth()
						+ ",Coins: " + adventurer.coins.size() + ",Value:" + adventurer.getValue() + ")");
				System.out.println("Opponent: " + oppo.getName() + " (Health: " + oppo.getHealth() + ",Coins: "
						+ oppo.coins.size() + ",Value:" + oppo.getValue() + ")");
				adventurer.item = new Item(-1, " ", 0);// clear adventurer's item
			} else {
				if (opponent.shouldFight(adventurer, levels.get(level - 1)))
					opponent.fight(adventurer, levels.get(level - 1));
				if (!adventurer.isAlive())
					break;
			}
			shop.operate(adventurer);
			level++;
		}
		System.out.println("\n===== Game Over =====");
		System.out.println("Destination: " + destination);
		System.out.println("Winner: " + (adventurer.isAlive() ? adventurer.getName() : oppo.getName()));
		System.out.println(
				"Adventurer's total coins: " + adventurer.coins.size() + " the value is " + adventurer.getValue());
		System.out.println("Opponent's total coins: " + oppo.coins.size() + " the value is " + oppo.getValue());
		double adventurerScore = (double) adventurer.getValue() / totalValue * 100;
		System.out.println("Adventurer's score: " + adventurerScore + "%");
	}

	// check the destination name
	private static boolean isValidDestination(String destination) {
		boolean isValid = (destination.length() >= 4 && destination.length() <= 16 && destination.matches("[a-zA-Z ]+")
				&& !destination.startsWith(" ") && !destination.endsWith(" "));
		return isValid;
	}

	// select an opponent randomly
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
    // initialise the total 12 levels
	private static List<Level> initLevel() {
		List<Level> levels = new ArrayList<>();
		for (int i = 1; i <= NUM_LEVELS; i++) {
			levels.add(new Level(i));
		}
		return levels;
	}
    // get total value set in 12 levels
	private static int getTotalValue(List<Level> levels) {
		int totalValue = 0;
		for (Level level : levels) {
			for (Coin coin : level.treasure) {
				if (coin.isReal) {
					totalValue += coin.faceValue;
				}
			}
		}
		totalValue += 8;
		return totalValue;
	}
}

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
	public Item item = new Item(-1, " ", 0);

	public Hobbit() {
		super("Hobbit", randomLifeValue());
	}

	private static int randomLifeValue() {
		Random rand = new Random();
		return rand.nextInt(5) + 5; // Random life value 5-9
	}

}

interface Opponent {
	boolean shouldFight(Hobbit adventurer, Level level);

	void fight(Hobbit adventurer, Level level);
}

class Elf extends Creature implements Opponent {
	public Elf() {
		super("Elf", 7);
		winOdds = 0.8;
	}

	@Override
	public boolean shouldFight(Hobbit adventurer, Level level) {
		// when opponent is dead
		if (this.getHealth() == 0) {
			adventurer.setHealth(adventurer.getHealth() + (adventurer.getHealth() <= 2 ? -1 : 1));
			adventurer.addCoins(level.treasure);
			System.out.println("Adventurer: " + adventurer.getName() + " (Health: " + adventurer.getHealth()
					+ ", Coins: " + adventurer.coins.size() + ",Value:" + adventurer.getValue() + ")");
			System.out.println("Opponent: " + this.getName() + " (Health: " + this.getHealth() + ", Coins: "
					+ this.coins.size() + ",Value:" + this.getValue() + ")");
			return false;
		}
	    else if (adventurer.getHealth() >= 2) {
			return true;
		} else {
			System.out.println("Adventurer is too weak to fight,Elf gets the coin.");
			this.addCoins(level.treasure);
			// due to the injury
			adventurer.setHealth(adventurer.getHealth() - 1); 
			this.setHealth(this.getHealth() + 1);
			System.out.println("Adventurer: " + adventurer.getName() + " (Health: " + adventurer.getHealth()
					+ ", Coins: " + adventurer.coins.size() + ",Value:" + adventurer.getValue() + ")");
			System.out.println("Opponent: " + this.getName() + " (Health: " + this.getHealth() + ", Coins: "
					+ this.coins.size() + ",Value:" + this.getValue() + ")");
			// clear adventurer's item
			adventurer.item = new Item(-1, " ", 0);
			return false;
		}
	}

	@Override
	public void fight(Hobbit adventurer, Level level) {
		Random random = new Random();
		double rand = random.nextDouble();
		if (rand <= winOdds) {
			System.out.println("Elf wins!");
			// realise the effect of Evasion Charm
			if (adventurer.item.getId() == 3) {
				System.out.println("But adventurer uses Evasion Charm,useless attack.");
				adventurer.item = new Item(-1, " ", 0);
			} else {
				adventurer.setHealth(adventurer.getHealth() - 2);
			}
			this.addCoins(level.treasure);
		} else {
			System.out.println("Adventurer wins!");
			// realise the effect of Crit Potion
			if (adventurer.item.getId() == 2) {
				System.out.println("And adventurer uses Crit Potion,enhance attack effect.");
				this.setHealth(this.getHealth() - 3);
			} else {
				this.setHealth(this.getHealth() - 2);
			}
			adventurer.addCoins(level.treasure);
		}
		if (level.getNumber() != 1) {
			// check adventurer's injury
			adventurer.setHealth(adventurer.getHealth() + (adventurer.getHealth() <= 2 ? -1 : 1));
			this.setHealth(this.getHealth() + 1);
		}
		System.out.println("Adventurer: " + adventurer.getName() + " (Health: " + adventurer.getHealth() + ",Coins: "
				+ adventurer.coins.size() + ",Value:" + adventurer.getValue() + ")");
		System.out.println("Opponent: " + this.getName() + " (Health: " + this.getHealth() + ",Coins: "
				+ this.coins.size() + ",Value:" + this.getValue() + ")");
		if (adventurer.getHealth() == 1) {
			// friendly remainder
			System.out.println("Adventurer has been injured,you'd bettter get a Life Potion.");
		}
		// clear adventurer's item
		adventurer.item = new Item(-1, " ", 0);
	}
}

class Dwarf extends Creature implements Opponent {
	public Dwarf() {
		super("Dwarf", 9);
		winOdds = 0.5;
	}

	@Override
	public boolean shouldFight(Hobbit adventurer, Level level) {
		// when opponent is dead
		if (this.getHealth() == 0) {
			adventurer.setHealth(adventurer.getHealth() + (adventurer.getHealth() <= 2 ? -1 : 1));
			adventurer.addCoins(level.treasure);
			System.out.println("Adventurer: " + adventurer.getName() + " (Health: " + adventurer.getHealth()
					+ ", Coins: " + adventurer.coins.size() + ",Value:" + adventurer.getValue() + ")");
			System.out.println("Opponent: " + this.getName() + " (Health: " + this.getHealth() + ", Coins: "
					+ this.coins.size() + ",Value:" + this.getValue() + ")");
			return false;
		} else if (adventurer.getHealth() >= 2 && this.getHealth() > 2) {
			return true;
		} else if (this.getHealth() > 2) {
			System.out.println("Adventurer is too weak to fight,Dwarf gets the coin.");
			this.addCoins(level.treasure);
			// due to the injury
			adventurer.setHealth(adventurer.getHealth() - 1); 
			this.setHealth(this.getHealth() + 1);
			System.out.println("Adventurer: " + adventurer.getName() + " (Health: " + adventurer.getHealth()
					+ ", Coins: " + adventurer.coins.size() + ",Value:" + adventurer.getValue() + ")");
			System.out.println("Opponent: " + this.getName() + " (Health: " + this.getHealth() + ", Coins: "
					+ this.coins.size() + ",Value:" + this.getValue() + ")");
			// clear adventurer's item
			adventurer.item = new Item(-1, " ", 0); 
			return false;
		} else {
			System.out.println("Dwarf is too weak to fight,Adventurer gets the coin.");
			adventurer.addCoins(level.treasure);
			//check adventurer's injury
			adventurer.setHealth(adventurer.getHealth() + (adventurer.getHealth() <= 2 ? -1 : 1));
			this.setHealth(this.getHealth() + 1);
			System.out.println("Adventurer: " + adventurer.getName() + " (Health: " + adventurer.getHealth()
					+ ",Coins: " + adventurer.coins.size() + ",Value:" + adventurer.getValue() + ")");
			System.out.println("Opponent: " + this.getName() + " (Health: " + this.getHealth() + ",Coins: "
					+ this.coins.size() + ",Value:" + this.getValue() + ")");
			// clear adventurer's item
			adventurer.item = new Item(-1, " ", 0);
			return false;
		}
	}

	@Override
	public void fight(Hobbit adventurer, Level level) {
		Random random = new Random();
		double rand = random.nextDouble();
		if (rand <= winOdds) {
			System.out.println("Dwarf wins!");
			// realise the effect of Evasion Charm
			if (adventurer.item.getId() == 3) {
				System.out.println("But adventurer uses Evasion Charm,useless attack.");
			} else {
				adventurer.setHealth(adventurer.getHealth() - 2);
			}
			this.addCoins(level.treasure);
		} else {
			System.out.println("Adventurer wins!");
			// realise the effect of Crit Potion
			if (adventurer.item.getId() == 2) {
				System.out.println("And adventurer uses Crit Potion,enhance attack effect.");
				this.setHealth(this.getHealth() - 3);
			} else {
				this.setHealth(this.getHealth() - 2);
			}
			adventurer.addCoins(level.treasure);
		}
		if (level.getNumber() != 1) {
			// check adventurer's injury
			adventurer.setHealth(adventurer.getHealth() + (adventurer.getHealth() <= 2 ? -1 : 1));
			this.setHealth(this.getHealth() + 1);
		}
		System.out.println("Adventurer: " + adventurer.getName() + " (Health: " + adventurer.getHealth() + ",Coins: "
				+ adventurer.coins.size() + ",Value:" + adventurer.getValue() + ")");
		System.out.println("Opponent: " + this.getName() + " (Health: " + this.getHealth() + ",Coins: "
				+ this.coins.size() + ",Value:" + this.getValue() + ")");
		// friendly remainder
		if (adventurer.getHealth() == 1) {
			System.out.println("Adventurer has been injured,you'd bettter get a Life Potion.");
		}
		// clear adventurer's item
		adventurer.item = new Item(-1, " ", 0);
	}
}

class Orc extends Creature implements Opponent {
	public Orc() {
		super("Orc", 8);
	}

	@Override
	public boolean shouldFight(Hobbit adventurer, Level level) {
		// when opponent is dead
		if (this.getHealth() == 0) {
			adventurer.setHealth(adventurer.getHealth() + (adventurer.getHealth() <= 2 ? -1 : 1));
			adventurer.addCoins(level.treasure);
			System.out.println("Adventurer: " + adventurer.getName() + " (Health: " + adventurer.getHealth()
					+ ", Coins: " + adventurer.coins.size() + ",Value:" + adventurer.getValue() + ")");
			System.out.println("Opponent: " + this.getName() + " (Health: " + this.getHealth() + ", Coins: "
					+ this.coins.size() + ",Value:" + this.getValue() + ")");
			return false;
		} else if (adventurer.getHealth() >= 2) {
			return true;
		} else {
			System.out.println("Adventurer is too weak to fight,Orc gets the coin.");
			this.addCoins(level.treasure);
			// due to the injury
			adventurer.setHealth(adventurer.getHealth() - 1);
			this.setHealth(this.getHealth() + 1);
			System.out.println("Adventurer: " + adventurer.getName() + " (Health: " + adventurer.getHealth()
					+ ",Coins: " + adventurer.coins.size() + ",Value:" + adventurer.getValue() + ")");
			System.out.println("Opponent: " + this.getName() + " (Health: " + this.getHealth() + ",Coins: "
					+ this.coins.size() + ",Value:" + this.getValue() + ")");
			// clear adventurer's item
			adventurer.item = new Item(-1, " ", 0);
			return false;
		}
	}

	@Override
	public void fight(Hobbit adventurer, Level level) {
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
			System.out.println("Orc wins!");
			// realise the effect of Evasion Charm
			if (adventurer.item.getId() == 3) {
				System.out.println("But adventurer uses Evasion Charm,useless attack.");
			} else {
				adventurer.setHealth(adventurer.getHealth() - 2);
			}
			this.addCoins(level.treasure);
		} else {
			System.out.println("Adventurer wins!");
			// realise the effect of Crit Potion
			if (adventurer.item.getId() == 2) {
				System.out.println("And adventurer uses Crit Potion,enhance attack effect.");
				this.setHealth(this.getHealth() - 3);
			} else {
				this.setHealth(this.getHealth() - 2);
			}
			adventurer.addCoins(level.treasure);
		}
		if (level.getNumber() != 1) {
			// check adventurer's injury
			adventurer.setHealth(adventurer.getHealth() + (adventurer.getHealth() <= 2 ? -1 : 1));
			this.setHealth(this.getHealth() + 1);
		}
		System.out.println("Adventurer: " + adventurer.getName() + " (Health: " + adventurer.getHealth() + ",Coins: "
				+ adventurer.coins.size() + ",Value:" + adventurer.getValue() + ")");
		System.out.println("Opponent: " + this.getName() + " (Health: " + this.getHealth() + ",Coins: "
				+ this.coins.size() + ",Value:" + this.getValue() + ")");
		// friendly remainder
		if (adventurer.getHealth() == 1) {
			System.out.println("Adventurer has been injured,you'd bettter get a Life Potion.");
		}
		adventurer.item = new Item(-1, " ", 0);
	}
}

class Item {
	private int id;
	private String name;
	private int price;
	public boolean isAvailable;

	public Item(int id, String name, int price) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.isAvailable = true;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	@Override
	public String toString() {
		String output = "Item: " + this.getName() + ",ID: " + this.getId() + " ,price: " + this.getPrice()
				+ " ,Is available: " + this.isAvailable;
		return output;
	}
}

class Shop {
	private List<Item> items = new ArrayList<>();

	public Shop() {
		items.add(new Item(1, "Life Potion", 2));
		items.add(new Item(2, "Crit Potion", 2));
		items.add(new Item(3, "Evasion Charm", 3));
	}

	public void operate(Hobbit adventurer) {
		System.out.println("--------------Shop--------------");
		Scanner scanner = new Scanner(System.in);
		for (Item item : items)
			System.out.println(item.toString());
		while (true) {
			System.out.print("Buy an item? (Enter ID or 'y' to skip): ");
			String input = scanner.nextLine().trim();
			// skip the shop
			if (input.equals("y")) {
				break;
			}
			try {
				if (this.buy(adventurer, Integer.parseInt(input))) {
					System.out.println("Bought " + items.get(Integer.parseInt(input) - 1).getName());
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid ID. Please enter a number.");
			} catch (IndexOutOfBoundsException e) {
				System.out.println("Invalid ID. Selecting from the given list.");
			}
		}
	}

	private boolean buy(Hobbit adventurer, int itemId) {
		List<Coin> payway1 = new ArrayList<>();
		List<Coin> payway2 = new ArrayList<>();
		List<Coin> payway3 = new ArrayList<>();
		List<Coin> payway4 = new ArrayList<>();
		List<Coin> payway5 = new ArrayList<>();
		List<Coin> change = new ArrayList<>();
		payway1.add(new Coin(2, true));
		payway2.add(new Coin(1, true));
		payway2.add(new Coin(1, true));
		payway3.add(new Coin(2, true));
		payway3.add(new Coin(1, true));
		payway4.add(new Coin(1, true));
		payway4.add(new Coin(1, true));
		payway4.add(new Coin(1, true));
		payway5.add(new Coin(2, true));
		payway5.add(new Coin(2, true));
		change.add(new Coin(1, true));
        // sold out
		if (!this.items.get(itemId - 1).isAvailable) {
			System.out.println(this.items.get(itemId - 1).getName() + " has been sold out.");
			return false;
		} // insufficient money
		else if (adventurer.getValue() < items.get(itemId - 1).getPrice()) {
			System.out.println("Adventurer don't have enough coins.");
			return false;
		} // adventurer could buy it successfully
		else {
			// when the price is 2
			if (this.items.get(itemId - 1).getPrice() == 2) {
				if (adventurer.coins.containsAll(payway1)) {
					adventurer.coins.removeAll(payway1);
				} else {
					adventurer.coins.removeAll(payway2);
				}
			} // when the price is 3
			else {
				if (adventurer.coins.containsAll(payway3)) {
					adventurer.coins.removeAll(payway3);
				} else if (adventurer.coins.containsAll(payway4)) {
					adventurer.coins.removeAll(payway4);
				} else {
					adventurer.coins.removeAll(payway5);
					adventurer.addCoins(change);
				}
			}
			// realise the effect of Life Potion
			if (itemId == 1) {
				adventurer.setHealth(adventurer.getHealth() + 2);
			} // add certain item to adventurer
			else {
				adventurer.item = this.items.get(itemId - 1);
			}
			// adventurer can't but anything that has been sold
			this.items.get(itemId - 1).isAvailable = false;
			return true;
		}
	}
}
