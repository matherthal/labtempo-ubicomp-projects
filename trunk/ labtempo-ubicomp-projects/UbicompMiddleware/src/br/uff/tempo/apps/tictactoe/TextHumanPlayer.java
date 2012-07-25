//
// TextHumanPlayer.java
//

package br.uff.tempo.apps.tictactoe;

import java.io.IOException;

public class TextHumanPlayer extends Player {

  //
  // Constructors
  //

  public TextHumanPlayer(Name name, Board board) {
    super(name, board);
    this.type = Type.Human;
  }

  //
  // Player interface
  //

  //@Override
  public Location getMove() {
    Location move = null;
    try {
      do {
        System.out.print("Player " + this.name + ", make your move (1-9): ");
        char key = (char)System.in.read();
        while (System.in.available() > 0) {
          System.in.read();
        }
        if (key == '1') {
          move = new Location(0, 0);
        }
        else if (key == '2') {
          move = new Location(0, 1);
        }
        else if (key == '3') {
          move = new Location(0, 2);
        }
        else if (key == '4') {
          move = new Location(1, 0);
        }
        else if (key == '5') {
          move = new Location(1, 1);
        }
        else if (key == '6') {
          move = new Location(1, 2);
        }
        else if (key == '7') {
          move = new Location(2, 0);
        }
        else if (key == '8') {
          move = new Location(2, 1);
        }
        else if (key == '9') {
          move = new Location(2, 2);
        }
        else {
          System.out.println("invalid key, use the keys 1 through 9");
        }
      } while (move == null);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return move;
  }

}
