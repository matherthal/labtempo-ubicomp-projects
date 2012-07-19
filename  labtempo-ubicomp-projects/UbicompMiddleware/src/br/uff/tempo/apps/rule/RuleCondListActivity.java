package br.uff.tempo.apps.rule;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import br.uff.tempo.R;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.resources.Condition;
import br.uff.tempo.middleware.resources.Stove;
import br.uff.tempo.middleware.resources.Teapot;



public class RuleCondListActivity extends Activity {
	private ArrayList<Condition> conds = new ArrayList<Condition>();
	private ArrayAdapter<String> lvAdapter;
	private ArrayList<String> listItems = new ArrayList<String>();
	
	@Override
	  public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rule_cond_list);
        
        //Initialize Condition's ListView
    	ListView lv = (ListView) findViewById(R.id.listViewConds);
    	listItems.add("Bed.Temperature > 20");
    	listItems.add("Stove.isOn");
		lvAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, listItems);
		lv.setAdapter(lvAdapter);
	}

	/*
	 * Button Add Condition
	 * Calls the activity for creation of conditions 
	 */
	public void buttonAddCond_Clicked(View view) {
		Intent intent = new Intent(RuleCondListActivity.this,
				RuleConditionActivity.class);
		startActivityForResult(intent, 1);
	}

	public void buttonRmCond_Clicked(View view) {

	}

	/*
	 * onActivityResult
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 * Receives the result of activities called. For getting the conditions and actions, in case. 
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				// Condition cond =
				// (Condition)data.getSerializableExtra("COND");
				//String[] condStr = data.getStringArrayExtra("COND_LIST");
				//createCond(condStr[0], condStr[1], condStr[2], condStr[3]);
				//>>>Condition cond = (Condition) Serialization.deserializeObject(data.getByteArrayExtra("COND"));
				//Add condition to list of conditions
		    	//>>>conds.add(cond);
		    	//Add condition to list in the view
				Teapot ra = new Teapot();
				ra.setId(0001);
				ra.setName("teapot01");
				ra.setType("Teapot");
				ra.identify();
				Condition cond = null;
				try {
					cond = new Condition(ra, "temperature", null, ">=", "100", 0);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	listItems.add(cond.toString());
				lvAdapter.notifyDataSetChanged();
				
				Toast.makeText(this, "Nova Condição", Toast.LENGTH_SHORT)
						.show();
			} else if (resultCode == RESULT_CANCELED) {
			}
		}
	}

	/*
	 * Creates a condition and adds to the list. Then a rule could be created after. 
	 */
	public void createCond(String raID, String attrib, String operation, String value) {
		//ResourceAgent ra = (ResourceAgent)discovery.search(s.getSelectedItem().toString()).get(0);
		ResourceAgent ra = new Stove();//FIXME: get it out of here, just for debug. The correct is above
    	
    	//Get attribute's acess method
		//Method mtd = null;
		//try {
			//mtd = ra.getClass().getMethod("get" + attrib);
			//mtd = ra.getClass().getMethod("getIsOn");
		//} catch (SecurityException e) {
			//Toast.makeText(this, "Erro ao pegar atributo", Toast.LENGTH_LONG);
			//return;
		//} catch (NoSuchMethodException e) {
		//	Toast.makeText(this, "Erro ao pegar atributo", Toast.LENGTH_LONG);
		//	return;
		//}
		String mtd = "get" + attrib;
		
    	Condition cond;
    	try {
    		//Initialize Condition
			cond = new Condition(ra, mtd, null, operation, value, 0);
		} catch (Exception e) {
			Toast.makeText(this, "Erro ao criar a condição", Toast.LENGTH_LONG);
			return;
		} 
    	
    	//Add condition to list of conditions
    	conds.add(cond);
    	//Add condition to list in the view
    	listItems.add(cond.toString());
		lvAdapter.notifyDataSetChanged();
	}
    
    public void buttonFinish_Clicked(View view) {
    	byte[] condsRaw = Serialization.serializeObject(conds);
    	//Send Condition back
    	Intent data = this.getIntent();
    	data.putExtra("COND_LIST", condsRaw);
    	if (getParent() == null) {
    	    setResult(Activity.RESULT_OK, data);
    	} else {
    	    getParent().setResult(Activity.RESULT_OK, data);
    	}
    	finish();
    }
}
