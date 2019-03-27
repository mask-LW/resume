package com.resume.vo;

import java.util.List;

public class Work {
	private List<Work_exp> past;
	private List<Work_exp> current;
	public List<Work_exp> getPast() {
		return past;
	}
	public void setPast(List<Work_exp> past) {
		this.past = past;
	}
	public List<Work_exp> getCurrent() {
		return current;
	}
	public void setCurrent(List<Work_exp> current) {
		this.current = current;
	}
	
	
}
