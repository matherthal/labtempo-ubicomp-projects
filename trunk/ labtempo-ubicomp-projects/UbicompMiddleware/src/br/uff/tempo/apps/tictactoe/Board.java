//
// Board.java
//

package br.uff.tempo.apps.tictactoe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import br.uff.tempo.R;

public class Board extends View {

  //
  // Constructors
  //

  public Board(Context context) {
    super(context);
    this.initialize();
    this.setFocusable(true);
    this.setFocusableInTouchMode(true);
  }

  //
  // View interface
  //

  @Override
  public void onDraw(Canvas canvas) {

    // Draw the background.
    Paint background = new Paint();
    background.setColor(this.getResources().getColor(R.color.puzzle_background));
    canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), background);

    // Draw the board...

    // Define colors for the grid lines.
    Paint line = new Paint();
    line.setColor(this.getResources().getColor(R.color.puzzle_line));
    Paint hilite = new Paint();
    hilite.setColor(this.getResources().getColor(R.color.puzzle_hilite));

    // Draw the grid lines.
    for (int i = 1; i < 3; i++) {
      canvas.drawLine(0, i * this.height, 3 * this.width, i * this.height, line);
      canvas.drawLine(0, i * this.height + 1, 3 * this.width, i * this.height + 1, hilite);
      canvas.drawLine(i * this.width, 0, i * this.width, 3 * this.height, line);
      canvas.drawLine(i * this.width + 1, 0, i * this.width + 1, 3 * this.height, hilite);
    }

    // Draw the number...

    // Define the color and style for the numbers.
    Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
    foreground.setColor(this.getResources().getColor(R.color.puzzle_foreground));
    foreground.setStyle(Style.FILL);
    foreground.setTextSize(this.height * 0.1f);
    foreground.setTextScaleX(this.width / this.height);
    foreground.setTextAlign(Paint.Align.LEFT);

    // Draw the number in the upper left corner of the tile.
    FontMetrics fm = foreground.getFontMetrics();
    for(int row = 0; row < 3; row++) {
      for(int column = 0; column < 3; column++) {
        canvas.drawText(Integer.toString(row * 3 + column + 1),
                        column * this.width + 3, row * this.height - fm.ascent,
                        foreground);
      }
    }

    // Draw the moves...

    // Define the color and style for the moves.
    foreground.setTextSize(this.height * 0.75f);
    foreground.setTextAlign(Paint.Align.CENTER);

    // Draw the move in the center of the tile.
    fm = foreground.getFontMetrics();
    // Centering in X, use alignment (and X at midpoint).
    float x = this.width / 2;
    // Centering in Y, measure ascent/descent first.
    float y = this.height / 2 - (fm.ascent + fm.descent) / 2;
    for (int row = 0; row < 3; row++) {
      for (int column = 0; column < 3; column++) {
        canvas.drawText(this.getMove(row, column).toString(),
                        column * this.width + x, row * this.height + y,
                        foreground);
      }
    }

  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    this.width = w / 3f;
    this.height = h / 3f;
    super.onSizeChanged(w, h, oldw, oldh);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (event.getAction() != MotionEvent.ACTION_DOWN) {
      return super.onTouchEvent(event);
    }
    int row = (int)(event.getY() / this.height);
    int column = (int)(event.getX() / this.width);
    this.currentMove = new Location(row, column);
    return true;
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    int row;
    int column;
    switch (keyCode) {
      case KeyEvent.KEYCODE_1:
        row = 0;
        column = 0;
        break;
      case KeyEvent.KEYCODE_2:
        row = 0;
        column = 1;
        break;
      case KeyEvent.KEYCODE_3:
        row = 0;
        column = 2;
        break;
      case KeyEvent.KEYCODE_4:
        row = 1;
        column = 0;
        break;
      case KeyEvent.KEYCODE_5:
        row = 1;
        column = 1;
        break;
      case KeyEvent.KEYCODE_6:
        row = 1;
        column = 2;
        break;
      case KeyEvent.KEYCODE_7:
        row = 2;
        column = 0;
        break;
      case KeyEvent.KEYCODE_8:
        row = 2;
        column = 1;
        break;
      case KeyEvent.KEYCODE_9:
        row = 2;
        column = 2;
        break;
      default:
        return super.onKeyDown(keyCode, event);
    }
    this.currentMove = new Location(row, column);
    return true;
  }

  //
  // Additional methods
  //

  public void initialize() {
    this.moves = new Player.Name[MAX_ROWS][MAX_COLUMNS];
    for (int row = 0; row < MAX_ROWS; row++) {
      for (int column = 0; column < MAX_COLUMNS; column++) {
        this.moves[row][column] = Player.Name.NONE;
      }
    }
  }

  public boolean setMove(Location move, Player player) {

    // Check for invalid move and player values.
    if ((move == null) || (player == null)) {
      return false;
    }

    // Check for invalid row and column values.
    int row = move.getRow();
    int column = move.getColumn();
    if ((row < 0) || (row >= MAX_ROWS) || (column < 0) || (column >= MAX_COLUMNS)) {
      return false;
    }

    // Check for a used location.
    if (this.getMove(row, column) != Player.Name.NONE) {
      return false;
    }

    // Store valid move.
    this.moves[row][column] = player.getName();

    return true;

  }

  public Location getNewMove() {
    Location move = this.currentMove;
    this.currentMove = null;
    return move;
  }

  public Player.Name getMove(int row, int column) {
    return this.moves[row][column];
  }

  //
  // Attributes
  //

  public static final int MAX_ROWS = 3;

  public static final int MAX_COLUMNS = 3;

  private Player.Name[][] moves;

  private float width;

  private float height;

  private Location currentMove;

}
