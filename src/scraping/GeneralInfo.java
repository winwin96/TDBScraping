package scraping;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GeneralInfo {

	public static String main(String url, Map<String, String> cook, String busi_list, ArrayList array, String time_form) {
		
		String return_url;
		
		try {

			Connection.Response APTDB_res2 = Jsoup.connect(url)
					.header("ACCEPT","text/html, application/xhtml+xml, image/jxr, */*")
					.header("Accept-Encoding", "gzip, deflate")
					.header("Accept-Language", "ko")
					.header("Connection", "Keep-Alive")
					.header("Host", "www.cretop.com")
					.header("Referer","https://www.tdb.or.kr/ked/KedSearchCompany.do")
					.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
					.header("Cookie","JSESSIONID="+(String)cook.get("JSESSIONID")+"; _xm_webid_1_="+(String)cook.get("_xm_webid_1_"))
					.cookies(cook)
					.method(Connection.Method.GET)
					.execute();	
				
			Document doc = APTDB_res2.parse();

			//기업 프로필, 신용정보
			return_url = profile(url, cook, busi_list, array, doc, time_form);
			
			//요약재무
			financial(url, cook, busi_list, array, doc, time_form);

		} catch (IOException e) {
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return GeneralInfo.main(url, cook, busi_list, array, time_form);
		}
		
		return return_url;
	}
	
	public static String profile(String url, Map<String, String> cook, String busi_list, ArrayList array, Document doc, String time_form) {
		
		String temp;
		String tel;
		BufferedWriter bw = null;
		StringBuilder data = new StringBuilder();
		data.append(busi_list); data.append(",");
		ArrayList list = new ArrayList();
		
		try {	
			
			bw = new BufferedWriter(new FileWriter("./result/"+ time_form.substring(4,8) + "/files/"
					+"common_info_" + time_form +".csv", true));
		
			//기업프로필
			Elements elements = doc.getElementsByClass("tb_type1").select("*");
			
			for (Element element : elements) {
				list.add(element.ownText());
			}
			
			//주요신용정보
			elements = doc.getElementsByClass("dep_5sp").select("*");
			
			for (Element element : elements) {
				list.add(element.ownText());
			}
			
			while(list.contains("")) {
				list.remove("");
			}
			
//			for(int i = 0 ; i <list.size(); i++) {
//				System.out.println(String.valueOf(i) + " : " + list.get(i));
//			}
			
			// 기업체명
			if (list.get(list.indexOf("기업체명")+1).toString().contains(",")) data.append("\"" + list.get(list.indexOf("기업체명")+1).toString() + "\"");
			else data.append(list.get(list.indexOf("기업체명")+1).toString()); data.append(",");
			
			// 영문기업명
			if (list.get(list.indexOf("영문기업명")+1).toString().contains(",")) data.append("\"" + list.get(list.indexOf("영문기업명")+1).toString() + "\"");
			else data.append(list.get(list.indexOf("영문기업명")+1).toString()); data.append(",");
			
			// 사업자번호
			data.append(list.get(list.indexOf("사업자번호")+1).toString()); data.append(",");
			
			// 법인(주민)번호
			data.append(list.get(list.indexOf("법인(주민)번호")+1).toString()); data.append(",");
			
			// 대표자명
			if (list.get(list.indexOf("대표자명")+1).toString().contains(",")) data.append("\"" + list.get(list.indexOf("대표자명")+1).toString() + "\"");
			else data.append(list.get(list.indexOf("대표자명")+1).toString()); data.append(",");
			
			// 종업원수
			if (list.get(list.indexOf("종업원수")+1).toString().contains(",")) data.append("\"" + list.get(list.indexOf("종업원수")+1).toString() + "\"");
			else data.append(list.get(list.indexOf("종업원수")+1).toString()); data.append(",");
			
			// 설립형태
			if (list.get(list.indexOf("설립형태")+1).toString().contains(",")) data.append("\"" + list.get(list.indexOf("설립형태")+1).toString() + "\"");
			else data.append(list.get(list.indexOf("설립형태")+1).toString()); data.append(",");
			
			// 설립일자
			if (list.get(list.indexOf("설립일자")+1).toString().contains(",")) data.append("\"" + list.get(list.indexOf("설립일자")+1).toString() + "\"");
			else data.append(list.get(list.indexOf("설립일자")+1).toString()); data.append(",");
			
			// 기업형태
			if (list.get(list.indexOf("기업형태")+1).toString().contains(",")) data.append("\"" + list.get(list.indexOf("기업형태")+1).toString() + "\"");
			else data.append(list.get(list.indexOf("기업형태")+1).toString()); data.append(",");
			
			// 기업규모
			if (list.get(list.indexOf("기업규모")+1).toString().contains(",")) data.append("\"" + list.get(list.indexOf("기업규모")+1).toString() + "\"");
			else data.append(list.get(list.indexOf("기업규모")+1).toString()); data.append(",");
			
			// 전화번호
			
			tel = list.get(list.indexOf("전화번호")+1).toString();
			if (tel.startsWith("-1")) tel = tel.substring(1);
			
			if (tel.contains(",")) data.append("\"" + tel + "\"");
			else data.append(tel); data.append(",");
			
			// 팩스번호
			if (list.get(list.indexOf("팩스번호")+1).toString().contains(",")) data.append("\"" + list.get(list.indexOf("팩스번호")+1).toString() + "\"");
			else data.append(list.get(list.indexOf("팩스번호")+1).toString()); data.append(",");
			
			// 홈페이지
			if (list.get(list.indexOf("홈페이지")+1).toString().contains(",")) data.append("\"" + list.get(list.indexOf("홈페이지")+1).toString() + "\"");
			else data.append(list.get(list.indexOf("홈페이지")+1).toString()); data.append(",");
			
			// 이메일
			if (list.get(list.indexOf("이메일")+1).toString().contains(",")) data.append("\"" + list.get(list.indexOf("이메일")+1).toString() + "\"");
			else data.append(list.get(list.indexOf("이메일")+1).toString()); data.append(",");
			
//			// 결산월
//			data.append(list.get(62).toString()); data.append(",");
//			
//			// 기업공개일자
//			if (list.get(65).toString().contains(",")) data.append("\"" + list.get(65).toString() + "\"");
//			else data.append(list.get(65).toString()); data.append(",");
			
			// 주소
			if (list.get(list.indexOf("도로명")+1).toString().contains(",")) data.append("\"" + list.get(list.indexOf("도로명")+1).toString() + "\"");
			else data.append(list.get(list.indexOf("도로명")+1).toString()); data.append(",");
			
			// 업종
			if (list.get(list.indexOf("업종")+1).toString().contains(",")) data.append("\"" + list.get(list.indexOf("업종")+1).toString() + "\"");
			else data.append(list.get(list.indexOf("업종")+1).toString()); data.append(",");
			
			// 주요제품
			temp = list.get(list.indexOf("주요제품(상품)")+1).toString();

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
			
			if (temp.contains(",")) data.append("\"" + temp + "\"");
			else data.append(temp); data.append(",");
			
			// 휴폐업졍보
			data.append(list.get(list.indexOf("휴폐업정보")+1).toString()); data.append(",");
			
			// 기업채무불이행상태
			data.append(list.get(list.indexOf("법인등기정보")+1).toString());
			
			// 다음 url을 요청하는 과정에서 해당 사이트는 퍼센트 인덱스에서 모든 %를 %25로 변환하여 요청하도록 되어있기에, 이러한 사항이 변경될시 replaceAll구문을 변경하도록 해야함
			url = "https://www.tdb.or.kr/ked/KedSearchCompanySub.do?bizRegNo=" + busi_list + "&compNm=" 
			+ URLEncoder.encode(String.valueOf(array.get(array.indexOf("https://www.cretop.com")+2)).trim(), "UTF-8").replaceAll("%", "%25") + "&ked=Y";

			bw.write(data.toString());
			bw.newLine();
			
			return url;
			
		} catch(IOException e) {
			
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return profile(url, cook, busi_list, array, doc, time_form);
			
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
	
	public static void financial(String url, Map<String, String> cook, String busi_list, ArrayList array, Document doc, String time_form) {
		
		BufferedWriter bw = null;
		StringBuilder data = new StringBuilder();
		ArrayList list = new ArrayList();
		ArrayList fin_year = new ArrayList();
		ArrayList inc_year = new ArrayList();
		Integer fir_index;
		Integer sec_index;
		
		try {	
			
			//년도 받아오기
			bw = new BufferedWriter(new FileWriter("./result/"+ time_form.substring(4,8) + "/files/"
					+"financial_info_" + time_form +".csv" , true));
			
			Elements elements = doc.getElementsByClass("data_table data_chart2").select("table.tb_type2 > thead > tr > *");
			
			for (Element element : elements) {
				list.add(element.ownText());
			}
			
			for(int i = 1 ; i < list.size() ; i++) {
				if ( ! String.valueOf(list.get(i)).contains("-")) {
					fin_year.add(list.get(i));
				}
			}
			
			list.clear();
			
			elements = doc.getElementsByClass("data_table data_chart3").select("table.tb_type2 > thead > tr > *");
			
			for (Element element : elements) {
				list.add(element.ownText());
			}
			
			for(int i = 1 ; i < list.size() ; i++) {
				if ( ! String.valueOf(list.get(i)).contains("-")) {
					inc_year.add(list.get(i));
				}
			}
			
			list.clear();
			
			//주요 재무상태표
			elements = doc.getElementsByClass("data_table data_chart2").select("table.tb_type2 > tbody > tr > *");
			
			for (Element element : elements) {
				list.add(element.ownText());
			}
			
			if(fin_year.size() != 0) {
				
				for(int i = 0 ; i < fin_year.size() ; i++ ) {
	
					for(int j = 0 ; j < 3 ; j++) {
						data.append(busi_list); 
						data.append(","); data.append("재무상태표");
						//년도
						data.append(",");
						if (fin_year.get(i).toString().contains(",")) data.append("\"" + fin_year.get(i).toString() + "\"");
						else data.append(fin_year.get(i).toString()); 
						//계정구분
						data.append(",");
						if (list.get(6*j).toString().contains(",")) data.append("\"" + list.get(6*j).toString() + "\"");
						else data.append(list.get(6*j).toString()); 
						//값
						data.append(","); 
						if (list.get(6*j + 6 - fin_year.size() + i).toString().contains(",")) data.append("\"" + list.get(6*j + 6 - fin_year.size() + i).toString() + "\"");
						else data.append(list.get(6*j + 6 - fin_year.size() + i).toString()); 

						bw.write(data.toString());
						bw.newLine();
						data.setLength(0);
						
					}
					
				}
				
			}
			
			list.clear();
			
			//주요 손익계산서
			elements = doc.getElementsByClass("data_table data_chart3").select("table.tb_type2 > tbody > tr > *");
			
			for (Element element : elements) {
				list.add(element.ownText());
			}
			
			if(inc_year.size() != 0) {
				
				for(int i = 0 ; i < inc_year.size() ; i++ ) {
	
					for(int j = 0 ; j < 3 ; j++) {
						data.append(busi_list); 
						data.append(","); data.append("손익계산서");
						//년도
						data.append(",");
						if (inc_year.get(i).toString().contains(",")) data.append("\"" + inc_year.get(i).toString() + "\"");
						else data.append(inc_year.get(i).toString()); 
						//계정구분
						data.append(",");
						if (list.get(6*j).toString().contains(",")) data.append("\"" + list.get(6*j).toString() + "\"");
						else data.append(list.get(6*j).toString()); 
						//값
						data.append(","); 
						if (list.get(6*j + 6 - inc_year.size() + i).toString().contains(",")) data.append("\"" + list.get(6*j + 6 - inc_year.size() + i).toString() + "\"");
						else data.append(list.get(6*j + 6 - inc_year.size() + i).toString()); 

						bw.write(data.toString());
						bw.newLine();
						data.setLength(0);
					}
					
				}
				
			}
			
			list.clear();
			
			
		} catch(IOException e) {
			
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			financial(url, cook, busi_list, array, doc, time_form);
			
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
