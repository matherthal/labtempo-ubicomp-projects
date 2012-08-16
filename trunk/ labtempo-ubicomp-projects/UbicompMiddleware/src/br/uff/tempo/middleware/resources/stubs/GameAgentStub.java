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
		params.add(new Tuple<String, Object>(Integer.class.getName(), row));
		params.add(new Tuple<String, Object>(Integer.class.getName(), column));
		params.add(new Tuple<String, Object>(String.class.getName(), player));

		makeCall("setMove", params);
	}

}
