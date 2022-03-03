package com.prac.spring;

public class VoteDTO {
	private String title;
	private String[] op;
	
	public VoteDTO() {;}
	
	public VoteDTO(String title, String[] op) {
		this.title = title; 
		this.op = op;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String[] getOp() {
		return op;
	}

	public void setOp(String[] op) {
		this.op = op;
	}
}
