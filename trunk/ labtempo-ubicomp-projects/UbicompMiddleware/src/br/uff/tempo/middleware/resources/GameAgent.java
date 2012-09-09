package br.uff.tempo.middleware.resources;

import br.uff.tempo.apps.tictactoe.Game;
import br.uff.tempo.apps.tictactoe.Location;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.resources.interfaces.IGameAgent;

public class GameAgent extends ResourceAgent implements IGameAgent {

	private Game game;

	public GameAgent(String name) {
		super(name, "br.uff.tempo.middleware.resources.GameAgent", 0);
	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
		if (method.equals("setMove")) {
			if (game != null) {
				String[] valueArray = value.toString().split(";");
				int row = Integer.valueOf(valueArray[0]);
				int column = Integer.valueOf(valueArray[1]);
				Location move = new Location(row, column);
				game.processMove(move, "local");
			}
		}

	}

	@Override
	public void setGame(Game game) {
		this.game = game;
	}

	@Override
	public void setMove(int row, int column, String player) {
		notifyStakeholders("setMove", row + ";" + column + ";" + player);
	}
}
