
/**
 * @author Trinh Nguyen
 * Does not explicitly run any method since board insertion is done by the GUI
 * This class is made to differentiate itself from a computer player
 */
public class HumanPlayer implements Connect4Player{
	
	@SuppressWarnings("unused")
	private BoardData _board;
	private Token _playerToken;
	private PlayerType _playerType;
	
	public HumanPlayer(BoardData board, Token playerToken) {
		_board = board;
		_playerToken = playerToken;
		_playerType = PlayerType.HUMAN;
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
		return "Human";
	}
	
	@Override
	public void makeMove() {
		
	}
	
	@Override
	public PlayerType getPlayerType() {
		return _playerType;
	}

	@Override
	public void startPlaying() {
		
	}
}
