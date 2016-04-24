
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Trinh Nguyen
 * Implements a Connect4Player. Acts as a player and uses some AI calculation to return
 * a move to be inserted into the board of BoardData
 */
public class ComputerPlayer implements Connect4Player {
	private Token _playerToken;
	private BoardData _board;
	private PlayerType _playerType;
	private Timer _timer;
	private static final int _delay = 800;

	public ComputerPlayer(BoardData board, Token playerToken) {
		_board = board;
		_playerToken = playerToken;
		_playerType = PlayerType.COMPUTER;
		_timer = new Timer();
	}

	@Override
	public void setTokenColor(Token token) {
		_playerToken = token;
	}

	@Override
	public Token getPlayerToken() {
		return _playerToken;
	}

	@Override
	public void setBoard(BoardData board) {
		_board = board;
	}

	@Override
	public String toString() {
		return "Computer";
	}

	@Override
	public PlayerType getPlayerType() {
		return _playerType;
	}

	@Override
	public void makeMove() {
		if (_board.getNewGameStatus()) {
			if (_timer != null) {
				_timer.cancel();
			}
		}
		if (_board.getPause()){
			
		}
		else if (_board.getTokenOfCurrentActivePlayer().equals(_playerToken) && 
				!_board.checkGameDraw() && !_board.checkGameWon()) {
			_board.insertIntoBoard(getMove());
		}
		else {

		}

	}

	public void startPlaying() {
		_timer.schedule(new makeMoveTask(), _delay, _delay);
	}

	class makeMoveTask extends TimerTask {
		public void run() {
			makeMove();
		}
	}

	private synchronized int getMove() {
		MovesRankList temp;
		temp = AIEngine();
		if (temp == null) {
			return 0;
		}
		else {
			return temp.getColumn();
		}
	}

	private ArrayList<MovesRankList> calculatePossibleMoves(BoardData board) {
		ArrayList<MovesRankList> moveslist = new ArrayList<>();
		for (int i = 0; i < _board.getColumnSize(); i++) {
			if (!_board.getSingleColumnAtIndex(i).isFull()) {
				MovesRankList temp = new MovesRankList(i,
						_board.getSingleColumnAtIndex(i).getCurrentSizeOfRow() + 1);
				moveslist.add(temp);
			}
		}
		return moveslist;
	}

	private ArrayList<MovesRankList> calculatePossibleMoves(ArrayList<SingleColumn> allColumns) {
		ArrayList<MovesRankList> moveslist = new ArrayList<>();
		for (int i = 0; i < allColumns.size(); i++) {
			for (int j = 0; j < allColumns.get(i).getTotalRowSize(); j++) {
				if (!allColumns.get(i).isFull()) {
					MovesRankList temp = new MovesRankList(i, allColumns.get(i).getCurrentSizeOfRow() + 1);
					moveslist.add(temp);
					break;
				}
			}
		}
		return moveslist;
	}

