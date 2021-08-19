package com.example.guichat;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    Socket socket;

    @Override
    //Всегда отрабатывает при старте программы
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textArea.setEditable(false);//не даём ничего вводить в окошко вывода сообщений чата
        textField.requestFocus();//передаёт фокус в поле ввода сообщений
        onlineUsers.setEditable(false);//не даём ничего вводить в окошко вывода сообщений чата
        textField.setVisible(false);//делаем невидимым окошко ввода сообщения
        send.setVisible(false);//делаем невидимой кнопку Отправить
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
    private TextArea onlineUsers;//это окошко, которое будет показывать нам он-лайн пользователей

    @FXML
    private Button exit;//кнопка Выйти

    @FXML
    //нажатие кнопки Выйти
    private void onExit() {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    //нажатие кнопки отправить
    private void onSendButtonClick() {
        try {
            if (textField.getText().trim() == "")
                return;//Если ничего не введено в окошко ввода сообщения, то ничего и не отправляем
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());//это поток вывода
            String text = textField.getText();
            out.writeUTF(text);
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
        textArea.clear();
        try {
            socket = new Socket("localhost", 8188);//193.168.46.140
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {//Бесконечно ожидаем сообщения от сервера
                        String response = "";
                        ArrayList<String> usersName = new ArrayList<>();//Коллекция с именами Пользователей
                        try {
                            Object object = ois.readObject();
                            if (object.getClass().equals(usersName.getClass())) {//Если приходит список пользователей
                                usersName = (ArrayList<String>) object;
                                onlineUsers.clear();
                                for (String userName : usersName) {
                                    onlineUsers.appendText(userName + "\n");
                                }
                            } else if (object.getClass().equals(response.getClass())) {
                                response = object.toString();
                                textArea.appendText(response + "\n");//Просто выводим сообщение
                            } else {
                                System.out.println("Произошла ошибка");
                            }
                        } catch (Exception e) {//чтобы не ругалось на readObject переделали IOException на Exception
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
            textArea.appendText("Сервер не доступен\n");
//            e.printStackTrace();
        }
    }
}