package rmi.zip.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class FxAlert {
    // alert창
    public static void alert(String header, String msg){
    	Alert alertWarning = new Alert(AlertType.WARNING);
    	alertWarning.setTitle("WARNING");
    	alertWarning.setHeaderText(header);
    	alertWarning.setContentText(msg);
    	alertWarning.showAndWait();
    }
    
    // 안내 창
    public static void info(String header, String msg){
    	Alert alertinfo = new Alert(AlertType.INFORMATION);
    	alertinfo.setTitle("INFORMATION");
    	alertinfo.setHeaderText(header);
    	alertinfo.setContentText(msg);
    	alertinfo.showAndWait();
    }
    
    // confirm창
    public static ButtonType confirm(String header, String msg){
    	Alert alertConfirm = new Alert(AlertType.CONFIRMATION);
    	alertConfirm.setTitle("CONFIRMATION");
    	alertConfirm.setHeaderText(header);
    	alertConfirm.setContentText(msg);
    	return alertConfirm.showAndWait().get();
    }
}