	private MovesRankList AIEngine() {
		ArrayList<MovesRankList> possibleMoves = calculatePossibleMoves(_board);
		ArrayList<SingleColumn> copyOfColumns = BoardColumnsCopier.copyAllColumns(_board.getAllColumns(), _board);

		// board is full
		if (possibleMoves.size() == 0) {
			return null;
		}

		// Only one possible move left
		if (possibleMoves.size() == 1) {
			return possibleMoves.get(0);
		}

		// check will win for sure
		MovesRankList temp = checkNextTurnIfWin(copyOfColumns, possibleMoves, _playerToken);
		if (temp != null) {
			return temp;
		}

		Token opponentToken = determineOpponent(_playerToken);

		// check will lose for sure next turn if player do not play here
		temp = checkNextTurnIfWin(copyOfColumns, possibleMoves, opponentToken);
		if (temp != null) {
			return temp;
		}


		//		// check if there is a move in which the opponent can get a double ended horizontal win condition
		//		temp = checkDoubleEndedHorizontalWinCondition(copyOfColumns, possibleMoves, opponentToken);
		//		if (temp != null) {
		//			return temp;
		//		}
		//
		//		// check if there is a move in which the opponent can get a double ended backward diagonal win condition
		//		temp = checkDoubleEndedBackwardDiagonalWinCondition(copyOfColumns, possibleMoves, opponentToken);
		//		if (temp != null) {
		//			return temp;
		//		}
		//
		//		// check if there is a move in which the opponent can get a double ended backward diagonal win condition
		//		temp = checkDoubleEndedForwardDiagonalWinCondition(copyOfColumns, possibleMoves, opponentToken);
		//		if (temp != null) {
		//			return temp;
		//		}

		// The following codes checks each possible moves and add or subtract points from the
		// move's rank based on each check

		for (MovesRankList e : possibleMoves) {

			// Check to see if inserting at a particular slot will
			// create a winning condition for the other player
			if (checkWillLoseNext(copyOfColumns, e, _playerToken)) {
				e.decrementRank(200);
			}
			else {
				if (checkTwoWinCondition(copyOfColumns, e, _playerToken)) {
					e.decrementRank(100);
				}
			}

			// Check to see if inserting at a particular slot will creating
			// a winning condition for the player next turn
			if (checkWillWinNext(copyOfColumns, e, _playerToken)) {
				e.incrementRank(20);
			}

			if (checkWillWinNextButOpponentCanCounterWin(copyOfColumns, e, _playerToken)) {
				e.decrementRank(120);
			}

			if (checkInsertWillMakeOpponentDoubleEndedHorizontal(copyOfColumns, e, _playerToken)) {
				e.decrementRank(90);
			}

			if (checkInsertWillMakeOpponentDoubleEndedForDiag(copyOfColumns, e, _playerToken)) {
				e.decrementRank(90);
			}

			if (checkInsertWillMakeOpponentDoubleEndedBackDiag(copyOfColumns, e, _playerToken)) {
				e.decrementRank(90);
			}

			if (checkDoubleEndedHorizontalWinCondition(copyOfColumns, e, _playerToken)) {
				e.incrementRank(10);
			}

			if (checkDoubleEndedBackwardDiagonalWinCondition(copyOfColumns, e, _playerToken)) {
				e.incrementRank(10);
			}

			if (checkDoubleEndedForwardDiagonalWinCondition(copyOfColumns, e, _playerToken)) {
				e.incrementRank(10);
			}

			if (checkWillGiveOpponentControl(copyOfColumns, e, _playerToken)) {
				e.decrementRank(1);
			}

			// OLD AI CODE
			// The following codes checks for adjacency. Being next to the same
			// token as the player, gives the move points for each token it is next to.

			int smallSequence = _board.getSequence() - 2;
			int counter = 0;
			Token tempToken;

			// check left

			if (e.getColumn() - smallSequence >= 0) {
				for (int i = 1; i <= smallSequence; i++) {
					tempToken = copyOfColumns.get(e.getColumn() - i).getTokenAtSlot(e.getRow());
					if (tempToken.equals(opponentToken)) {
						counter++;
					}
					else {
						counter = 0;
						break;
					}
				}

				if (counter == smallSequence) {
					e.incrementRank(1);
				}
			}
			counter = 0;

			// check right

			if (e.getColumn() + smallSequence < _board.getColumnSize()) {
				for (int i = 1; i <= smallSequence; i++) {
					tempToken = copyOfColumns.get(e.getColumn() + i).getTokenAtSlot(e.getRow());
					if (tempToken.equals(opponentToken)) {
						counter++;
					}
					else {
						counter = 0;
						break;
					}
				}

				if (counter == smallSequence) {
					e.incrementRank(1);
				}
			}

			counter = 0;

			/*// check above (will never happen)
			if (e.getRow() + smallSequence < _board.getRowSize()) {
				tempToken = copyOfColumns.get(e.getColumn()).getTokenAtSlot(e.getRow() + 1);
				if (tempToken.equals(opponentToken)) {
					e.incrementRank(1);
				}
			}*/

			// check below

			if (e.getRow() - smallSequence >= 0) {
				for (int i = 1; i <= smallSequence; i++) {
					tempToken = copyOfColumns.get(e.getColumn()).getTokenAtSlot(e.getRow() - i);
					if (tempToken.equals(opponentToken)) {
						counter++;
					}
					else {
						counter = 0;
						break;
					}
				}

				if (counter == smallSequence) {
					e.incrementRank(1);
				}
			}

			counter = 0;

			// check top right

			if (e.getColumn() + smallSequence < _board.getColumnSize() && e.getRow() + smallSequence < _board.getRowSize()) {
				for (int i = 1; i <= smallSequence; i++) {
					tempToken = copyOfColumns.get(e.getColumn() + i).getTokenAtSlot(e.getRow() + i);
					if (tempToken.equals(opponentToken)) {
						counter++;
					}
					else {
						counter = 0;
						break;
					}
				}

				if (counter == smallSequence) {
					e.incrementRank(1);
				}
			}

			counter = 0;

			// check top left

			if (e.getColumn() - smallSequence >= 0 && e.getRow() + smallSequence < _board.getRowSize()) {
				for (int i = 1; i <= smallSequence; i++) {
					tempToken = copyOfColumns.get(e.getColumn() - i).getTokenAtSlot(e.getRow() + i);
					if (tempToken.equals(opponentToken)) {
						counter++;
					}
					else {
						counter = 0;
						break;
					}
				}

				if (counter == smallSequence) {
					e.incrementRank(1);
				}
			}

			counter = 0;

			// check bottom right

			if (e.getColumn() + smallSequence < _board.getColumnSize() && e.getRow() - smallSequence >= 0) {
				for (int i = 1; i <= smallSequence; i++) {
					tempToken = copyOfColumns.get(e.getColumn() + i).getTokenAtSlot(e.getRow() - i);
					if (tempToken.equals(opponentToken)) {
						counter++;
					}
					else {
						counter = 0;
						break;
					}
				}

				if (counter == smallSequence) {
					e.incrementRank(1);
				}
			}

			counter = 0;

			// check bottom left

			if (e.getColumn() - smallSequence >= 0 && e.getRow() - smallSequence >= 0) {
				for (int i = 1; i <= smallSequence; i++) {
					tempToken = copyOfColumns.get(e.getColumn() - i).getTokenAtSlot(e.getRow() - i);
					if (tempToken.equals(opponentToken)) {
						counter++;
					}
					else {
						counter = 0;
						break;
					}
				}

				if (counter == smallSequence) {
					e.incrementRank(1);
				}
			}

			counter = 0;

		}

		// Keeps only the possible moves that matches the rank of the
		// highest move and then randomly pick one move out of the pool to return.
		int maxRank = Integer.MIN_VALUE;
		for (MovesRankList e : possibleMoves) {
			if (e.getRank() > maxRank) {
				maxRank = e.getRank();
			}
		}
		Iterator<MovesRankList> it = possibleMoves.iterator();
		while (it.hasNext()) {
			if (it.next().getRank() != maxRank) {
				it.remove();
			}
		}
		Random randomGenerator = new Random();
		int index = randomGenerator.nextInt(possibleMoves.size());
		MovesRankList moveChoice = possibleMoves.get(index);
		return moveChoice;
	}

