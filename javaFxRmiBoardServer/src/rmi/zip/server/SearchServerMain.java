package rmi.zip.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import rmi.zip.service.FxBoardServiceImpl;
import rmi.zip.service.IFxBoardService;

public class SearchServerMain {
	public static void main(String[] args) {
		try {
			IFxBoardService fxBoard = new FxBoardServiceImpl();
			Registry reg = LocateRegistry.createRegistry(9988);
			reg.rebind("fxBoard", fxBoard);
			
			System.out.println("서버준비 완료...");
			
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
