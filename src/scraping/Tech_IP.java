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

public class Tech_IP {

	public static void main(Map<String, String> cook, String busi_list, String time_form) {
		
		String temp;
		StringBuilder data = new StringBuilder();
		Integer list_num = 0;
		Integer count = 0;
		ArrayList list = new ArrayList();
		BufferedWriter bw = null; 
		
		try {
			bw= new BufferedWriter(new FileWriter("./result/"+ time_form.substring(4,8) + "/files/"
					+"IP_info_" + time_form +".csv", true));
			
			Connection.Response subip_res = Jsoup.connect("https://www.tdb.or.kr/ked/KedSearchCompanySubIp.do")
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
					.data("bizRegiNo",busi_list)
					.data("pageIndex","1")
					.cookies(cook)
					.method(Connection.Method.POST)
					.execute();
			
			Document doc = subip_res.parse();
			
			count = Integer.valueOf(doc.getElementsByClass("count").first().ownText());
			
			if( count == 0) {
				data.append(","); data.append("조회된 자료가 없습니다.");
			}
			else {
				// 첫 번째 항목 data 추가
				Elements elements = doc.getElementsByClass("table-wrap").select("tbody > tr > *");
				for (Element element : elements) {
					list.add(element.ownText());
				}
				
				for(int i=0 ; i<list.size()/4; i++) {
					data.append(busi_list); 
					for(int j=0 ; j<4 ; j++) {
						data.append(",");
						if(j == 3) {
							temp = list.get(4*i+j).toString();
							while(temp.contains("\"")) {
								if(Character.isDigit(temp.charAt(temp.indexOf("\"")-1))){
									temp = temp.replaceFirst("\"" , "inch");
								}
								else {
									temp = temp.replaceFirst("\"" , "\'");
									if(temp.contains("\'")) {
										temp = temp.replaceFirst("\"", "\'");
									}
								}
							}
						}
						if (list.get(4*i+j).toString().contains(",")) data.append("\"" + list.get(4*i+j).toString() + "\"");
						else data.append(list.get(4*i+j).toString());
					}
					
					bw.write(data.toString());
					bw.newLine();
					data.setLength(0);
				}
				
				list.clear();
				
				if(count > 10) {
					// 특허권 수가 10개를 넘을 경우, 최대 100개까지 받아올 수 있도록 함
					for(int i = 2 ; i <= (int)Math.ceil(Double.valueOf(count)/10); i++) { 
						if(i == 11) {
							break;
						}
						subip_res = Jsoup.connect("https://www.tdb.or.kr/ked/KedSearchCompanySubIp.do")
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
								.data("bizRegiNo",busi_list)
								.data("pageIndex",String.valueOf(i))
								.cookies(cook)
								.method(Connection.Method.POST)
								.execute();
						
						doc = subip_res.parse();
						
						elements = doc.getElementsByClass("table-wrap").select("tbody > tr > *");
						for (Element element : elements) {
							list.add(element.ownText());
						}
						for(int j=0 ; j<list.size()/4; j++) {
							data.append(busi_list); 
							for(int k=0 ; k<4 ; k++) {
								data.append(",");
								if(k == 3) {
									temp = list.get(4*j+k).toString();
									while(temp.contains("\"")) {
										if(Character.isDigit(temp.charAt(temp.indexOf("\"")-1))){
											temp = temp.replaceFirst("\"" , "inch");
										}
										else {
											temp = temp.replaceFirst("\"" , "\'");
											if(temp.contains("\'")) {
												temp = temp.replaceFirst("\"", "\'");
											}
										}
									}
								}
								if (list.get(4*j+k).toString().contains(",")) data.append("\"" + list.get(4*j+k).toString() + "\"");
								else data.append(list.get(4*j+k).toString());
							}
							bw.write(data.toString());
							bw.newLine();
							data.setLength(0);
						}
						list.clear();
					}
				}
			}
		} catch (IOException e) {
			
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			Tech_IP.main(cook, busi_list, time_form);
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
