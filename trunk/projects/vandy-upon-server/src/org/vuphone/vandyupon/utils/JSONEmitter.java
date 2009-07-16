package org.vuphone.vandyupon.utils;

import java.util.ArrayList;

import org.vuphone.vandyupon.notification.ResponseNotification;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

public class JSONEmitter extends Emitter {
	
	public JSONEmitter() {
		super("json");
	}

	public String emit(ResponseNotification rn, ArrayList<ObjectAliasContainer> objaliases, 
			ArrayList<FieldAliasContainer> fldaliases){
		XStream xs = new XStream(new JettisonMappedXmlDriver());
        xs.setMode(XStream.NO_REFERENCES);
        
        for(ObjectAliasContainer oac:objaliases){
        	xs.alias(oac.getAlias(), oac.getType());
        }
        
        for (FieldAliasContainer fac:fldaliases){
        	xs.aliasField(fac.getAlias(), fac.getDefiningClass(), fac.getFieldName());
        }
        
        return xs.toXML(rn);
	}

}
