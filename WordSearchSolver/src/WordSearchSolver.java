import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import ArrayList.ArrayList;
import ciic4020.map.Map;

public class WordSearchSolver {

	public static void main(String[] args) {
		ArrayList<String[]> rows = new ArrayList<String[]>(10);
		ArrayList<String> words = new ArrayList<String>(1);
		words.add("CAT");
		words.add("DOG");
		words.add("ELEPHANT");
		words.add("HORSE");
		words.add("BIRD");
		words.add("HUMAN");
		File sopa = new File("..\\WordSearchSolver\\res\\sample1.txt");

		try (Scanner s = new Scanner(sopa, StandardCharsets.UTF_8.name())) {
			while (s.hasNextLine()) {
				String line = s.nextLine();
				//the name in the format are the first elements before the comma.
				String[] currRow = line.split("");
				rows.add(currRow);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (String[] s : rows) {
			String str = "";
			for (int i = 0; i < s.length; i++) {
				str += s[i];
			}
			System.out.println(str);
		}

		Solver mySolver = new Solver(rows, words);
		Map<String, ArrayList<Integer>> locations = mySolver.getLocations();

		SolutionPrinter(locations);
	}

	public static void SolutionPrinter(Map<String, ArrayList<Integer>> locations) {
		ArrayList<ArrayList<String>> rowsSolved = new ArrayList<ArrayList<String>>(10);
		for (int i = 0; i < 10; i++) {
			ArrayList<String> rowsEmpty = new ArrayList<String>(10);
			for (int j = 0; j < 10; j++) {
				rowsEmpty.add(" ");
			}
			rowsSolved.add(rowsEmpty);
		}
		for (String word : locations.getKeys()) {
			int rowIndex = locations.get(word).get(0);
			int clmnIndex = locations.get(word).get(1);
			int currLetter = 0;
			String[] myWord = word.split("");
			System.out.println(word + " is " + locations.get(word).get(2));
			switch(locations.get(word).get(2)) {

			case 0: // horRight
				for (int i = 0; i < word.length(); i++) {
					rowsSolved.get(rowIndex).add(clmnIndex++, myWord[currLetter++]);
				}
				break;
			case 1: //horLeft
				for (int i = 0; i < word.length(); i++) {
					rowsSolved.get(rowIndex).add(clmnIndex--, myWord[currLetter++]);
				}
				break;
			case 2: //verUp
				for (int i = 0; i < word.length(); i++) {
					rowsSolved.get(rowIndex--).add(clmnIndex, myWord[currLetter++]);
				}
				break;
			case 3: //verDown
				for (int i = 0; i < word.length(); i++) {
					rowsSolved.get(rowIndex++).add(clmnIndex, myWord[currLetter++]);
				}
				break;
			case 4: //diagUpRight
				for (int i = 0; i < word.length(); i++) {
					rowsSolved.get(rowIndex--).add(clmnIndex++, myWord[currLetter++]);
				}
				break;
			case 5: //diagUpLeft
				for (int i = 0; i < word.length(); i++) {
					rowsSolved.get(rowIndex--).add(clmnIndex--, myWord[currLetter++]);
				}
				break;
			case 6: //diagDownRight
				for (int i = 0; i < word.length(); i++) {
					rowsSolved.get(rowIndex++).add(clmnIndex++, myWord[currLetter++]);
				}
				break;
			case 7: //diagDownLeft
				for (int i = 0; i < word.length(); i++) {
					rowsSolved.get(rowIndex++).add(clmnIndex--, myWord[currLetter++]);
				}
				break;
			default:
				break;
			}
			System.out.println(word + " is in!");
		}
		for (ArrayList<String> currRow: rowsSolved) {
			String myRow = "";
			for (String r: currRow) {
				myRow += r;
			}
			System.out.println(myRow);
		}
	}
}

