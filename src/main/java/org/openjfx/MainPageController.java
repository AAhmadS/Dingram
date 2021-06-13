package org.openjfx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SubScene;

import java.net.URL;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {

    @FXML
    private SubScene timeline;

    @FXML
    private SubScene explore;

    @FXML
    private SubScene chats;

    @FXML
    private SubScene settings;

    @FXML
    private SubScene personal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
