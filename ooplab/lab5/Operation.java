package lab5;

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class Operation {

	public static void main(String[] args) {
		FamilyMart familyMart = new FamilyMart();

		while(true) {
			familyMart.load();
			if(familyMart.isEx )
            	continue;
            familyMart.purchase();
            familyMart.cleanExpiredDay();
            familyMart.sell();
            if(familyMart.isEx )
            	continue;
            familyMart.cleanExpiredNight();
            familyMart.report();
		}
    }
}

class Product implements Comparable<Product> {
    private String name;
    private double price;
    private int life;
    private Date productDate;
    private Date cleanTime;

    public Product(String name, double price, int life, Date productDate,Date cleanTime) {
        this.name = name;
        this.price = price;
        this.life = life;
        this.productDate = productDate;
        this.cleanTime = cleanTime;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getLife() {
        return life;
    }

    public Date getProductDate() {
        return productDate;
    }
    
    public Date getCleanTime() {
        return cleanTime;
    }

    @Override
    public int compareTo(Product another) {
        return this.cleanTime.compareTo(another.getCleanTime());
    }
}

class FamilyMart {
	private PriorityQueue<Product> inventory;
	private Date currentDate;
	private String purchase;
	private String sell;
	private double revenue;
	private int duration;
	public boolean isEx;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	private FileWriter result;

    public FamilyMart() {
        inventory = new PriorityQueue<>();
        currentDate = new Date();
        revenue = 0;
    	duration = 0;
    	 try {
             result = new FileWriter("result.txt");
         } catch (IOException e) {
             e.printStackTrace();
         } 
        try {
             currentDate = dateFormat.parse("2022/05/02");
        } catch (Exception e) {
             e.printStackTrace();
        }
    }

    public void load() {
    	isEx = false;
    	Scanner input = new Scanner(System.in);
        System.out.println("Date: " + dateFormat.format(currentDate));
    	System.out.println("Input the purchase and sell file,separated by space.\nOr \"exit\" to end recording.");
		String recording = input.nextLine();
		if(recording.equals("exit")) {
			try {
	             result.close();
	         } catch (IOException e) {
	             e.printStackTrace();
	         } 
			System.exit(0);
		}
		String[] files = recording.split(" ");
		try {
		     purchase = files[0];
		     sell = files[1];
		}catch(ArrayIndexOutOfBoundsException e) {
			System.out.println(e);
            isEx = true;
		}
	}
    public void purchase() {
        try {
            File file = new File(purchase);
            Scanner scanner = new Scanner(file);
            String omit = scanner.nextLine();
            while (scanner.hasNext()) {
                String name = scanner.next();
                double price = scanner.nextDouble();
                int life = scanner.nextInt();
                Date productDate = new Date();
                Date cleanTime = new Date();
                try {
                    productDate = dateFormat.parse(scanner.next());
               } catch (Exception e) {
                    e.printStackTrace();
               }
                Calendar expiration = Calendar.getInstance();
                expiration.setTime(productDate);
                expiration.add(Calendar.DATE,life-1);
                cleanTime = expiration.getTime();

                Product product = new Product(name,price,life,productDate,cleanTime);
                inventory.add(product);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
            isEx = true;
        }
    }

    public void sell() {
        try {
            File file = new File(sell);
            Scanner scanner = new Scanner(file);
            String omit = scanner.nextLine();
            while (scanner.hasNextLine()) {
            	double discount = 1;
                String line = scanner.nextLine();
                String[] data = line.split("\\s+");

                String name = data[0];
                if(data.length == 2)
                   discount = Double.parseDouble(data[1]);
                Product product = find(name);
                if (product != null) {
                        inventory.remove(product);
                        revenue += (product.getPrice() * discount);
                    }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
        	System.out.println(e);
            isEx = true;
        }
    }

    public void cleanExpiredNight() {
        List<Product> expiredProducts = new ArrayList<>();

        for (Product product : inventory) {
            if (isExpiredNight(product)) {
                expiredProducts.add(product);
            }
        }

        inventory.removeAll(expiredProducts);
    }

    private Product find(String name) {
        for (Product product : inventory) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }

    private boolean isExpiredNight(Product product) {
        boolean isExpired = product.getCleanTime().compareTo(currentDate) <= 0;
        return isExpired;
    }

    public void cleanExpiredDay() {
        List<Product> expiredProducts = new ArrayList<>();

        for (Product product : inventory) {
            if (isExpiredDay(product)) {
                expiredProducts.add(product);
            }
        }

        inventory.removeAll(expiredProducts);
    }
    
    private boolean isExpiredDay(Product product) {
        boolean isExpired = product.getCleanTime().compareTo(currentDate) < 0;
        return isExpired;
    }
    
    public void report() {
        try {
            result.write(duration + " day : turnover:" + revenue + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Calendar date = Calendar.getInstance();
        date.setTime(currentDate);
        date.add(Calendar.DATE, 1); 
        currentDate = date.getTime();
        duration++;
    }
}

