

import java.util.ArrayList;



/**
 * @author Trinh Nguyen
 * Used to check if there is a straight line winning condition. Returns true
 * if there is. Used by BoardData to determine a winner. Also used by Computer
 * Player to check for winning plays.
 */
public class WinningStrategy {
	private ArrayList<SlotIndexSaver> _slotIndexSaver;

	public boolean checkPlayerWonStraightLine(ArrayList<SingleColumn> allColumns, int sequence, int totalColumnSize,
			int totalRowSize, Token currentPlayerToken, int currentColumnLocation) {

		int currentRowLocation = allColumns.get(currentColumnLocation).getCurrentSizeOfRow();
		int sequenceCounter = 0;

		ArrayList<SlotIndexSaver> slotIndexSaver = new ArrayList<SlotIndexSaver>();

		// horizontal check
		for (int i = 0; i < totalColumnSize; i++) {
			Token currentToken = allColumns.get(i).getTokenAtSlot(currentRowLocation);
			if (currentToken.equals(currentPlayerToken)) {
				slotIndexSaver.add(new SlotIndexSaver(i, currentRowLocation));
				sequenceCounter++;
			} else {
				slotIndexSaver.clear();
				sequenceCounter = 0;
			}
			if (sequenceCounter == sequence) {
				_slotIndexSaver = slotIndexSaver;
				return true;
			}
		}
		slotIndexSaver.clear();
		sequenceCounter = 0;

		// vertical check
		for (int i = 0; i < totalRowSize; i++) {
			Token currentToken = allColumns.get(currentColumnLocation).getTokenAtSlot(i);
			if (currentToken.equals(currentPlayerToken)) {
				slotIndexSaver.add(new SlotIndexSaver(currentColumnLocation, i));
				sequenceCounter++;
			} else {
				slotIndexSaver.clear();
				sequenceCounter = 0;
			}
			if (sequenceCounter == sequence) {
				_slotIndexSaver = slotIndexSaver;
				return true;
			}
		}
		slotIndexSaver.clear();
		sequenceCounter = 0;

		int column;
		int row;

		// forward diagonal check

		// finds the lowest diagonal left corner from the point
		column = currentColumnLocation;
		row = currentRowLocation;
		while (row != 0 && column != 0) {
			row--;
			column--;
		}

		for (; column < totalColumnSize && row < totalRowSize; row++, column++) {
			Token currentToken = allColumns.get(column).getTokenAtSlot(row);
			if (currentToken.equals(currentPlayerToken)) {
				slotIndexSaver.add(new SlotIndexSaver(column, row));
				sequenceCounter++;
			} else {
				slotIndexSaver.clear();
				sequenceCounter = 0;
			}
			if (sequenceCounter == sequence) {
				_slotIndexSaver = slotIndexSaver;
				return true;
			}
		}

		slotIndexSaver.clear();
		sequenceCounter = 0;

		// backward diagonal check

		// finds the bottom most diagonal corner from the current spot
		column = currentColumnLocation;
		row = currentRowLocation;
		while (row > 0 && column < totalColumnSize - 1) {
			row--;
			column++;
		}

		for (; column >= 0 && row < totalRowSize; row++, column--) {
			Token currentToken = allColumns.get(column).getTokenAtSlot(row);
			if (currentToken.equals(currentPlayerToken)) {
				slotIndexSaver.add(new SlotIndexSaver(column, row));
				sequenceCounter++;
			} else {
				slotIndexSaver.clear();
				sequenceCounter = 0;
			}
			if (sequenceCounter == sequence) {
				_slotIndexSaver = slotIndexSaver;
				return true;
			}
		}

		return false;
	}

	public ArrayList<SlotIndexSaver> getSlotIndexSaver() {
		return _slotIndexSaver;
	}

	public boolean checkDoubleEndedHorizontal(ArrayList<SingleColumn> allColumns, int sequence, int totalColumnSize,
			int totalRowSize, Token currentPlayerToken, int currentColumnLocation) {

		int currentRowLocation = allColumns.get(currentColumnLocation).getCurrentSizeOfRow();
		int sequenceCounter = 0;


		// horizontal double end check
		if (currentRowLocation == 0) {
			for (int i = 0; i < totalColumnSize; i++) {
				if ((i + sequence) >= totalColumnSize) {
					return false;
				}
				Token currentToken = allColumns.get(i).getTokenAtSlot(currentRowLocation);
				if (currentToken.equals(Token.BLANK)) {
					sequenceCounter++;
					int j = 1;
					for (; j < sequence; j++) {
						currentToken = allColumns.get(i + j).getTokenAtSlot(currentRowLocation);
						if (currentToken.equals(currentPlayerToken)) {
							sequenceCounter++;
						}
					}
					if (sequenceCounter == sequence) {
						currentToken = allColumns.get(i + j).getTokenAtSlot(currentRowLocation);
						if (currentToken.equals(Token.BLANK)) {
							return true;
						}
					}
				}
				sequenceCounter = 0;
			}
			sequenceCounter = 0;
		}
		else {
			Token tokenBelowCurrent;
			for (int i = 0; i < totalColumnSize; i++) {
				if ((i + sequence) >= totalColumnSize) {
					return false;
				}
				Token currentToken = allColumns.get(i).getTokenAtSlot(currentRowLocation);
				tokenBelowCurrent = allColumns.get(i).getTokenAtSlot(currentRowLocation - 1);
				if (currentToken.equals(Token.BLANK) && !tokenBelowCurrent.equals(Token.BLANK)) {
					sequenceCounter++;
					int j = 1;
					for (; j < sequence; j++) {
						currentToken = allColumns.get(i + j).getTokenAtSlot(currentRowLocation);
						if (currentToken.equals(currentPlayerToken)) {
							sequenceCounter++;
						}
					}
					if (sequenceCounter == sequence) {
						currentToken = allColumns.get(i + j).getTokenAtSlot(currentRowLocation);
						tokenBelowCurrent = allColumns.get(i + j).getTokenAtSlot(currentRowLocation - 1);
						if (currentToken.equals(Token.BLANK) && !tokenBelowCurrent.equals(Token.BLANK)) {
							return true;
						}
					}
				}
				sequenceCounter = 0;
			}
		}

		return false;
	}

