package br.uff.tempo.middleware.resources.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.apps.tictactoe.Game;
import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.resources.interfaces.IGameAgent;

public class GameAgentStub extends ResourceAgentStub implements IGameAgent {

	public GameAgentStub(String url) {
		super(url);
	}

	@Override
	public void setGame(Game game) {

	}

	@Override
	public void setMove(int row, int column, String player) {
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>("row", row));
		params.add(new Tuple<String, Object>("column", column));
		params.add(new Tuple<String, Object>("player", player));

		makeCall("setMove", params);
	}

}
