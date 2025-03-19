package edu.kh.jdbc.service;

import java.sql.Connection;
import java.util.List;

import edu.kh.jdbc.common.JDBCTemplate;
import edu.kh.jdbc.dao.UserDAO;
import edu.kh.jdbc.dto.User;

// (Model 중 하나)Service : 비스니스 로직을 처리하는 계층,
// 데이터를 가공하고 트랜잭션(commit, rollback) 관리 수행
public class UserService {
	
	// 필드
	private UserDAO dao = new UserDAO();

	// 메서드
	/** 전달 받은 아이디와 일치하는 User 정보 반환 서비스
	 * @param input (입력된 아이디)
	 * @return 아이디가 일치하는 회원 정보가 담긴 User객체,
	 * 		   없다면 null 반환
	 */
	public User selectId(String input) {
		
		// 1. 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 2. 데이터 가공(할게 없으면 넘어감)
				
		// 3. DAO 메서드 호출 결과 반환
		User user = dao.selectId(conn, input); 
		
		// 4. DML(commit/rollback)
		
		// 5. 다쓴 커넥션 자원 반환
		JDBCTemplate.close(conn);
		
		// 6. 결과를 view 리턴
		
		
		return user;
	}

	/** 1. User 등록 서비스
	 * @param user : 입력 받은 id,pw,name이 세팅된 객체
	 * @return 결과 행의 갯수
	 * @throws Exception 
	 */
	public int insertUser(User user) throws Exception {
		// 1. 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 2. 데이터 가공 (할게 없으면 넘어감)
		
		// 3. DAO 메서드 호출 후 결과 반환받기
		int result = dao.insertUser(conn, user); 
		
		// 4. DML(INSERT) 수행 결과에 따라 트랜잭션 제어처리
		if(result > 0) { // INSERT 성공
			JDBCTemplate.commit(conn);
		}else { // INSERT 실패
			JDBCTemplate.rollback(conn);
		}
		
		// 5. Connection 반환하기
		JDBCTemplate.close(conn);
		
		// 6. 결과 반환
		return result;
	}

	/** 2. User 전체 조회 서비스
	 * @return : 조회된 User들이 담긴 List
	 * @throws Exception 
	 */
	public List<User> selectAll() throws Exception {
		// 1. 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 2. 데이터 가공
		// 3. DAO 메서드 호출(SELECT) 후 결과 반환(List<User>) 받기
		List<User> userList = dao.selectAll(conn); 
		
		// 4. Connection  반환
		JDBCTemplate.close(conn);
		
		// 5. 결과 반환		
		return userList;
	}

	/** 3. User 중 이름에 검색어가 포함된 회원 조회 서비스
	 * @param keyword : 입력한 키워드
	 * @return searchList : 조회된 회원 리스트
	 */
	public List<User> selectName(String keyword) throws Exception {
		// 1. 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		// 2. 데이터 가공
		
		// 3. DAO 메서드 호출(SELECT) 후 결과 반환(List<User>) 받기
		List<User> searchList = dao.selectName(conn, keyword); 
		
		JDBCTemplate.close(conn);
		
		return searchList;
	}

	/** 4. USER_NO를 입력받아 일치하는 User 조회 서비스
	 * @param input
	 * @return user (조회된 회원 정보 또는 null)
	 * @throws Exception
	 */
	public User selectUser(int input) throws Exception {
		
		Connection conn = JDBCTemplate.getConnection();
		
		User user = dao.selectUser(conn, input);
		
		JDBCTemplate.close(conn);
		
		
		return user;
	}
	

}
