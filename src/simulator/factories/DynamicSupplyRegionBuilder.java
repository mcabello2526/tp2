package simulator.factories;

import org.json.JSONObject;
import simulator.model.DynamicSupplyRegion;
import simulator.model.Region;

public class DynamicSupplyRegionBuilder extends Builder<Region>{

	public DynamicSupplyRegionBuilder() {
		super("dynamic", "Food supply region");
	}

	@Override
	protected Region createInstance(JSONObject data) {
		double factor = 2.0;
		double food = 100.0;
		
		if(data.has("factor")) {
			factor = data.getDouble("factor");
		}
		if(data.has("food")) {
			food = data.getDouble("food");
		}
		
		return new DynamicSupplyRegion(food, factor);
	}

}
