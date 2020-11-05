

import java.util.ArrayList;

public class BeanStation {
	private String StationName;//站名
	private ArrayList<String> BelongsToLine= new ArrayList<String>();//所属线名
	private ArrayList<BeanStation> NeighborStation =new ArrayList<BeanStation>();//邻接站点，相当于创建邻接表
	private int isVisited=0;//是否被访问
	private String parent=null;//上一站点

	public String getStationName() {
		return StationName;
	}

	public void setStationName(String stationName) {
		StationName = stationName;
	}

	public ArrayList<String> getBelongsToLine() {
		return BelongsToLine;
	}

	public void addBelongsToLine(String lineName) {
		 BelongsToLine.add(lineName);
	}

	public void setBelongsToLine(ArrayList<String> belongsToLine) {
		BelongsToLine = belongsToLine;
	}

	public ArrayList<BeanStation> getNeighborStation() {
		return NeighborStation;
	}

	public void setNeighborStation(ArrayList<BeanStation> neighborStation) {
		NeighborStation = neighborStation;
	}

	public int getIsVisited() {
		return isVisited;
	}

	public void setIsVisited(int isVisited) {
		this.isVisited = isVisited;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

}

