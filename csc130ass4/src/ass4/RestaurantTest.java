package ass4;

public class RestaurantTest {
	
	public static void main(String[] args) {
		Restaurant frame = new Restaurant();
        frame.readData("foods.txt");
        frame.setSize(300, 300);
        frame.setVisible(true);
        frame.start();
        frame.writeBack("foods.txt");
	}

}
