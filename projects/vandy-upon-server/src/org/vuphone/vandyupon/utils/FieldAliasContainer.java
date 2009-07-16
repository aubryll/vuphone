package org.vuphone.vandyupon.utils;

public class FieldAliasContainer {
	
	private String alias_;
	private String name_;
	private Class def_;
	
	public FieldAliasContainer(String alias, Class def, String name){
		alias_ = alias;
		name_ = name;
		def_ = def;
	}
	
	public String getAlias(){
		return alias_;
	}
	
	public Class getDefiningClass(){
		return def_;
	}
	
	public String getFieldName(){
		return name_;
	}

}
