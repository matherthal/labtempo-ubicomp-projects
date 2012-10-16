package br.uff.tempo.middleware.management;

/**
 * Contains fundamental description about one Resource Agent at Resource Repository
 * 
 * Informations to communicate, types, name and others
 */
public class ResourceAgentDescription {
	
	private ResourceAgentNS raNS;
	
	/* Today "rai" carry the a idea of type, name and "ResourceAgent" description. Probably it should be revisited */
	private String rai;
	
	/* Just to demonstrate the idea of this class */
	private String type;
	
	/* Just to demonstrate the idea of this class */
	private String subType;
	
	/* Just to demonstrate the idea of this class */
	private String name;
	
	public ResourceAgentDescription(ResourceAgentNS raNS, String rai) {
		this.raNS = raNS;
		this.rai = rai;
	}

	public ResourceAgentNS getRaNS() {
		return raNS;
	}

	public void setRaNS(ResourceAgentNS raNS) {
		this.raNS = raNS;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getRai() {
		return rai;
	}

	public void setRai(String rai) {
		this.rai = rai;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
