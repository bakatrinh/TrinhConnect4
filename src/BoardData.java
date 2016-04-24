
import java.util.ArrayList;

/**
 * @author Trinh Nguyen BoardData holds the data of the current board, it's
 *         players, and all of its attributes. GUI reads the data here to build
 *         a graphical board.
 */
public class BoardData {
	// Basic attributes in the game.
	private int _totalMoves;
	private int _columnSize;
	private int _rowSize;
	private int _sequence;

	// Contains all the columns in this board (each Column holds data for
	// individual slots)
	private ArrayList<SingleColumn> _allColumns;
	// Used when checking for winning condition (all slots part of the winning
	// sequence are saved here)
	private ArrayList<SlotIndexSaver> _slotIndexSaver;
	// Reference to the GUI in case the board needs to be refreshed
	// Used to store history of moves for undo
	private ArrayList<SlotIndexSaver> _movesHistory;
	private BoardColumnsGUI _boardGUI;
	// This sets the color of whoever's turn it is using switchTokenColor()
	private Token _token = Token.RED;
	// Used to identify whether this player is a human or computer
	private Connect4Player _yellowPlayer;
	// Used to identify whether this player is a human or computer
	private Connect4Player _redPlayer;
	// checks to see if someone won the game
	private boolean _gameWon;
	// checks to see if the game ended in a draw
	private boolean _gameDraw;
	// if new game button is pushed. Set's this to true to close old threads
	private boolean _newGame = false;
	// Computer uses this to check if the new game JDialog is opened and pauses its computation.
	private boolean _pause = false;

	public BoardData(int columnSize, int rowSize, int sequence, PlayerType redPlayer, PlayerType yellowPlayer) {
		_gameWon = false;
		_gameDraw = false;
		_allColumns = new ArrayList<SingleColumn>();
		_movesHistory = new ArrayList<SlotIndexSaver>();
		_slotIndexSaver = new ArrayList<SlotIndexSaver>();
		_columnSize = columnSize;
		_rowSize = rowSize;
		_sequence = sequence;
		_totalMoves = 0;
		_token = Token.RED;
		setYellowPlayerType(yellowPlayer);
		setRedPlayerType(redPlayer);

		// creates an empty board based on the size given
		for (int i = 0; i < _columnSize; i++) {
			_allColumns.add(new SingleColumn(_rowSize, i));
		}
	}

	public int getMovesTotal() {
		return _totalMoves;
	}

	public int getRowSize() {
		return _rowSize;
	}

	public int getColumnSize() {
		return _columnSize;
	}

	public int totalSlots() {
		return _rowSize * _columnSize;
	}

	public int getSequence() {
		return _sequence;
	}

	public boolean getNewGameStatus() {
		return _newGame;
	}

	public void setNewGame() {
		_newGame = true;
	}

	// attaches a UI to the current BoardData
	public void setBoardGUI(BoardColumnsGUI boardGUI) {
		_boardGUI = boardGUI;
	}

	// Sets the current yellow player to either a HumanPlayer or ComputerPlayer
	public void setYellowPlayerType(PlayerType player) {
		if (player.equals(PlayerType.HUMAN)) {
			_yellowPlayer = new HumanPlayer(this, Token.YELLOW);
		} else {
			_yellowPlayer = new ComputerPlayer(this, Token.YELLOW);
		}
	}

	// Sets the current red player to either a HumanPlayer or ComputerPlayer
	public void setRedPlayerType(PlayerType player) {
		if (player.equals(PlayerType.HUMAN)) {
			_redPlayer = new HumanPlayer(this, Token.RED);
		} else {
			_redPlayer = new ComputerPlayer(this, Token.RED);
		}
	}

	public Connect4Player getYellowPlayer() {
		return _yellowPlayer;
	}

	public Connect4Player getRedPlayer() {
		return _redPlayer;
	}

	// Accesses a specific column on the board
	public SingleColumn getSingleColumnAtIndex(int i) {
		return _allColumns.get(i);
	}

