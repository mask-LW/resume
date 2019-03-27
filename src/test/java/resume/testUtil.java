package resume;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.google.gson.Gson;

public class testUtil {
	
	public static void main() {
 	String result =  "{\"data\": {\"regresume\": {\"school_exp\": {\"past\": [{\"pro\": \"\", \"time\": \"\", \"school\": \"\", \"edu\": \"研究生\", \"degree\": \"\"}, {\"pro\": \"\", \"time\": \"1986年7月\", \"school\": \"南京财经大学财务会计系\", \"edu\": \"本科毕业\", \"degree\": \"\"}, {\"pro\": \"\", \"time\": \"2000年\", \"school\": \"美国纽约州立大学布法罗分校\", \"edu\": \"\", \"degree\": \"\"}, {\"pro\": \"\", \"time\": \"2000年\", \"school\": \"中国人民大学商学院\", \"edu\": \"\", \"degree\": \"EMBA\"}, {\"pro\": \"\", \"time\": \"2000年\", \"school\": \"中国人民大学商学院\", \"edu\": \"\", \"degree\": \"高级工商管理硕士\"}, {\"pro\": \"\", \"time\": \"2003年\", \"school\": \"美国哈佛大学\", \"edu\": \"\", \"degree\": \"\"}, {\"pro\": \"\", \"time\": \"2003年\", \"school\": \"清华大学高级运营总裁班\", \"edu\": \"\", \"degree\": \"\"}, {\"pro\": \"\", \"time\": \"2015年\", \"school\": \"中山大学资本运作高级研修班\", \"edu\": \"\", \"degree\": \"\"}], \"current\": []}, \"basic_info\": {\"sex\": \"男\", \"nationality\": \"中国\", \"nation\": \"汉族\", \"name\": \"郭涛\", \"born\": \"1962年12月8日\"}, \"work_exp\": {\"past\": [{\"job\": \"\", \"time\": \"2000年\", \"place\": \"美国纽约州立大学布法罗分校\", \"department\": \"\"}, {\"job\": \"\", \"time\": \"2003年\", \"place\": \"美国哈佛大学\", \"department\": \"\"}, {\"job\": \"成本会计员\", \"time\": \"1986年9月至1989年1月\", \"place\": \"北京正大饲料有限公司\", \"department\": \"\"}, {\"job\": \"会计主管\", \"time\": \"1986年9月至1989年1月\", \"place\": \"北京正大饲料有限公司\", \"department\": \"\"}, {\"job\": \"财务司库\", \"time\": \"1989年1月至1992年1月\", \"place\": \"中国惠普有限公司\", \"department\": \"\"}, {\"job\": \"高级财会分析\", \"time\": \"1989年1月至1992年1月\", \"place\": \"中国惠普有限公司\", \"department\": \"\"}, {\"job\": \"财务行政经理\", \"time\": \"1992年1月至1996年2月\", \"place\": \"美国DEC电脑中国有限公司中国区\", \"department\": \"\"}, {\"job\": \"财务行政总监\", \"time\": \"1996年2月至2002年3月\", \"place\": \"微软中国有限公司中国区\", \"department\": \"\"}, {\"job\": \"产品财务副总裁\", \"time\": \"2002年3月至2006年5月\", \"place\": \"中国网通控股有限公司\", \"department\": \"\"}, {\"job\": \"运营副总裁\", \"time\": \"2002年3月至2006年5月\", \"place\": \"中国网通控股有限公司\", \"department\": \"\"}, {\"job\": \"首席财务官\", \"time\": \"2006年6月至2008年5月\", \"place\": \"中国宽带产业基金\", \"department\": \"\"}, {\"job\": \"财务副总裁\", \"time\": \"2008年6月至2009年12月\", \"place\": \"华亿新媒体集团旅游卫视\", \"department\": \"\"}, {\"job\": \"首席运营官\", \"time\": \"2010年2月至2013年12月\", \"place\": \"北京东胜创新生物科技有限公司\", \"department\": \"\"}, {\"job\": \"首席财务官\", \"time\": \"2010年2月至2013年12月\", \"place\": \"北京东胜创新生物科技有限公司\", \"department\": \"\"}, {\"job\": \"首席执行官\", \"time\": \"2010年2月至2013年12月\", \"place\": \"北京东胜创新生物科技有限公司\", \"department\": \"\"}, {\"job\": \"资深顾问\", \"time\": \"2014年2月至2015年12月\", \"place\": \"北京普利咨询管理有限公司\", \"department\": \"\"}], \"current\": [{\"job\": \"\", \"time\": \"2016年1月至今\", \"place\": \"深圳市长城网信息科技股份有限公司\", \"department\": \"\"}, {\"job\": \"董事\", \"time\": \"现\", \"place\": \"深圳市长城网信息科技股份有限公司\", \"department\": \"\"}, {\"job\": \"财务总监\", \"time\": \"现\", \"place\": \"深圳市长城网信息科技股份有限公司\", \"department\": \"\"}, {\"job\": \"董事会秘书\", \"time\": \"现\", \"place\": \"深圳市长城网信息科技股份有限公司\", \"department\": \"\"}]}}}, \"msg\": \"操作成功\", \"code\": 1}" ;
 	Gson gson = new Gson();
	Map<String,String> map = new HashMap<String,String>();
	map = gson.fromJson(result, map.getClass());
	String value = null;
	//获取data里面的数据
	Iterator<String> iter = map.keySet().iterator();
    while(iter.hasNext()){
    	String key=iter.next();
    	String value1 = map.get(key);
    	System.out.print(key+value);
    	if( key == "data") {
    		value = map.get(key);
    	}
   }
	
	
	
	
	}
}
