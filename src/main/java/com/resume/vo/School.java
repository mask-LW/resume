package com.resume.vo;

import java.util.List;

public class School {
	private List<School_exp> past;
	private List<School_exp> current;
	
	public List<School_exp> getPast() {
		return past;
	}
	public void setPast(List<School_exp> past) {
		this.past = past;
	}
	public List<School_exp> getCurrent() {
		return current;
	}
	public void setCurrent(List<School_exp> current) {
		this.current = current;
	}
	
}
