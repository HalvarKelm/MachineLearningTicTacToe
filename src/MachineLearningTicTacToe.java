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

	// saves the corresponding action
	int[] round = null;
	int position = 0;
	int[] history = null;
	public static ArrayList<int[]> knownFields;

	public static void main(String[] args) {
		/*
		 * TODO: 1. Let the machine learning alg play against the random
		 * computer 2. Save a number for any possiblity 3. Save for every
		 * possibility a field to chose 4. change the field to chose till the
		 * win/draw possibility is 100%
		 */
		knownFields = new ArrayList<int[]>();
		double winPercentage = 0.0;
		double wongames = 0.0;
		double games = 0.0;
		for (int i = 0; i < 100000; i++) {
			games++;
			boolean won = play();
			if (won) {
				wongames++;
			}
		}
		System.out.println("Played games: " + games);
		winPercentage = (wongames / games) * 100;
		System.out.println("You won " + winPercentage + "%.");
		System.out.println("I found " + knownFields.size() + " fields.");
	}

	private static boolean play() {
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
				checkRandomField(field, true);
				boolean isInKnown = false;
				for (int i = 0; i < knownFields.size(); i++) {
					if (!isDifferent(knownFields.get(i), field)) {
						isInKnown = true;
					}
				}
				if (isInKnown == false) {
					knownFields.add(field);
				}
				isInKnown = false;
				play = !play;
			} else {
				checkRandomField(field, false);
				play = !play;
			}
			// printScreen(field);
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