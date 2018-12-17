package com.play.ground;

import javax.faces.bean.ManagedBean;
import java.util.Random;

@ManagedBean
public class Navigator {
	private String[] results = { "page1", "page2", "page3" };

	public String choosePage() {
		int i = new Random().nextInt(this.results.length);
		return "random/" + this.results[i];
	}
}
