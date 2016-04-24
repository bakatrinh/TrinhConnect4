

import java.util.ArrayList;

/**
 * @author Trinh Nguyen
 * A static class that copies, by value, all the tokens in the Array. Used by various classes such as
 * ComputerPlayer to make copies of the board so it can test out moves for its AI
 */
public class BoardColumnsCopier {

	public static ArrayList<SingleColumn> copyAllColumns(ArrayList<SingleColumn> columns, BoardData board) {
		ArrayList<SingleColumn> allColumnsTemp = new ArrayList<>();
		SingleColumn toBeCopied;
		for (int i = 0; i < board.getColumnSize(); i++) {
			toBeCopied = columns.get(i);
			allColumnsTemp.add(new SingleColumn(board.getRowSize(), toBeCopied.getColumnNumber()));
			for (int j = 0; j < allColumnsTemp.get(i).getTotalRowSize(); j++) {
				Token temp = toBeCopied.getTokenAtSlot(j);
				if (temp.equals(Token.RED)) {
					allColumnsTemp.get(i).insert(Token.RED);
				}
				if (temp.equals(Token.YELLOW)) {
					allColumnsTemp.get(i).insert(Token.YELLOW);
				}
			}
		}
		return allColumnsTemp;
	}
}