	public boolean checkDoubleEndedFowardDiagonal(ArrayList<SingleColumn> allColumns, int sequence, int totalColumnSize,
			int totalRowSize, Token currentPlayerToken, int currentColumnLocation) {

		int currentRowLocation = allColumns.get(currentColumnLocation).getCurrentSizeOfRow();
		int sequenceCounter = 0;
		Token tokenBelowCurrent;
		
		// finds the lowest diagonal left corner from the point
		int column = currentColumnLocation;
		int row = currentRowLocation;
		while (row != 0 && column != 0) {
			row--;
			column--;
		}

		// Forward diagonal double end check
		for (; column < totalColumnSize && row < totalRowSize; row++, column++) {
			if ((column + sequence) >= totalColumnSize || ((row + sequence) >= totalRowSize)) {
				return false;
			}
			Token currentToken = allColumns.get(column).getTokenAtSlot(row);
			if (currentToken.equals(Token.BLANK)) {
				if (row == 0) {
					sequenceCounter++;
				}
				else {
					tokenBelowCurrent = allColumns.get(column).getTokenAtSlot(row - 1);
					if (!tokenBelowCurrent.equals(Token.BLANK)) {
						sequenceCounter++;
					}
				}
				int j = 1;
				for (; j < sequence; j++) {
					currentToken = allColumns.get(column + j).getTokenAtSlot(row + j);
					if (currentToken.equals(currentPlayerToken)) {
						sequenceCounter++;
					}
				}
				if (sequenceCounter == sequence) {
					currentToken = allColumns.get(column + j).getTokenAtSlot(row + j);
					tokenBelowCurrent = allColumns.get(column + j).getTokenAtSlot(row + j - 1);
					if (currentToken.equals(Token.BLANK) && !tokenBelowCurrent.equals(Token.BLANK)) {
						return true;
					}
				}
			}
			sequenceCounter = 0;
		}
		sequenceCounter = 0;
		return false;
	}

	public boolean checkDoubleEndedBackwardDiagonal(ArrayList<SingleColumn> allColumns, int sequence, int totalColumnSize,
			int totalRowSize, Token currentPlayerToken, int currentColumnLocation) {

		int currentRowLocation = allColumns.get(currentColumnLocation).getCurrentSizeOfRow();
		int sequenceCounter = 0;
		Token tokenBelowCurrent;
		// finds the bottom most diagonal corner from the current spot
		int column = currentColumnLocation;
		int row = currentRowLocation;
		while (row > 0 && column < totalColumnSize - 1) {
			row--;
			column++;
		}

		// Backward diagonal double end check
		for (; column >= 0 && row < totalRowSize; row++, column--) {
			if ((column - sequence) < 0 || ((row + sequence) >= totalRowSize)) {
				return false;
			}
			Token currentToken = allColumns.get(column).getTokenAtSlot(row);
			if (currentToken.equals(Token.BLANK)) {
				if (row == 0) {
					sequenceCounter++;
				}
				else {
					tokenBelowCurrent = allColumns.get(column).getTokenAtSlot(row - 1);
					if (!tokenBelowCurrent.equals(Token.BLANK)) {
						sequenceCounter++;
					}
				}
				int j = 1;
				for (; j < sequence; j++) {
					currentToken = allColumns.get(column - j).getTokenAtSlot(row + j);
					if (currentToken.equals(currentPlayerToken)) {
						sequenceCounter++;
					}
				}
				if (sequenceCounter == sequence) {
					currentToken = allColumns.get(column - j).getTokenAtSlot(row + j);
					tokenBelowCurrent = allColumns.get(column - j).getTokenAtSlot(row + j - 1);
					if (currentToken.equals(Token.BLANK) && !tokenBelowCurrent.equals(Token.BLANK)) {
						return true;
					}
				}
			}
			sequenceCounter = 0;
		}
		sequenceCounter = 0;
		return false;
	}

}
