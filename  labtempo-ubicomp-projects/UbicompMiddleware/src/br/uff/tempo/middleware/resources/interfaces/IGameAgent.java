package br.uff.tempo.middleware.resources.interfaces;

import br.uff.tempo.apps.tictactoe.Game;

public interface IGameAgent {

	void setGame(Game game);

	void setMove(int row, int column, String player);

}
