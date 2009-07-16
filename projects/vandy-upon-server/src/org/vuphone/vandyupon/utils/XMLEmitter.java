package org.vuphone.vandyupon.utils;

import java.util.ArrayList;

import org.vuphone.vandyupon.notification.ResponseNotification;

import com.thoughtworks.xstream.XStream;

public class XMLEmitter extends Emitter {

	public XMLEmitter(){
		super("xml");
	}

	@Override
	public String emit(ResponseNotification rn,
			ArrayList<ObjectAliasContainer> objaliases,
			ArrayList<FieldAliasContainer> fldaliases) {
		XStream xs = new XStream();

		for(ObjectAliasContainer oac:objaliases){
			xs.alias(oac.getAlias(), oac.getType());
		}

		for (FieldAliasContainer fac:fldaliases){
			xs.aliasField(fac.getAlias(), fac.getDefiningClass(), fac.getFieldName());
		}

		return xs.toXML(rn);
	}



}
