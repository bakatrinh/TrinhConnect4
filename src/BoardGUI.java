

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;


/**
 * @author Trinh Nguyen
 * Holds Single Board Columns that make up a board using the data in BoardData
 *
 */
public class BoardGUI extends JPanel {
	private BoardColumnsGUI _boardColumnsGUI;
	private BoardData _board;
	private MainGameGUI _mainGameGUI;
	private static final long serialVersionUID = 1L;

	public BoardGUI(BoardData board, MainGameGUI mainGameGUI) {
		_board = board;
		_mainGameGUI = mainGameGUI;
		_boardColumnsGUI = new BoardColumnsGUI(_board, _mainGameGUI);
		setLayout(new BorderLayout());
		add(_boardColumnsGUI, BorderLayout.CENTER);
		if (board.getColumnSize() > 8) {
			setPreferredSize(new Dimension(600, 600));
		}
	}
	
	public void refresh() {
		_boardColumnsGUI.refreshBoard();
	}
}
