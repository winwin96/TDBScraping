package scraping;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelAdmin {

	public static void main(String args[]) {
		
	}

	// 일반 정보에 해당하는 컬럼을 생성하는 메서드
	public static void common(String time_form) {
		
		BufferedWriter bw = null;
		StringBuilder data = new StringBuilder();
		
		try {
			
			bw = new BufferedWriter(new FileWriter("./result/"+ time_form.substring(4,8) + "/files/"
			+ "common_info_" + time_form +".csv", true));
			data.append("조회값"); data.append(",");
 			data.append("기업체명"); data.append(",");
			data.append("영문기업명"); data.append(",");
			data.append("사업자번호"); data.append(",");
			data.append("법인(주민)번호"); data.append(",");
			data.append("대표자명"); data.append(",");
			data.append("종업원수"); data.append(",");
			data.append("설립형태"); data.append(",");
			data.append("설립일자"); data.append(",");
			data.append("기업형태"); data.append(",");
			data.append("기업규모"); data.append(",");
			data.append("전화번호"); data.append(",");
			data.append("팩스번호"); data.append(",");
			data.append("홈페이지"); data.append(",");
			data.append("이메일"); data.append(",");
			data.append("도로명"); data.append(",");
			data.append("업종"); data.append(",");
			data.append("주요제품"); data.append(",");
			data.append("휴폐업정보"); data.append(",");
			data.append("기업채무불이행상태");
			
			bw.write(data.toString());
			bw.newLine();
		} catch(IOException e) {
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			common(time_form);
			
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

	// 인증/수상에 해당하는 컬럼을 생성하는 메서드
	public static void confirm(String time_form) {
		
		BufferedWriter bw = null;
		StringBuilder data = new StringBuilder();
		
		try {
			bw = new BufferedWriter(new FileWriter("./result/"+ time_form.substring(4,8) + "/files/"
					+"confirm_info_" + time_form +".csv", true));
			data.append(""); data.append(",");
			data.append("이노비즈"); data.append(","); data.append(","); data.append(","); data.append(","); data.append(",");
			data.append("벤처인"); data.append(","); data.append(","); data.append(","); data.append(",");
			data.append("메인비즈"); data.append(","); data.append(","); data.append(",");
			data.append("장영실상"); data.append(","); data.append(","); data.append(","); data.append(",");
			data.append("NET"); data.append(","); data.append(","); data.append(",");
			
			bw.write(data.toString());
			bw.newLine();
			data.setLength(0);
			
			data.append("조회값"); data.append(","); data.append("인증번호"); data.append(","); data.append("관련업종"); data.append(",");
			data.append("인증기간"); data.append(","); data.append("주소"); data.append(","); data.append("연락처"); data.append(",");
			data.append("확인번호"); data.append(","); data.append("확인유형"); data.append(","); data.append("인증기간"); data.append(",");
			data.append("지역"); data.append(","); data.append("인증번호"); data.append(","); data.append("업종"); data.append(",");
			data.append("업종기간"); data.append(","); data.append("수상차수"); data.append(","); data.append("제품명"); data.append(",");
			data.append("모델명"); data.append(","); data.append("기술명"); data.append(","); data.append("인증번호"); data.append(",");
			data.append("기술명"); data.append(","); data.append("인증기간");
			
			bw.write(data.toString());
			bw.newLine();
			
		} catch(IOException e) {
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			confirm(time_form);
			
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
	
	// 재무정보에 해당하는 컬럼을 생성하는 메서드
	public static void financial(String time_form) {
		
		BufferedWriter bw = null;
		StringBuilder data = new StringBuilder();
		
		try {
			bw = new BufferedWriter(new FileWriter("./result/"+ time_form.substring(4,8) + "/files/"
					+"financial_info_" + time_form +".csv", true));
			data.append("조회값"); data.append(",");
			data.append("재무구분"); data.append(",");
			data.append("년도"); data.append(",");
			data.append("계정구분"); data.append(","); 
			data.append("값(단위:백만원)");
			
			bw.write(data.toString());
			bw.newLine();
			data.setLength(0);
			
		} catch(IOException e) {
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			financial(time_form);
			
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
	
	// 지식재산권에 해당하는 컬럼을 생성하는 메서드
	public static void IP(String time_form) {
		
		BufferedWriter bw = null;
		StringBuilder data = new StringBuilder();
		
		try {
			bw = new BufferedWriter(new FileWriter("./result/"+ time_form.substring(4,8) + "/files/"
					+"IP_info_" + time_form +".csv", true));
			data.append("조회값"); data.append(",");
			data.append("구분"); data.append(",");
			data.append("등록번호"); data.append(",");
			data.append("등록일자"); data.append(","); 
			data.append("출원명");
			
			bw.write(data.toString());
			bw.newLine();
			data.setLength(0);
			
		} catch(IOException e) {
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			IP(time_form);
			
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

	// 지식재산권 개수에 해당하는 컬럼을 생성하는 메서드
	public static void IPNum(String time_form) {
		
		BufferedWriter bw = null;
		StringBuilder data = new StringBuilder();
		Integer now_year = Integer.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy")));
		
		try {
			bw = new BufferedWriter(new FileWriter("./result/"+ time_form.substring(4,8) + "/files/"
					+"IPNum_info_" + time_form +".csv", true));
			data.append("조회값"); data.append(",");
			data.append("구분"); data.append(",");
			data.append(now_year-3); data.append(",");
			data.append(now_year-2); data.append(","); 
			data.append(now_year-1); data.append(",");
			data.append(now_year); data.append(",");
			data.append("구분"); data.append(",");
			data.append(now_year-3); data.append(",");
			data.append(now_year-2); data.append(","); 
			data.append(now_year-1); data.append(",");
			data.append(now_year);
			
			bw.write(data.toString());
			bw.newLine();
			
		} catch(IOException e) {
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			IPNum(time_form);
			
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

	// 연구소에 해당하는 컬럼을 생성하는 메서드
	public static void research(String time_form) {
		
		BufferedWriter bw = null;
		StringBuilder data = new StringBuilder();
		
		try {
			bw = new BufferedWriter(new FileWriter("./result/"+ time_form.substring(4,8) + "/files/"
					+"research_info_" + time_form +".csv", true));
			data.append(""); data.append(",");
			data.append("기업전담부서"); data.append(","); data.append(","); data.append(","); data.append(",");
			data.append("기업부설연구소"); 
			
			bw.write(data.toString());
			bw.newLine();
			data.setLength(0);
			
			data.append("조회값"); data.append(","); data.append("인정번호"); data.append(","); data.append("전담부서명"); data.append(",");
			data.append("연구분야"); data.append(","); data.append("인정일자"); data.append(","); data.append("인정번호"); data.append(",");
			data.append("연구소명"); data.append(","); data.append("연구분야"); data.append(","); data.append("인정일자"); 
			
			bw.write(data.toString());
			bw.newLine();
			
		} catch(IOException e) {
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			research(time_form);
			
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
	
	// TCB에 해당하는 컬럼을 생성하는 메서드
	public static void TCB(String time_form) {
		
		BufferedWriter bw = null;
		StringBuilder data = new StringBuilder();
		
		try {
			bw = new BufferedWriter(new FileWriter("./result/"+ time_form.substring(4,8) + "/files/"
					+"TCB_info_" + time_form +".csv", true));
			data.append("조회값"); data.append(",");
			data.append("평가일자"); data.append(",");
			data.append("평가기관"); data.append(",");
			data.append("의뢰기관"); data.append(",");
			data.append("기술등급(T1 ~ T6 해당여부)"); 
			
			bw.write(data.toString());
			bw.newLine();
			
		} catch(IOException e) {
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			TCB(time_form);
			
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

	// 생성된 파일들을 묶어 하나의 엑셀 파일로 만드는 메서드
	// CSV 파일에선 쉼표가 포함된 하나의 객체를 큰따옴표를 통해 묶도록 하지만 POI를 통해 엑셀로 옮기는 과정에선 이를 인식하지 못하고 있기에 수동적인 방법을 사용하고 있으므로 문제 발생시 수정 필요
	public static void merge(String time_form) {
		String thisline; 
	    int rowCounter = 0;
	    int length; 
		int start = 0;
		int end = 0 ;
	    List<String> rowList = new ArrayList<String>();
	    try { 

	        File folder = new File("./result/"+ time_form.substring(4,8) + "/files/");
	        
	        // 파일의 이름에 적힌 시간을 기준으로 묶어야하는 csv 파일을 구분함
	        File[] listOfFiles = folder.listFiles((FilenameFilter) new FilenameFilter() { 
                
                @Override 
                public boolean accept(File dir, String name) { 
                     return name.endsWith(time_form + ".csv"); 
                }
                
	        });
	        
	        HSSFWorkbook workbook = new HSSFWorkbook();
	        for (File file : listOfFiles) { 
	            if (file.isFile()) { 
	                
	                rowCounter = 0;   
	                rowList = new ArrayList<String>();
	                HSSFSheet sheet = workbook.createSheet(file.getName());
	                FileInputStream fis = new FileInputStream(file);
	                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	                
	                while ((thisline = br.readLine()) != null) {
	                    rowList.add(thisline);
	                }
	                
	                for (String rowLine: rowList) {
	                    HSSFRow row = sheet.createRow(rowCounter);
	                    rowCounter++;
	                    //String rowContentArr[] = rowLine.split(",");
	                    ArrayList rowContentArr = new ArrayList();
	                    length = rowLine.length();
	                    
	                    while(rowLine.substring(start, length).indexOf(",") != -1) {
	            			
	            			end = start + rowLine.substring(start, length).indexOf(",");
	            			
	            			if(rowLine.substring(start, end).contains("\"")) {
	            				end = start + rowLine.substring(start+1, length).indexOf("\"")+2;
	            				rowContentArr.add(rowLine.substring(start+1,end-1));
	            				start = end + 1;
	            				if(start > length) {
	            					break;
	            				}
	            			}
	            			else {
	            				rowContentArr.add(rowLine.substring(start,end));
	            				start = end + 1;
	            				if(start > length) {
	            					break;
	            				}
	            			}
	            			
	            		}
	            		
	            		if(start <= length) {
	            			rowContentArr.add(rowLine.substring(end+1,length));
	            		}
	                    
	                    for (int p = 0; p < rowContentArr.size(); p++) {
	                        @SuppressWarnings("deprecation")
	                        HSSFCell cell = row.createCell((short) p);
	                        cell.setCellValue(rowContentArr.get(p).toString());
	                    }
	                    
	                    start = 0;
	                    end = 0;
	                } 
	                fis.close();
	                FileOutputStream fileOut = new FileOutputStream("./result/"+ time_form.substring(4,8) + "/"
	            			+"TDBScraping_" + time_form + ".xls");
	                workbook.write(fileOut);
	                fileOut.flush();
	                fileOut.close();
	                br.close();
	            }
	        }

	        System.out.println("완료 ");
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	        
	        merge(time_form);
	        
	    }
	}
}
