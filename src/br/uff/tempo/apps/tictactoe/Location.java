//
// Location.java
//

package br.uff.tempo.apps.tictactoe;

public class Location {

  //
  // Constructors
  //

  public Location(int r, int c) {
    this.row = r;
    this.column = c;
  }

  //
  // Object interface
  //

  @Override
  public String toString() {
    return "Location(" + this.row + ", " + this.column + ")";
  }

  //
  // Additional methods
  //

  public int getRow() {
    return this.row;
  }

  public int getColumn() {
    return this.column;
  }

  //
  // Attributes
  //

  private int row;

  private int column;

}
