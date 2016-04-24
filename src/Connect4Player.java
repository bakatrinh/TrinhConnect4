
/**
 * @author Trinh Nguyen
 * Two type of players possible, Human and Computer. These players are
 * contained in BoardData to let the program knows what kind of player
 * is currently playing.
 */
public interface Connect4Player {
	
	public void setTokenColor(Token token);

	public Token getPlayerToken();
	
	public void setBoard(BoardData board);
	
	public void makeMove();

	public PlayerType getPlayerType();
	
	public void startPlaying();
}
