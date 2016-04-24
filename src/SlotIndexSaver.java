


/**
 * @author Trinh Nguyen
 * Stores simple columns and rows number.
 * Used by BoardData and WinningStrategy to store
 * locations of pieces that are part of the winning sequence.
 * This allows the gui to change these pieces to blinking pieces.
 * Also used to store history of moves for the undo feature
 */
public class SlotIndexSaver {
	private int _row;
	private int _column;

	public SlotIndexSaver(int column, int row) {
		_column = column;
		_row = row;
	}

	public int getColumn() {
		return _column;
	}

	public int getRow() {
		return _row;
	}

}
