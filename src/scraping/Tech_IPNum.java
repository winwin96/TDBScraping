package scraping;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Tech_IPNum {

	public static void main(Map<String, String> cook, String busi_list, String time_form) {
		
		StringBuilder data = new StringBuilder();
		data.append(busi_list); 
		ArrayList list = new ArrayList();
		Integer[] index = {1,2,3,4,5,7,8,9,10,11};
		Integer number;
		BufferedWriter bw = null; 
		
		try {
			bw= new BufferedWriter(new FileWriter("./result/"+ time_form.substring(4,8) + "/files/"
					+"IPNum_info_" + time_form +".csv", true));
			Connection.Response subipstat_res = Jsoup.connect("https://www.tdb.or.kr/ked/KedSearchCompanySubIpStat.do")
					.timeout(100000)
					.header("Accept","*/*")
					.header("Accept-Encoding", "gzip, deflate")
					.header("Accept-Language", "ko")
					.header("Cache-Control", "no-cache")
					.header("Connection", "Keep-Alive")
					.header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
					.header("Cookie", "PHAROSVISITOR="+(String)cook.get("PHAROSVISITOR")+ "; JSESSIONID="+ (String)cook.get("JSESSIONID") +"; keywords=" + busi_list)
					.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
					.header("Host", "www.tdb.or.kr")
					.header("Referer", "https://www.tdb.or.kr/prtl/eval/CompIndu.do?kwd=" + busi_list + "&compNm=" + busi_list)
					.header("X-Requested-With","XMLHttpRequest")
					.data("bizRegiNo", busi_list)
					.data("pageIndex", "1")
					.cookies(cook)
					.method(Connection.Method.POST)
					.execute();
			
			Document doc = subipstat_res.parse();
			Elements elements = doc.select("tbody > tr").select("*");
			
			for (Element element : elements) {
				list.add(element.ownText());
			}
			
			for(int i=0 ; i<index.length ; i++) {
				number = index[i];
				data.append(",");
				if (list.get(number).toString().contains(",")) data.append("\"" + list.get(number).toString() + "\"");
				else data.append(list.get(number).toString()); 
			}
			
			bw.write(data.toString());
			bw.newLine();
			
		} catch (IOException e) {
			
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			Tech_IPNum.main(cook, busi_list, time_form);
		} finally {
			try {
				if (bw != null) {
					bw.flush();
					bw.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}