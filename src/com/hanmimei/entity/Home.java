package com.hanmimei.entity;

import java.util.List;

public class Home {
	private int page_count;
	private List<Slider> sliders;
	private List<Theme> themes;
	private HMessage hMessage;
	public int getPage_count() {
		return page_count;
	}
	public void setPage_count(int page_count) {
		this.page_count = page_count;
	}
	public List<Slider> getSliders() {
		return sliders;
	}
	public void setSliders(List<Slider> sliders) {
		this.sliders = sliders;
	}
	public List<Theme> getThemes() {
		return themes;
	}
	public void setThemes(List<Theme> themes) {
		this.themes = themes;
	}
	public HMessage gethMessage() {
		return hMessage;
	}
	public void sethMessage(HMessage hMessage) {
		this.hMessage = hMessage;
	}
	

}
