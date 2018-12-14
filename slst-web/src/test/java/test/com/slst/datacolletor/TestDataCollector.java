package test.com.slst.datacolletor;

import com.slst.datacollector.service.DataCollectorService;
import com.slst.datacollector.web.vo.WIFIReportVO;
import org.springframework.util.Base64Utils;
import test.com.slst.BaseTest;

import javax.annotation.Resource;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-05-09 18:40
 */
public class TestDataCollector extends BaseTest {
    @Resource
    private DataCollectorService dataCollectorService;



    public static void main(String[] args) throws Exception {
//        String data="1=&sta=dc4f223eb429&shop=1&token=123232jd&r=8134ad506c4e&id=12a2c334&type=probea&data=2123 "+((char )1) +"8c34fd3dad70"+URLEncoder.encode("&&&&") + ((char )60)+ ((char )61)+ ((char )62)+((char )1)+"7c34fd3dad70"+((char )60)+ ((char )61)+ ((char )62)+((char )1)+"ac25";
//        String data="=1&sta=dc4f223eb429&shop=1&token=123232jd&r=8134ad506c4e&id=0012a2c334&type=flash&data=" +((char )1) + "8c34fd3dad705a00212e24c6"+((char )1) +"7c34fd3dad705a00c1232d56"+((char )1);
//        String data="=1&sta=dc4f223eb429&shop=1&token=123232jd&r=8134ad506c4e&id=12a2c334&type=ap&data=" +((char )1) + "282cb2fd2b00" +((char )50)+((char )56) + "first1" +((char )1) + "9c216a8416ea05" +((char )51)+((char )57)+"second" +((char )1) + "50bd5f2df979" +((char )51) + ((char )5)+ " 717*1F1" +((char )1) + "50bd5f38db13"+((char )51)+((char )57)+"17*2F2"+ ((char) 1);
        String data="=1&sta=dc4f223eb429&shop=1&token=123232jd&r=8134ad506c4e&id=12a2c334&type=check&data=t572007166r0e0c0h31624hb18fe34010150aps1800api5si10em2pl1024pll3072tag20 sp1";
//        String data="=1&sta=dc4f223eb429&shop=1&token=123232jd&r=8134ad506c4e&id=12a2c334&type=configa&data=ch1cas5cat5sc5t30603970r0e0c0h47368cst36000iga1st1";
//        System.out.println((int)'&');
//        System.out.println(data);
//        data = "c3RhPTY4YzYzYWUwZGZlZiZzaG9wPTEmdG9rZW49MTIzMjMyamQmcj05NGQ5YjNhOWMyNmYmaWQ9MmRlY2ZkY2UmdHlwZT1wcm9iZWEmZGF0YT0lMDE5NGQ5YjNhOWMyNmYmJTAxJTAxMDAxN2M0Y2QwMWNmeCUwMTc4NjI1NmM2NzI1YkwlMDE1Yzk2OWQ3MWVmNjNRJTAxOTRkOWIzYTljMjZmJTIxJTAxN2NjNzA5N2Y0MWFmeCUwMSUwMWRjODVkZWQxNjE3ZjklMDE1NGRjMWQ3MmE0NDB4JTAxNzg2MjU2YzY3MjViRSUwMTVjOTY5ZDcxZWY2M0olMDE5NGQ5YjNhOWMyNmYlMUUlMDFlNGE3YTA5M2UzOTJ4JTAxNGMzNDg4OTQ5YjY2eCUwMTNjNDZkODMwZTAyMDYlMDElMDE2MGYxODk2ZjJmY2JDJTAxOTRkOWIzYTljMjZmKyUwMTkwYzM1ZjE3NWQ2MXglMDE0YzM0ODg5NDliNjZ4JTAxZTRhN2M1YzhjYzA4JTNBJTAxJTAxZjBiNDI5ZDI1MTFjWCUwMTVjOTY5ZDcxZWY2MyU1QyUwMWQ0NmE2YTE1NWJmMXglMDE5NGQ5YjNhOWMyNmYqJTAxPQ==";
//        data = "c3RhPTY4YzYzYWUwZGZlZiZzaG9wPTEmdG9rZW49MTIzMjMyamQmcj05NGQ5YjNhOWMyNmYmaWQ9NWY0NTQzZWMmdHlwZT1wcm9iZWEmZGF0YT0lMDE2OGM2M2FlMGRmZWYmJTAxMDhmNjljMDYzNDdmRSUwMTIwM2NhZThlMjZlY0clMDE9";
        System.out.println(WIFIReportVO.buildWIFIReportVO(data));
        System.out.println(WIFIReportVO.buildWIFIReportVO(new String(Base64Utils.decodeFromString(data))));

//        HttpUtils.postJSON("http://127.0.0.1:8080/soundtooth/callout" , "{\"user_name\":\"用户名\",\"iSeatNo\":\"1001001\",\"callId\":\"210ab0fdfca1439ba268a6bf8266305c\",\"telA\":\"18133331269\",\"telB\":\"181****1200\",\"telX\":\"17100445588\",\"telG\":\"07556882659\",\"duration\":\"19\",\"userData\":\"ef7ede6f36c84f1d45333863c38ff14c\"}");
////        HttpUtils.postJSON("http://127.0.0.1:8080/soundtooth/callout" , "{\"user_name\":\"用户名\",\"iSeatNo\":\"1001001\",\"callId\":\"210ab0fdfca1439ba268a6bf8266305c\",\"telA\":\"18133331269\",\"telB\":\"181****1200\",\"telX\":\"17100445588\",\"telG\":\"07556882659\",\"duration\":\"19\",\"userData\":\"ef7ede6f36c84f1d45333863c38ff14c\"}");
//        System.out.println(HttpUtils.postJSON("http://s.slstdt.com/collector/collect" , ""));
//        System.out.println(HttpUtils.postJSON("http://s.slstdt.com/soundtooth/callout" , ""));


    }
}
