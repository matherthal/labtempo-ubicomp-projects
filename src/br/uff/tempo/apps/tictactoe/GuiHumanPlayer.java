//
// GuiHumanPlayer.java
//

package br.uff.tempo.apps.tictactoe;

public class GuiHumanPlayer extends Player {

  //
  // Constructors
  //

  public GuiHumanPlayer(Name name, Board board) {
    super(name, board);
    this.type = Type.Human;
  }

  //
  // Player interface
  //

  //@Override
  public Location getMove() {
    return this.board.getNewMove();
  }

}
