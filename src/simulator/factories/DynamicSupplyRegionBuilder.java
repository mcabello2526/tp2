package simulator.factories;

import org.json.JSONObject;
import simulator.model.DynamicSupplyRegion;
import simulator.model.Region;

public class DynamicSupplyRegionBuilder extends Builder<Region> {

	public DynamicSupplyRegionBuilder() {
		super("dynamic", "Food supply region");
	}

	@Override
	protected Region createInstance(JSONObject data) {
		double factor = 2.0;
		double food = 100.0;

		if (data.has("factor")) {
			factor = data.getDouble("factor");
		}
		if (data.has("food")) {
			food = data.getDouble("food");
		}

		return new DynamicSupplyRegion(food, factor);
	}
	
	@Override
	protected void fillInData(JSONObject o) {
		o.put("factor", "food increase factor (optional, default 2.0)"); 
	    o.put("food", "initial amount of food (optional, default 100.0)"); 
	}

}
