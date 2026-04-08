package simulator.model;

public interface MapInfo extends JSONable, Iterable<MapInfo.RegionData> {

	public int getCols();

	public int getRows();

	public int getWidth();

	public int getHeight();

	public int getRegionWidth();

	public int getRegionHeight();
	
	public record RegionData(int row, int col, RegionInfo r) {  // posicion de la region y su informacion para que no se modifique desde fuera 
	} 
}