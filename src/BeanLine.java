

import java.util.ArrayList;

public class BeanLine  {
	private String LineName;//线名
	private  ArrayList<BeanStation> SubStation=new ArrayList<BeanStation>();//子站名

	public BeanLine(String tem){//存储线路信息
		String[] str=tem.split(" ");
		LineName=str[0];
		for(int i=1;i<str.length;i++){
			BeanStation station=new BeanStation();
			station.setStationName(str[i]);
			SubStation.add(station);
		}
	}

	public String getLineName() {
		return LineName;
	}

	public void setLineName(String lineName) {
		LineName = lineName;
	}

	public ArrayList<BeanStation> getSubStation() {
		return SubStation;
	}

	public void setSubStation(ArrayList<BeanStation> subStation) {
		SubStation = subStation;
	}
}
