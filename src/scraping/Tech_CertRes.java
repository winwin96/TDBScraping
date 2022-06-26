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

public class Tech_CertRes {

	public static void main(String url, Map<String, String> cook, String busi_list, String time_form) {

		try {

			Connection.Response kedsub_res = Jsoup.connect(url).header("Accept", "*/*")
					.header("Accept-Encoding", "gzip, deflate").header("Accept-Language", "ko")
					.header("Connection", "Keep-Alive").header("Host", "www.tdb.or.kr")
					.header("Referer","https://www.tdb.or.kr/prtl/eval/CompIndu.do?kwd=" + busi_list + "&compNm=" + busi_list)
					.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
					.header("X-Requested-With", "XMLHttpRequest").cookies(cook).method(Connection.Method.POST)
					.execute();

			Document doc = kedsub_res.parse();
			
			//연구소현황
			research(url, busi_list, doc, time_form);

			//인증, 수상
			certification(url, busi_list, doc, time_form);
			
		} catch (IOException e) {
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			Tech_CertRes.main(url, cook, busi_list, time_form);
		}
	}

	public static void research(String url, String busi_list, Document doc, String time_form) {

		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter("./result/"+ time_form.substring(4,8) + "/files/"
					+"research_info_" + time_form +".csv" , true));

			StringBuilder data = new StringBuilder();
			String temp;
			data.append(busi_list);
			ArrayList list = new ArrayList();
			Elements elements;
			Integer size;

			// 기업전담부서
			if (doc.getElementsByClass("tblType1 mt10").first().select("tbody > tr").text().contains("검색 결과가 없습니다.")) {
				data.append(",");
				data.append("검색 결과가 없습니다.");
				data.append(",");
				data.append("-");
				data.append(",");
				data.append("-");
				data.append(",");
				data.append("-");
			} else {
				elements = doc.getElementsByClass("tblType1 mt10").first().select("tbody > tr").select("*");

				for (Element element : elements) {
					list.add(element.ownText());
				}

				data.append(",");
				if (list.get(1).toString().contains(","))
					data.append("\"" + list.get(1).toString() + "\"");
				else
					data.append(list.get(1));

				data.append(",");
				temp = list.get(2).toString();
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
				if (temp.contains(","))
					data.append("\"" + temp + "\"");
				else
					data.append(temp);

				data.append(",");
				temp = list.get(3).toString();
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
				if (temp.contains(","))
					data.append("\"" + temp + "\"");
				else
					data.append(temp);

				data.append(",");
				if (list.get(4).toString().contains(","))
					data.append("\"" + list.get(4).toString() + "\"");
				else
					data.append(list.get(4));

				list.clear();
			}

