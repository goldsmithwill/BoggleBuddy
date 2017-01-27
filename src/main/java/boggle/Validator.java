package boggle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Validator {
	private char[][] board;
	private Map<List<int[]>, String> wordPathMap;
	private Set<String> wordSet;

	// validator constructor starts everything up
	public Validator() throws FileNotFoundException {
		String filename = "/Users/willgoldsmith/Documents/workspace/boggle-buddy/src/main/resources/words.txt";
		setWords(filename);
		setBoard(new char[4][4]);

		for (int i = 0; i < getBoard().length; i++) {
			System.out.println(Arrays.toString(getBoard()[i]));
		}

		//this whole chunk of code is just for testing
		//it's all hardcoding and stuff
		List<int[]> tempPath = new ArrayList<int[]>();
		tempPath.add(new int[] { 1, 1 });
		getNextIndexes(tempPath);
	}

	// findWords method to walk through board
	public void findWords(String word, int[] currentIndex, List<int[]> path) {

		word += getBoard()[currentIndex[0]][currentIndex[1]];

		if (getWordSet().contains(word)) {
			getWordPathMap().put(path, word);
		} else {
			path.add(currentIndex);

			// getValidNextIndexes(currentIndex, path) return an
			// int[indexes][x+y]
			// for loop going through the returned array and running findWords
			// for each one

			// findWords(word, getNextIndexes(path), path);
		}

	}

	// // validateWord method to search if word is in wordList
	// public boolean validateWord(String searchWord) {
	//
	// // arraylist of valid words that start with searchword
	// List<String> validWords = new ArrayList<String>();
	//
	// // for loop to find all words that start with searchWord
	// // those words are added to validWords
	// for (int k = 0; k < getWordArray().length; k++) {
	// if (getWordArray()[k].startsWith(searchWord)) {
	// // System.out.println(getWordArray()[k]);
	// validWords.add(getWordArray()[k]);
	// }
	// }
	//
	// // if the wordList contains the searchWord
	// if (getWordSet().contains(searchWord)) {
	// return false;
	// }
	// // if no words start with searchWord
	// if (validWords.size() == 0) {
	// return false;
	// }
	// // if at least one word starts with searchWord
	// else {
	// return true;
	// }
	// }

	// getNextIndexes to return all valid next indexes
	// for validateWord method
	public List<int[]> getNextIndexes(List<int[]> path) {

		// getting currentIndex from last element in path
		int[] currentIndex = path.get(path.size() - 1);
		System.out.print(Arrays.toString(currentIndex));

		// setting x and y from currentIndex
		int x = currentIndex[0];
		int y = currentIndex[1];

		// nextIndexes list to be returned
		List<int[]> nextIndexes = new ArrayList<int[]>();

		// array w/ all possible indexes touching currentIndex
		int[][] nextIndexValues = { { x - 1, y }, { x, y - 1 }, { x + 1, y }, { x, y + 1 }, { x - 1, y - 1 },
				{ x + 1, y - 1 }, { x - 1, y + 1 }, { x + 1, y + 1 } };

		// valid test char to test if the nextIndexValues are valid
		char validTestChar;

		for (int i = 0; i < 8; i++) {

			// try catch to see which index values are valid or invalid
			try {
				// test if nextIndexValues are valid,
				// using board and validTestChar
				validTestChar = getBoard()[nextIndexValues[i][0]][nextIndexValues[i][1]];

				// if valid, add the nextIndexValues to nextIndexes
				nextIndexes.add(new int[] { nextIndexValues[i][0], nextIndexValues[i][1] });

				System.out.println(Arrays.toString(nextIndexValues[i]));
			} catch (ArrayIndexOutOfBoundsException e) {
				// if invalid, it doesn't add nextIndexValues to nextIndexes
			}

		}

		return nextIndexes;
	}

	// getters and setters

	public char[][] getBoard() {
		return board;
	}

	public void setBoard(char[][] board) {
		this.board = board;
		// 2d char array for board
		// nested for loops to generate board
		for (int i = 0; i < this.board.length; i++) {
			for (int j = 0; j < this.board[0].length; j++) {

				// generates random num 97-122 (for ASCII)
				Random random = new Random();
				int letterNum = random.nextInt(26) + 97;

				// casts char to convert dec to letter
				// adding letter to board
				this.board[i][j] = (char) letterNum;
			}
		}
	}

	public void setWords(String filename) throws FileNotFoundException {
		// scanning words.txt file into ArrayList
		Scanner scanner = new Scanner(new File(filename));

		Set<String> lines = new HashSet<String>();

		while (scanner.hasNextLine()) {
			String nextLine = scanner.nextLine();
			// if the word is at least 3 letters
			if (nextLine.length() > 2) {
				lines.add(nextLine);
			}
		}

		// making array from arraylist
		setWordSet(lines);

		setWordPathMap(new HashMap<List<int[]>, String>());
		scanner.close();
	}

	public Map<List<int[]>, String> getWordPathMap() {
		return wordPathMap;
	}

	public void setWordPathMap(Map<List<int[]>, String> wordPathMap) {
		this.wordPathMap = wordPathMap;
	}

	public Set<String> getWordSet() {
		return wordSet;
	}

	public void setWordSet(Set<String> wordSet) {
		this.wordSet = wordSet;
	}

}
