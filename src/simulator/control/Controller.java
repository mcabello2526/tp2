package simulator.control;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.AnimalInfo;
import simulator.model.MapInfo;
import simulator.model.Simulator;
import simulator.view.SimpleObjectViewer;
import simulator.view.SimpleObjectViewer.ObjInfo;;

//(1) sacar las especificaciones de los animales/regiones desde un JSONObject
//(2) ejecutar el simulador para un tiempo determinado y escribir los diferentes 
//estados inicial y final en un OutputStream

public class Controller {
	private Simulator sim;
	

	public Controller(Simulator sim) {
		this.sim = sim;
	}
	
	public void loadData(JSONObject data) {
		JSONArray regionsArray = new JSONArray();
		if(data.has("regions")){
			regionsArray = data.getJSONArray("regions");
			for(int i = 0; i < regionsArray.length(); i++) {
				JSONObject obj = regionsArray.getJSONObject(i);
				JSONArray row = obj.getJSONArray("row");
				JSONArray col = obj.getJSONArray("col");
				JSONObject spec = obj.getJSONObject("spec");
				// o
				int rf, rt, cf, ct;
				rf = row.getInt(0);
				rt = row.getInt(1);
				cf = col.getInt(0);
				ct = col.getInt(1);
				for(int j = rf; j <= rt; j++) {
					for(int h = cf; h <= ct; h++) {
						sim.setRegion(j, h, spec);
					}
				}
			}
		}
		
		JSONArray animalsArray = data.getJSONArray("animals");
		int N;
		for(int i = 0; i < animalsArray.length(); i++) {
			JSONObject obj = animalsArray.getJSONObject(i);
			
			N = obj.getInt("amount");
			JSONObject spec = obj.getJSONObject("spec");
			for(int j = 0; j < N; j++) {
				sim.addAnimal(spec); // sim.addAnimal(o)
			}
		}
	}
	
	private List<ObjInfo> toAnimalsInfo(List<? extends AnimalInfo> animals) {
		List<ObjInfo> ol = new ArrayList<>(animals.size());
		for (AnimalInfo a : animals)
			ol.add(new ObjInfo(a.getGeneticCode(), (int) a.getPosition().getX(), (int) a.getPosition().getY(),8));
		return ol;
	}

	public void run(double t, double dt, boolean sv, OutputStream out) {
		
		//del visor 
		SimpleObjectViewer view = null;  
		if (sv) {  
		   MapInfo m = sim.getMapInfo();  
		   view = new SimpleObjectViewer("[ECOSYSTEM]", m.getWidth(), m.getHeight(), m.getCols(), m.getRows());  
		   view.update(toAnimalsInfo(sim.getAnimals()), sim.getTime(), dt);  
		}
		
		JSONObject initState = sim.asJSON();
		while (sim.getTime() <= t) {
			sim.advance(dt);
			if (sv) {
				view.update(toAnimalsInfo(sim.getAnimals()), sim.getTime(), dt);
			}
		}
		
		JSONObject finalState = sim.asJSON();
		JSONObject obj = new JSONObject();
		obj.put("in", initState);
		obj.put("out", finalState);
		PrintStream p = new PrintStream(out);
		p.println(obj.toString(2));
		
		if (sv == true) {
			view.close();
		}
	}
}