	private boolean checkWillLoseNext(ArrayList<SingleColumn> AllColumns, MovesRankList nextMove, Token playerToken) {

		ArrayList<SingleColumn> copyOfColumns = BoardColumnsCopier.copyAllColumns(AllColumns, _board);
		copyOfColumns.get(nextMove.getColumn()).insert(playerToken);

		ArrayList<MovesRankList> possibleMoves = calculatePossibleMoves(copyOfColumns);

		Token opponentToken = determineOpponent(playerToken);

		MovesRankList temp = checkNextTurnIfWin(copyOfColumns, possibleMoves, opponentToken);
		if (temp != null) {
			copyOfColumns.get(nextMove.getColumn()).removeToken(nextMove.getRow());
			return true;
		}

		return false;
	}

	private boolean checkWillGiveOpponentControl(ArrayList<SingleColumn> AllColumns, MovesRankList nextMove, Token playerToken) {

		ArrayList<SingleColumn> copyOfColumns = BoardColumnsCopier.copyAllColumns(AllColumns, _board);
		copyOfColumns.get(nextMove.getColumn()).insert(playerToken);
		ArrayList<MovesRankList> secondPossibleMoves;

		ArrayList<MovesRankList> possibleMoves = calculatePossibleMoves(copyOfColumns);

		Token opponentToken = determineOpponent(playerToken);

		for (MovesRankList e : possibleMoves) {
			copyOfColumns.get(e.getColumn()).insert(opponentToken);
			secondPossibleMoves = calculatePossibleMoves(copyOfColumns);
			MovesRankList temp = checkNextTurnIfWin(copyOfColumns, secondPossibleMoves, opponentToken);
			if (temp != null) {
				copyOfColumns.get(e.getColumn()).removeToken(e.getRow());
				return true;
			}
			copyOfColumns.get(e.getColumn()).removeToken(e.getRow());
		}
		return false;
	}

