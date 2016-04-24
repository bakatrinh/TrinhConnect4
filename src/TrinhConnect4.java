import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class TrinhConnect4 {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainGameGUI game = new MainGameGUI(7, 7, 4, PlayerType.HUMAN, PlayerType.COMPUTER);
				game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				game.setResizable(true);
				game.pack();
				game.setLocationRelativeTo(null);
				game.setVisible(true);	
			}
		});
	}

}
