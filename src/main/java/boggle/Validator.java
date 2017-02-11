package boggle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Validator {
	private char[][] board;
	private List<String> wordList;
	private Set<String> validWordSet;

	// validator constructor starts everything up
	public Validator() throws FileNotFoundException {
		String filename = "/Users/willgoldsmith/Documents/workspace/boggle-buddy/src/main/resources/words.txt";
		setWords(filename);
		setBoard(new char[4][4]);

		for (int i = 0; i < getBoard().length; i++) {
			System.out.println(Arrays.toString(getBoard()[i]));
		}

		generateValidWordSet();
		//
		// // this whole chunk of code is just for testing
		// // it's all hardcoding and stuff
		// List<int[]> tempPath = new ArrayList<int[]>();
		// tempPath.add(new int[] { 1, 1 });
		// getNextIndexes(tempPath);
	}

	// generateWordSet method to generate a set of
	// valid words that exist in the board
	public void generateValidWordSet() {
		// creating inital nextIndexes list
		List<int[]> boardIndexes = new ArrayList<int[]>();

		for (int i = 0; i < getBoard().length; i++) {
			for (int j = 0; j < getBoard()[0].length; j++) {
				boardIndexes.add(new int[] { i, j });
			}
		}

		// for loop to go through wordList
		for (int k = 0; k < getWordList().size(); k++) {
			// creating current word
			String currentWord = getWordList().get(k);

			char[][] boardCopy = new char[4][4];
			boardCopy = updateToBoard(boardCopy);

			// putting currentWord into recursive validateWord method
			validateWord(currentWord, boardCopy, boardIndexes, 0);
		}
		
		System.out.println(getValidWordSet());
	}

	// boolean recursive validateWord method
	// to compare input word to board
	public void validateWord(String searchWord, char[][] board, List<int[]> nextIndexes, int charIndex) {
		// creating currentChar from charIndex
		if (charIndex == searchWord.length()) {
			getValidWordSet().add(searchWord);
			return;
		}
		
		char currentChar = searchWord.charAt(charIndex);
		
		for (int i = 0; i < nextIndexes.size(); i++) {
			// creating x and y from current index in nextIndexes
			int x = nextIndexes.get(i)[0];
			int y = nextIndexes.get(i)[1];

			// if the currentIndex is currentChar
			if (board[x][y] == currentChar) {
				// making board index w/ currentChar blank
				board[x][y] = ' ';

				// recursion
				validateWord(searchWord, board, getNextIndexes(new int[] { x, y }), (charIndex + 1));

				// resetting board to global default
				board = updateToBoard(board);
			}
		}
	}

	// // findWords method to walk through board
	// public void findWords(String word, int[] currentIndex, List<int[]> path)
	// {
	//
	// word += getBoard()[currentIndex[0]][currentIndex[1]];
	//
	// if (getWordSet().contains(word)) {
	// getWordPathMap().put(path, word);
	// } else {
	// path.add(currentIndex);
	//
	// // getValidNextIndexes(currentIndex, path) return an
	// // int[indexes][x+y]
	// // for loop going through the returned array and running findWords
	// // for each one
	//
	// // findWords(word, getNextIndexes(path), path);
	// }
	//
	// }

	// // validateWord method to search if word is in wordList
	// public boolean validateWord(String searchWord) {
	//
	// // arraylist of valid words that start with searchword
	// List<String> validWordSet = new ArrayList<String>();
	//
	// // for loop to find all words that start with searchWord
	// // those words are added to validWordSet
	// for (int k = 0; k < getWordArray().length; k++) {
	// if (getWordArray()[k].startsWith(searchWord)) {
	// // System.out.println(getWordArray()[k]);
	// validWordSet.add(getWordArray()[k]);
	// }
	// }
	//
	// // if the wordList contains the searchWord
	// if (getWordSet().contains(searchWord)) {
	// return false;
	// }
	// // if no words start with searchWord
	// if (validWordSet.size() == 0) {
	// return false;
	// }
	// // if at least one word starts with searchWord
	// else {
	// return true;
	// }
	// }

	// getNextIndexes to return all valid next indexes
	// for validateWord method
	public List<int[]> getNextIndexes(int[] currentIndex) {

		// setting x and y from currentIndex
		int x = currentIndex[0];
		int y = currentIndex[1];

		// nextIndexes list to be returned
		List<int[]> nextIndexes = new ArrayList<int[]>();

		// array w/ all possible indexes touching currentIndex
		int[][] nextIndexValues = { { x - 1, y }, { x, y - 1 }, { x + 1, y }, { x, y + 1 }, { x - 1, y - 1 },
				{ x + 1, y - 1 }, { x - 1, y + 1 }, { x + 1, y + 1 } };

		for (int i = 0; i < nextIndexValues.length; i++) {

			// if statement to make sure no values in the index are
			// smaller than 0 or bigger than 3
			// (aka breaking the boundaries of the board)
			if ((nextIndexValues[i][0] >= 0 && nextIndexValues[i][0] <= 3)
					&& (nextIndexValues[i][1] >= 0 && nextIndexValues[i][1] <= 3)) {

				// if the values aren't breaking boundaries, add to nextIndexes
				nextIndexes.add(new int[] { nextIndexValues[i][0], nextIndexValues[i][1] });
				// System.out.print(Arrays.toString(new int[] {
				// nextIndexValues[i][0], nextIndexValues[i][1] }));
			}

		}

		return nextIndexes;

	}

	// method updating passed in value to board field values
	public char[][] updateToBoard(char[][] boardCopy) {
		boardCopy = new char[getBoard().length][getBoard()[0].length];
		for (int m = 0; m < getBoard().length; m++) {
			for (int n = 0; n < getBoard()[0].length; n++) {
				boardCopy[m][n] = getBoard()[m][n];
			}
		}
		return boardCopy;

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

		List<String> lines = new ArrayList<String>();

		while (scanner.hasNextLine()) {
			String nextLine = scanner.nextLine();
			// if the word is at least 3 letters
			if (nextLine.length() > 2) {
				lines.add(nextLine);
			}
		}

		// making array from arraylist
		setWordList(lines);

		setValidWordSet(new HashSet<String>());
		scanner.close();
	}

	public List<String> getWordList() {
		return wordList;
	}

	public void setWordList(List<String> wordList) {
		this.wordList = wordList;
	}

	public Set<String> getValidWordSet() {
		return validWordSet;
	}

	public void setValidWordSet(Set<String> validWordSet) {
		this.validWordSet = validWordSet;
	}

}
