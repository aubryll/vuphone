package org.vuphone.vandyupon.utils;

public class ObjectAliasContainer {
	
	private String alias_;
	private Class class_;
	
	public ObjectAliasContainer(String alias, Class type){
		alias_ = alias;
		class_ = type;
	}
	
	public String getAlias(){
		return alias_;
	}
	
	public Class getType(){
		return class_;
	}

}
