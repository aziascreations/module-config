package com.azias.module.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigManager {
	private final static Logger logger = LoggerFactory.getLogger(ConfigManager.class);
	protected ArrayList<String> configsFolder;
	protected HashMap<String, Config> configs;

	public ConfigManager() throws ConfigException {
		this("./config/");
	}
	
	public ConfigManager(String defaultConfigFolder) throws ConfigException {
		logger.debug("Instantiating ConfigManager with \"{}\" as the argument.", defaultConfigFolder);
		this.configsFolder = new ArrayList<String>();
		this.configs = new HashMap<String, Config>();
		
		if(!this.addConfigFolder(defaultConfigFolder)) {
			logger.error("Unable to add the default config folder: {}", defaultConfigFolder);
			throw new ConfigException("Unable to add \"%s\" as a config folder.", defaultConfigFolder);
		}
	}
	
	public boolean addConfigFolder(String folderPath) {
		File folder = new File(folderPath);
		if(folder.exists()) {
			if(folder.isDirectory()) {
				logger.debug("Added \"{}\" as a config folder.", folderPath);
				this.configsFolder.add(folderPath);
				return true;
			} else {
				logger.warn("Unable to add a file as ");
			}
		} else {
			logger.warn("Unable to find \"{}\" to add it as a config folder.", folderPath);
		}
		return false;
	}
	
	public boolean registerConfig(Config config) {
		return this.registerConfig(config, false);
	}
	
	public boolean registerConfig(Config config, boolean override) {
		if(this.configs.containsKey(config.getId())) {
			if(override) {
				logger.debug("Removing {} from the registered configs.", config.getId());
			} else {
				logger.warn("A config is already registered with this id: {}.", config.getId());
				return false;
			}
		}
		
		this.configs.put(config.getId(), config);
		logger.debug("Registered config with {} as it's id.", config.getId());
		return true;
	}
	
	public String getString(String configKey, String fieldKey) {
		if(!this.configs.containsKey(configKey)) {
			logger.warn("Requested String from unregistered config: {}:{}.\nReturning an empty String.", configKey, fieldKey);
			return "";
		}
		return this.configs.get(configKey).getString(fieldKey);
	}
	
	public Integer getInteger(String configKey, String fieldKey) {
		if(!this.configs.containsKey(configKey)) {
			logger.warn("Requested Integer from unregistered config: {}:{}.\nReturning 0.", configKey, fieldKey);
			return 0;
		}
		return this.configs.get(configKey).getInteger(fieldKey);
	}
	
	public Float getFloat(String configKey, String fieldKey) {
		if(!this.configs.containsKey(configKey)) {
			logger.warn("Requested Float from unregistered config: {}:{}.\nReturning 0.0F.", configKey, fieldKey);
			return 0.0F;
		}
		return this.configs.get(configKey).getFloat(fieldKey);
	}
	
	public Boolean getBoolean(String configKey, String fieldKey) {
		if(!this.configs.containsKey(configKey)) {
			logger.warn("Requested Boolean from unregistered config: {}:{}.\nReturning false.", configKey, fieldKey);
			return false;
		}
		return this.configs.get(configKey).getBoolean(fieldKey);
	}
}
