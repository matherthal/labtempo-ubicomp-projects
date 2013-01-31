package br.uff.tempo.apps.simulators.creator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import br.uff.tempo.R;

public class SimulCreatorActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simul_creator);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.simul_creator, menu);
        return true;
    }
}
