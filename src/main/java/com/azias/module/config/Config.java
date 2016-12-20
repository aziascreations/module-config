package com.azias.module.config;

import java.util.HashMap;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class Config {
	private final static Logger logger = LoggerFactory.getLogger(Config.class);
	protected final String id = "config.default";
	protected String configFileName, configFileLocation;
	protected boolean hasBeenLoaded;
	
	// Config "fields"
	protected HashMap<String, String> defautsString;
	protected HashMap<String, Integer> defautsInteger;
	protected HashMap<String, Float> defautsFloat;
	protected HashMap<String, Boolean> defautsBoolean;
	
	//Only contains the "final" values.
	protected HashMap<String, String> fieldsString;
	protected HashMap<String, Integer> fieldsInteger;
	protected HashMap<String, Float> fieldsFloat;
	protected HashMap<String, Boolean> fieldsBoolean;
	
	public Config(String configFileLocation) throws ConfigException {
		//Couldn't use "this.id" in the "this(var, var)" constructor.
		this.configFileName = this.id;
		this.configFileLocation = configFileLocation;
		this.hasBeenLoaded = false;
		
		this.initializeFields();
		this.registerFields();
	}
	
	public Config(String configFileName, String configFileLocation) throws ConfigException {
		this.configFileName = configFileName;
		this.configFileLocation = configFileLocation;
		this.hasBeenLoaded = false;
		
		this.initializeFields();
		this.registerFields();
	}
	
	private void initializeFields() {
		this.defautsString = new HashMap<String, String>();
		this.defautsInteger = new HashMap<String, Integer>();
		this.defautsFloat = new HashMap<String, Float>();
		this.defautsBoolean = new HashMap<String, Boolean>();
		
		this.fieldsString = new HashMap<String, String>();
		this.fieldsInteger = new HashMap<String, Integer>();
		this.fieldsFloat = new HashMap<String, Float>();
		this.fieldsBoolean = new HashMap<String, Boolean>();
	}
	
	protected void registerFields() throws ConfigException {
		logger.warn("The default \"registerFields\" function was called for {}.", this.id);
	}
	
	public boolean processConfigFile(String configText) throws ConfigException {
		JsonParser jsonParser = new JsonParser();
		//TODO: check for nulls for the getters
		try {
			JsonObject jObj = (JsonObject)jsonParser.parse(configText);
			for(Entry<String, JsonElement> e : jObj.entrySet()) {
				if(this.defautsString.containsKey(e.getKey())) {
					this.fieldsString.put(e.getKey(), e.getValue().getAsString());
				} else if(this.defautsInteger.containsKey(e.getKey())) {
					this.defautsInteger.put(e.getKey(), e.getValue().getAsInt());
				} else if(this.defautsFloat.containsKey(e.getKey())) {
					this.defautsFloat.put(e.getKey(), e.getValue().getAsFloat());
				} else if(this.defautsBoolean.containsKey(e.getKey())) {
					this.defautsBoolean.put(e.getKey(), e.getValue().getAsBoolean());
				} else {
					logger.warn("Unable to process {}:{}", this.id, e.getKey());
				}
			}
		} catch(JsonSyntaxException jse) {
			logger.error("Syntax error in config's json String, using the default config.");
			jse.printStackTrace();
			return false;
		}
		
		logger.info("Config {} loaded.", this.id);
		this.hasBeenLoaded = true;
		return true;
	}
	
	@Deprecated
	protected String getJsonConfig() {
		//JsonObject jsonObj = new JsonObject();
		/*Iterator it = mp.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }/**/
		return "";
	}
	
	protected void registerString(String name, String defaultValue) throws ConfigException {
		if(Strings.isNullOrEmpty(defaultValue)) {
			logger.error("The default value for {}:{} is null.", this.id, name);
			throw new ConfigException("The default value for %s:%s is null", this.id, name);
		}
		this.defautsString.put(name, defaultValue);
	}
	
	protected void registerInteger(String name, Integer defaultValue) throws ConfigException {
		if(defaultValue==null) {
			logger.error("The default value for {}:{} is null.", this.id, name);
			throw new ConfigException("The default value for %s:%s is null", this.id, name);
		}
		this.defautsInteger.put(name, defaultValue);
	}
	
	protected void registerFloat(String name, Float defaultValue) throws ConfigException {
		if(defaultValue==null) {
			logger.error("The default value for {}:{} is null.", this.id, name);
			throw new ConfigException("The default value for %s:%s is null", this.id, name);
		}
		this.defautsFloat.put(name, defaultValue);
	}
	
	protected void registerBoolean(String name, Boolean defaultValue) throws ConfigException {
		if(defaultValue==null) {
			logger.error("The default value for {}:{} is null.", this.id, name);
			throw new ConfigException("The default value for %s:%s is null", this.id, name);
		}
		this.defautsBoolean.put(name, defaultValue);
	}
	
	protected String getString(String key) {
		if(Strings.isNullOrEmpty(key)) {
			logger.warn("Requested String from {}:{} with a null or empty key.\nReturning an empty String.", this.id, key);
			return "";
		}
		String result;
		
		if(this.fieldsString.containsKey(key)) {
			result = this.fieldsString.get(key);
		} else {
			result = this.defautsString.get(key);
		}
		
		if(Strings.isNullOrEmpty(result)) {
			logger.error("The value from {}:{} is null or empty.\nThis shouldn't happen.", this.id, key);
			return "";
		}
		return result;
	}
	
	protected Integer getInteger(String key) {
		if(Strings.isNullOrEmpty(key)) {
			logger.warn("Requested Integer from {}:{} with a null or empty key.\nReturning 0.", this.id, key);
			return 0;
		}
		Integer result;
		
		if(this.fieldsInteger.containsKey(key)) {
			result = this.fieldsInteger.get(key);
		} else {
			result = this.defautsInteger.get(key);
		}
		
		if(result==null) {
			logger.error("The value from {}:{} is null or empty.\nThis shouldn't happen.", this.id, key);
			return 0;
		}
		return result;
	}
	
	protected Float getFloat(String key) {
		if(Strings.isNullOrEmpty(key)) {
			logger.warn("Requested Float from {}:{} with a null or empty key.\nReturning 0.0F.", this.id, key);
			return 0.0F;
		}
		Float result;
		
		if(this.fieldsFloat.containsKey(key)) {
			result = this.fieldsFloat.get(key);
		} else {
			result = this.defautsFloat.get(key);
		}
		
		if(result==null) {
			logger.error("The value from {}:{} is null or empty.\nThis shouldn't happen.", this.id, key);
			return 0.0F;
		}
		return result;
	}
	
	protected Boolean getBoolean(String key) {
		if(Strings.isNullOrEmpty(key)) {
			logger.warn("Requested Boolean from {}:{} with a null or empty key.\nReturning false.", this.id, key);
			return false;
		}
		Boolean result;
		
		if(this.fieldsBoolean.containsKey(key)) {
			result = this.fieldsBoolean.get(key);
		} else {
			result = this.defautsBoolean.get(key);
		}
		
		if(result==null) {
			logger.error("The value from {}:{} is null or empty.\nThis shouldn't happen.", this.id, key);
			return false;
		}
		return result;
	}
	
	public String getId() {
		return this.id;
	}
	
	public boolean isLoaded() {
		return this.hasBeenLoaded;
	}
}
