package br.uff.tempo.middleware.resources.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.apps.tictactoe.Board;
import br.uff.tempo.apps.tictactoe.Game;
import br.uff.tempo.apps.tictactoe.Player;
import br.uff.tempo.apps.tictactoe.Player.Name;
import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.resources.interfaces.IGameAgent;

public class GameAgentStub extends ResourceAgentStub implements IGameAgent {
	
	private static final long serialVersionUID = 1L;

	public GameAgentStub(String rai) {
		super(rai);
	}

	@Override
	public void setGame(Game game) {

	}

	@Override
	public void setMove(int row, int column, String player) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Integer.class.getName(), row));
		params.add(new Tuple<String, Object>(Integer.class.getName(), column));
		params.add(new Tuple<String, Object>(String.class.getName(), player));

		makeCall("setMove", params, void.class);
	}

	@Override
	public void setPlayer(Player.Name player) {
		List<Tuple<String, Object>> params  = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Player.Name.class.getName(), player));
		
		makeCall("setPLayer", params, void.class);
	}
	
	@Override
	public void setBoard(Board board) {
	
	}

	@Override
	public Name getPlayer() {
		// TODO Auto-generated method stub
		return null;
	}

}
