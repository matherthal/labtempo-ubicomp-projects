//
// SmartComputerPlayer.java
//

package br.uff.tempo.apps.tictactoe;

public class SmartComputerPlayer extends Player {

  //
  // Constructors
  //

  public SmartComputerPlayer(Name name, Board board) {
    super(name, board);
    this.type = Type.Computer;
  }

  //
  // Player interface
  //

  //@Override
  public Location getMove() {
    Location location = null;
    location = this.getWinningMove();
    if (location == null) {
      location = this.getBlockingMove();
    }
    if (location == null) {
      location = this.getCenterMove();
    }
    if (location == null) {
      location = this.getCornerMove();
    }
    if (location == null) {
      location = this.getSideMove();
    }
    return location;
  }

  //
  // Additional methods
  //

  private Location getWinningMove() {
    return this.findTwoMarksInALineOnTheBoard(this.name);
  }

  private Location getBlockingMove() {
    return this.findTwoMarksInALineOnTheBoard(Player.Name.getOpponent(this.name));
  }

  private Location getCenterMove() {
    if (this.board.getMove(1, 1) == Player.Name.NONE) {
      return new Location(1, 1);
    }
    return null;
  }

  private Location getCornerMove() {

    // Pick any corner thats opposite is also empty.
    if ((this.board.getMove(0, 0) == Player.Name.NONE) &&
        (this.board.getMove(2, 2) == Player.Name.NONE)) {
      return new Location(2, 2);
    }
    if ((this.board.getMove(0, 2) == Player.Name.NONE) &&
        (this.board.getMove(2, 0) == Player.Name.NONE)) {
      return new Location(0, 2);
    }

    // Pick any empty corner.
    if (this.board.getMove(0, 0) == Player.Name.NONE) {
      return new Location(0, 0);
    }
    if (this.board.getMove(0, 2) == Player.Name.NONE) {
      return new Location(0, 2);
    }
    if (this.board.getMove(2, 0) == Player.Name.NONE) {
      return new Location(2, 0);
    }
    if (this.board.getMove(2, 2) == Player.Name.NONE) {
      return new Location(2, 2);
    }

    return null;

  }

  private Location getSideMove() {

    // Pick any empty side that also has this player's move in a corner.
    if ((this.board.getMove(0, 1) == Player.Name.NONE) &&
        ((this.board.getMove(0, 0) == this.name) ||
         (this.board.getMove(0, 2) == this.name))) {
      return new Location(0, 1);
    }
    if ((this.board.getMove(1, 0) == Player.Name.NONE) &&
        ((this.board.getMove(0, 0) == this.name) ||
         (this.board.getMove(2, 0) == this.name))) {
      return new Location(1, 0);
    }
    if ((this.board.getMove(1, 2) == Player.Name.NONE) &&
        ((this.board.getMove(0, 2) == this.name) ||
         (this.board.getMove(2, 2) == this.name))) {
      return new Location(1, 2);
    }
    if ((this.board.getMove(2, 1) == Player.Name.NONE) &&
        ((this.board.getMove(2, 0) == this.name) ||
         (this.board.getMove(2, 2) == this.name))) {
      return new Location(2, 1);
    }

    // Pick any side thats opposite is also empty.
    if ((this.board.getMove(1, 0) == Player.Name.NONE) &&
        (this.board.getMove(1, 2) == Player.Name.NONE)) {
      return new Location(1, 0);
    }
    if ((this.board.getMove(0, 1) == Player.Name.NONE) &&
        (this.board.getMove(2, 1) == Player.Name.NONE)) {
      return new Location(0, 1);
    }

    // Pick any empty side.
    if (this.board.getMove(0, 1) == Player.Name.NONE) {
      return new Location(0, 1);
    }
    if (this.board.getMove(1, 0) == Player.Name.NONE) {
      return new Location(1, 0);
    }
    if (this.board.getMove(1, 2) == Player.Name.NONE) {
      return new Location(1, 2);
    }
    if (this.board.getMove(2, 1) == Player.Name.NONE) {
      return new Location(2, 1);
    }

    return null;

  }

  private Location findTwoMarksInALineOnTheBoard(Player.Name n) {

    Location location;

    // Check the rows.
    for (int row = 0; row < Board.MAX_ROWS; row++) {
      location = this.findTwoMarksInALine(n, row, 0, row, 1, row, 2);
      if (location != null) {
        return location;
      }
    }

    // Check the columns.
    for (int column = 0; column < Board.MAX_COLUMNS; column++) {
      location = this.findTwoMarksInALine(n, 0, column, 1, column, 2, column);
      if (location != null) {
        return location;
      }
    }

    // Check the diagonals.
    location = this.findTwoMarksInALine(n, 0, 0, 1, 1, 2, 2);
    if (location != null) {
      return location;
    }
    location = this.findTwoMarksInALine(n, 0, 2, 1, 1, 2, 0);
    if (location != null) {
      return location;
    }

    return null;

  }

  private Location findTwoMarksInALine(Player.Name n,
                                       int row1, int column1,
                                       int row2, int column2,
                                       int row3, int column3) {

    if ((this.board.getMove(row1, column1) == Player.Name.NONE) &&
        (this.board.getMove(row2, column2) == n) &&
        (this.board.getMove(row3, column3) == n)) {
      return new Location(row1, column1);
    }

    if ((this.board.getMove(row2, column2) == Player.Name.NONE) &&
        (this.board.getMove(row1, column1) == n) &&
        (this.board.getMove(row3, column3) == n)) {
      return new Location(row2, column2);
    }

    if ((this.board.getMove(row3, column3) == Player.Name.NONE) &&
        (this.board.getMove(row1, column1) == n) &&
        (this.board.getMove(row2, column2) == n)) {
      return new Location(row3, column3);
    }

    return null;

  }

}
