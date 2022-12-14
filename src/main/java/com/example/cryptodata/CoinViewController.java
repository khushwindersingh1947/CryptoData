package com.example.cryptodata;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CoinViewController implements Initializable {

    @FXML
    private ListView<Coin> dataList;

    @FXML
    private ChoiceBox<String> orderByCombo;

    @FXML
    private TextField searchField;

    @FXML
    private ImageView imageView;

    @FXML
    private Label errorLabel;

    @FXML
    private Label countLabel;

    //variables to store values when scene is changed
    private static String search;
    private static String orderBy;
    private static Coin selectedCoin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        orderByCombo.getItems().addAll("price", "marketCap", "24hVolume", "change", "listedAt");
        orderByCombo.getSelectionModel().selectFirst();
        imageView.setImage(new Image(Main.class.getResourceAsStream("images/default-coin.jpg")));

        countLabel.setText("Total Coins: 0");
        errorLabel.setText("Crypto is Love!");
        errorLabel.setTextFill(Color.color(0.25,0.25,1));

        dataList.getSelectionModel().selectedItemProperty().addListener((obs,old,coinSelected)->{
            if(coinSelected != null){
                String iconUrl = coinSelected.getIconUrl();
                selectedCoin = coinSelected;
                try{
                    iconUrl = iconUrl.replace(".svg",".png");
                    imageView.setImage(new Image(iconUrl));
                }catch (IllegalArgumentException e){
                    imageView.setImage(new Image(Main.class.getResourceAsStream("images/default-coin.jpg")));
                }
            }
        });

        orderByCombo.getSelectionModel().selectedItemProperty().addListener((obs,old,selected)->{
            try {
                getCoins();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        dataList.setOnMouseClicked((click) -> {
            if (click.getClickCount() == 2) {
                try {
                    changeSceneToDetails(new ActionEvent(dataList,dataList));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    void getCoins() throws IOException, InterruptedException {
        dataList.getItems().clear();
        String searchText = searchField.getText();
        if(!searchText.isBlank()) {
            search = searchText;
            orderBy = orderByCombo.getValue();
            errorLabel.setText("Crypto is Love!");
            APIResponse apiResponse = APIUtility.getCoinsFromAPI(searchText, orderByCombo.getValue());
            if(apiResponse.getStatus() && apiResponse.validData()){
                dataList.getItems().addAll(apiResponse.getData().getCoins());
                countLabel.setText("Total Coins: " + apiResponse.getData().getCoins().length);
            }else{
                errorLabel.setText("Coin not Available! Please check the coin name or symbol");
            }
        }else{
            errorLabel.setText("Please enter a search value");
        }
    }


    void retainCoins() throws IOException, InterruptedException {
        if(!search.isBlank()) {
            searchField.setText(search);
            orderByCombo.setValue(orderBy);
            APIResponse apiResponse = APIUtility.getCoinsFromAPI(search, orderBy);
            if(apiResponse.getStatus() && apiResponse.validData()){
                dataList.getItems().addAll(apiResponse.getData().getCoins());
                countLabel.setText("Total Coins: " + apiResponse.getData().getCoins().length);
                dataList.getSelectionModel().select(selectedCoin);
            }else{
                errorLabel.setText("Coin not Available! Please check the coin name or symbol");
            }
        }else{
            errorLabel.setText("Please enter a search value");
        }
    }

    @FXML
    void changeSceneToDetails(ActionEvent event) throws IOException {
        if(dataList.getSelectionModel().getSelectedItem()!=null){
            SceneChange.changeSceneToCoinViewDetail(event,"coin-detail-view.fxml",dataList.getSelectionModel().getSelectedItem());
        }else{
            errorLabel.setText("Please select a coin from the list");
        }
    }


}
