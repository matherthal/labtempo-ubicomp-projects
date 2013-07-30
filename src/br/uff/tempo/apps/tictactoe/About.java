//
// About.java
//

package br.uff.tempo.apps.tictactoe;

import android.app.Activity;
import android.os.Bundle;
import br.uff.tempo.R;

public class About extends Activity {

  //
  // Activity interface
  //

  @Override
  protected void onCreate(Bundle savedInstance) {
    super.onCreate(savedInstance);
    this.setContentView(R.layout.about);
  }

}
