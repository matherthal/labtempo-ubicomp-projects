package br.uff.tempo.middleware.resources;

import org.json.JSONException;

import br.uff.tempo.apps.tictactoe.Game;
import br.uff.tempo.apps.tictactoe.Location;
import br.uff.tempo.middleware.comm.current.api.JSONHelper;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.resources.interfaces.IGameAgent;

public class GameAgent extends ResourceAgent implements IGameAgent {

	private Game game;

	public GameAgent(String name) {
		super(name, "br.uff.tempo.middleware.resources.GameAgent", 0);
	}

	@Override
	public void notificationHandler(String change) {
		String method = JSONHelper.getChange("method", change).toString();
		if (method.equals("setMove")) {
			if (game != null) {
				String value = JSONHelper.getChange("value", change).toString();
				String[] valueArray = value.split(";");
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
		try {
			notifyStakeholders(JSONHelper.createChange(this.getURL(), "setMove", row + ";" + column + ";" + player));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
