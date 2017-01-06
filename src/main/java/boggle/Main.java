package boggle;

import java.io.FileNotFoundException;

public class Main {

	public static void main(String[] args) {

		try {
			Validator validator = new Validator();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
