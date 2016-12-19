package com.azias.module.config;

public class ConfigException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public ConfigException(String par1) {
		super(par1);
	}
	
	public ConfigException(String par1, Object... par2) {
		super(String.format(par1, par2));
	}
}
