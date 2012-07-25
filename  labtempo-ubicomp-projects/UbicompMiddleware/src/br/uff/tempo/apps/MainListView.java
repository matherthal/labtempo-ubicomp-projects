package br.uff.tempo.apps;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import br.uff.tempo.R;
import br.uff.tempo.apps.baseview.BaseView;
import br.uff.tempo.apps.bed.BedView;
import br.uff.tempo.apps.map.MapActivity;
import br.uff.tempo.apps.rule.RuleActivity;
import br.uff.tempo.apps.stove.StoveView;
import br.uff.tempo.apps.tictactoe.TicTacToe;
import br.uff.tempo.apps.tv.TvView;
import br.uff.tempo.middleware.comm.current.api.Dispatcher;

public class MainListView extends ListActivity {

	// @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Dispatcher d = Dispatcher.getInstance();

		if (!d.isAlive())
			d.start();// start listener

		// Print devices list
		String[] devices = getResources().getStringArray(R.array.devices_array);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, devices));

		final ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			// @Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				String item = lv.getItemAtPosition(position).toString();

				// TODO: Change name to ID. There's a problem with some special
				// characters

				// Calling appropriate activity
				if (item.equals("Regra")) {
					Intent intent = new Intent(MainListView.this, RuleActivity.class);
					startActivity(intent);
				} else if (item.equals("Fogao")) {
					Intent intent = new Intent(MainListView.this, StoveView.class);
					startActivity(intent);
				} else if (item.equals("Cama")) {
					Intent intent = new Intent(MainListView.this, BedView.class);
					startActivity(intent);
				} else if (item.equals("Televisao")) {
					Intent intent = new Intent(MainListView.this, TvView.class);
					startActivity(intent);
				} else if (item.equals("Repositorio de Recursos")) {
					Intent intent = new Intent(MainListView.this, BaseView.class);
					startActivity(intent);
				} else if (item.equals("Mapa da casa")) {
					Intent intent = new Intent(MainListView.this, MapActivity.class);
					startActivity(intent);
				} else if (item.equals("Jogo da velha")) {
					Intent intent = new Intent(MainListView.this, TicTacToe.class);
					startActivity(intent);
				} else {
					Toast.makeText(MainListView.this, "Não existe agente para este item: " + item.toString(), Toast.LENGTH_SHORT).show();
				}
			}
		});

		// ///////////DEBUG
		// Log.d("", "Teste inicio");
		// Toast.makeText(this, "Teste inicio", Toast.LENGTH_SHORT).show();
		// Exemplo de agregação, interpretação e criação
		// intent = new Intent(this, Stove.class);
		// bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		// new Stove("Fogao Querido");
		// //IResourceDiscovery discover = ResourceAgent.getRDS();
		// IResourceDiscovery discover = new
		// ResourceDiscoveryStub(IResourceDiscovery.RDS_ADDRESS);
		// Log.d("", "search");
		// String rai = discover.search("Stove").get(0);
		// Log.d("", "rai = " + rai);
		// IStove stove = new StoveStub(rai);
		// Log.d("", "stove = " + stove.getId());
		//
		// Interpreter intp = new Interpreter();
		// Log.d("", "interpretador = " + intp.getId());
		// intp.setName("Perigo Vazamento de Gás");
		//
		// //Set Context Variable
		// String cvName = "Vazamento de gás";
		// intp.setContextVariable(stove, cvName);
		//
		// //Set conditional results
		// float cte = 0.7f;
		// String res = "Emergência!";
		// intp.setConditionalResult(Operation.GreaterThanOrEqual, cte, res);
		// cte = 0.4f;
		// res = "Alerta";
		// intp.setConditionalResult(Operation.GreaterThanOrEqual, cte, res);
		// cte = 0.4f;
		// res = "Normal";
		// intp.setConditionalResult(Operation.LessThan, cte, res);
		//
		// //Interpretation
		// res = intp.interpretToString();
		// Log.d("", "Interpretation: " + res);
		//
		// Toast.makeText(this, "Resultado: " + res, Toast.LENGTH_LONG).show();
	}
}
