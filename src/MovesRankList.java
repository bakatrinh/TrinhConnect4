


/**
 * @author Trinh Nguyen
 * Used by computer playing to store possible moves and rank them
 */
public class MovesRankList {
	private int _column;
	private int _row;
	private int _rank;

	public MovesRankList(int column, int row) {
		this._column = column;
		this._row = row;
		this._rank = 1;
	}

	public int getColumn() {
		return _column;
	}

	public int getRow() {
		return _row;
	}

	public int getRank() {
		return _rank;
	}

	public void setRank(int setrank) {
		_rank = setrank;
	}

	public void incrementRank(int amount) {
		_rank = (_rank + amount);
	}

	public void decrementRank(int amount) {
		_rank = (_rank - amount);
	}

	@Override
	public String toString() {
		String temp;

		temp = "[" + _column + "," + _row + "](" + _rank + ")";

		return temp;
	}
}