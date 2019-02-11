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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import rmi.zip.service.IFxBoardService;
import rmi.zip.util.FxAlert;
import rmi.zip.vo.FxBoardVO;

public class BoardViewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private BorderPane viewPane;

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
    private Button btnDel;

    @FXML
    private Button btnList;
    
    private BoardListController listController;
    private FxBoardVO fxBoardVo;
    private IFxBoardService service;

	public void setListController(BoardListController listController) {
		this.listController = listController;
	}
	
	// FxBoardVO객체를 매개변수로 받아서 현재 객체의 fxBoardVo변수에 저장하고 
	// 화면에 출력하는 메서드
	public void setFxBoardVo(FxBoardVO fxBoardVo) {
		this.fxBoardVo = fxBoardVo;

		lblWriter.setText(fxBoardVo.getBoard_writer());
		lblDate.setText(fxBoardVo.getBoard_date());
		tfTitle.setText(fxBoardVo.getBoard_title());
		taContent.setText(fxBoardVo.getBoard_content());
	}

	// '삭제'버튼을 클릭했을 때 처리
	@FXML
    void btnDelClicked(ActionEvent event) throws RemoteException {
		// 정말로 삭제할지 여부를 다시한번 확인하기
		ButtonType btnType = FxAlert.confirm("삭제작업",
				fxBoardVo.getBoard_no() + "번 게시글을 정말로 삭제하시겠습니까?");
    	if(btnType != ButtonType.OK){
    		return;
    	}
    	
		// fxBoardVo개체에서 게시글 번호를 이용하여 DB에서 해당 게시글 삭제
		int cnt = service.deleteBoard(fxBoardVo.getBoard_no());
		
		if(cnt>0) {
			// 게시글 삭제에 성공하면 BoardListController의 setBoardList()메서드를 호출해서
    		// 전체 데이터를 다시 가져오고, setPagination()메서드를 호출해서
    		// 페이징 처리를 다시 한 후 'List화면'으로 되돌아 간다.
			FxAlert.info("삭제작업", fxBoardVo.getBoard_no() + "번글을 삭제했습니다.");
			listController.setBoardList();
    		listController.setPagination();
			StackPane root = (StackPane) btnDel.getScene().getRoot();
	    	root.getChildren().remove(viewPane);
		}
    }

	// '수정'버튼을 클릭했을 때 처리
    @FXML
    void btnEditClicked(ActionEvent event) {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/BoardEdit.fxml"));
    		Parent editPane = loader.load();
    		
    		BoardEditController editController = loader.getController();
    		
    		// BoardEditController에 BoardListController객체와 FxBoardVO객체 전달
    		editController.setListController(listController);
    		editController.setFxBoardVo(fxBoardVo);
    		
    		// 현재화면(View화면)은 삭제하고 수정화면으로 전환한다.
    		StackPane root = (StackPane) btnEdit.getScene().getRoot();
    		root.getChildren().remove(viewPane);
    		root.getChildren().add(editPane);
    		
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    // '리스트보기'버튼을 클릭했을 때 처리
    @FXML
    void btnListClicked(ActionEvent event) throws RemoteException {
    	listController.setBoardList();
		listController.setPagination();
    	StackPane root = (StackPane) btnList.getScene().getRoot();
    	root.getChildren().remove(viewPane);
    }

    @FXML
    void initialize() {
        assert viewPane != null : "fx:id=\"viewPane\" was not injected: check your FXML file 'BoardView.fxml'.";
        assert lblWriter != null : "fx:id=\"lblWriter\" was not injected: check your FXML file 'BoardView.fxml'.";
        assert lblDate != null : "fx:id=\"lblDate\" was not injected: check your FXML file 'BoardView.fxml'.";
        assert tfTitle != null : "fx:id=\"tfTitle\" was not injected: check your FXML file 'BoardView.fxml'.";
        assert taContent != null : "fx:id=\"taContant\" was not injected: check your FXML file 'BoardView.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'BoardView.fxml'.";
        assert btnDel != null : "fx:id=\"btnDel\" was not injected: check your FXML file 'BoardView.fxml'.";
        assert btnList != null : "fx:id=\"btnList\" was not injected: check your FXML file 'BoardView.fxml'.";
        
        // Service 객체 생성


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
