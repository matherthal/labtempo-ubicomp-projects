package br.uff.tempo.middleware.comm.interest.api;

import br.uff.tempo.middleware.management.ResourceAgentNS;

public class RepaMessageContent {
	
	private String id;
	
	private Integer prefixFrom;
	
	private ResourceAgentNS raNSFrom;
	
	private Integer prefixTo;
	
	private ResourceAgentNS raNSTo;
	
	private String interest;
	
	private String content;
	
	private boolean reply;

	private RepaMessageContent(String id, Integer prefixFrom, ResourceAgentNS raNSFrom, Integer prefixTo, ResourceAgentNS raNSTo, String interest, String content) {
		this.id = id;
		this.prefixFrom = prefixFrom;
		this.raNSFrom = raNSFrom;
		this.prefixTo = prefixTo;
		this.raNSTo = raNSTo;
		this.interest = interest;
		this.content = content;
	}
	
	public RepaMessageContent(String id, ResourceAgentNS raNSFrom, ResourceAgentNS raNSTo, String interest, String content) {
		this(id, null, raNSFrom, null, raNSTo, interest, content);
	}
	
	public RepaMessageContent(String id, ResourceAgentNS raNSFrom, Integer prefixTo, String interest, String content) {
		this(id, null, raNSFrom, prefixTo, null, interest, content);
	}	
	
	public RepaMessageContent(String id, Integer prefixFrom, ResourceAgentNS raNSTo, String interest, String content) {
		this(id, prefixFrom, null, null, raNSTo, interest, content);
	}
	
	public RepaMessageContent(String id, Integer prefixFrom, Integer prefixTo, String interest, String content) {
		this(id, prefixFrom, null, prefixTo, null, interest, content);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Integer getPrefixFrom() {
		return prefixFrom;
	}

	public void setPrefixFrom(Integer prefixFrom) {
		this.prefixFrom = prefixFrom;
	}

	public ResourceAgentNS getRaNSFrom() {
		return raNSFrom;
	}

	public void setRaNSFrom(ResourceAgentNS raNSFrom) {
		this.raNSFrom = raNSFrom;
	}

	public Integer getPrefixTo() {
		return prefixTo;
	}

	public void setPrefixTo(Integer prefixTo) {
		this.prefixTo = prefixTo;
	}
	
	public ResourceAgentNS getRaNSTo() {
		return raNSTo;
	}

	public void setRaNSTo(ResourceAgentNS raNSTo) {
		this.raNSTo = raNSTo;
	}
	
	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isReply() {
		return reply;
	}

	public void setReply(boolean reply) {
		this.reply = reply;
	}

	public void swapRaNS() {
		ResourceAgentNS aux = this.raNSFrom;
		this.raNSFrom = this.raNSTo;
		this.raNSTo = aux;
	}

	public void swapPrefix() {
		Integer aux = this.prefixFrom;
		this.prefixFrom = this.prefixTo;
		this.prefixTo = aux;
	}

	@Override
	public String toString() {
		return String.format("id: %s prefixFrom: %s raNSFrom: %s prefixTo: %s raNSTo: %s interest: %s content: %s", id, prefixFrom, raNSFrom, prefixTo, raNSTo, interest, content);
	}
}