			// 기업부설연구소
			if (doc.getElementsByClass("tblType1 mt10").get(1).select("tbody > tr").text().contains("검색 결과가 없습니다.")) {
				data.append(",");
				data.append("검색 결과가 없습니다.");
				data.append(",");
				data.append("-");
				data.append(",");
				data.append("-");
				data.append(",");
				data.append("-");
			} else {
				elements = doc.getElementsByClass("tblType1 mt10").get(1).select("tbody > tr").select("*");

				for (Element element : elements) {
					list.add(element.ownText());
				}

				
				for (int i = 0; i < list.size()/5; i++) {
					
					if(i != 0) {
						data.append(busi_list); data.append(",,,,");
					}
					
					data.append(",");
					if (list.get(5 * i + 1).toString().contains(","))
						data.append("\"" + list.get(5 * i + 1).toString() + "\"");
					else
						data.append(list.get(5 * i + 1));

					data.append(",");
					temp = list.get(5 * i + 2).toString();
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
					if (temp.contains(","))
						data.append("\"" + temp + "\"");
					else
						data.append(temp);

					data.append(",");
					temp = list.get(5 * i + 3).toString();
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
					if (temp.contains(","))
						data.append("\"" + temp + "\"");
					else
						data.append(temp);

					data.append(",");
					if (list.get(5 * i + 4).toString().contains(","))
						data.append("\"" + list.get(5 * i + 4).toString() + "\"");
					else
						data.append(list.get(5 * i + 4));
					
					bw.write(data.toString());
					bw.newLine();
					data.setLength(0);
				}
			}

			

		} catch (Exception e) {

			try {
				Thread.sleep(20000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			e.printStackTrace();
			research(url, busi_list, doc, time_form);

		} finally {
			try {
				if (bw != null) {
					bw.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public static void certification(String url, String busi_list, Document doc, String time_form) {

		BufferedWriter bw = null;

		try {
			StringBuilder data = new StringBuilder();
			ArrayList list = new ArrayList();
			Elements elements;
			String image;
			bw = new BufferedWriter(new FileWriter("./result/"+ time_form.substring(4,8) + "/files/"
					+"confirm_info_" + time_form +".csv" , true));
			
			for (int i = 0; i < doc.getElementsByClass("confirmListWrap clear").size(); i++) {
				
				elements = doc.getElementsByClass("confirmListWrap clear").get(i).select("*");

				for (Element element : elements) {
					list.add(element.ownText());
				}
				
//				for (int j = 0 ; j <list.size() ; j++) {
//					System.out.println(String.valueOf(j) + " : " + list.get(j));
//				}
				
				image = doc.getElementsByClass("confirmListWrap clear").get(i).select("img").attr("src");
				
				// 이노비즈
				if (image.contains("innobiz_img")) {
					data.append(busi_list);
					
					//인증번호
					data.append(certWrite(3,7,list));
					
					//관련업종
					data.append(certWrite(5,2,list));
					
					//인증기간
					data.append(certWrite(7,2,list));
					
					//주소
					data.append(certWrite(9,2,list));
					
					//연락처
					data.append(certWrite(11,2,list));
					
					bw.write(data.toString());
					bw.newLine();
					data.setLength(0);
					list.clear();
				}
				
				// 메인비즈
				else if (image.contains("mainbiz_img")) {
					data.append(busi_list);
					
					for (int j = 0 ; j < 9 ; j++) {
						data.append(",");
					}
					
					//인증번호
					data.append(certWrite(3,7,list));
					
					//업종
					data.append(certWrite(5,2,list));
					
					//업종기간
					data.append(certWrite(7,2,list));
					
					bw.write(data.toString());
					bw.newLine();
					data.setLength(0);
					list.clear();
				}
				
				// 장영실상
				else if (image.contains("ir_img")) {
					data.append(busi_list);
					for (int j = 0 ; j < 12 ; j++) {
						data.append(",");
					}
					
					//수상차수
					data.append(certWrite(3,7,list));

					//제품명
					data.append(certWrite(5,2,list));
					
					//모델명
					data.append(certWrite(7,2,list));

					//기술명
					data.append(certWrite(9,2,list));
					
					bw.write(data.toString());
					bw.newLine();
					data.setLength(0);
					list.clear();
				}
				
				// NET
				else if (image.contains("net_img")) {
					data.append(busi_list);
					for (int j = 0 ; j < 16 ; j++) {
						data.append(",");
					}
					
					//인증번호
					data.append(certWrite(3,7,list));

					//기술명
					data.append(certWrite(5,2,list));

					//인증기간
					data.append(certWrite(7,2,list));
					
					bw.write(data.toString());
					bw.newLine();
					data.setLength(0);
					list.clear();
				}
				
				// 벤처기업
				else if (image.contains("venture_img")) {
					data.append(busi_list);
					for (int j = 0 ; j < 5 ; j++) {
						data.append(",");
					}
					
					//확인번호
					data.append(certWrite(3,9,list));

					//확인유형
					data.append(certWrite(5,2,list));

					//인증기간
					data.append(certWrite(7,2,list));

					//지역
					data.append(certWrite(9,2,list));
					
					bw.write(data.toString());
					bw.newLine();
					data.setLength(0);
					list.clear();
				}

			}

		} catch (Exception e) {

			try {
				Thread.sleep(20000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			certification(url, busi_list, doc, time_form);
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

	// 인증/수상에서 데이터를 쓰기 위한 과정이 반복적으로 이루어지므로 작성한 메서드
	// 매개변수중 get_index는 해당 내용이 담겨있는 인덱스의 번호를, sub_index는 해당 내용에서 원하는 인덱스의 내용이 시작되는 인덱스를 나타냄
	public static String certWrite(Integer get_index, Integer sub_index, ArrayList list) {
		
		
		String data = "";
		data = data + ",";
		String temp; 
		if (list.get(get_index).toString().length() > 1) {
			temp = list.get(get_index).toString().substring(sub_index, list.get(get_index).toString().length());
			
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
			
			if (temp.contains(",")) {
				data = data + "\"" + temp.toString() + "\"";
			} else {
				data = data + temp.toString();
			}
			
		} else
			data = data + "-";
		
		return data;
	}
}
