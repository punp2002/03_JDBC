package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class JDBCExample7 {

	public static void main(String[] args) {
		// EMPLOYEE	테이블에서
		// 사번, 이름, 성별, 급여, 직급명, 부서명을 조회
		// 단, 입력 받은 조건에 맞는 결과만 조회하고 정렬할 것
				
		// - 조건 1 : 성별 (M, F)
		// - 조건 2 : 급여 범위
		// - 조건 3 : 급여 오름차순/내림차순
				
		// [실행화면]
		// 조회할 성별(M/F) : F
		// 급여 범위(최소, 최대 순서로 작성) :
		// 3000000
		// 4000000
		// 급여 정렬(1.ASC, 2.DESC) : 2
				
		// 사번 | 이름   | 성별 | 급여    | 직급명 | 부서명
		//--------------------------------------------------------
		// 218  | 이오리 | F    | 3890000 | 사원   | 없음
		// 203  | 송은희 | F    | 3800000 | 차장   | 해외영업2부
		// 212  | 장쯔위 | F    | 3550000 | 대리   | 기술지원부
		// 222  | 이태림 | F    | 3436240 | 대리   | 기술지원부
		// 207  | 하이유 | F    | 3200000 | 과장   | 해외영업1부
		// 210  | 윤은해 | F    | 3000000 | 사원   | 해외영업1부
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		Scanner sc = null;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			String url = "jdbc:oracle:thin:@localhost:1521:XE"; 
			String userName = "kh";		
			String password = "kh1234"; 
			
			conn = DriverManager.getConnection(url, userName, password);
			
			sc = new Scanner(System.in);
			// EMPLOYEE	테이블에서
			// 사번, 이름, 성별, 급여, 직급명, 부서명을 조회
			// 단, 입력 받은 조건에 맞는 결과만 조회하고 정렬할 것
			
            System.out.print("성별 (M 또는 F) 입력: ");
            String gender = sc.next().toUpperCase();
            
            System.out.print("급여 범위(최소, 최대 순서로 작성) : ");
            int min = sc.nextInt();
            int max = sc.nextInt();

            System.out.println("급여 정렬 (1: 오름차순, 2: 내림차순) 입력: ");
            int sort = sc.nextInt();
						
			String sql = """
					SELECT EMP_ID, EMP_NAME, DECODE(SUBSTR(E.EMP_NO, 8, 1), '1', 'M', '2', 'F') GENDER, 
					SALARY, JOB_NAME, NVL(DEPT_TITLE, 'DJQTDMA') DEPT_TITLE
					FROM EMPLOYEE E
					JOIN JOB J ON (E.JOB_CODE = J.JOB_CODE)
					LEFT JOIN DEPARTMENT D ON (DEPT_CODE = DEPT_ID)
					WHERE DECODE(SUBSTR(EMP_NO, 8, 1), '1', 'M', '2', 'F') = ? 
					AND SALARY BETWEEN ? AND ?
					ORDER BY SALARY
					"""; 
			
			// 급여의 오름차순인지 내림차순인지 조건에 따라 SQL 보안하기
			if(sort == 1) sql += " ASC";
			else		  sql += " DESC";
			
			 pstmt = conn.prepareStatement(sql);
			 
			 // 5. ? (위치홀더)에 알맞은 값 세팅
	         pstmt.setString(1, gender);   // 성별
	         pstmt.setInt(2, min);   // 최소 급여
	         pstmt.setInt(3, max);   // 최대 급여
	         
	         
	         rs = pstmt.executeQuery();
	         
	         System.out.println("사번 | 이름   | 성별 | 급여   | 직급명 | 부서명");
	         System.out.println("----------------------------------------------------");
	         
	         boolean flag = true; // true : 조회결과가 없음, false :  조회결과 존재함
	         
	         while(rs.next()) {
	        	 flag = false; // while 문이 1회 이상 반복함 == 조회결과가 1행이라도 있다.
	        	 
	        	 String empId = rs.getString("EMP_ID");
	        	 String empName = rs.getString("EMP_NAME");
	        	 String gen = rs.getString("GENDER");
	        	 int salary = rs.getInt("SALARY");
	        	 String jobName = rs.getString("JOB_NAME");
	        	 String deptTitle = rs.getString("DEPT_TITLE");
	        	 
	        	 System.out.printf("%-4s | %3s | %-4s | %7d | %-3s  | %s \n",
							empId, empName, gen, salary, jobName, deptTitle);
	         }
	         if(flag) { // flag == true 인 경우 -> while  문 안쪽 수행 x -> 조회결과가 1행도 없음
	        	 System.out.println("조회 결과 없음");
	         }

			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				
				if(conn != null) conn.close();
				if(sc != null) sc.close();
				
			} catch (Exception e) {
			e.printStackTrace();
			}
		}
		
	}

}