	private boolean checkWillWinNext(ArrayList<SingleColumn> AllColumns, MovesRankList nextMove, Token playerToken) {
		ArrayList<SingleColumn> copyOfColumns = BoardColumnsCopier.copyAllColumns(_board.getAllColumns(), _board);
		copyOfColumns.get(nextMove.getColumn()).insert(playerToken);

		ArrayList<MovesRankList> possibleMoves = calculatePossibleMoves(copyOfColumns);

		MovesRankList temp = checkNextTurnIfWin(copyOfColumns, possibleMoves, playerToken);
		if (temp != null) {
			copyOfColumns.get(nextMove.getColumn()).removeToken(nextMove.getRow());
			return true;
		}

		return false;
	}

	private boolean checkWillWinNextButOpponentCanCounterWin(ArrayList<SingleColumn> AllColumns, MovesRankList nextMove, Token playerToken) {
		ArrayList<SingleColumn> copyOfColumns = BoardColumnsCopier.copyAllColumns(_board.getAllColumns(), _board);

		copyOfColumns.get(nextMove.getColumn()).insert(playerToken);
		MovesRankList opponentCounterMove;
		MovesRankList playerCounterMove;
		ArrayList<MovesRankList> possibleMoves = calculatePossibleMoves(copyOfColumns);
		Token opponentToken = determineOpponent(playerToken);

		MovesRankList temp = checkNextTurnIfWin(copyOfColumns, possibleMoves, playerToken);
		if (temp != null) {
			opponentCounterMove = temp;
			copyOfColumns.get(opponentCounterMove.getColumn()).insert(opponentToken);
			ArrayList<MovesRankList> secondPossibleMovesList = calculatePossibleMoves(copyOfColumns);
			MovesRankList temp2 = checkNextTurnIfWin(copyOfColumns, secondPossibleMovesList, opponentToken);
			if (temp2 != null) {
				playerCounterMove = temp2;
				copyOfColumns.get(playerCounterMove.getColumn()).insert(playerToken);
				ArrayList<MovesRankList> thirdPossibleMovesList = calculatePossibleMoves(copyOfColumns);
				MovesRankList temp3 = checkNextTurnIfWin(copyOfColumns, thirdPossibleMovesList, opponentToken);
				if (temp3 != null) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean checkDoubleEndedHorizontalWinCondition(ArrayList<SingleColumn> AllColumns, MovesRankList nextMove,
			Token playerToken) {
		WinningStrategy checker = new WinningStrategy();
		AllColumns.get(nextMove.getColumn()).insert(playerToken);
		if (checker.checkDoubleEndedHorizontal(AllColumns, _board.getSequence(), _board.getColumnSize(),
				_board.getRowSize(), playerToken, nextMove.getColumn())) {
			AllColumns.get(nextMove.getColumn()).removeToken(nextMove.getRow());
			return true;
		}
		AllColumns.get(nextMove.getColumn()).removeToken(nextMove.getRow());
		return false;
	}

	private boolean checkDoubleEndedForwardDiagonalWinCondition(ArrayList<SingleColumn> AllColumns, MovesRankList nextMove,
			Token playerToken) {

		WinningStrategy checker = new WinningStrategy();
		AllColumns.get(nextMove.getColumn()).insert(playerToken);
		if (checker.checkDoubleEndedFowardDiagonal(AllColumns, _board.getSequence(), _board.getColumnSize(),
				_board.getRowSize(), playerToken, nextMove.getColumn())) {
			AllColumns.get(nextMove.getColumn()).removeToken(nextMove.getRow());
			return true;
		}
		AllColumns.get(nextMove.getColumn()).removeToken(nextMove.getRow());
		return false;
	}

	private boolean checkDoubleEndedBackwardDiagonalWinCondition(ArrayList<SingleColumn> AllColumns, MovesRankList nextMove,
			Token playerToken) {

		WinningStrategy checker = new WinningStrategy();
		AllColumns.get(nextMove.getColumn()).insert(playerToken);
		if (checker.checkDoubleEndedBackwardDiagonal(AllColumns, _board.getSequence(), _board.getColumnSize(),
				_board.getRowSize(), playerToken, nextMove.getColumn())) {
			AllColumns.get(nextMove.getColumn()).removeToken(nextMove.getRow());
			return true;
		}
		AllColumns.get(nextMove.getColumn()).removeToken(nextMove.getRow());
		return false;
	}

	private MovesRankList checkNextTurnIfWin(ArrayList<SingleColumn> AllColumns, ArrayList<MovesRankList> movesList,
			Token playerToken) {
		WinningStrategy checker = new WinningStrategy();
		for (MovesRankList e : movesList) {
			AllColumns.get(e.getColumn()).insert(playerToken);
			if (checker.checkPlayerWonStraightLine(AllColumns, _board.getSequence(), _board.getColumnSize(),
					_board.getRowSize(), playerToken, e.getColumn())) {
				AllColumns.get(e.getColumn()).removeToken(e.getRow());
				return e;
			}
			AllColumns.get(e.getColumn()).removeToken(e.getRow());
		}
		return null;
	}

	// If the player can win even if the opponent blocks their first win condition returns true
	private boolean checkTwoWinCondition(ArrayList<SingleColumn> AllColumns, MovesRankList nextMove,
			Token playerToken) {
		MovesRankList thirdMove = null;
		ArrayList<MovesRankList> secondPossibleMovesList = null;
		ArrayList<MovesRankList> thirdPossibleMovesList = null;

		ArrayList<SingleColumn> copyOfColumns = BoardColumnsCopier.copyAllColumns(AllColumns, _board);

		copyOfColumns.get(nextMove.getColumn()).insert(playerToken);
		ArrayList<MovesRankList> movesList = calculatePossibleMoves(copyOfColumns);

		Token opponentToken = determineOpponent(playerToken);

		for (MovesRankList e : movesList)
		{
			copyOfColumns.get(e.getColumn()).insert(opponentToken);
			secondPossibleMovesList = calculatePossibleMoves(copyOfColumns);
			MovesRankList temp = checkNextTurnIfWin(copyOfColumns, secondPossibleMovesList, opponentToken);
			if (temp != null) {
				thirdMove = temp;
				copyOfColumns.get(thirdMove.getColumn()).insert(playerToken);
				thirdPossibleMovesList = calculatePossibleMoves(copyOfColumns);
				MovesRankList temp2 = checkNextTurnIfWin(copyOfColumns, thirdPossibleMovesList, opponentToken);
				if (temp2 != null) {
					return true;
				}
				else {
					copyOfColumns.get(thirdMove.getColumn()).removeToken(thirdMove.getRow());
				}
			}
			copyOfColumns.get(e.getColumn()).removeToken(e.getRow());
		}

		return false;
	}

	// Checks to see if inserting here will create a double ended win condition for opponent
	private boolean checkInsertWillMakeOpponentDoubleEndedHorizontal(ArrayList<SingleColumn> AllColumns, MovesRankList nextMove,
			Token playerToken) {

		ArrayList<SingleColumn> copyOfColumns = BoardColumnsCopier.copyAllColumns(AllColumns, _board);

		copyOfColumns.get(nextMove.getColumn()).insert(playerToken);
		ArrayList<MovesRankList> movesList = calculatePossibleMoves(copyOfColumns);
		WinningStrategy checker = new WinningStrategy();
		Token opponentToken = determineOpponent(playerToken);

		for (MovesRankList e : movesList)
		{
			copyOfColumns.get(e.getColumn()).insert(opponentToken);
			if (checker.checkDoubleEndedHorizontal(copyOfColumns, _board.getSequence(), _board.getColumnSize(),
					_board.getRowSize(), opponentToken, e.getColumn())) {
				copyOfColumns.get(e.getColumn()).removeToken(e.getRow());
				return true;
			}
			copyOfColumns.get(e.getColumn()).removeToken(e.getRow());
		}

		return false;
	}

	// Checks to see if inserting here will create a double ended win condition for opponent
	private boolean checkInsertWillMakeOpponentDoubleEndedBackDiag(ArrayList<SingleColumn> AllColumns, MovesRankList nextMove,
			Token playerToken) {

		ArrayList<SingleColumn> copyOfColumns = BoardColumnsCopier.copyAllColumns(AllColumns, _board);

		copyOfColumns.get(nextMove.getColumn()).insert(playerToken);
		ArrayList<MovesRankList> movesList = calculatePossibleMoves(copyOfColumns);
		WinningStrategy checker = new WinningStrategy();
		Token opponentToken = determineOpponent(playerToken);

		for (MovesRankList e : movesList)
		{
			copyOfColumns.get(e.getColumn()).insert(opponentToken);
			if (checker.checkDoubleEndedBackwardDiagonal(copyOfColumns, _board.getSequence(), _board.getColumnSize(),
					_board.getRowSize(), opponentToken, e.getColumn())) {
				copyOfColumns.get(e.getColumn()).removeToken(e.getRow());
				return true;
			}
			copyOfColumns.get(e.getColumn()).removeToken(e.getRow());
		}

		return false;

	}

	// Checks to see if inserting here will create a double ended win condition for opponent
	private boolean checkInsertWillMakeOpponentDoubleEndedForDiag(ArrayList<SingleColumn> AllColumns, MovesRankList nextMove,
			Token playerToken) {

		ArrayList<SingleColumn> copyOfColumns = BoardColumnsCopier.copyAllColumns(AllColumns, _board);

		copyOfColumns.get(nextMove.getColumn()).insert(playerToken);
		ArrayList<MovesRankList> movesList = calculatePossibleMoves(copyOfColumns);
		WinningStrategy checker = new WinningStrategy();
		Token opponentToken = determineOpponent(playerToken);

		for (MovesRankList e : movesList)
		{
			copyOfColumns.get(e.getColumn()).insert(opponentToken);
			if (checker.checkDoubleEndedFowardDiagonal(copyOfColumns, _board.getSequence(), _board.getColumnSize(),
					_board.getRowSize(), opponentToken, e.getColumn())) {
				copyOfColumns.get(e.getColumn()).removeToken(e.getRow());
				return true;
			}
			copyOfColumns.get(e.getColumn()).removeToken(e.getRow());
		}

		return false;

	}

	public Token determineOpponent(Token playerToken) {
		if (playerToken.equals(Token.YELLOW)) {
			return Token.RED;
		} else {
			return Token.YELLOW;
		}
	}
}
