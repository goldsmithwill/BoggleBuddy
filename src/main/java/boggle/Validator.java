package boggle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Validator {
	private char[][] board;
	private String[] wordArray;
	private List<String> wordList;
	private int[] anchorIndex;
	private int[] searchIndex;

	// validator constructor starts everything up

	public Validator() throws FileNotFoundException {
		String filename = "/Users/willgoldsmith/Documents/workspace/boggle-buddy/src/main/resources/words.txt";
		setWords(filename);
		setBoard(new char[4][4]);
		setAnchorIndex(new int[] { 0, 0 });
		setSearchIndex(new int[] { 0, 0 });

		for (int i = 0; i < board.length; i++) {
			System.out.println(Arrays.toString(board[i]));
		}

		lookup("");
	}

	// walkthrough method to walk through board

	public void walkthrough(String searchWord, String[] validWords) {
		
		lookup(searchWord);

	}

	// lookup method to search if word is in wordList

	public void lookup(String searchWord) {

		// arraylist of valid words that start with searchword
		List<String> validWords = new ArrayList<String>();

		// for loop to find all words that start with searchWord
		// those words are added to validWords
		for (int k = 0; k < getWordArray().length; k++) {
			if (getWordArray()[k].startsWith(searchWord)) {
				// System.out.println(getWordArray()[k]);
				validWords.add(getWordArray()[k]);
			}
		}

		// if the wordList contains the searchWord
		if (getWordList().contains(searchWord)) {
//			System.out.println(getWordList().indexOf(searchWord));
			System.out.println(searchWord);
			nextAnchor();
			searchWord = Character.toString(getBoard()[getAnchorX()][getAnchorY()]);
			walkthrough(searchWord, validWords.toArray(new String[validWords.size()]));
		}
		
		// if no words start with searchWord
		if (validWords.size() == 0) {
			nextAnchor();
			searchWord = Character.toString(getBoard()[getAnchorX()][getAnchorY()]);
			walkthrough(searchWord, validWords.toArray(new String[validWords.size()]));
		}
		// if at least one word starts with searchWord
		else {
			nextSearchIndex();
			searchWord += getBoard()[getAnchorX()][getAnchorY()];
			walkthrough (searchWord, validWords.toArray(new String[validWords.size()]));
		}
	}

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

		List<String> lines = new ArrayList<String>();

		while (scanner.hasNextLine()) {
			lines.add(scanner.nextLine());
		}

		// making array from arraylist
		setWordList(lines);
		setWordArray(lines.toArray(new String[0]));
		scanner.close();
	}

	// methods for currentIndex

	public void nextSearchIndex() {
		try {
			setSearchIndex(new int[] { getSearchX() + 1, getSearchY() });
		} catch (ArrayIndexOutOfBoundsException e) {
			setSearchIndex(new int[] { 0, getSearchY() + 1 });
		}
	}

	public void nextAnchor() {
		try {
			setAnchorIndex(new int[] { getAnchorX() + 1, getAnchorY() });
		} catch (ArrayIndexOutOfBoundsException e) {
			setAnchorIndex(new int[] { 0, getAnchorY() + 1 });
		}

		setSearchIndex(new int[] { 0, 0 });
	}

	// methods to get "x" and "y" from index

	public int getAnchorX() {
		return getAnchorIndex()[0];
	}

	public int getAnchorY() {
		return getAnchorIndex()[1];
	}

	public int getSearchX() {
		return getSearchIndex()[0];
	}

	public int getSearchY() {
		return getSearchIndex()[1];
	}

	// getters and setters

	public String[] getWordArray() {
		return wordArray;
	}

	public void setWordArray(String[] wordArray) {
		this.wordArray = wordArray;
	}

	public List<String> getWordList() {
		return wordList;
	}

	public void setWordList(List<String> wordList) {
		this.wordList = wordList;
	}

	public int[] getAnchorIndex() {
		return anchorIndex;
	}

	public void setAnchorIndex(int[] anchorIndex) {
		this.anchorIndex = anchorIndex;
	}

	public int[] getSearchIndex() {
		return searchIndex;
	}

	public void setSearchIndex(int[] searchIndex) {
		this.searchIndex = searchIndex;
	}
}
