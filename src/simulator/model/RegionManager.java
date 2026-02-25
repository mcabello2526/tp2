package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.json.JSONArray;
import org.json.JSONObject;


public class RegionManager implements AnimalMapView {
	private int width;
	private int height;
	private int cols;
	private int rows;
	private int regionWidth;
	private int regionHeight;
	
	private Region[][] regions;
	private Map<Animal, Region> animalRegion;

	public RegionManager(int cols, int rows, int width, int height) {
		this.cols = cols;
		this.rows = rows;
		this.width = width;
		this.height = height;
		this.regionWidth = (width/cols);
		this.regionHeight = (height/rows);
		
		this.regions = new Region[rows][cols];
		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < cols; col++) {
				this.regions[row][col] = new DefaultRegion();
			}
		}
		
		this.animalRegion = new HashMap<>();
	}

	void setRegion(int row, int col, Region r) {
		Region region = regions[row][col];
		regions[row][col] = r;
		//List<Animal> animals = r.getAnimals();
		
		for(Animal a: region.getAnimals()) {
			r.addAnimal(a);;
			animalRegion.put(a, r);
		}
	}
	
	void registerAnimal(Animal a) {
		a.init(this);

		int row = (int) (a.getPosition().getY() / regionHeight);
		int col = (int) (a.getPosition().getX() / regionWidth);
		row = Math.min(row, rows -1);
		col = Math.min(col, cols -1);
		Region r = regions[row][col];
		r.addAnimal(a);
		animalRegion.put(a, r);
		
	}
	
	void unregisterAnimal(Animal a) {
		animalRegion.get(a).removeAnimal(a);
		animalRegion.remove(a);
	}
	
	void updateAnimalRegion(Animal a) {
		int row = (int) (a.getPosition().getY() / regionHeight);
		int col = (int) (a.getPosition().getX() / regionWidth);
		row = Math.min(row, rows -1);
		col = Math.min(col, cols -1);
		
		Region newRegion = regions[row][col];
		Region r = animalRegion.get(a);
		
		if(newRegion != r) {
			r.removeAnimal(a);
			newRegion.addAnimal(a);
			animalRegion.put(a,  newRegion);
		}
	}
	
	void updateAllRegions(double dt) {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				regions[row][col].update(dt);
			}
		}
	}

	public JSONObject asJSON() {
		JSONArray regionsArray = new JSONArray(); 
		for(int row = 0; row < rows; row++) {
			for(int  col = 0; col < cols; col++) {
				JSONObject obj = new JSONObject(); 
				obj.put("row", row);
				obj.put("col", col);
				obj.put("data", regions[row][col].asJSON());
				regionsArray.put(obj);
			}
		}
		
		JSONObject result = new JSONObject(); 
		result.put("regions", regionsArray);
		return result;
	}
	
	
	////////
	@Override
	public int getCols() {
		
		return cols;
	}

	@Override
	public int getRows() {
		
		return rows;
	}

	@Override
	public int getWidth() {
		
		return width;
	}

	@Override
	public int getHeight() {
		
		return height;
	}

	@Override
	public int getRegionWidth() {
		
		return regionWidth;
	}

	@Override
	public int getRegionHeight() {
		
		return regionHeight;
	}

	@Override
	public double getFood(AnimalInfo a, double dt) {
		return animalRegion.get(a).getFood(a, dt);
	}

	@Override
	public List<Animal> getAnimalsInRange(Animal a, Predicate<Animal> filter){
		List<Animal> animalList = new ArrayList<>();
		double range, x, y;
		range = a.getSightRange();
		x = a.getPosition().getX();
		y = a.getPosition().getY();
		
		int left, right, top, bottom;
		left = Math.max(0,  (int)((x-range)/regionWidth));
		right = Math.min(this.cols-1, (int)((range+x) / regionWidth));
		top = Math.max(0, (int) ((y-range)/regionHeight));
		bottom = Math.min(this.rows-1, (int) ((range + y)/regionHeight));
		
		
		for(int row = top; row <= bottom; row++) {
			for(int col = left; col <= right; col++) {
				for(Animal animal: regions[row][col].getAnimals()) {
					if (animal != a && filter.test(animal)) {
						if(a.getPosition().distanceTo(animal.getPosition()) <= range) {
							animalList.add(animal);
						}
					}
				}
			}
		}
		
		return animalList;
	}
		
}
