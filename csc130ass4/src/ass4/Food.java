package ass4;

public class Food {
	
	private String name;
	private String id;
	private String category;
	private String description;
	private String type;
	private String size;
	private double price;
	
	public Food() {
		name = "";
		id = "";
		category = "";
		description = "";
		type = "";
		size = "";
		price = 0;
	}
	
	public Food(String nName, String nId, String nCategory, 
			String nDescription, String nType, String nSize, double nPrice) {
		name = nName;
		id = nId;
		category = nCategory;
		description = nDescription;
		type = nType;
		size = nSize;
		price = nPrice;
	}
	
	public void setFood(String nName, String nCate, String nDes, String nType, String nSize, double nPrice) {
		name = nName;
		category = nCate;
		description = nDes;
		type = nType;
		size = nSize;
		price = nPrice;		
	}
	
	public String getFood() {
		return "Food Name: " + name +
				"\nID: " + id +
				"\nCategory: " + category +
				"\nDescription: " + description +
				"\nType: " + type +
				"\nSize: " + size +
				"\nPrice: " + price;
	}
	
	public String toString() {
		return name + "\n" + id + "\n" + category + "\n" + description + 
				"\n" + type + "\n" + size + "\n" + price;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getType() {
		return type;
	}
	
	public String getSize() {
		return size;
	}
	
	public String getPrice() {
		return Double.toString(price);
	}

}
