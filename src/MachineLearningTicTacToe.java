import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/*
 * 
 *  Author: Halvar Kelm
 * 
 * 	This program is using machine learning to get the best field for any TicTacToe possibility
 */
public class MachineLearningTicTacToe {
	private static final int SIZE = 3;
	private static final int POSSIBILITIES = 19863;
	
	// saves the corresponding action
	static int[] round = new int[POSSIBILITIES];
	int position = 0;
	static ArrayList<int[]> history = null;

	// public static ArrayList<int[]> knownFields;

	public static void main(String[] args) {
		/*
		 * TODO: 1. Let the machine learning alg play against the random
		 * computer 2. Save a number for any possiblity 3. Save for every
		 * possibility a field to chose 4. change the field to chose till the
		 * win/draw possibility is 100%
		 */
		// knownFields = new ArrayList<int[]>();
		/*int[] field = new int[SIZE * SIZE];
		for (int i = 0; i < SIZE * SIZE; i++)
			field[i] = 0;
		printScreen(field);
		System.out.println("is "+calcField(field));
		field[0] = 1;
		printScreen(field);
		System.out.println("is "+calcField(field));
		for (int i = 0; i < SIZE * SIZE; i++)
			field[i] = 2;
		printScreen(field);
		System.out.println("is "+calcField(field));*/
		init();
		play();
		//train();
		
		// System.out.println("I found " + knownFields.size() + " fields.");
		// BEST: 380318 bei 100000
		// 570672 bei 150000
		// 570419 bei 150000
		// 570442
		// 571000
		  
	}
	
	private static void train(){
		double winPercentage = 0.0;
		double wongames = 0.0;
		double games = 0.0;
		for (int i = 0; i < 1000000; i++) {
			games++;
			boolean won = play();
			if (won) {
				wongames++;
			}
		}
		
		System.out.println("Played games: " + games);
		winPercentage = (wongames / games) * 100;
		System.out.println("You won " + winPercentage + "%.");
	}
	
	private static void init(){
		for(int i = 0; i < POSSIBILITIES; i++){
			round[i] = ThreadLocalRandom.current().nextInt(0, 8 + 1);;
		}
	}
	
	private static int calcField(int[] field){
		int finalInt = 0;
		int[] threes = {1,3,9,27,81,243,729,2187,6561};
		for(int i = 0; i < SIZE * SIZE; i++){
			finalInt += field[i]*threes[i];
		}
		return finalInt;
	}
	
	private static boolean play() {
		history = new ArrayList<int[]>();
		int[] field = new int[SIZE * SIZE];
		for (int i = 0; i < SIZE * SIZE; i++)
			field[i] = 0;

		boolean play = false;
		int randomNum = ThreadLocalRandom.current().nextInt(1, 2 + 1);

		if (randomNum == 1) {
			play = true;
		}

		int win = 0;
		boolean draw = false;

		while (win == 0 && draw == false) {
			if (play == true) {
				//save field in history
				history.add(field);
				//checkRandomField(field, true);
				field[getRightField(field)] = 1;
				/*
				 * boolean isInKnown = false; for (int i = 0; i <
				 * knownFields.size(); i++) { if
				 * (!isDifferent(knownFields.get(i), field)) { isInKnown = true;
				 * } } if (isInKnown == false) { knownFields.add(field); }
				 * isInKnown = false;
				 */
				play = !play;
			} else {
				checkRandomField(field, false);
				play = !play;
			}
			 printScreen(field);
			win = checkForWin(field);
			if (win == 0) {
				draw = checkForDraw(field);
			}
		}
		boolean endwin = false;
		if (win == 1 || draw == true) {
			endwin = true;
		}
		return endwin;
	}
	
	private static int getRightField(int[] field){
		int fieldNr = calcField(field);
		return round[fieldNr];
	}

	private static boolean isDifferent(int field1[], int field2[]) {
		boolean isNotTheSame = true;
		for (int i = 0; i < field1.length; i++) {
			if (field1[i] != field2[i]) {
				isNotTheSame = true;
			}
		}
		return isNotTheSame;
	}

	private static void checkRandomField(int[] field, boolean ml) {
		int freeFields = 0;
		for (int i = 0; i < SIZE * SIZE; i++) {
			if (field[i] == 0) {
				freeFields++;
			}
		}
		int[] Values = new int[freeFields];
		int k = 0;
		for (int i = 0; i < SIZE * SIZE; i++) {
			if (field[i] == 0) {
				Values[k] = i;
				k++;
			}
		}
		int randomNum = ThreadLocalRandom.current().nextInt(0, freeFields + 1) / 2;
		if (ml == true) {
			field[Values[randomNum]] = 1;
		} else {
			field[Values[randomNum]] = 2;
		}

	}

	private static void printScreen(int[] field) {
		for (int i = 0, j = 0; i < SIZE * SIZE; i++) {
			j++;
			System.out.print("[" + field[i] + "]");
			if (j == 3) {
				System.out.println("");
				j = 0;
			}
		}
	}

	private static boolean checkForDraw(int[] inputField) {
		int freeFields = 0;
		for (int i = 0; i < SIZE * SIZE; i++) {
			if (inputField[i] == 0) {
				freeFields++;
			}
		}
		if (freeFields > 0) {
			return false;
		} else {
			return true;
		}
	}

	private static int checkForWin(int[] InputField) {
		/*
		 * WIN CASES: - ALL OF A ROW - ALL OF A COLUMN - LEFT UPPER CORNER TO
		 * RIGHT LOWER - TOP RIGHT CORNER TO LEFT LOWER
		 */

		// Inputfield to 2dimensional array
		int[][] field = new int[SIZE][SIZE];
		int x = 0;
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				field[i][j] = InputField[x++];
			}
		}

		// Check for Vertical and Horizontal
		boolean win = true;
		for (int k = 0; k < 2; k++) {
			for (int i = 0; i < SIZE; i++) {
				win = true;
				int Check = -1;
				if (k == 0) {
					Check = field[i][0];
				} else {
					Check = field[0][i];
				}
				for (int j = 0; j < SIZE; j++) {
					if (k == 0) {
						if (field[i][j] != Check) {
							win = false;
							break;
						}
					} else {
						if (field[j][i] != Check) {
							win = false;
							break;
						}
					}
				}
				if (win == true) {

					if (Check == 1) {
						return 1;
					} else if (Check == 2) {
						return 2;
					}
				}
			}
		}

		// Check for diagonal win
		for (int k = 0; k < 2; k++) {
			win = true;
			int Check = -1;
			if (k == 0) {
				Check = field[0][0];
				int i = 0, j = 0;
				while (i + 1 < SIZE && j + 1 < SIZE) {
					int next = field[i + 1][j + 1];
					if (next != Check) {
						win = false;
					}
					Check = next;
					i++;
					j++;
				}
			} else {
				Check = field[0][SIZE - 1];
				int i = 0, j = SIZE - 1;
				while (i + 1 < SIZE && j - 1 >= 0) {
					int next = field[i + 1][j - 1];
					if (next != Check) {
						win = false;
					}
					Check = next;
					i++;
					j--;
				}
			}

			if (win == true && Check == 1) {
				return 1;
			} else if (win == true && Check == 2) {
				return 2;
			}
		}
		return 0;
	}
}