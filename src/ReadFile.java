import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;

public class ReadFile {
    public ArrayList<String> getFlieData(){
        //考虑到工程项目的移值，SubwayInfo.txt在不同pc的位置会不同
        //如果还是在FileReader中用绝对路径就会出错
        //所以这里选用它的相对路径

        URL path = SubwayTest.class.getClassLoader().getResource("");
        String proFilePath = path.toString().substring(6);

        String[] str=proFilePath.split("/");
        StringBuilder realPath=new StringBuilder();

        for(int i=0;i<str.length-3;i++){
            String a=str[i];
            realPath.append(a+'/');
        }
        ArrayList<String> res=new ArrayList<String>();
        proFilePath = realPath.toString() + "src/SubwayInfo.txt";

        try {
            Reader reader= new FileReader(proFilePath);
            BufferedReader br = new BufferedReader(reader);
            String tem = "";
            while ((tem = br.readLine()) != null)
            {
                res.add(tem);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return  res;
    }
}
