package scraping;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Launcher {
	
	ArrayList list = new ArrayList();
	static Map<String, String> login_cook; 
	static int now;
	static List<String> busi_list = new ArrayList<String>();
	static String kedcd;
	static String url;
	static String base_url;
	static Document doc;
	static Connection.Response login_res;
	static Connection.Response compindu_res;
	static Connection.Response kedser_res;
	static Connection.Response keddo_res;
	static Map<String, String> keddo_cook;
	static Map<String, String> keddo_cook2;
	static ArrayList keddo_res_array;
	static ArrayList keddo_res2_array;
	static Iterator keddo_res_iter;
	static Iterator keddo_res2_iter;
	static String[] keddo_res_split;
	static String[] keddo_res2_split;
	static Connection.Response APTDB_res;
	static Map<String, String> APTDB_cook;
	static String data_enpnmf;
	static String data_reprnm;
	static String data_rdnmbzpladdrb;
	static String data_bzno;
	static String data_cono;
	static String data_ipocd;
	static String data_ksic9bzccd;
	static String data_ksic9bzccdnm;
	static String data_ksic10bzccd;
	static String data_ksic10bzccdnm;
	static String data_enpcls;
	static String data_kedcd;
	static String data_title;

	public static void main(String[] args)
			throws KeyManagementException, NoSuchAlgorithmException, InvalidFormatException, IOException {
		
		// 처음 실행 시 사업자 등록 번호 파일의 인덱스 0부터 시작
		int start = 0;
		
		// 코드 실행 시간을 기준으로 시간을 받음
		LocalDateTime present = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");
		String time_form = present.format(formatter);
		
		String folder_time = present.format(formatter).substring(4,8);
		
		// 분별을 위한 폴더 생성
		makeFolder("./result");
		makeFolder("./result/" + time_form.substring(4,8));
		makeFolder("./result/" + time_form.substring(4,8) + "/files" );
		
		// 배치파일을 통해 사업자 등록 번호 파일의 이름을 받아옴
		String file_name = args[0];
		
		if(file_name == null) {
			System.out.println("파일 이름을 입력해주세요");
			return;
		}
		else if(!new File("./" + file_name).exists()) {
			System.out.println("존재하지 않는 파일입니다.");
			return;
		}
		
		// 스크래핑에 앞서 스크래핑하는 각 내용의 컬럼 생성
		ExcelAdmin.common(time_form);
		ExcelAdmin.confirm(time_form);
		ExcelAdmin.financial(time_form);
		ExcelAdmin.IP(time_form);
		ExcelAdmin.IPNum(time_form);
		ExcelAdmin.research(time_form);
		ExcelAdmin.TCB(time_form);
		
		// 시작 인덱스, 실행 시간, 사업자 등록 번호가 저장된 파일 이름을 매개변수로 startWork 메서드 실행
		startWork(start, time_form, file_name);
	}

	public static void startWork(int start, String time_form, String file_name) throws KeyManagementException, NoSuchAlgorithmException {
		
		// 실행시간을 측정하기 위한 변수 선언
		long afterTime;
		long beforeTime;
		long secDiffTime;
		
		// getBusiNums 메서드를 통해 사업자 등록 번호가 적힌 파일에서 스크래핑해야 하는 사업자 등록 번호를 리스트로 받아옴
		busi_list = getBusiNums(file_name);
		
		// https 접속 방식에서 필요한 SSL 인증서 우회를 하기 위해 setSSL()메서드 실행
		base_url = "https://www.tdb.or.kr/loginPage.do";
		if (base_url.indexOf("https://") >= 0) { 
			setSSL();
		}
		
		// startWork 메서드 실행시 지정해주었던 시작 인덱스부터 원하는 곳까지 반복문 실행
		for (int i = start; i < busi_list.size(); i++) { // 
			
			System.out.print(String.valueOf(i+1) + "번째 : " + busi_list.get(i) + " ");
			
			beforeTime = System.currentTimeMillis();
			
			// 처음 사이트에 접속하여 세션을 받아오고, 해당 쿠키를 저장
			try {
				//현재 작업중인 인덱스를 변수 now에 저장, 오류가 발생할 시 해당 인덱스부터 다시 실행될 수 있도록 함
				now = i;

				// 로그인
				login_res = Jsoup.connect(base_url)
						.header("ACCEPT", "text/html, application/xhtml+xml, image/jxr, */*")
						.header("Accept-Encoding", "gzip, deflate").header("Accept-Language", "ko")
						.header("Connection", "Keep-Alive")
						.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
						.header("Host", "www.tdb.or.kr").execute();
				login_cook = login_res.cookies();
				
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

				// 해당 요청도 사이트에서 직접 동작 시 부가적으로 이루어지나, 이어지는 요청에 필수적인 정보를 포함하지 않으며 시간이 오래걸려 생략함
				
//				url = "https://www.tdb.or.kr/prtl/eval/CompIndu.do?kwd=" + busi_list.get(i) + "&compNm="
//						+ busi_list.get(i);
//				compindu_res = Jsoup.connect(url)
//						.header("ACCEPT", "text/html, application/xhtml+xml, image/jxr, */*")
//						.header("Accept-Encoding", "gzip, deflate").header("Accept-Language", "ko")
//						.header("Cache-Control", "no-cache").header("Content-Type", "application/x-www-form-urlencoded")
//						.header("Connection", "Keep-Alive")
//						.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
//						.header("Host", "www.tdb.or.kr").header("Host", "www.tdb.or.kr")
//						.header("Referer", "https://www.tdb.or.kr/loginPage.do")
//						.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
//						.data("boxClick", "").data("category", "total").data("commCd", "00")
//						.data("compNm", busi_list.get(i)).data("dateSrchType", "all").data("jumpPage", "")
//						.data("kwd", busi_list.get(i)).data("preKwd", busi_list.get(i)).data("sort", "r")
//						.data("subCategory", "all").data("viewMode", "").cookies(login_res.cookies())
//						.method(Connection.Method.POST).execute();

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

				url = "https://www.tdb.or.kr/ked/KedSearchCompany.do?kwd=" + busi_list.get(i) + "&compNm="
						+ busi_list.get(i);
				kedser_res = Jsoup.connect(url)
						.header("ACCEPT", "text/html, application/xhtml+xml, image/jxr, */*")
						.header("Accept-Encoding", "gzip, deflate").header("Accept-Language", "ko")
						.header("Connection", "Keep-Alive")
						.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
						.header("Host", "www.tdb.or.kr")
						.header("Referer","https://www.tdb.or.kr/prtl/eval/CompIndu.do?kwd=" + busi_list.get(i) + "&compNm="+ busi_list.get(i))
						.cookies(login_res.cookies()).method(Connection.Method.GET).execute();

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////				
				
				// 해당 사항에서 응답하는 내용에서 다음 URL을 얻을 수 있으므로 파싱이 필수적
				keddo_res = Jsoup.connect("https://www.tdb.or.kr/ked/KedSearchCompany.do")
						.header("ACCEPT", "text/html, application/xhtml+xml, image/jxr, */*")
						.header("Accept-Encoding", "gzip, deflate").header("Accept-Language", "ko")
						.header("Connection", "Keep-Alive").header("Host", "www.tdb.or.kr")
						.header("Cache-Control", "no-cache").header("Content-Type", "application/x-www-form-urlencoded")
						.header("Referer","https://www.tdb.or.kr/prtl/eval/CompIndu.do?kwd=" + busi_list.get(i) + "&compNm="+ busi_list.get(i))
						.header("Cookie","PHAROSVISITOR=" + (String) login_cook.get("PHAROSVISITOR") + "; JSESSIONID="+ (String) login_cook.get("JSESSIONID") + "; keywords=" + busi_list.get(i))
						.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
						.data("compNm", busi_list.get(i)).data("kwd", busi_list.get(i)).method(Connection.Method.POST)
						.cookies(login_res.cookies()).execute();

				doc = keddo_res.parse();
				keddo_cook = keddo_res.cookies();

				// 응답 내용은 script로 이루어져 있어, jsoup를 활용한 파싱이 불가능하기에, 전체 내용을 리스트로 받아 url을 얻어냄
				keddo_res_split = String.valueOf(doc).split(" |	|\'|\"|\\;");
				keddo_res_array = new ArrayList<>(Arrays.asList(keddo_res_split));
				keddo_res_iter = keddo_res_array.iterator();
				while (keddo_res_iter.hasNext()) {
					if ("".equals(keddo_res_iter.next())) {
						keddo_res_iter.remove();
					}
				}
				
				url = String.valueOf(keddo_res_array.get(keddo_res_array.indexOf("stdUrl") + 2))
						+ String.valueOf(keddo_res_array.get(keddo_res_array.lastIndexOf("location.href") + 4)) + busi_list.get(i);
		

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				// 해당 사항에서 응답하는 내용에서 다음 요청에 필요한 변수와 쿠키를 얻을 수 있으므로 필수적
				APTDB_res = Jsoup.connect(url)
						.header("ACCEPT", "text/html, application/xhtml+xml, image/jxr, */*")
						.header("Accept-Encoding", "gzip, deflate").header("Accept-Language", "ko")
						.header("Connection", "Keep-Alive").header("Host", "www.cretop.com")
						.header("Referer", "https://www.tdb.or.kr/ked/KedSearchCompany.do")
						.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
						.method(Connection.Method.GET).cookies(login_res.cookies()).execute();

				doc = APTDB_res.parse();
				APTDB_cook = APTDB_res.cookies();
				
				// 사업자 등록 번호에 해당하는 정보가 TDB 사이트에 존재하지 않는 경우 해당 사항에서 종료됨
				if(doc.select("table.tb_bbs_list > tbody > tr > td").text().contains("검색된 데이터가 없습니다.")) {
					System.out.print("skip , ");
					//NoData 클래스를 활용하여 해당 사업자 등록 번호에 포함된 정보가 없다는 것을 나타낼 필요가 있는 곳에 해당 내용을 저장
					NoData.main(busi_list.get(i), time_form);
					ExcelAdmin.merge(time_form);
					
					continue;
				}
				else {
					//사업자 번호, 대표자 이름, 회사 이름 등의 정보를 포함하여 저장
					data_enpnmf = doc.getElementsByClass("enp").attr("data-enpnmf");
					data_reprnm = doc.getElementsByClass("enp").attr("data-repernm");
					data_rdnmbzpladdrb = doc.getElementsByClass("enp").attr("data-rdnmbzpladdrb");
					data_bzno = doc.getElementsByClass("enp").attr("data-bzno");
					data_cono = doc.getElementsByClass("enp").attr("data-cono");
					data_kedcd = doc.getElementsByClass("enp").attr("data-kedcd");
//					data_ipocd = doc.getElementsByClass("enp").attr("data-ipocd");
//					data_ksic9bzccd = doc.getElementsByClass("enp").attr("data-ksic9bzccd");
//					data_ksic9bzccdnm = doc.getElementsByClass("enp").attr("data-ksic9bzccdnm");
//					data_ksic10bzccd = doc.getElementsByClass("enp").attr("data-ksic10bzccd");
//					data_ksic10bzccdnm = doc.getElementsByClass("enp").attr("data-ksic10bzccdnm");
//					data_enpcls = doc.getElementsByClass("enp").attr("data-enpcls");
//					data_title = doc.getElementsByClass("enp").attr("title").split(" 선택")[0];
					
				}
				
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

				// 해당 사항에서 응답하는 내용에서 다음 URL을 얻을 수 있으므로 파싱이 필수적
				Connection.Response keddo_res2 = Jsoup.connect("https://www.tdb.or.kr/ked/KedSearchCompany.do")
						.header("ACCEPT","text/html, application/xhtml+xml, image/jxr, */*")
						.header("Accept-Encoding", "gzip, deflate")
						.header("Accept-Language", "ko")
						.header("Cache-Control", "no-cache")
						.header("Connection", "Keep-Alive")
						.header("Host", "www.tdb.or.kr")
						.header("Cache-Control","no-cache")
						.header("Content-Type", "application/x-www-form-urlencoded")
						.header("Referer",url)
						.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
						.cookies(login_res.cookies())
						.data("bizRegiNo",data_bzno)
						.data("compAddr", data_rdnmbzpladdrb)
						.data("compNm", data_enpnmf)
						.data("corpRgstNo", data_cono)
						.data("id","ked001")
						.data("kedcd",data_kedcd)
						.data("kedCnt","1")
						.data("kwd","")
						.data("leadrNm",data_reprnm)
						.method(Connection.Method.POST)
						.execute();
						
				doc = keddo_res2.parse();
				keddo_cook2 = keddo_res2.cookies();
				
				//마찬가지로 script로 이루어진 사항에선 jsoup로 파싱이 불가능하므로 리스트를 활용해 필요한 정보를 파싱
				keddo_res2_split = String.valueOf(doc).split("\"");
				keddo_res2_array = new ArrayList<>(Arrays.asList(keddo_res2_split));
				keddo_res2_iter = keddo_res2_array.iterator();
				while(keddo_res2_iter.hasNext()) {
					if("".equals(keddo_res2_iter.next())) {
						keddo_res2_iter.remove();
					}
				}
				url = "https://www.cretop.com" + String.valueOf(keddo_res2_array.get(keddo_res2_array.indexOf("/ap/APTDB01R0.do?id=kfb001&kedcd=")+4)).trim();
				
			} catch (Exception e) {
				
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
				e.printStackTrace();
				// 해당 사항에서 오류가 발생한다면 startWork를 now에 저장된 인덱스부터 반복하도록 다시 실행
				startWork(now, time_form, file_name);
				return;
			}
			
			
			
			// '일반정보' 에 관한 정보를 스크래핑하는 메서드 실행
			url = GeneralInfo.main(url, APTDB_cook,busi_list.get(i),keddo_res2_array, time_form);
			
			// 기술력정보 - '인증/수상, 연구소현황' 에 관련된 정보를 스크래핑하는 메서드 실행
			Tech_CertRes.main(url,login_cook,busi_list.get(i), time_form);
			
			// 기술력정보 - '지식재산권' 에 관한 정보를 스크래핑하는 메서드 실행
			Tech_IP.main(login_cook,busi_list.get(i),time_form);
			
			// 기술력정보 - '지식재산권 건수 추이' 에 관한 정보를 스크래핑하는 메서드 실행
			Tech_IPNum.main(login_cook,busi_list.get(i),time_form);
			
			// 'TCB' 에 관한 정보를 스크래핑하는 메서드 실행
			TCB.main(login_cook,busi_list.get(i),time_form);
			
			// 시간 측정 종료 및 처리 시간 출력
			afterTime = System.currentTimeMillis();
			secDiffTime = (afterTime - beforeTime)/1000;
			System.out.print(" " + secDiffTime + " ");
			
			// 각 내용이 저장된 7개의 CSV 파일을 하나의 엑셀 파일로 합칠 수 있도록 하는 ExcelAdmin 클래스 실행
			ExcelAdmin.merge(time_form);
			
			try {
				// 스크래핑이 완료된 마지막 사업자 등록 번호를 저장하며 추후에 활용할 수 있도록 함
				updateIndex(busi_list.get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			// 잦은 사이트 요청을 통해 차단이 이루어지는 경우 잠시 작동을 멈추도록 하여 이를 방지, 현재까지는 차단이 이루어지지 않고 있기에 주석 처리 하였음
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			
		}
		
		// 스크래핑이 종료되면 마지막 스크래핑이 이루어진 사업자 등록 번호가 저장된 파일을 삭제함
		if (new File("./last_index.csv").exists()) {
			new File("./last_index.csv").delete();
		}
		
		System.out.println("스크래핑 종료");
		
	}
	
	// HTTPS 접속 인증서 우회 메서드
	public static void setSSL() throws NoSuchAlgorithmException, KeyManagementException {

		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new SecureRandom());
		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}

	// 마지막으로 스크래핑된 사업자 등록번호가 저장된 CSV 파일을 읽어 해당 사업자 등록번호를 반환하는 메서드
	public static String lastIndex() {
		
		BufferedReader br = null;
		String last_index = null;
		
		try {
            br = new BufferedReader(new FileReader("./last_index.csv"));
            last_index = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) { 
                    br.close(); 
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
		return last_index;
	}
	
	// 스크래핑이 성공적으로 이루어진 마지막 사업자 등록번호를 CSV 파일로 저장하여 기록하는 메서드
	public static void updateIndex(String busi_nums) throws InterruptedException {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter("last_index.csv", false));
			bw.write(busi_nums.toString());
		} catch (Exception e) {
			e.printStackTrace();
			Thread.sleep(500);
			updateIndex(busi_nums);
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
	
	// 폴더 생성
	public static void makeFolder(String path) {
		
		File Folder = new File(path);
		
		if (!Folder.exists()) {
			try{
			    Folder.mkdir(); 
		        } 
		        catch(Exception e){
			    e.getStackTrace();
			}        
		}
	}
	
	// 사업자 등록 번호가 저장된 파일에서 사업자 등록 번호를 읽어 이를 리스트로 반환하는 메서드
	public static List<String> getBusiNums(String file_name) {
		
		List<String> list = new ArrayList<String>();
		File file = new File("./" + file_name);
		String value = "";
		Integer last_index;

		XSSFWorkbook busi_nums = null;
		try {
			
			// 만약 마지막으로 처리한 사업자 등록 번호가 기록된 csv파일이 존재하면 이를 이용하고, 아니면 처음부터 실행될 수 있도록 함
			if (new File("./last_index.csv").exists()) {
				busi_nums = new XSSFWorkbook(file);
				XSSFSheet sheet = busi_nums.getSheetAt(0);
				int rows = sheet.getPhysicalNumberOfRows();

				for (int i = 0; i < rows; i++) {
					XSSFRow row = sheet.getRow(i);
					if (row != null) {
						XSSFCell cell = row.getCell(0);

						if (String.valueOf(cell.getCellType()) != "STRING") {
							cell.setCellType(CellType.STRING);
						}
						value = cell.getStringCellValue();
					}
					list.add(value);
				}
				
				last_index = list.indexOf(lastIndex());
				
				if (last_index == -1) {
					return list;
				}
				else {
					return list.subList(last_index+1, list.size());
				}
			}
			else {
				busi_nums = new XSSFWorkbook(file);
				XSSFSheet sheet = busi_nums.getSheetAt(0);
				int rows = sheet.getPhysicalNumberOfRows();

				for (int i = 0; i < rows; i++) {
					XSSFRow row = sheet.getRow(i);
					if (row != null) {
						XSSFCell cell = row.getCell(0);

						if (String.valueOf(cell.getCellType()) != "STRING") {
							cell.setCellType(CellType.STRING);
						}
						value = cell.getStringCellValue();
					}

					list.add(value);
				}
				return list;
			}
			
		} catch (InvalidFormatException e) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			System.out.println("사업자등록번호 파일 읽기 실패");
			e.printStackTrace();
			return getBusiNums(file_name);
		} catch (IOException e) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return getBusiNums(file_name);
		} 
	}
}

