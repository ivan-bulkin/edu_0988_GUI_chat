package com.example.guichat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    Socket socket;

    @Override
    //Всегда отрабатывает при старте программы
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textArea.setEditable(false);//не даём ничего вводить в окошко вывода сообщений чата
        textField.requestFocus();//передаёт фокус в поле ввода сообщений
    }

    @FXML
    private TextArea textArea;//это окошко вывода сообщений

    @FXML
    private TextField textField;//это окошко ввода сообщения

    @FXML
    private Button connect;//это кнопка Подключиться

    @FXML
    private Button send;//это кнопка Отправить

    @FXML
    //нажатие кнопки отправить
    private void onSendButtonClick() {
        try {
            if (textField.getText().trim() == "")
                return;//Если ничего не введено в окошко ввода сообщения, то ничего и не отправляем
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());//это поток вывода
            String text = textField.getText();
            out.writeUTF(text);
//        textArea.setText(text);//не подходит под нашу задачу, т.к. перезаписывает всё поле сообщений, а надо добавлять сообщения к уже существующим
            textArea.appendText("Вы: " + text + "\n");
            textField.clear();//очищаем поле ввода сообщений
            textField.requestFocus();//передаёт фокус в поле ввода сообщений
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    //нажатие кнопки Подключиться
    private void connect() {
        try {
            socket = new Socket("193.168.46.140", 8188);//localhost
            DataInputStream in = new DataInputStream(socket.getInputStream());//это поток ввода
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String response;
                    while (true) {//Бесконечно ожидаем сообщения от сервера
                        try {
                            response = in.readUTF();//принимаем и читаем ответ от Сервера
                            textArea.appendText(response + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            thread.start();
            connect.setVisible(false);
            textField.setVisible(true);
            send.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}