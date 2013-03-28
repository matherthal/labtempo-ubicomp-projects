package br.uff.tempo.apps.talk2String.client;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.apps.talk2String.ITalk;
import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;

public class TalkStub extends ResourceAgentStub implements ITalk {

	private static final long serialVersionUID = 71625180679220725L;
	
	public TalkStub(String rans) {
		super(rans);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getMessage() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		return (String) makeCall("getMessage", params, String.class);
	}

	@Override
	public void setMessage(String msg) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), msg));
		
		makeCall("setMessage", params, void.class);
	}
	
	public String getURLStream(){
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		return (String) makeCall("getURLStream", params, String.class);
	}

	@Override
	public void setURLStream(String url) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), url));
		
		makeCall("setURLStream", params, void.class);
	}

}
