package rmi.zip.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ibatis.sqlmap.client.SqlMapClient;

import rmi.zip.util.BuildedSqlMapClient;
import rmi.zip.vo.FxBoardVO;



public class FxBoardDaoImpl implements IFxBoardDao {
	private static FxBoardDaoImpl dao;
	private SqlMapClient smc;
	
	private FxBoardDaoImpl() {
		smc = BuildedSqlMapClient.getSqlMapClient();
	}
	
	public static FxBoardDaoImpl getInstance() {
		if(dao==null) {
			dao = new FxBoardDaoImpl();
		}
		return dao;
	}

	

	@Override
	public int insertBoard(FxBoardVO fxBoardVo) {
		int cnt = 0;
		try {
			Object obj = smc.insert("fxBoard.insertBoard", fxBoardVo);
			if(obj==null) {
				cnt = 1;
			}
		} catch (SQLException e) {
			cnt = 0;
			e.printStackTrace();
		} 
		return cnt;
	}

	@Override
	public int deleteBoard(int boardNo) {
		int cnt = 0;
		try {
			cnt = smc.delete("fxBoard.deleteBoard", boardNo);
			
		} catch (SQLException e) {
			cnt = 0;
			e.printStackTrace();
		} 
		return cnt;
	}

	@Override
	public int updateBoard(FxBoardVO fxBoardVo) {
		int cnt = 0;
		try {
			cnt = smc.update("fxBoard.updateBoard", fxBoardVo);
			
		} catch (SQLException e) {
			cnt = 0;
			e.printStackTrace();
		} 
		return cnt;
	}

	@Override
	public FxBoardVO getBoard(int boardNo) {
		FxBoardVO fxBoardVo = null;
		
		// 조회수를 1 증가시킨다.
		if(setCountIncrement(boardNo)==0) {
			return null;
		}
		
		try {
			fxBoardVo = (FxBoardVO) smc.queryForObject("fxBoard.getBoard", boardNo);
			
		} catch (SQLException e) {
			fxBoardVo = null;
			e.printStackTrace();
		} 
		
		return fxBoardVo;
	}

	@Override
	public List<FxBoardVO> getAllBoardList() {
		List<FxBoardVO> boardList = null;
		try {
			boardList = smc.queryForList("fxBoard.getAllBoardList");
			
		} catch (SQLException e) {
			boardList = null;
			e.printStackTrace();
		}
		
		return boardList;
	}

	@Override
	public List<FxBoardVO> getSearchBoardList(Map<String, String> searchMap) {
		List<FxBoardVO> boardList = null;
		try {
			boardList = smc.queryForList("fxBoard.getSearchBoardList", searchMap);
			
		} catch (SQLException e) {
			boardList = null;
			e.printStackTrace();
		} 
		return boardList;
	}

	@Override
	public int setCountIncrement(int boardNo) {
		int cnt = 0;
		try {
			cnt = smc.update("fxBoard.setCountIncrement", boardNo);
			
		} catch (SQLException e) {
			cnt = 0;
			e.printStackTrace();
		} 
		return cnt;
	}

}
