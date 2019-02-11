package rmi.zip.client;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import rmi.zip.service.IFxBoardService;
import rmi.zip.util.FxAlert;
import rmi.zip.vo.FxBoardVO;

public class BoardEditController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private BorderPane editPane;
    
    @FXML
    private Label lblWriter;

    @FXML
    private Label lblDate;
    
    @FXML
    private TextField tfTitle;

    @FXML
    private TextArea taContent;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnCancel;

    private BoardListController listController;
    private FxBoardVO fxBoardVo;
    private IFxBoardService service;

	public void setListController(BoardListController listController) {
		this.listController = listController;
	}
	
	public void setFxBoardVo(FxBoardVO fxBoardVo) {
		this.fxBoardVo = fxBoardVo;
		
		lblWriter.setText(fxBoardVo.getBoard_writer());
		lblDate.setText(fxBoardVo.getBoard_date());
		tfTitle.setText(fxBoardVo.getBoard_title());
		taContent.setText(fxBoardVo.getBoard_content());
	}
	
	// '취소'버튼을 클릭했을 때 처리
    @FXML
    void btnCancelClicked(ActionEvent event) {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/BoardView.fxml"));
    		Parent viewPane = loader.load();
    		
    		BoardViewController viewController = loader.getController();
    		
    		// BoardViewController객체에 BoardListController객체와 FxBoardVO객체 전달
    		viewController.setListController(listController);
    		viewController.setFxBoardVo(fxBoardVo);
    		
    		// 현재화면(Edit화면)을 삭제한 후 View화면으로 전환한다.
    		StackPane root = (StackPane) btnEdit.getScene().getRoot();
    		root.getChildren().remove(editPane);
    		root.getChildren().add(viewPane);
    		
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    // '수정'버튼을 클릭했을 때 처리
    @FXML
    void btnEditClicked(ActionEvent event) throws RemoteException {
    	String title = tfTitle.getText().trim();
    	String content = taContent.getText().trim();
    	if(title.isEmpty()) {
    		FxAlert.alert("수정작업", "수정할 게시판 제목을 입력하세요.");
    		tfTitle.requestFocus();
    		return;
    	}
    	
    	if(content.isEmpty()) {
    		FxAlert.alert("수정작업", "수정할 게시판 내용을 입력하세요.");
    		taContent.requestFocus();
    		return;
    	}
    	
    	// 새로 입력한 내용을 FxBoardVO객체에 저장한 후 
    	// DB에 Update작업을 진행한다.
    	fxBoardVo.setBoard_title(title);
    	fxBoardVo.setBoard_content(content);;
    	
    	int cnt = service.updateBoard(fxBoardVo);
    	
    	if(cnt>0) {
    		// Update작업이 성공하면 BoardListController의 setBoardList()메서드를 호출해서
    		// 전체 데이터를 다시 가져오고, setPagination()메서드를 호출해서
    		// 페이징 처리를 다시 한 후 'List화면'으로 되돌아 간다.
    		FxAlert.info("수정작업", fxBoardVo.getBoard_no() + "번글을 수정했습니다.");
    		listController.setBoardList();
    		listController.setPagination();
    		StackPane root = (StackPane) btnCancel.getScene().getRoot();
        	root.getChildren().remove(editPane);
    	}
    }

    @FXML
    void initialize() {
        assert lblWriter != null : "fx:id=\"lblWriter\" was not injected: check your FXML file 'BoardEdit.fxml'.";
        assert lblDate != null : "fx:id=\"lblDate\" was not injected: check your FXML file 'BoardEdit.fxml'.";
        assert taContent != null : "fx:id=\"taContent\" was not injected: check your FXML file 'BoardEdit.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'BoardEdit.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'BoardEdit.fxml'.";
        
        // Service객체 생성
        
        try {
        	Registry reg = LocateRegistry.getRegistry("localhost",9988);
        	
        	service = (IFxBoardService) reg.lookup("fxBoard");
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
    }
}
