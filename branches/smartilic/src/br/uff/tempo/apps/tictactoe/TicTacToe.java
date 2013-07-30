//
// TicTacToe.java
//

package br.uff.tempo.apps.tictactoe;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import br.uff.tempo.R;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.ResourceRegister;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.interfaces.IResourceRegister;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.management.stubs.ResourceRegisterStub;
import br.uff.tempo.middleware.resources.GameAgent;
import br.uff.tempo.middleware.resources.interfaces.IGameAgent;
import br.uff.tempo.middleware.resources.stubs.GameAgentStub;

public class TicTacToe extends Activity {
	IGameAgent gameAgent;
	
  //
  // Constructors
  //

  public TicTacToe() {
		IResourceDiscovery discovery = new ResourceDiscoveryStub(IResourceDiscovery.rans);
		List<ResourceData> gameList = discovery.search(ResourceData.TYPE, ResourceAgent.type(GameAgent.class));
		int id = (int) Math.round(50 * Math.random());
		gameAgent = new GameAgent("Game"+id, "Game"+id);
		((GameAgent) gameAgent).identify();
		if (gameList != null){
			if (gameList.size()==1) {
				gameAgent.setPlayer(Player.Name.O);
			}
			for (ResourceData iGameAgentData : gameList) {
				IGameAgent iGameAgent = new GameAgentStub(iGameAgentData.getRans());
				((GameAgentStub) iGameAgent).registerStakeholder("setMove", ((GameAgent) gameAgent).getRANS());
				((GameAgent) gameAgent).registerStakeholder("setMove", iGameAgentData.getRans());
			}
		} else
		{
			gameAgent.setPlayer(Player.Name.X);
		}
		this.game = new Game(this, gameAgent);
		gameAgent.setGame((Game)this.game);
	  //this.game = new Game(this);
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
    Log.d("TicTacToe", gameAgent.getPlayer().toString()+" onPause()");
  }

  @Override
  public void onResume() {
    super.onResume();
    this.game.resume();
    Log.d("TicTacToe", gameAgent.getPlayer().toString()+" onResume()");
  }
  
  

  //
  // Attributes
  //

	@Override
protected void onDestroy() {
	super.onDestroy();
	IResourceDiscovery discovery = new ResourceDiscoveryStub(IResourceDiscovery.rans);
    IResourceRegister register = new ResourceRegisterStub(discovery.search(ResourceData.TYPE, ResourceAgent.type(ResourceRegister.class)).get(0).getRans());
    register.unregister(((GameAgent) gameAgent).getRANS());
    Log.d("TicTacToe", gameAgent.getPlayer().toString()+" destroyed");
}



	private Game game;

}
