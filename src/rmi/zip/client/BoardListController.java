package rmi.zip.client;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import rmi.zip.service.IFxBoardService;
import rmi.zip.util.FxAlert;
import rmi.zip.vo.FxBoardVO;

public class BoardListController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnNew;

    @FXML
    private ComboBox<String> cmbSearchItem;

    @FXML
    private TextField tfSearchWord;

    @FXML
    private Button btnSearch;

    @FXML
    private TableView<FxBoardVO> table;
    
    @FXML
    private TableColumn<?, ?> noCol;

    @FXML
    private TableColumn<?, ?> titleCol;

    @FXML
    private TableColumn<?, ?> writerCol;

    @FXML
    private TableColumn<?, ?> dateCol;

    @FXML
    private TableColumn<?, ?> cntCol;

    @FXML
    private Pagination pagination;

    // '새글쓰기'버튼을 클릭했을 때 처리
    @FXML
    void btnNewClicked(ActionEvent event) {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/BoardNew.fxml"));
    		Parent newPane = loader.load();
    		
    		BoardNewController newController = loader.getController();
    		
    		// BoardNewController에 현재객체(BoardListController객체) 전달
    		newController.setListController(this);
    		
    		StackPane root = (StackPane) btnNew.getScene().getRoot();
    		root.getChildren().add(newPane);
    		
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    // '검색'버튼을 클릭했을 때 처리
    @FXML
    void btnSearchClicked(ActionEvent event) throws RemoteException {
    	String search_field = cmbSearchItem.getValue();
    	if(search_field == null) {
    		FxAlert.alert("검색작업", "검색항목을 선택하세요");
    		return;
    	}
    	
    	// 검색 항목에 맞는 DB의 컬럼 설정
    	switch(search_field) {
    		case "작성자" : search_field = "board_writer"; break;
    		case "제목" : search_field = "board_title"; break;
    		case "내용" : search_field = "board_content"; break;
    	}
    	
    	String search_word = tfSearchWord.getText();
    	
    	// '검색 항목'과 '검색 단어'를 Map에 저장 후 검색 결과 가져오기
    	HashMap<String, String> searchMap = new HashMap<>();
    	searchMap.put("search_field", search_field);
    	searchMap.put("search_word", search_word);
    	
    	boardList = service.getSearchBoardList(searchMap);
    	
    	setPagination();  // 검색된 자료를 이용하여 Pagination객체 다시 설정하기
    	
    }

    // TableView의 게시글을 선택했을 때 처리 ==> 게시글 보기 화면으로 전환
    @FXML
    void tableClicked(MouseEvent event) throws RemoteException {
    	if(table.getSelectionModel().isEmpty()) {
    		FxAlert.alert("내용보기", "내용를 볼 게시글을 선택한 후 사용하세요.");
    		return;
    	}
    	
    	FxBoardVO fxBoardVo = table.getSelectionModel().getSelectedItem();
    	
		// 게시글 카운트 증가
		service.setCountIncrement(fxBoardVo.getBoard_no());
		
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/BoardView.fxml"));
    		Parent viewPane = loader.load();
    		
    		BoardViewController viewController = loader.getController();
    		
    		// BoardViewController에 BoardListController객체와 선택한 게시글정보(FxBoardVO객체) 전달
    		viewController.setListController(this);
    		viewController.setFxBoardVo(fxBoardVo);
    		
    		StackPane root = (StackPane) btnNew.getScene().getRoot();
    		root.getChildren().add(viewPane);
    		
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    private IFxBoardService service;
    private List<FxBoardVO> boardList;  // 전체 게시글 List가 저장될 변수
    private int rowsPerPage = 15;	// 한 화면에 보여줄 레코드 수
    
    @FXML
    void initialize() throws RemoteException {
        assert btnNew != null : "fx:id=\"btnNew\" was not injected: check your FXML file 'BoardList.fxml'.";
        assert cmbSearchItem != null : "fx:id=\"cmbSearchItem\" was not injected: check your FXML file 'BoardList.fxml'.";
        assert tfSearchWord != null : "fx:id=\"tfSearchWord\" was not injected: check your FXML file 'BoardList.fxml'.";
        assert btnSearch != null : "fx:id=\"btnSearch\" was not injected: check your FXML file 'BoardList.fxml'.";
        assert table != null : "fx:id=\"table\" was not injected: check your FXML file 'BoardList.fxml'.";
        assert pagination != null : "fx:id=\"pagination\" was not injected: check your FXML file 'BoardList.fxml'.";
        
        // Service 객체 생성
        
        try {
			Registry reg
				= LocateRegistry.getRegistry("localhost",9988);
			service = (IFxBoardService) reg.lookup("fxBoard");
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
        
        // TableView의 각 컬럼 설정
        noCol.setCellValueFactory(new PropertyValueFactory<>("board_no"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("board_title"));
        writerCol.setCellValueFactory(new PropertyValueFactory<>("board_writer"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("board_date"));
        cntCol.setCellValueFactory(new PropertyValueFactory<>("board_cnt"));
        
        // 검색용 ComboBox 설정
        cmbSearchItem.getItems().addAll("작성자", "제목", "내용");
        
        // Pagination의 페이지 번호를 변경했을 때 이벤트 처리
        pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        		changeTableView(newValue.intValue());
        	}
		});
        
        
        setBoardList(); // 전체 게시글 리스트 가져오기
        setPagination(); // 페이징 처리 및 TableView에 데이터 출력하기
        

    }
    
    // Pagination에서 선택한 페이지의 index번째에 맞는 데이터 가져와
 	// TableView에 다시 셋팅하기
 	public void changeTableView(int index) {
 		int fromIndex = index * rowsPerPage;   // 시작위치
 		int toIndex = Math.min(fromIndex + rowsPerPage, boardList.size()); // 종료위치
 		
 		// 시작위치부터 종료위치 이전까지의 데이터 가져와 TableView에 셋팅
 		table.setItems(FXCollections.observableArrayList(boardList.subList(fromIndex, toIndex)));
 	}
    
 	// Pagination을 초기화하는 메서드
 	public void setPagination() {
 		// 전체 페이지 수
         pagination.setPageCount((int)Math.ceil((double)boardList.size()/rowsPerPage));
         // 현재 선택한 페이지의 index값 설정(1페이지로 설정)
         pagination.setCurrentPageIndex(0);
         // 선택한 현재 페이지에 맞는 데이터를 TableView에 셋팅한다.
         changeTableView(pagination.getCurrentPageIndex());
 	}
 	
 	// DB에서 게시글의 전체 데이터를 다시 가져와 List에 저장하는 메서드
 	public void setBoardList() throws RemoteException {
    	boardList = service.getAllBoardList();
    }
}
