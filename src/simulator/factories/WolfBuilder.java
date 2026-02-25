package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Animal;
import simulator.model.SelectionStrategy;
import simulator.model.Wolf;
import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.model.SelectFirst;

public class WolfBuilder extends Builder<Animal>{
	private Factory<SelectionStrategy> factory;

	public WolfBuilder(Factory<SelectionStrategy> strategyFactory) {
		super("wolf", "Wolf animal factory");
		this.factory = strategyFactory;
	}

	@Override
	protected Animal createInstance(JSONObject data) {
		SelectionStrategy mateStrategy = new SelectFirst();
		SelectionStrategy huntStrategy = new SelectFirst();
		
		if(data.has("mate_strategy")){
			mateStrategy = this.factory.createInstance(data.getJSONObject("mate_strategy"));
		}
		if(data.has("hunt_strategy")){
			huntStrategy = this.factory.createInstance(data.getJSONObject("hunt_strategy"));
		}
		
		Vector2D pos = null;
		if(data.has("pos")) {
			JSONObject obj = data.getJSONObject("pos");
			JSONArray x_range = obj.getJSONArray("x_range");
			JSONArray y_range = obj.getJSONArray("y_range");
			double auxX1, auxY1, auxX2, auxY2, resX, resY;
			//valores min (de intervslo)
			auxX1 = x_range.getDouble(0);
			auxY1 = y_range.getDouble(0);
			//valores max 
			auxX2 = x_range.getDouble(1);
			auxY2 = y_range.getDouble(1);
			
			resX = Utils.RAND.nextDouble(auxX1, auxX2);
			resY = Utils.RAND.nextDouble(auxY1, auxY2);
			
			pos = new Vector2D(resX, resY);
		}
		
		return new Wolf(mateStrategy, huntStrategy, pos);
	}

}
