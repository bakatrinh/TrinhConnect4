# TrinhConnect4

Author: Trinh Nguyen

Trinh's Connect 4 Game
===============================
TrinhConnect4.java is the main file to start the game. It creates a new BoardData and a new display the GUI window.

It then creates a separate thread for any Computer Player created. This Computer Player periodically checks the board for its turn. If it is the computer's turn, it will calculate a move and runs the insert method.

The graphics for the tokens are changed to a simpler version if the board size is large.

Default board size is 7 (7x7). Default sequence is 4. The game starts in human versus computer mode. Red player always go first.

The maximum board size is 50 and the minimum is 2, the sequence size must be equal to or less than board size.

The game also has a new game button to change the board size, sequence, and player type of the game.

To insert pieces into the board, highlight and click the column with the mouse.

Against a computer opponent, the user needs to press undo twice consecutively since the computer will make another move if it is it's turn.
