

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * @author TrinhNguyen
 * 
 * This panel consist of all of the individual panels which is a graphical
 * representation of the columns information in BoardData. The panels are
 * lined up side by side to resemble a board game.
 */
public class BoardColumnsGUI extends JPanel {

	private BoardData _board;
	private JPanel _currentPanel;
	private static final int COLUMNGRAPHICSCAP = 14;
	private static final int ROWGRAPHICSCAP = 14;
	private static final long serialVersionUID = 1L;
	private boolean _lowRes;
	private ArrayList<ArrayList<TokenGUI>> _boardAllTokensGUI;
	private ArrayList<SingleColumnGUI> _allColumnsGUI;
	private Timer _timer;
	private ArrayList<TokenGUI> _blinkList;
	private int _delay = 500;
	private MainGameGUI _mainGameGUI;

	public BoardColumnsGUI(BoardData board, MainGameGUI mainGameGUI) {
		_board = board;
		_mainGameGUI = mainGameGUI;
		_boardAllTokensGUI = new ArrayList<ArrayList<TokenGUI>>();
		if (_board.getColumnSize() > COLUMNGRAPHICSCAP || _board.getRowSize() > ROWGRAPHICSCAP) {
			_lowRes = true;
		}
		else {
			_lowRes = false;
		}
		
		// Starts a swing timer that repaints the TokenGUI listed in _blinklist in interval.
		// Note, create timer once, creating more than one elsewhere will cause multiple timers and causes tokens to blink too
		// fast over time
		_timer = new Timer(_delay, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if (_board.checkGameWon()) {
					for (TokenGUI e : _blinkList) {
						e.repaint();
					}
				}
				else if (_timer != null && _timer.isRunning()) {
					_timer.stop();
				}
			}
		});
		_blinkList = new ArrayList<TokenGUI>();
		_board.setBoardGUI(this);
		setLayout(new BorderLayout());
		initializeTokens(_board);
	}
	
	// Adds new blank tokens corresponding to the number of tokens on BoardData
	public void initializeTokens(BoardData board) {
		for (int i = 0; i < _board.getColumnSize(); i++) {
			_boardAllTokensGUI.add(new ArrayList<TokenGUI>());
			for (int j = 0; j < _board.getRowSize(); j ++) {
				TokenGUI temp = new TokenGUI(Token.BLANK, _lowRes);
				_boardAllTokensGUI.get(i).add(temp);
			}
		}

		_currentPanel = new JPanel(new GridLayout(1, 0));
		_allColumnsGUI = new ArrayList<SingleColumnGUI>();
		for (int i = 0; i < _board.getColumnSize(); i++) {
			SingleColumnGUI temp = new SingleColumnGUI(_board.getSingleColumnAtIndex(i), _board, this, _boardAllTokensGUI.get(i));
			_currentPanel.add(temp);
			_allColumnsGUI.add(temp);
		}
		add(_currentPanel);
	}
	
	// Stores a list of TokenGUI that are part of the blinking token set
	public ArrayList<TokenGUI> getBlinkList() {
		return _blinkList;
	}

	public void startBlink() {
		_timer.start();
	}
	
	public void setMainGameGUI(MainGameGUI mainGameGUI) {
		_mainGameGUI = mainGameGUI;
	}

	// Called every time a change to the board by insert or winning condition happens.
	// Or undo method is called.
	// Needed so the graphical board can refresh and the changes are shown to the player
	public void refreshBoard() {
		if (_board.checkGameWon()) {
			_blinkList = new ArrayList<TokenGUI>();
		}
		for (SingleColumnGUI e : _allColumnsGUI) {
			e.refresh();
		}
		if (_board.checkGameWon()) {
			startBlink();
		}
		else if (_timer != null && _timer.isRunning()) {
			_timer.stop();
		}
		_mainGameGUI.updateStatus();
	}
}
