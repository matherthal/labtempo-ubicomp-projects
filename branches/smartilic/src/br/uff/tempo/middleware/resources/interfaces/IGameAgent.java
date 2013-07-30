package br.uff.tempo.middleware.resources.interfaces;

import br.uff.tempo.apps.tictactoe.Board;
import br.uff.tempo.apps.tictactoe.Game;
import br.uff.tempo.apps.tictactoe.Player;

public interface IGameAgent {

	void setGame(Game game);

	void setPlayer(Player.Name player);
	
	void setMove(int row, int column, String player);

	Player.Name getPlayer();

	void setBoard(Board board);

}
