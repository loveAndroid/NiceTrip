package com.azir.mvp.bean;

public class Message {

	private String title;
	private String info;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public String toString() {
		return "Message [title=" + title + ", info=" + info + "]";
	}
}
