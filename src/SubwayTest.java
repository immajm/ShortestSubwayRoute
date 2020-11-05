import java.io.*;
import java.net.URL;
import java.util.*;

public class SubwayTest {
    private Map<String,BeanStation>  StationSet=new HashMap<String, BeanStation>();
    private ArrayList<BeanLine> LineSet=new ArrayList<BeanLine>();

    public static void main(String[] args) throws IOException {
        SubwayTest test=new SubwayTest();
        test.initTest();    //读入并按需求存储信息
        test.startPlaying();  //开始交互
    }

    public void initTest() throws IOException {//读入并按需求存储信息
        ReadFile file=new ReadFile();//读入文件内信息
        ArrayList<String> fileTxt=file.getFlieData();
        for(String temp:fileTxt){
            //利用Line类的构造函数建Line类，并加到LineSet里
            BeanLine line=new BeanLine(temp);
            LineSet.add(line);
        }

        //LineSet已经存好 现在读入StationSet
        //两重循环，第一重遍历所有的线路，第二重循环遍历每一条线路上的站点
        for(BeanLine line:LineSet) {
            for (int i = 0; i < line.getSubStation().size(); i++) {
                //检查是否已经存在，将该站点存入(Map)StationSet中
                if(!StationSet.containsKey(line.getSubStation().get(i).getStationName()))
                StationSet.put(line.getSubStation().get(i).getStationName(),line.getSubStation().get(i));

                //更新信息：将该站点前后没有放入NeighborStation的站点加入
                //直接修改(map)StationSet，或者使用getOrDefault()从map中取出后更新,本次使用直接修改

                //加入前一站点
                if (i > 0) {
                    BeanStation front_neighbor = new BeanStation();
                    front_neighbor=line.getSubStation().get(i-1);
                    if(!StationSet.get(line.getSubStation().get(i).getStationName()).getNeighborStation().contains(front_neighbor)){
                        StationSet.get(line.getSubStation().get(i).getStationName()).getNeighborStation().add(front_neighbor);
                    }
                }
                //加入后一站点
                if (i < line.getSubStation().size() - 1) {
                    BeanStation next_neighbor = new BeanStation();
                    next_neighbor=line.getSubStation().get(i+1);
                    if(!StationSet.get(line.getSubStation().get(i).getStationName()).getNeighborStation().contains(next_neighbor)){
                        StationSet.get(line.getSubStation().get(i).getStationName()).getNeighborStation().add(next_neighbor);
                    }
                }
                //记录所属线路
                String lineName = line.getLineName();
                StationSet.get(line.getSubStation().get(i).getStationName()).getBelongsToLine().add(lineName);
            }
        }
    }

    public void startPlaying(){//开始交互
        System.out.println("************************************************************");
        System.out.println("                      {欢迎使用SuMa识途}                      ");
        System.out.println("************************************************************");
        System.out.println(" ");
        System.out.println("1 ：查询地铁线路(-a显示全部)      ");
        System.out.println("2 ：查询最短路径      ");
        System.out.println("0 ：退出查询         ");
        System.out.println();

        Scanner sc =new Scanner(System.in);
        while(true){
            String choice=sc.next();

            if(choice.equals("1")){
                System.out.println("请输入线路名称：");
                LineSearch(sc.next());
            }
            else if(choice.equals("1-a")){
                showAllLines();
            }
            else if(choice.equals("2")){
                System.out.println("请输入#起点站#和#终点站#");
                String start=sc.next();
                String end=sc.next();
                if(Check(start,end)==1) {
                    SearchRoute(StationSet,start,end);//开始寻路
                }
            }
            else if(choice.equals("0")){
                System.out.println("退出查询");
                break;
            }
            else{
                System.out.println("只有俩功能，配合点！！！(´థ౪థ)σ");
                System.out.println(" ");
            }

            System.out.println("请重新选择");
            System.out.println("1 ：查询地铁线路      ");
            System.out.println("2 ：查询最短路径      ");
            System.out.println("0 ：退出查询         ");
        }

        System.out.println("************************************************************");
        System.out.println("                          查询结束！                          ");
        System.out.println("************************************************************");
    }

    void LineSearch(String LineName){
        int isExist=0;
        for(BeanLine line:LineSet){
            if(LineName.equals(line.getLineName())){
                System.out.println("您好，"+line.getLineName()+"包含以下站点：");
                for(int i=0;i<line.getSubStation().size();i++){
                    System.out.print(line.getSubStation().get(i).getStationName()+" ");
                }
                System.out.println(" ");
                isExist=1;
                break;
            }
        }
        if(isExist==0){
            System.out.println("该线路不存在");
            System.out.println(" ");
        }
    }

