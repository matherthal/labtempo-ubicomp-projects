//
// DumbComputerPlayer.java
//

package br.uff.tempo.apps.tictactoe;

public class DumbComputerPlayer extends Player {

  //
  // Constructors
  //

  public DumbComputerPlayer(Name name, Board board) {
    super(name, board);
    this.type = Type.Computer;
  }

  //
  // Player interface
  //

  //@Override
  public Location getMove() {
    for (int row = 0; row < Board.MAX_ROWS; row++) {
      for (int column = 0; column < Board.MAX_COLUMNS; column++) {
        if (this.board.getMove(row, column) == Player.Name.NONE) {
          return new Location(row, column);
        }
      }
    }
    return null;
  }

}
