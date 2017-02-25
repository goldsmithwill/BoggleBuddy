package boggle;

import java.util.*;
import java.io.*;

public class WordListFilter {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		Scanner console = new Scanner(System.in);

		// create files and file reading/writing objects
		File f = new File("/Users/willgoldsmith/Documents/workspace/boggle-buddy/src/main/resources/words.txt");
		File temp = new File("myTempFile.txt");
		Scanner input = new Scanner(f); // reader
		BufferedWriter writer = new BufferedWriter(new FileWriter(temp)); // writer

		while (input.hasNextLine()) {
			String line = input.nextLine();
			if (line.length() < 3 || line.contains("\'") || line.matches(".*\\d.*") || line.contains("/")) {
				continue;
			}

			writer.write(line + System.getProperty("line.separator"));
		}

		writer.close();
		console.close();
		input.close();

		// rename temp file as f file
		temp.renameTo(f);
	}
}