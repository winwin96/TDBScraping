package scraping;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class NoData {

	public static void main(String busi_list, String time_form) {
		
		noCommon(busi_list, time_form);
		noIP(busi_list, time_form);
		noIPNum(busi_list, time_form);
		//noConfirm(busi_list);
		//noResearch(busi_list);
		//noTCB(busi_list);
		
	}
	
	// 일반 정보 파일에 검색한 사업자 등록 번호에 해당하는 정보가 없다는 것을 기록
	public static void noCommon(String busi_list, String time_form) {
		StringBuilder data = new StringBuilder();
		data.append(busi_list); 
		BufferedWriter bw = null;
		
		try {
			bw= new BufferedWriter(new FileWriter("./result/"+ time_form.substring(4,8) + "/files/"
					+"common_info_" + time_form +".csv", true));
			
			data.append(","); data.append("조회된 자료가 없습니다.");
			bw.write(data.toString());
			bw.newLine();
			data.setLength(0);
		} catch (IOException e) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			noCommon(busi_list, time_form);
		} finally {
			if (bw != null){
				try {
					bw.flush();
					bw.close();
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	// 지식재산권 파일에 검색한 사업자 등록 번호에 해당하는 정보가 없다는 것을 기록
	public static void noIP(String busi_list, String time_form ) {
		StringBuilder data = new StringBuilder();
		data.append(busi_list); 
		BufferedWriter bw = null;
		
		try {
			bw= new BufferedWriter(new FileWriter("./result/"+ time_form.substring(4,8) + "/files/"
					+"IP_info_" + time_form +".csv", true));
			
			data.append(","); data.append("조회된 자료가 없습니다.");
			bw.write(data.toString());
			bw.newLine();
			data.setLength(0);
		} catch (IOException e) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			noIP(busi_list,time_form);
		} finally {
			if (bw != null){
				try {
					bw.flush();
					bw.close();
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}

	// 지식재산권 수 파일에 검색한 사업자 등록 번호에 해당하는 정보가 없다는 것을 기록
	public static void noIPNum(String busi_list, String time_form) {
		StringBuilder data = new StringBuilder();
		data.append(busi_list); 
		BufferedWriter bw = null;
		try {
			bw= new BufferedWriter(new FileWriter("./result/"+ time_form.substring(4,8) + "/files/"
					+"IPNum_info_" + time_form +".csv", true));
			
			data.append(","); data.append("조회된 자료가 없습니다.");
			
			bw.write(data.toString());
			bw.newLine();
			data.setLength(0);
		} catch (IOException e) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			noIPNum(busi_list, time_form);
		} finally {
			if (bw != null){
				try {
					bw.flush();
					bw.close();
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	
}
