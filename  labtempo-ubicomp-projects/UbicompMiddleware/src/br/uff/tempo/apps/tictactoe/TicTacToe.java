//
// TicTacToe.java
//

package br.uff.tempo.apps.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import br.uff.tempo.R;

public class TicTacToe extends Activity {

  //
  // Constructors
  //

  public TicTacToe() {
    this.game = new Game(this);
  }

  //
  // Activity interface
  //

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.setContentView(this.game.initialize());
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