    void showAllLines(){
        for(BeanLine line:LineSet){
            System.out.println(line.getLineName());
            for(int i=0;i<line.getSubStation().size();i++){
                System.out.print(line.getSubStation().get(i).getStationName()+" ");
            }
            System.out.println(" ");
        }
    }

    int Check(String start,String end){
        int isCorrect=1;
        if(!StationSet.containsKey(start)){
            isCorrect=0;
            System.out.println("起点不存在");
        }
        if(!StationSet.containsKey(end)){
            isCorrect=0;
            System.out.println("终点不存在");
        }
        if(start.equals(end)){
            System.out.println("您已到达终点");
            isCorrect=0;
        }
        return isCorrect;
    }

    void SearchRoute(Map<String,BeanStation> StationSet,String start,String end) {
        Queue<BeanStation> queue= new LinkedList<>();
        String next_start=null;
        int neighbor_size=0;
        String temp=null;
        int isfind=0;
        //等距图中，单源最短距离计算，Dijkstra退化为BFS
        queue.add(StationSet.get(start));//将起点放入队列
        StationSet.get(start).setIsVisited(1);//起点已访问
        while(!queue.isEmpty()){
            next_start=queue.peek().getStationName();
            neighbor_size=StationSet.get(next_start).getNeighborStation().size();
            for(int i=0;i<neighbor_size;i++){
                temp=StationSet.get(next_start).getNeighborStation().get(i).getStationName();
                //找到终点
                if(temp.equals(end)){
                    StationSet.get(temp).setParent(next_start);
                    isfind=1;
                    break;
                }
                else if(StationSet.get(temp).getIsVisited()==0){//若未被访问过
                    StationSet.get(temp).setParent(next_start);//设父亲节点
                    StationSet.get(temp).setIsVisited(1);//该点被访问
                    queue.add(StationSet.get(temp));//加入队列
                    //必须先设父节点、改边isVisited，再放入queue，否则节点未更新，无限循环
                    //应该找到map对应键值更新父节点和visit，而不是在map对应键值的邻居节点的父节点和visit进行更新,故增加temp
                }
            }
            if(isfind==1) break;//设置判断标志，否则继续while循环
            queue.poll();
        }
        showRoute(end);
    }

    private void showRoute(String end) {
        int count=0;
        //需要判断相隔一个站点的两个站是否在一条线路上，用list比stack输出方便
        ArrayList<String> path= new ArrayList<>();
        BeanStation station=new BeanStation();
        station=StationSet.get(end);
        //回溯父节点
        while(station.getParent()!=null){
            path.add(station.getStationName());
            station=StationSet.get(station.getParent());
            count++;
        }
        path.add(station.getStationName());
        System.out.println("至少需要乘坐"+count+"站哦");
        //将站点按固定格式输出，此时path是反向存放的
        print(path);

    }

    void print(ArrayList<String> path){
        System.out.println("  一开始位于："+findSameLine(path.get(path.size()-1),path.get(path.size()-2)));
        System.out.print(" "+path.get(path.size()-1)+" ");
        int i;
        for(i=path.size()-1;i>2;i--){
            System.out.print("-> "+path.get(i-1)+" ");
            //判断两站有无换乘
            if(isChange(path.get(i),path.get(i-2))){
                //如果换乘了 输出换乘信息 并重启一行输出
                System.out.println();
                System.out.println("  "+findSameLine(path.get(i),path.get(i-1))+" --> "+findSameLine(path.get(i-1),path.get(i-2)));
                System.out.print(" "+path.get(i-1)+" ");
            }
        }
        System.out.println("-> "+path.get(i-2)+" ");
        System.out.println("  最终位于："+findSameLine(path.get(i),path.get(i-1)));
    }

    public String findSameLine(String station1,String station2){
        for(String name:StationSet.get(station1).getBelongsToLine()){
            if(StationSet.get(station2).getBelongsToLine().contains(name))
                return name;
        }
        return "奇怪，它们肯定线路相同呀";
    }

    public boolean isChange(String station1,String station2){
        for(String name:StationSet.get(station1).getBelongsToLine()){
            if(StationSet.get(station2).getBelongsToLine().contains(name))
                return false;
        }
        return true;
    }

}
