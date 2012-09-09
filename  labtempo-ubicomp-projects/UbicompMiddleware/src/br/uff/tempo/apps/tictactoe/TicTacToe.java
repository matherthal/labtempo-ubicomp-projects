//
// TicTacToe.java
//

package br.uff.tempo.apps.tictactoe;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import br.uff.tempo.R;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.resources.GameAgent;
import br.uff.tempo.middleware.resources.interfaces.IGameAgent;
import br.uff.tempo.middleware.resources.stubs.GameAgentStub;

public class TicTacToe extends Activity {

  //
  // Constructors
  //

  public TicTacToe() {
		IResourceDiscovery discovery = new ResourceDiscoveryStub(IResourceDiscovery.RDS_ADDRESS);
		ArrayList<String> gameList = discovery.search("GameAgent");
		IGameAgent gameAgent;
		int id = (int) Math.round(50 * Math.random());
		gameAgent = new GameAgent("Game"+id);
		((GameAgent) gameAgent).identify();
		if (gameList != null){
			for (String iGameAgentRai : gameList) {
				IGameAgent iGameAgent = new GameAgentStub(iGameAgentRai);
				((GameAgentStub) iGameAgent).registerStakeholder("setMove", ((GameAgent) gameAgent).getRAI());
				((GameAgent) gameAgent).registerStakeholder("setMove", iGameAgentRai);
			}
		}
		this.game = new Game(this, gameAgent);
		gameAgent.setGame((Game)this.game);
  }

  //
  // Activity interface
  //

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
		View view = this.game.initialize();
		this.setContentView(view);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    MenuInflater inflater = this.getMenuInflater();
    inflater.inflate(R.menu.menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.new_game:
        this.setContentView(this.game.initialize());
        return true;
      case R.id.settings:
        this.startActivity(new Intent(this, Settings.class));
        return true;
      case R.id.about:
        this.startActivity(new Intent(this, About.class));
        return true;
    }
    return false;
  }

  @Override
  public void onPause() {
    super.onPause();
    this.game.pause();
  }

  @Override
  public void onResume() {
    super.onResume();
    this.game.resume();
  }

  //
  // Attributes
  //

	private Game game;

}
