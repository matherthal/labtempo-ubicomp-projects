package br.uff.tempo.apps.map.rule;

import org.andengine.entity.text.Text;

public class ContextMenuItem {
	
	private String label;
	private Text text;
	private Object extra;
	private Integer id;
	private String rans;
	private String methodName;
	
	public String getRans() {
		return rans;
	}

	public void setRans(String rans) {
		this.rans = rans;
	}

	private static int count = 0;
	
	public ContextMenuItem(String label) {
		this.label = label;
		this.id = count++;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Object getExtra() {
		return extra;
	}

	public void setExtra(Object extra) {
		this.extra = extra;
	}

	public Integer getId() {
		return id;
	}

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
}
