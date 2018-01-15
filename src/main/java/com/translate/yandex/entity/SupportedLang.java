package com.translate.yandex.entity;

import java.util.List;

public class SupportedLang {

	private List<String> dirs;
	private List<String> langs;
	
	public List<String> getDirs() {
		return dirs;
	}
	public void setDirs(List<String> dirs) {
		this.dirs = dirs;
	}
	public List<String> getLangs() {
		return langs;
	}
	public void setLangs(List<String> langs) {
		this.langs = langs;
	}
}
