import ArrayList.ArrayList;
import ciic4020.hashtable.HashTableSC;
import ciic4020.hashtable.SimpleHashFunction;
import ciic4020.map.Map;

public class Solver {

	public static enum direction {
		horRight, horLeft, verUp, verDown, diagUpRight, diagUpLeft, diagDownRight, diagDownLeft
	}
	//for now: 0, 1, 2 , 3 , 4, 5, 6
	
	private ArrayList<String[]> rows;
	private int nRows; 
	private int nColumns;
	ArrayList<String> words;
	Map<String, ArrayList<Integer>> locations;

	public Solver(ArrayList<String[]> rows, ArrayList<String> words) {
		this.rows = rows;
		this.nRows = rows.size();
		this.nColumns = rows.get(0).length; //at least 1 and all must have same size
		this.words = words;
	}
	
	public boolean isValid() {
		int columnLength = nColumns;
		for (String[] clmn : rows) {
			if(clmn.length != columnLength) {
				return false;
			}
		}
		return true;
	}
	
	
	public Map<String, ArrayList<Integer>> getLocations() {
		Map<String, ArrayList<Integer>> myMap = new HashTableSC<String, ArrayList<Integer>>(10, new SimpleHashFunction<>());
		//order letters go at same time
		for (String currWord : words) {
			System.out.println("looking for... " + currWord);
			ArrayList<Integer> coord = find(currWord);
			if(coord != null) {
				myMap.put(currWord, coord);
				System.out.println("Found!");
			}
		}
		return myMap;
	}
	//should i traverse once before to get all the letters?
	public ArrayList<Integer> find(String currWord) {
		ArrayList<Integer> hereItIs = null;
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nColumns; j++) {
				if(rows.get(i)[j].equals(currWord.split("")[0])) {
					System.out.println("found first letter at: " + i +" " + j);
					hereItIs = findFromFirst(i, j, currWord);
					if(hereItIs != null) {
						return hereItIs;
					}
				}
			}
		}
		return hereItIs; //TODO is not here
	}
	//get first letter and try all locations
	private ArrayList<Integer> findFromFirst(int rowIndex, int clmnIndex, String currWord) {
		boolean found = false;
		int directionIndex = -1;
		//horRight: 0
		if(currWord.length() <= nColumns - clmnIndex) { //there's enough space, look right
			directionIndex = findHorRight(rowIndex, clmnIndex, currWord);
			if(directionIndex != -1) {
				found = true;
			}
		}
		
		//horLeft: 1
		if(!found && (currWord.length() <= clmnIndex + 1)) { //there's enough space, look left
			directionIndex = findHorLeft(rowIndex, clmnIndex, currWord);
			if(directionIndex != -1) {
				found = true;
			}
		}
		//verUp: 2
		if(!found && (currWord.length() <= rowIndex + 1)) { //there's enough space, look up
			directionIndex = findVerUp(rowIndex, clmnIndex, currWord);
			if(directionIndex != -1) {
				found = true;
			}
		}
		//verDown: 3
		if(!found && (currWord.length() <= nRows - rowIndex)) { //there's enough space, look down
			directionIndex = findVerDown(rowIndex, clmnIndex, currWord);
			if(directionIndex != -1) {
				found = true;
			}
		}
		//diagUpRight: 4
		if(!found && ((currWord.length() <= rowIndex + 1) && (currWord.length() <= nColumns - clmnIndex))) { 
		//there has to be enough space to the right and up.
			directionIndex = findDiagUpRight(rowIndex, clmnIndex, currWord);
			if(directionIndex != -1) {
				found = true;
			}
		}
		//diagUpLeft: 5
		if(!found && ((currWord.length() <= rowIndex + 1) && (currWord.length() <= clmnIndex+1) )) { 
		//there has to be enough space to the left and up.
			directionIndex = findDiagUpLeft(rowIndex, clmnIndex, currWord);
			if(directionIndex != -1) {
				found = true;
			}
		}
		//diagDownRight: 6
		if(!found && ((currWord.length() <= nRows - rowIndex) && (currWord.length() <= nColumns - clmnIndex))) { 
		//there has to be enough space to the right and down.
			directionIndex = findDiagDownRight(rowIndex, clmnIndex, currWord);
			if(directionIndex != -1) {
				found = true;
			}
		}
		//diagDownLeft: 7
		if(!found && ((currWord.length() <= nRows - rowIndex) && (currWord.length() < clmnIndex+1)) ) { 
		//there has to be enough space to the left and down.
			directionIndex = findDiagDownLeft(rowIndex, clmnIndex, currWord);
			if(directionIndex != -1) {
				found = true;
			}
		}
		if(found) {
			ArrayList<Integer> isAt = new ArrayList<Integer>(3);
			isAt.add(rowIndex);
			isAt.add(clmnIndex);
			isAt.add(directionIndex);
			return isAt; //pass down coordinates
		}
		return null;
	}
	
	//returns direction index
	private Integer findHorRight(int rowIndex, int clmnIndex, String currWord) {
		//horRight: 0
		int j = 0;
		int i = clmnIndex;
		while(j < currWord.length() && (currWord.charAt(j) == rows.get(rowIndex)[i].charAt(0))) {
			j++;
			i++;
		}
		if(j >= currWord.length()) {
			return 0;
		}
		return -1;
	}

	private Integer findHorLeft(int rowIndex, int clmnIndex, String currWord) {
		//horLeft: 1
		int j = 0;
		int i = clmnIndex;
		while(j < currWord.length() && (currWord.charAt(j) == rows.get(rowIndex)[i].charAt(0))) {
			j++;
			i--;
		}
		if(j >= currWord.length()) {
			return 1;
		}
		return -1;
	}
	
	private Integer findVerUp(int rowIndex, int clmnIndex, String currWord) {
		//verUp: 2
		int j = 0;
		int i = rowIndex;
		while(j < currWord.length() && (currWord.charAt(j) == rows.get(i)[clmnIndex].charAt(0))) {
			j++;
			i--;
		}
		if(j >= currWord.length()) {
			return 2;
		}
		return -1;
	}

	private Integer findVerDown(int rowIndex, int clmnIndex, String currWord) {
		//verDown: 3
		int j = 0;
		int i = rowIndex;
		while(j < currWord.length() && (currWord.charAt(j) == rows.get(i)[clmnIndex].charAt(0))) {
			j++;
			i++;
		}
		if(j >= currWord.length()) {
			return 3;
		}
		return -1;
	}
	
	private Integer findDiagUpRight(int rowIndex, int clmnIndex, String currWord) {
		//diagUpRight: 4
		int j = 0;
		int i = rowIndex;
		int k = clmnIndex;
		while(j < currWord.length() && (currWord.charAt(j) == rows.get(i)[k].charAt(0)) ) {
			j++;
			i--;
			k++;
		}
		if(j >= currWord.length()) {
			return 4;
		}
		return -1;
	}
	
	private Integer findDiagUpLeft(int rowIndex, int clmnIndex, String currWord) {
		//diagUpLeft: 5
		int j = 0;
		int i = rowIndex;
		int k = clmnIndex;
		while(j < currWord.length() && (currWord.charAt(j) == rows.get(i)[k].charAt(0))) {
			j++;
			i--;
			k--;
		}
		if(j >= currWord.length()) {
			return 5;
		}
		return -1;
	}
	
	private Integer findDiagDownRight(int rowIndex, int clmnIndex, String currWord) {
		//diagDownRight: 6
		int j = 0;
		int i = rowIndex;
		int k = clmnIndex;
		while(j < currWord.length() && (currWord.charAt(j) == rows.get(i)[k].charAt(0))) {
			j++;
			i++;
			k++;
		}
		if(j >= currWord.length()) {
			return 6;
		}
		return -1;
	}
	
	private Integer findDiagDownLeft(int rowIndex, int clmnIndex, String currWord) {
		//diagDownLeft: 7
		int j = 0;
		int i = rowIndex;
		int k = clmnIndex;
		while(j < currWord.length() && (currWord.charAt(j) == rows.get(i)[k].charAt(0))) {
			j++;
			i++;
			k--;
		}
		if(j >= currWord.length()) {
			return 7;
		}
		return -1;
	}
}
