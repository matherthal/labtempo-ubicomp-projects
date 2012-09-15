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
import br.uff.tempo.apps.baselocationview.BaseLocationView;
import br.uff.tempo.apps.baseview.BaseView;
import br.uff.tempo.apps.counter.CounterView;
import br.uff.tempo.apps.map.MapActivity;
import br.uff.tempo.apps.onOffCounter.CounterApp;
import br.uff.tempo.apps.onoffsample.OnOffView;
import br.uff.tempo.apps.rule.RuleActivity;
import br.uff.tempo.apps.simulators.bed.BedView;
import br.uff.tempo.apps.simulators.lamp.LampView;
import br.uff.tempo.apps.simulators.stove.StoveView;
import br.uff.tempo.apps.simulators.tv.TvView;
import br.uff.tempo.apps.tictactoe.TicTacToe;
import br.uff.tempo.middleware.LogViewerActivity;
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
				} else if (item.equals("Base de Lugares")) {
					Intent intent = new Intent(MainListView.this, BaseLocationView.class);
					startActivity(intent);
				} else if (item.equals("Mapa da casa")) {
					Intent intent = new Intent(MainListView.this, MapActivity.class);
					startActivity(intent);
				} else if (item.equals("Jogo da velha")) {
					Intent intent = new Intent(MainListView.this, TicTacToe.class);
					startActivity(intent);
				} else if (item.equals("Lampada")) {
					Intent intent = new Intent(MainListView.this, LampView.class);
					startActivity(intent);
				} else if (item.equals("Controlador de Lampada")) {
					Intent intent = new Intent(MainListView.this, AppLampController.class);
					startActivity(intent);
				} else if (item.equals("DeviceOnOff")) {
					Intent intent = new Intent(MainListView.this, OnOffView.class);
					startActivity(intent);
				} else if (item.equals("Contador de OnOff")) {
					Intent intent = new Intent(MainListView.this, CounterApp.class);
					startActivity(intent);
				} else if (item.equals("Counter")) {
					Intent intent = new Intent(MainListView.this, CounterView.class);
					startActivity(intent);
				} else if (item.equals("Sistema de Controle de Lâmpadas")) {
					Intent intent = new Intent(MainListView.this, AppLampControlSystem.class);
					startActivity(intent);
				} else if (item.equals("SmartAndroid Debugger")) {
					Intent intent = new Intent(MainListView.this, LogViewerActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(MainListView.this, "Não existe agente para este item: " + item.toString(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
