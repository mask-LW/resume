package resume;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.google.gson.Gson;

public class testUtil {
	
	public static void main() {
 	String result =  "{\"data\": {\"regresume\": {\"school_exp\": {\"past\": [{\"pro\": \"\", \"time\": \"\", \"school\": \"\", \"edu\": \"�о���\", \"degree\": \"\"}, {\"pro\": \"\", \"time\": \"1986��7��\", \"school\": \"�Ͼ��ƾ���ѧ������ϵ\", \"edu\": \"���Ʊ�ҵ\", \"degree\": \"\"}, {\"pro\": \"\", \"time\": \"2000��\", \"school\": \"����ŦԼ������ѧ�����޷�У\", \"edu\": \"\", \"degree\": \"\"}, {\"pro\": \"\", \"time\": \"2000��\", \"school\": \"�й������ѧ��ѧԺ\", \"edu\": \"\", \"degree\": \"EMBA\"}, {\"pro\": \"\", \"time\": \"2000��\", \"school\": \"�й������ѧ��ѧԺ\", \"edu\": \"\", \"degree\": \"�߼����̹���˶ʿ\"}, {\"pro\": \"\", \"time\": \"2003��\", \"school\": \"���������ѧ\", \"edu\": \"\", \"degree\": \"\"}, {\"pro\": \"\", \"time\": \"2003��\", \"school\": \"�廪��ѧ�߼���Ӫ�ܲð�\", \"edu\": \"\", \"degree\": \"\"}, {\"pro\": \"\", \"time\": \"2015��\", \"school\": \"��ɽ��ѧ�ʱ������߼����ް�\", \"edu\": \"\", \"degree\": \"\"}], \"current\": []}, \"basic_info\": {\"sex\": \"��\", \"nationality\": \"�й�\", \"nation\": \"����\", \"name\": \"����\", \"born\": \"1962��12��8��\"}, \"work_exp\": {\"past\": [{\"job\": \"\", \"time\": \"2000��\", \"place\": \"����ŦԼ������ѧ�����޷�У\", \"department\": \"\"}, {\"job\": \"\", \"time\": \"2003��\", \"place\": \"���������ѧ\", \"department\": \"\"}, {\"job\": \"�ɱ����Ա\", \"time\": \"1986��9����1989��1��\", \"place\": \"���������������޹�˾\", \"department\": \"\"}, {\"job\": \"�������\", \"time\": \"1986��9����1989��1��\", \"place\": \"���������������޹�˾\", \"department\": \"\"}, {\"job\": \"����˾��\", \"time\": \"1989��1����1992��1��\", \"place\": \"�й��������޹�˾\", \"department\": \"\"}, {\"job\": \"�߼��ƻ����\", \"time\": \"1989��1����1992��1��\", \"place\": \"�й��������޹�˾\", \"department\": \"\"}, {\"job\": \"������������\", \"time\": \"1992��1����1996��2��\", \"place\": \"����DEC�����й����޹�˾�й���\", \"department\": \"\"}, {\"job\": \"���������ܼ�\", \"time\": \"1996��2����2002��3��\", \"place\": \"΢���й����޹�˾�й���\", \"department\": \"\"}, {\"job\": \"��Ʒ�����ܲ�\", \"time\": \"2002��3����2006��5��\", \"place\": \"�й���ͨ�ع����޹�˾\", \"department\": \"\"}, {\"job\": \"��Ӫ���ܲ�\", \"time\": \"2002��3����2006��5��\", \"place\": \"�й���ͨ�ع����޹�˾\", \"department\": \"\"}, {\"job\": \"��ϯ�����\", \"time\": \"2006��6����2008��5��\", \"place\": \"�й������ҵ����\", \"department\": \"\"}, {\"job\": \"�����ܲ�\", \"time\": \"2008��6����2009��12��\", \"place\": \"������ý�弯����������\", \"department\": \"\"}, {\"job\": \"��ϯ��Ӫ��\", \"time\": \"2010��2����2013��12��\", \"place\": \"������ʤ��������Ƽ����޹�˾\", \"department\": \"\"}, {\"job\": \"��ϯ�����\", \"time\": \"2010��2����2013��12��\", \"place\": \"������ʤ��������Ƽ����޹�˾\", \"department\": \"\"}, {\"job\": \"��ϯִ�й�\", \"time\": \"2010��2����2013��12��\", \"place\": \"������ʤ��������Ƽ����޹�˾\", \"department\": \"\"}, {\"job\": \"�������\", \"time\": \"2014��2����2015��12��\", \"place\": \"����������ѯ�������޹�˾\", \"department\": \"\"}], \"current\": [{\"job\": \"\", \"time\": \"2016��1������\", \"place\": \"�����г�������Ϣ�Ƽ��ɷ����޹�˾\", \"department\": \"\"}, {\"job\": \"����\", \"time\": \"��\", \"place\": \"�����г�������Ϣ�Ƽ��ɷ����޹�˾\", \"department\": \"\"}, {\"job\": \"�����ܼ�\", \"time\": \"��\", \"place\": \"�����г�������Ϣ�Ƽ��ɷ����޹�˾\", \"department\": \"\"}, {\"job\": \"���»�����\", \"time\": \"��\", \"place\": \"�����г�������Ϣ�Ƽ��ɷ����޹�˾\", \"department\": \"\"}]}}}, \"msg\": \"�����ɹ�\", \"code\": 1}" ;
 	Gson gson = new Gson();
	Map<String,String> map = new HashMap<String,String>();
	map = gson.fromJson(result, map.getClass());
	String value = null;
	//��ȡdata���������
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