	// Access the array of all the columns on the board
	public ArrayList<SingleColumn> getAllColumns() {
		return _allColumns;
	}

	// Called every time an insert has been called to switch player turns
	private void switchTokenColor() {
		if (_token.equals(Token.RED)) {
			_token = Token.YELLOW;
		} else {
			_token = Token.RED;
		}
	}

	// Used to find out which player's turn it is
	public Token getTokenOfCurrentActivePlayer() {
		return _token;
	}

	public boolean checkGameWon() {
		return _gameWon;
	}

	public boolean checkGameDraw() {
		return _gameDraw;
	}

	// Used by GUI to know when a Human player is allowed to make a move
	public boolean getComputerStatus() {
		if (_token.equals(Token.RED)) {
			if (_redPlayer instanceof ComputerPlayer) {
				return true;
			}
		} else {
			if (_token.equals(Token.YELLOW)) {
				if (_yellowPlayer instanceof ComputerPlayer) {
					return true;
				}
			}
		}
		return false;
	}

	// The main insertion method used by both players. Checks for winning/draw
	// condition
	// and switches _token afterward
	public synchronized void insertIntoBoard(int column) {
		if (!_gameWon && !_gameDraw) {
			_allColumns.get(column).insert(_token);
			_movesHistory.add(new SlotIndexSaver(column, _allColumns.get(column).getCurrentSizeOfRow()));
			WinningStrategy winCondition = new WinningStrategy();
			if (winCondition.checkPlayerWonStraightLine(_allColumns, _sequence, _columnSize, _rowSize, _token,
					column)) {
				_slotIndexSaver = winCondition.getSlotIndexSaver();
				_gameWon = true;
				for (int i = 0; i < _slotIndexSaver.size(); i++) {
					if (_token.equals(Token.YELLOW)) {
						_allColumns.get(_slotIndexSaver.get(i).getColumn())
						.setTokenAtSlot(_slotIndexSaver.get(i).getRow(), Token.BLINKINGYELLOW);
					} else {
						_allColumns.get(_slotIndexSaver.get(i).getColumn())
						.setTokenAtSlot(_slotIndexSaver.get(i).getRow(), Token.BLINKINGRED);
					}
				}
				_totalMoves++;
			} else {
				if (_totalMoves != _columnSize * _rowSize) {
					_totalMoves++;
				}
				switchTokenColor();

				if (_totalMoves == (_columnSize * _rowSize)) {
					_gameDraw = true;
				}
			}
		}
		refreshBoard();
	}

	public void restorePreBlink() {
		for (int i = 0; i < _slotIndexSaver.size(); i++) {
			if (_token.equals(Token.YELLOW)) {
				_allColumns.get(_slotIndexSaver.get(i).getColumn()).setTokenAtSlot(_slotIndexSaver.get(i).getRow(),
						Token.YELLOW);
			} else {
				_allColumns.get(_slotIndexSaver.get(i).getColumn()).setTokenAtSlot(_slotIndexSaver.get(i).getRow(),
						Token.RED);
			}
		}
		refreshBoard();
	}

	public void refreshBoard() {
		_boardGUI.refreshBoard();
	}

	public void undo() {
		if (!_movesHistory.isEmpty()) {
			int column = _movesHistory.get(_movesHistory.size() - 1).getColumn();
			int row = _movesHistory.get(_movesHistory.size() - 1).getRow();
			_movesHistory.remove(_movesHistory.size() - 1);
			if (_gameWon) {
				restorePreBlink();
				switchTokenColor();
			}
			_allColumns.get(column).removeToken(row);
			_totalMoves--;
			_gameWon = false;
			_gameDraw = false;
			switchTokenColor();
			_boardGUI.refreshBoard();
		}
	}
	
	public void pause() {
		_pause = true;
	}
	
	public void unpause() {
		_pause = false;
	}
	
	public boolean getPause() {
		return _pause;
	}
}