package simulator.model;

import org.json.*;


// formato de los objetos 

public interface JSONable {
	default public JSONObject asJSON() {
		return new JSONObject(); 
	}
}
