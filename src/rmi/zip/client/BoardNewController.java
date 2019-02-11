package rmi.zip.client;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import rmi.zip.service.IFxBoardService;
import rmi.zip.util.FxAlert;
import rmi.zip.vo.FxBoardVO;

public class BoardNewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private BorderPane newPane;

    @FXML
    private TextField tfWriter;

    @FXML
    private TextField tfTitle;

    @FXML
    private TextArea taContent;

    @FXML
    private Button btnOk;

    @FXML
    private Button btnCancel;
    
    private BoardListController listController;
    private IFxBoardService service;


	public void setListController(BoardListController listController) {
		this.listController = listController;
	}

	// '취소'버튼을 클릭했을 때 처리
	@FXML
    void btnCancelClicked(ActionEvent event) {
    	StackPane root = (StackPane) btnCancel.getScene().getRoot();
    	root.getChildren().remove(newPane);
    }

	// '저장'버튼을 클릭했을 때 처리
    @FXML
    void btnOkClicked(ActionEvent event) throws RemoteException {
    	String writer = tfWriter.getText().trim();
    	if(writer.isEmpty()) {
    		FxAlert.alert("새글작업", "작성자를 입력하세요.");
    		tfWriter.requestFocus();
    		return;
    	}
    	String title = tfTitle.getText().trim();
    	if(title.isEmpty()) {
    		FxAlert.alert("새글작업", "제목을 입력하세요.");
    		tfTitle.requestFocus();
    		return;
    	}
    	String content = taContent.getText().trim();
    	if(content.isEmpty()) {
    		FxAlert.alert("새글작업", "제목을 입력하세요.");
    		taContent.requestFocus();
    		return;
    	}
    	
    	// 입력한 자료를 FxBoardVO객체에 저장 후 Insert작업 진행
    	FxBoardVO fxBoardVo = new FxBoardVO();
    	fxBoardVo.setBoard_writer(writer);
    	fxBoardVo.setBoard_title(title);    	fxBoardVo.setBoard_content(content);
    	
    	int cnt = service.insertBoard(fxBoardVo);
    	
    	if(cnt>0) {
    		// Insert작업이 성공되면 BoardListController의 setBoardList()메서드를 호출해서
    		// 전체 데이터를 다시 가져오고, setPagination()메서드를 호출해서
    		// 페이징 처리를 다시 한 후 'List화면'으로 되돌아 간다.
    		FxAlert.info("새글작성", "새글작성을 성공했습니다.");
    		listController.setBoardList();
    		listController.setPagination();
    		StackPane root = (StackPane) btnCancel.getScene().getRoot();
        	root.getChildren().remove(newPane);
    	}
    	
    }

    @FXML
    void initialize() {
        assert newPane != null : "fx:id=\"newPane\" was not injected: check your FXML file 'BoardNew.fxml'.";
        assert tfWriter != null : "fx:id=\"tfWriter\" was not injected: check your FXML file 'BoardNew.fxml'.";
        assert tfTitle != null : "fx:id=\"tfTitle\" was not injected: check your FXML file 'BoardNew.fxml'.";
        assert taContent != null : "fx:id=\"taContent\" was not injected: check your FXML file 'BoardNew.fxml'.";
        assert btnOk != null : "fx:id=\"btnOk\" was not injected: check your FXML file 'BoardNew.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'BoardNew.fxml'.";
        
        // Service객체 생성
        try {
			Registry reg = LocateRegistry.getRegistry("localhost",9988);
			service = (IFxBoardService) reg.lookup("fxBoard");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
}
