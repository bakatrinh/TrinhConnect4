

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.Timer;

public class TokenGUI extends JComponent implements ActionListener{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Stroke _strokeThin = new BasicStroke(1);
	private static final Stroke _strokeThick = new BasicStroke(8);
	private static final Stroke _strokeNone = new BasicStroke(0);

	public static final int WIDTH = 70;
	public static final int HEIGHT = 70;
	private Token _token;
	private Color _boardColor = new Color(39, 123, 207);
	private boolean _blink = true;
	private boolean _lowRes;
	private Timer _timer;
	private Graphics2D g2d;

	public TokenGUI(Token token, boolean lowRes) {
		_token = token;
		_lowRes = lowRes;
		if (_token.equals(Token.BLINKINGRED) || _token.equals(Token.BLINKINGYELLOW))
		{
			_timer = new Timer(500, this);
			_timer.start();
		}
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g2d = (Graphics2D) g;
		
		if (!_lowRes) {
			RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHints(hints);
		}

		// set the background color
		g2d.setColor(_boardColor);
		g2d.fillRect(0, 0, super.getWidth(), super.getHeight());
		g2d.setColor(Color.BLACK);
		if (!_lowRes) {
			g2d.setStroke(_strokeThin);
			g2d.drawRect(0, 0, super.getWidth(), super.getHeight());
			g2d.setStroke(_strokeThick);
		}
		else {
			//g.setStroke(_strokeThin);
		}

		switch (_token) {
		case RED:
			if (!_lowRes) {
				g2d.drawOval(5, 5, super.getWidth() - 10, super.getHeight() - 10);
				g2d.setStroke(_strokeNone);
				g2d.setColor(Color.RED);
				g2d.fillOval(5, 5, super.getWidth() - 10, super.getHeight() - 10);
			}
			else {
				//g.drawOval(4, 4, super.getWidth()-6, super.getHeight()-6);
				g2d.setStroke(_strokeNone);
				g2d.setColor(Color.RED);
				g2d.fillOval(4, 4, super.getWidth()-6, super.getHeight()-6);
			}
			break;
		case YELLOW:
			if (!_lowRes) {
				g2d.drawOval(5, 5, super.getWidth() - 10, super.getHeight() - 10);
				g2d.setStroke(_strokeNone);
				g2d.setColor(Color.YELLOW);
				g2d.fillOval(5, 5, super.getWidth() - 10, super.getHeight() - 10);
			}
			else {
				//g.drawOval(4, 4, super.getWidth()-6, super.getHeight()-6);
				g2d.setStroke(_strokeNone);
				g2d.setColor(Color.YELLOW);
				g2d.fillOval(4, 4, super.getWidth()-6, super.getHeight()-6);
			}
			break;
		case PALERED:
			if (!_lowRes) {
				g2d.setColor(new Color(71, 71, 71));
				g2d.drawOval(5, 5, super.getWidth() - 10, super.getHeight() - 10);
				g2d.setStroke(_strokeNone);
				g2d.setColor(new Color(245, 162, 162));
				g2d.fillOval(5, 5, super.getWidth() - 10, super.getHeight() - 10);
			}
			else {
				//g.setColor(new Color(71, 71, 71));
				//g.drawOval(4, 4, super.getWidth()-6, super.getHeight()-6);
				g2d.setStroke(_strokeNone);
				g2d.setColor(new Color(245, 162, 162));
				g2d.fillOval(4, 4, super.getWidth()-6, super.getHeight()-6);
			}
			break;
		case PALEYELLOW:
			if (!_lowRes) {
				g2d.setColor(new Color(71, 71, 71));
				g2d.drawOval(5, 5, super.getWidth() - 10, super.getHeight() - 10);
				g2d.setStroke(_strokeNone);
				g2d.setColor(new Color(245, 245, 135));
				g2d.fillOval(5, 5, super.getWidth() - 10, super.getHeight() - 10);
			}
			else {
				//g.setColor(new Color(71, 71, 71));
				//g.drawOval(4, 4, super.getWidth()-6, super.getHeight()-6);
				g2d.setStroke(_strokeNone);
				g2d.setColor(new Color(245, 245, 135));
				g2d.fillOval(4, 4, super.getWidth()-6, super.getHeight()-6);
			}
			break;
		case BLINKINGRED:
			if (_blink) {
				_blink = false;
			} else {
				if (!_lowRes) {
					g2d.drawOval(5, 5, super.getWidth() - 10, super.getHeight() - 10);
					g2d.setStroke(_strokeNone);
					g2d.setColor(Color.RED);
					g2d.fillOval(5, 5, super.getWidth() - 10, super.getHeight() - 10);
				}
				else {
					//g.drawOval(4, 4, super.getWidth()-6, super.getHeight()-6);
					g2d.setStroke(_strokeNone);
					g2d.setColor(Color.RED);
					g2d.fillOval(4, 4, super.getWidth()-6, super.getHeight()-6);
				}
				_blink = true;
			}
			break;
		case BLINKINGYELLOW:
			if (_blink) {
				_blink = false;
			} else {
				if (!_lowRes) {
					g2d.drawOval(5, 5, super.getWidth() - 10, super.getHeight() - 10);
					g2d.setStroke(_strokeNone);
					g2d.setColor(Color.YELLOW);
					g2d.fillOval(5, 5, super.getWidth() - 10, super.getHeight() - 10);
				}
				else {
					//g.drawOval(4, 4, super.getWidth()-6, super.getHeight()-6);
					g2d.setStroke(_strokeNone);
					g2d.setColor(Color.YELLOW);
					g2d.fillOval(4, 4, super.getWidth()-6, super.getHeight()-6);
				}
				_blink = true;
			}
			break;
		case BLANK:
			if (!_lowRes) {
				g2d.drawOval(5, 5, super.getWidth() - 10, super.getHeight() - 10);
				g2d.setStroke(_strokeNone);
				g2d.setColor(Color.WHITE);
				g2d.fillOval(5, 5, super.getWidth()-10, super.getHeight()-10);
			}
			else {

			}
			break;
		}
	}

	//Swing timer code (use for swing components)
//	public void startBlink() {
//		int delay=500;// wait for second
//
//		_timer = new Timer(delay, new AbstractAction() {
//			@Override
//			public void actionPerformed(ActionEvent ae) {
//				if (_token.equals(Token.BLINKINGRED) || _token.equals(Token.BLINKINGYELLOW)) {
//					repaint();
//				}
//				else if (_timer != null && _timer.isRunning()) {
//					_timer.stop();
//				}
//			}
//		});
//		_timer.start();
//	}
//	
//	public void stopBlink() {
//		if (_timer != null && _timer.isRunning()) {
//			_timer.stop();
//		}
//	}

	public void setToken(Token token) {
		_token = token;
		repaint();
	}
	
	public Token getToken() {
		return _token;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

}
