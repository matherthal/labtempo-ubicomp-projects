//
// Game.java
//

package br.uff.tempo.apps.tictactoe;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import br.uff.tempo.R;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.interfaces.IResourceRegister;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.management.stubs.ResourceRegisterStub;
import br.uff.tempo.middleware.resources.GameAgent;
import br.uff.tempo.middleware.resources.interfaces.IGameAgent;

public class Game{

  //
  // Constructors
  //
	private IGameAgent gameAgent;

	public Game(Context context) {
		this.context = context;
	}
	
	public Game(Context context, IGameAgent gameAgent) {
		this.context = context;
		this.gameAgent = gameAgent;
		this.gameAgent.setGame(this);
	}

  //
  // Additional methods
  //

  public void output(String message) {
    Toast toast = Toast.makeText(this.context, message, Toast.LENGTH_LONG);
    toast.setGravity(Gravity.CENTER, 0, 0);
    toast.show();
  }

  public View initialize() {

    // Initialize the board.
    this.board = new Board(this.context);
    this.board.requestFocus();
    gameAgent.setBoard(board);

    // Initialize the players.
    this.playerX = this.getPlayer(Player.Name.X);
    this.playerO = this.getPlayer(Player.Name.O);
    this.currentPlayer = this.playerX;

    // Initialize the game variables.
    this.numberOfMoves = 0;
    this.gameOver = false;
    if (this.handler == null) {
      this.handler = new Handler();
    }
    this.resume();

    return this.board;

  }

  public void resume() {
    if (this.currentPlayer.getType() == Player.Type.Human) {
      this.playerPrompts.get(this.currentPlayer.getName()).show();
    }
    this.handler.postDelayed(this.updateTimeTask, this.pause);
  }

  public void pause() {
    this.handler.removeCallbacks(this.updateTimeTask);
  }

  private Player getPlayer(Player.Name name) {
    Player player;
    Player.Type type = Settings.getPlayerType(this.context, name);
    if (type == Player.Type.Human) {
      player = new GuiHumanPlayer(name, this.board);
      Dialog prompt = new Dialog(this.context, R.style.toast);
      Window window = prompt.getWindow();
      LayoutParams params = window.getAttributes();
      params.height = WindowManager.LayoutParams.WRAP_CONTENT;
      params.width = WindowManager.LayoutParams.WRAP_CONTENT;
      params.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
      params.flags |= WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
      params.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
      params.format = PixelFormat.TRANSLUCENT;
      params.type = WindowManager.LayoutParams.TYPE_TOAST;
      TextView text = new TextView(this.context);
      text.setText("Player " + name + "'s turn");
      text.setPadding(10, 10, 10, 10);
      text.setTextColor(0xffffffff);
      prompt.setContentView(text, params);
      this.playerPrompts.put(name, prompt);
    }
    else {
      player = new SmartComputerPlayer(name, this.board);
    }
    return player;
  }

	public void processMove(Location move, String flag) {
    // Store the move if it is valid.
	    if (!this.gameOver) {
	    	if (this.currentPlayer.getName().equals(gameAgent.getPlayer())){
	    		if (this.board.setMove(move, this.currentPlayer)) {
			    
					if (!flag.equals("local")) {
						gameAgent.setMove(move.getRow(), move.getColumn(), this.currentPlayer.toString());
					}
				      				
					this.update();
	    		}
	    	}
	
	    }

  }
	
	public void update() {
		if (this.currentPlayer.getType() == Player.Type.Human) {
	        this.playerPrompts.get(this.currentPlayer.getName()).hide();
		}
		
		this.board.invalidate();
	    this.numberOfMoves++;
	
	    // Check for the end of the game.
	    if (this.checkForWin()) {
		      this.output("Player " + this.currentPlayer.getName() + " wins!");
		      this.gameOver = true;
	    }
	    else if (this.numberOfMoves == 9) {
	        this.output("Draw");
	        this.gameOver = true;
	    }
	    else {
	
	        // Switch players.
	        if (this.currentPlayer == this.playerX) {
	        	this.currentPlayer = this.playerO;
	        }
	        else {
	        	this.currentPlayer = this.playerX;
	        }
	
	        // Show the prompt for the human player.
	        if (this.currentPlayer.getType() == Player.Type.Human) {
	        	this.playerPrompts.get(this.currentPlayer.getName()).show();
	        }
	
	    }
	}

  private boolean checkForWin() {
    boolean won;
    won = this.checkForWinInRows();
    if (won) {
      return true;
    }
    won = this.checkForWinInColumns();
    if (won) {
      return true;
    }
    won = this.checkForWinInDiagonal1();
    if (won) {
      return true;
    }
    won = this.checkForWinInDiagonal2();
    return won;
  }

  private boolean checkForWinInRows() {
    boolean won;
    for (int row = 0; row < Board.MAX_ROWS; row++) {
      won = this.checkForWinInRow(row);
      if (won) {
        return true;
      }
    }
    return false;
  }

  private boolean checkForWinInRow(int row) {
    Player.Name m0 = this.board.getMove(row, 0);
    if (m0 == Player.Name.NONE) {
      return false;
    }
    for (int column = 0; column < Board.MAX_COLUMNS; column++) {
      if (this.board.getMove(row, column) != m0) {
        return false;
      }
    }
    return true;
  }

  private boolean checkForWinInColumns() {
    boolean won;
    for (int column = 0; column < Board.MAX_COLUMNS; column++) {
      won = this.checkForWinInColumn(column);
      if (won) {
        return true;
      }
    }
    return false;
  }

  private boolean checkForWinInColumn(int column) {
    Player.Name m0 = this.board.getMove(0, column);
    if (m0 == Player.Name.NONE) {
      return false;
    }
    for (int row = 0; row < Board.MAX_ROWS; row++) {
      if (this.board.getMove(row, column) != m0) {
        return false;
      }
    }
    return true;
  }

  private boolean checkForWinInDiagonal1() {
    Player.Name m0 = this.board.getMove(0, 0);
    if (m0 == Player.Name.NONE) {
      return false;
    }
    for (int row = 0; row < Board.MAX_ROWS; row++) {
      if (this.board.getMove(row, row) != m0) {
        return false;
      }
    }
    return true;
  }

  private boolean checkForWinInDiagonal2() {
    Player.Name m0 = this.board.getMove(0, 2);
    if (m0 == Player.Name.NONE) {
      return false;
    }
    for (int row = 0; row < Board.MAX_ROWS; row++) {
      if (this.board.getMove(row, 2-row) != m0) {
        return false;
      }
    }
    return true;
  }

  //
  // Attributes
  //

  public Context context;

  private Board board;

  private Player playerX;

  private Player playerO;

  private Player currentPlayer;

  public Player getCurrentPlayer() {
	return currentPlayer;
}

private Map<Player.Name, Dialog> playerPrompts =
    new HashMap<Player.Name, Dialog>();

  private int numberOfMoves;

  private boolean gameOver;

  private Handler handler;

  private final int pause = 500;

  private Runnable updateTimeTask = new Runnable() {
	    //@Override
	    public void run() {
	    	Game.this.processMove(Game.this.currentPlayer.getMove(), "notify");
	    	if (Game.this.gameOver) {
	    		Game.this.pause();
	    	}
	    	else {
	    		Game.this.handler.postDelayed(this, Game.this.pause);
	    	}
	    }
  };

}
