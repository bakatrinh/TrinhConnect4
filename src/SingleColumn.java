

import java.util.ArrayList;

/**
 * @author Trinh Nguyen
 * Raw data of individual columns with Tokens from each rows. Has codes for insertion
 * and removal
 */
public class SingleColumn {
	private ArrayList<Token> _thisColumn;
	private int _currentSizeOfRow;
	private int _totalRowSize;
	private int _columnNumber;

	public SingleColumn(int rowSize, int columnNumber) {
		_thisColumn = new ArrayList<Token>();
		_columnNumber = columnNumber;
		_totalRowSize = rowSize;
		_currentSizeOfRow = 0;
		for (int i = 0; i < _totalRowSize; i++) {
			_thisColumn.add(Token.BLANK);
		}
	}

	public ArrayList<Token> _getColumn() {
		return _thisColumn;
	}

	public int getColumnNumber() {
		return _columnNumber;
	}
	
	// Returns the index of the latest Token highest on the board
	public int getCurrentSizeOfRow() {
		return _currentSizeOfRow - 1;
	}
	
	public void setCurrentSizeOfRow(int currentSizeOfRow) {
		_currentSizeOfRow = currentSizeOfRow;
	}

	public int getTotalRowSize() {
		return _totalRowSize;
	}

	public void insert(Token playerToken) {
		if (_currentSizeOfRow < _totalRowSize) {
			_thisColumn.set(_currentSizeOfRow, playerToken);
			_currentSizeOfRow++;
		}
	}

	public void removeToken(int row) {
		_thisColumn.set(row, Token.BLANK);
		_currentSizeOfRow--;
	}
	
	// Returns true if column is full
	public boolean isFull() {
		if (_currentSizeOfRow < _totalRowSize) {
			return false;
		} else {
			return true;
		}
	}

	// Returns the token at a specific row
	public Token getTokenAtSlot(int index) {
		return _thisColumn.get(index);
	}

	public void setTokenAtSlot(int index, Token token) {
		_thisColumn.set(index, token);
	}
}
