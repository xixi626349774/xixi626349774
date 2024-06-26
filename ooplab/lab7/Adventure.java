package lab7;

import java.util.Scanner;
import java.util.Random;

public class Adventure {
	 private static final int NUM_LEVELS = 12;
	 private static Scanner scanner = new Scanner(System.in);

	 public static void main(String[] args) {
	     System.out.println("Welcome to Middle-Earth Adventure!");
	
	     System.out.print("Enter the destination name (4-16 characters, letters and spaces only.Do not start or end with a blank): ");
	     String destination = scanner.nextLine();
	     while (!isValidDestination(destination)) {
		     System.out.print("Invalid destination name. Please enter again: ");
		     destination = scanner.nextLine();
	     }

		 Hobbit adventurer = new Hobbit();
		 Creature opponent = getRandomOpponent();

	     int level = 1;
	     while (adventurer.isAlive() && level <= NUM_LEVELS) {
		     System.out.println("\n===== Level " + level + " =====");
		     if (adventurer.getHealth() >= 2) {
	            boolean adventurerWins = adventurer.fight(opponent);
	            if (adventurerWins) {
	                System.out.println("Adventurer wins!");	                
	            } 
	            else {
	                System.out.println("Opponent wins!");
	            }
	         }
	        else {	
	        	opponent.setCoins(opponent.getCoins() + 1);
	            System.out.println("Adventurer is too weak to fight. Opponent takes the coin.");
	        }
	        if(level > 1 && adventurer.getHealth() != 0) {
	             adventurer.setHealth(adventurer.getHealth() + 1);
	             if(opponent.getHealth() != 0) {
	            	 opponent.setHealth(opponent.getHealth() + 1);
	             }
	        }
	        System.out.println("Adventurer: " + adventurer.getName() + " (Health: " + adventurer.getHealth() + ", Coins: " + adventurer.getCoins() + ")");
	        System.out.println("Opponent: " + opponent.getName() + " (Health: " + opponent.getHealth() + ", Coins: " + opponent.getCoins() + ")");
	        if(level < 12 && adventurer.isAlive()) {
	             System.out.println("Press 'y' to continue to the next level...");
	             String input = scanner.nextLine().trim();
	             while (!input.equals("y")) {
		              System.out.print("Invalid input,try again");
		              input = scanner.nextLine().trim();
		         }
	        }
            level++;
	    }
	        System.out.println("\n===== Game Over =====");
	        System.out.println("Destination: " + destination);
	        System.out.println("Winner: " + (adventurer.isAlive() ? adventurer.getName() : opponent.getName()));
	        System.out.println("Adventurer's total coins: " + adventurer.getCoins());
	        System.out.println("Opponent's total coins: " + opponent.getCoins());
	        double adventurerScore = (double) adventurer.getCoins() / (adventurer.getCoins() + opponent.getCoins()) * 100;
	        System.out.println("Adventurer's score: " + adventurerScore + "%");
	}
	    //check the destination name 
    private static boolean isValidDestination(String destination) {
	    boolean isValid = (destination.length() >= 4 && destination.length() <= 16 && destination.matches("[a-zA-Z ]+") && !destination.startsWith(" ") && !destination.endsWith(" "));
	    return isValid;
	}

	    //select an opponent randomly
	private static Creature getRandomOpponent() {
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
}

class Creature {
    private String name;
    private int health;
    private int coins;

    public Creature(String name, int health) {
        this.name = name;
        this.health = health;
        this.coins = 0;
    }

    //Getters and setters
    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    // Method to check if the creature is alive
    public boolean isAlive() {
        return health > 0;
    }

    // Method to realise fight function
    public boolean fight(Creature opponent) {
    	if(opponent.getHealth() == 0) {
    		this.coins++;
    		return true;
    	}
    	else {
            Random random = new Random();
            double probability;
            if(this.getHealth() > opponent.getHealth())
        	    probability = 0.7;
            else if(this.getHealth() < opponent.getHealth())
            	probability = 0.3;
            else
        	    probability = 0.5;
            double rand = random.nextDouble();
            if (rand <= probability) {
                this.coins++;
                opponent.setHealth(opponent.getHealth() - 2);
                return true;
            } 
            else{
                opponent.setCoins(opponent.getCoins() + 1);
                this.setHealth(this.getHealth() - 2);
                return false;
            }    
         }
     }
}

class Hobbit extends Creature {
    public Hobbit() {
        super("Hobbit", randomLifeValue());
    }

    private static int randomLifeValue() {
        Random rand = new Random();
        return rand.nextInt(5) + 5; // Random life value 5-9
    }

}

class Dwarf extends Creature {
    public Dwarf() {
        super("Dwarf", 9);
    }
 
}

class Elf extends Creature {
    public Elf() {
        super("Elf", 7);
    }
    
}

class Orc extends Creature{
    public Orc() {
        super("Orc", 5);
	}

}