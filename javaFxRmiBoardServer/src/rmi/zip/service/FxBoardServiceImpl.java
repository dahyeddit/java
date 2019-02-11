package rmi.zip.service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

import rmi.zip.dao.FxBoardDaoImpl;
import rmi.zip.dao.IFxBoardDao;
import rmi.zip.vo.FxBoardVO;

public class FxBoardServiceImpl extends UnicastRemoteObject implements IFxBoardService {
	//------------------------------------------------------
	private IFxBoardDao dao;

	public FxBoardServiceImpl() throws RemoteException{
		dao = FxBoardDaoImpl.getInstance();
	}

	//-----------------------------------------------------

	@Override
	public int insertBoard(FxBoardVO fxBoardVo) throws RemoteException{
		return dao.insertBoard(fxBoardVo);
	}

	@Override
	public int deleteBoard(int boardNo) throws RemoteException{
		return dao.deleteBoard(boardNo);
	}

	@Override
	public int updateBoard(FxBoardVO fxBoardVo) throws RemoteException {
		return dao.updateBoard(fxBoardVo);
	}

	@Override
	public FxBoardVO getBoard(int boardNo) throws RemoteException {
		return dao.getBoard(boardNo);
	}

	@Override
	public List<FxBoardVO> getAllBoardList() throws RemoteException {
		return dao.getAllBoardList();
	}

	@Override
	public List<FxBoardVO> getSearchBoardList(Map<String, String> searchMap) throws RemoteException {
		return dao.getSearchBoardList(searchMap);
	}

	@Override
	public int setCountIncrement(int boardNo) throws RemoteException {
		return dao.setCountIncrement(boardNo);
	}
}
