package com.play.ground;

import java.util.Arrays;

import javax.faces.bean.ManagedBean;

import org.apache.commons.lang3.StringUtils;

@ManagedBean
public class LanguageForm {
	private String[] validLanguages = { "java", "groovy" };
	private String language;

	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String language) {
		if (language != null) {
			this.language = language.trim();
		}
	}

	public String showChoice() {
		String result = "check-language/";
		if (StringUtils.isBlank(this.language)) {
			return result + "missing-language";
		} else if (Arrays.stream(this.validLanguages).anyMatch(this.language::equalsIgnoreCase)) {
			return result + "good-language";
		} else {
			return result + "bad-language";
		}
	}
}
