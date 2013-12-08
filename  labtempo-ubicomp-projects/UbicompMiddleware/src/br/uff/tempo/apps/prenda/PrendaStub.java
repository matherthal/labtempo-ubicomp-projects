package br.uff.tempo.apps.prenda;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.LogOpenHelper.LogObject;
import br.uff.tempo.middleware.management.Place;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.stubs.Stub;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.Stakeholder;

public class PrendaStub extends Stub implements IPrenda {
	
	public PrendaStub(String rans) {
		super(rans);
	}

	@Override
	public String getResourceClassName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerStakeholder(String method, String rai) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Stakeholder> getStakeholders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeStakeholder(String method, String rai) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getRANS() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRANS(String rai) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setType(String type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<ResourceAgent> getInterests() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInterests(ArrayList<ResourceAgent> interests) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isRegistered() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean identify() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unregister() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Position getPosition() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();

		return (Position) makeCall("getPosition", params, Position.class);
	}

	@Override
	public void notifyStakeholders(String method, Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateLocation(Position position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean identifyPosition(Position position) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean identifyInPlace(String placeName, Position position) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public LogObject getLog(Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void log(String record) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPosition(Position pos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Place getPlace() {
		// TODO Auto-generated method stub
		return null;
	}

}
