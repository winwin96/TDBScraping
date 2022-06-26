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

public class TCB {

	public static void main(Map<String, String> cook, String busi_list, String time_form) {
		
		StringBuilder data = new StringBuilder();
		ArrayList list = new ArrayList();
		Integer size;
		BufferedWriter bw = null;
		
		try {
			bw= new BufferedWriter(new FileWriter("./result/"+ time_form.substring(4,8) + "/files/"
					+"TCB_info_" + time_form +".csv" , true));
			Connection.Response tcb_res = Jsoup.connect("https://www.tdb.or.kr/ked/KedSearchCompanyTcb.do?bizRegNo="+busi_list+"&ked=Y")
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
					.cookies(cook)
					.method(Connection.Method.POST)
					.execute();
			
			Document doc = tcb_res.parse();
			
			// TCB 평가 이력이 없는 경우
			if(doc.select("div.corporation_skillInfo").text().contains("기술신용평가(TCB평가) 이력이 없습니다.")) {
				data.append(busi_list);
				data.append(","); data.append("-"); 
				data.append(","); data.append("-"); 
				data.append(","); data.append("-"); 
				data.append(","); data.append("-");
				
				bw.write(data.toString());
				bw.newLine();
				data.setLength(0);
			}
			else{
				
				Elements elements = doc.select("tbody#tcbEvlBody > tr").select("*");
				
				for (Element element : elements) {
					list.add(element.ownText());
				}
				
				size = list.size();
				
				for (int i=0 ; i<size/5 ; i++) {
					data.append(busi_list);
					data.append(","); data.append(list.get(5*i+1));
					data.append(","); data.append(list.get(5*i+2));
					data.append(","); data.append(list.get(5*i+3));
					data.append(","); data.append(list.get(5*i+4));
					
					bw.write(data.toString());
					bw.newLine();
					data.setLength(0);
				}
			}
			
		} catch (IOException e) {
			
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			TCB.main(cook, busi_list, time_form);
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
