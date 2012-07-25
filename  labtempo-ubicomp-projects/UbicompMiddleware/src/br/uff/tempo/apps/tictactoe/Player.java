//
// Player.java
//

package br.uff.tempo.apps.tictactoe;

public abstract class Player {

  //
  // Constructors
  //

  public Player(Name name, Board board) {
    this.name = name;
    this.type = Type.Computer;
    this.board = board;
  }

  //
  // Additional methods
  //

  public Name getName() {
    return this.name;
  }

  public Type getType() {
    return this.type;
  }

  public abstract Location getMove();

  public enum Name {
    NONE {
      public String toString() {
        return " ";
      }
    },
    X,
    O;
    public static Name getOpponent(Name n) {
      switch (n) {
        case X:
          return O;
        case O:
          return X;
        default:
          return NONE;
      }
    }
  };

  public enum Type {
    Computer, Human
  };

  //
  // Object interface
  //

  @Override
  public String toString() {
    return "Player(" + this.name + ")";
  }

  //
  // Attributes
  //

  protected Name name;

  protected Type type;

  protected Board board;

}
