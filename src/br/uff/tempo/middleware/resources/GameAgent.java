package br.uff.tempo.middleware.resources;

import br.uff.tempo.apps.tictactoe.Board;
import br.uff.tempo.apps.tictactoe.Game;
import br.uff.tempo.apps.tictactoe.Location;
import br.uff.tempo.apps.tictactoe.Player;
import br.uff.tempo.apps.tictactoe.Player.Name;
import br.uff.tempo.apps.tictactoe.TicTacToe;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.resources.interfaces.IGameAgent;

public class GameAgent extends ResourceAgent implements IGameAgent {
	
	private static final long serialVersionUID = 1L;

	private Game game;
	private Player.Name player;
	private Board board;
	private Location move;

	public GameAgent(String name, String rans) {
		super(name, "br.uff.tempo.middleware.resources.GameAgent", rans);
	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
		if (method.equals("setMove")) {
			if (board != null) {
				String[] valueArray = value.toString().split(";");
				int row = Integer.valueOf(valueArray[0]);
				int column = Integer.valueOf(valueArray[1]);
				Location move = new Location(row, column);
				board.setMove(move, game.getCurrentPlayer());
				((TicTacToe)game.context).runOnUiThread(updateTimeTask);
			}
		}

	}

	@Override
	public void setGame(Game game) {
		this.game = game;
	}

	public void setBoard(Board board) {
		this.board = board;
	}
	
	@Override
	public void setMove(int row, int column, String player) {
		notifyStakeholders("setMove", row + ";" + column + ";" + player);
	}
	
	public void setPlayer(Player.Name player) {
		this.player = player;
	}

	private Runnable updateTimeTask = new Runnable() {
	    //@Override
	    public void run() {
	    	game.update();
	    }
	};

	@Override
	public Name getPlayer() {
		return player;
	}
}
