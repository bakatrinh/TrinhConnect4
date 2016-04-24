
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * @author Trinh Nguyen GUI to display the individual columns in board data Also
 *         contains code to change appearance when the mouse hovers over each
 *         column. Clicking on this columns triggers the insert method at the
 *         column this panel belongs to
 */
public class SingleColumnGUI extends JPanel implements MouseListener {

	private BoardData _board;
	private SingleColumn _currentColumn;
	private BoardColumnsGUI _columnsGUI;
	private JPanel _currentPanel;
	private ArrayList<TokenGUI> _currentColumnGUI;

	/*
	 * The slots graphics will be replaced with less detailed tokens if the
	 * total tokens on the board is more than this number.
	 */
	private static final long serialVersionUID = 1L;

	public SingleColumnGUI(SingleColumn currentColumn, BoardData board, BoardColumnsGUI columnsGUI,
			ArrayList<TokenGUI> currentColumnGUI) {
		_board = board;
		_currentColumn = currentColumn;
		_columnsGUI = columnsGUI;
		_currentColumnGUI = currentColumnGUI;
		setLayout(new BorderLayout());
		initializeFreshTokensGUI();
		addMouseListener(this);
	}

	public void initializeFreshTokensGUI() {
		_currentPanel = new JPanel(new GridLayout(0, 1));
		for (int i = _currentColumn.getTotalRowSize() - 1; i >= 0; i--) {
			_currentPanel.add(_currentColumnGUI.get(i));
		}
		add(_currentPanel);
		refresh();
	}

	public void refresh() {
		normalPanel();
	}

	private void normalPanel() {
		for (int i = _currentColumn.getTotalRowSize() - 1; i >= 0; i--) {
			Token temp = _currentColumn.getTokenAtSlot(i);
			_currentColumnGUI.get(i).setToken(temp);
			if (temp.equals(Token.BLINKINGRED) || temp.equals(Token.BLINKINGYELLOW)) {
				_columnsGUI.getBlinkList().add(_currentColumnGUI.get(i));
			}
		}
	}



	private void hoverPanel() {
		normalPanel();
		int tokenIndex = _currentColumn.getCurrentSizeOfRow() + 1;
		if (_board.getTokenOfCurrentActivePlayer().equals(Token.YELLOW)) {
			_currentColumnGUI.get(tokenIndex).setToken(Token.PALEYELLOW);
		} else {
			_currentColumnGUI.get(tokenIndex).setToken(Token.PALERED);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {

		if (!_board.checkGameWon() && !_board.checkGameDraw() && !_board.getComputerStatus()) {
			if (!_currentColumn.isFull()) {
				_board.insertIntoBoard(_currentColumn.getColumnNumber());
				_columnsGUI.refreshBoard();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (!_board.checkGameWon() && !_board.checkGameDraw() && !_currentColumn.isFull()
				&& !_board.getComputerStatus()) {
			hoverPanel();
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (!_board.checkGameWon() && !_board.checkGameDraw() && !_currentColumn.isFull()
				&& !_board.getComputerStatus()) {
			normalPanel();
		}
	}
}
