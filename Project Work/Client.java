package com.example.demo2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends Application {

    DataInputStream fromServer;
    DataOutputStream toServer;
    String filePath;

    @Override
    public void start(Stage stage) throws Exception {
        Socket socket = new Socket("localhost", 8081);

        new Thread(() -> {
            try {
                fromServer = new DataInputStream(socket.getInputStream());
                toServer = new DataOutputStream(socket.getOutputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                filePath = fromServer.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        pinPage(stage, socket);
    }

    Scene window;
    int W = 400;
    int H = 400;

    public void pinPage(Stage stage, Socket socket){
        BorderPane pane = new BorderPane();
        TextField textField = new TextField();
        Button answerPin = new Button("Answer!");
        VBox vBox = new VBox();
        vBox.getChildren().addAll(textField, answerPin);
        vBox.setMaxWidth(150);
        vBox.setAlignment(Pos.CENTER);
        answerPin.setMaxWidth(150);
        StackPane stack = new StackPane();
        stack.getChildren().add(vBox);
        StackPane stackPane = new StackPane();
        stackPane.setStyle("-fx-background-color: #3e147f");
        stackPane.getChildren().add(stack);
        window = new Scene(stackPane, W, H);
        stage.setScene(window);
        stage.setTitle("Enter PIN!");
        stage.show();

        new Thread(() -> {
            try {
                fromServer = new DataInputStream(socket.getInputStream());
                toServer = new DataOutputStream(socket.getOutputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {

                while(true){
                    answerPin.setOnAction(e -> {
                        try {
                            toServer.writeUTF(textField.getText());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        String pin = null;
                        try {
                            pin = fromServer.readUTF();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        if(pin.equals("Success")) {
                            nickname(stage, socket);
                        } else {
                            System.out.println("Wrong Pin");
                            pinPage(stage, socket);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }).start();
    }

    public void nickname(Stage stage, Socket socket){
        getScene(stage);
        BorderPane pane = new BorderPane();
        TextField textField = new TextField();
        Button answerPin = new Button("GO!");
        VBox vBox = new VBox();
        vBox.getChildren().addAll(textField, answerPin);
        vBox.setMaxWidth(150);
        vBox.setAlignment(Pos.CENTER);
        answerPin.setMaxWidth(150);
        StackPane stack = new StackPane();
        stack.getChildren().add(vBox);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(stack);
        stackPane.setStyle("-fx-background-color: #3e147f");
        window = new Scene(stackPane, W, H);
        stage.setTitle("Enter NICKNAME!");
        stage.setScene(window);
        stage.show();
        new Thread(() -> {
            try {
                fromServer = new DataInputStream(socket.getInputStream());
                toServer = new DataOutputStream(socket.getOutputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                answerPin.setOnAction(e -> {
                    try {
                        toServer.writeUTF(textField.getText());
                        Platform.runLater(() -> {
                            wait(stage);
                        });

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
                String start = fromServer.readUTF();
                System.out.println(start);

                window = stage.getScene();
                window.setRoot(list.get(counter));

                while (true){
                    String next = fromServer.readUTF();
                    if(next.equals("next")){
                        System.out.println(next);
                        counter++;
                        window = stage.getScene();
                        window.setRoot(list.get(counter));
                    } else {
                        System.out.println(next);
                        StackPane stackkoi = new StackPane();
                        Text text = new Text();
                        text.setText("Your score: " + String.valueOf(counterOfCorrectAnswers) + "/" + size);
                        text.setFont(new Font(25));

                        stackkoi.getChildren().add(text);
                        StackPane panegoi = new StackPane();
                        panegoi.getChildren().add(stackkoi);
                        panegoi.setStyle("-fx-background-color: #3e147f");
                        Platform.runLater(() -> {
                            window = stage.getScene();
                            window.setRoot(panegoi);
                        });
                        try {
                            toServer.writeUTF(textField.getText() + "---" + String.valueOf(counterOfCorrectAnswers));
                            toServer.flush();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }).start();

    }


    public void wait(Stage stage){
        StackPane stack = new StackPane();
        Text text = new Text("You are IN!\n" + "Wait...");
        text.setFont(new Font(20));
        stack.getChildren().add(text);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(stack);
        stackPane.setStyle("-fx-background-color: #3e147f");
        window = stage.getScene();
        window.setRoot(stackPane);
    }

    public Button kahootButton(String text,String btnColor) {
        Button btn = new Button(text);
        btn.setMinWidth(W / 2. - 5);
        btn.setMinHeight(H / 2. - 5);
        btn.setStyle("-fx-background-color: " + btnColor + ";" + "-fx-text-fill: " + btnColor + ";");
        btn.setTextFill(Color.WHITE);
        btn.setWrapText(true);
        btn.setPadding(new Insets(10));
        Font font = Font.font("Times New Roman", FontWeight.BOLD, 15);
        btn.setFont(font);
        return btn;
    }

    int testCounter = 0;
    private int counterFillin = 0;
    private int counterOfCorrectAnswers = 0;

    public StackPane test(Stage stage){
        Quiz quiz = new Quiz();
        Test test = quiz.testArrayList.get(testCounter);

        Button red = kahootButton(test.getOptionAt(0),"red");
        Button orange = kahootButton(test.getOptionAt(1),"orange");
        Button blue = kahootButton(test.getOptionAt(2),"blue");
        Button green = kahootButton(test.getOptionAt(3),"green");

        red.setOnAction(e -> {
            StackPane stack = new StackPane();
            Text textkoi = new Text("Wait! Checking...");
            StackPane stackPanegoi = new StackPane();
            stackPanegoi.getChildren().add(textkoi);
            stack.getChildren().add(stackPanegoi);
            textkoi.setFont(new Font(25));
            stack.setStyle("-fx-background-color: #46178f");


            window = stage.getScene();
            window.setRoot(stack);

            if (test.getAnswer().equals(test.getOptionAt(0))) {
                counterOfCorrectAnswers++;
                textkoi.setText("You are Correct!");
            } else {
                textkoi.setText("You are inCorrect!\n\n" + "Correct answer: " + test.getAnswer());
            }
        });

        orange.setOnAction(e -> {
            StackPane stack = new StackPane();
            Text textkoi = new Text("Wait! Checking...");
            StackPane stackPanegoi = new StackPane();
            stackPanegoi.getChildren().add(textkoi);
            stack.getChildren().add(stackPanegoi);
            textkoi.setFont(new Font(25));
            stack.setStyle("-fx-background-color: #46178f");


            window = stage.getScene();
            window.setRoot(stack);

            if (test.getAnswer().equals(test.getOptionAt(1))) {
                counterOfCorrectAnswers++;
                textkoi.setText("You are Correct!");
            } else {
                textkoi.setText("You are inCorrect!\n\n" + "Correct answer: " + test.getAnswer());
            }
        });

        blue.setOnAction(e -> {
            StackPane stack = new StackPane();
            Text textkoi = new Text("Wait! Checking...");
            StackPane stackPanegoi = new StackPane();
            stackPanegoi.getChildren().add(textkoi);
            stack.getChildren().add(stackPanegoi);
            textkoi.setFont(new Font(25));
            stack.setStyle("-fx-background-color: #46178f");


            window = stage.getScene();
            window.setRoot(stack);

            if (test.getAnswer().equals(test.getOptionAt(2))) {
                counterOfCorrectAnswers++;
                textkoi.setText("You are Correct!");
            } else {
                textkoi.setText("You are inCorrect!\n\n" + "Correct answer: " + test.getAnswer());
            }
        });

        green.setOnAction(e -> {
            StackPane stack = new StackPane();
            Text textkoi = new Text("Wait! Checking...");
            StackPane stackPanegoi = new StackPane();
            stackPanegoi.getChildren().add(textkoi);
            stack.getChildren().add(stackPanegoi);
            textkoi.setFont(new Font(25));
            stack.setStyle("-fx-background-color: #46178f");


            window = stage.getScene();
            window.setRoot(stack);

            if (test.getAnswer().equals(test.getOptionAt(3))) {
                counterOfCorrectAnswers++;
                textkoi.setText("You are Correct!");
            } else {
                textkoi.setText("You are inCorrect!\n\n" + "Correct answer: " + test.getAnswer());
            }
        });

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(red, orange);
        VBox vBox1 = new VBox(10);
        vBox1.getChildren().addAll(blue, green);

        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(vBox, vBox1);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(hBox);
        testCounter++;
        return stackPane;

    }



    public StackPane fillin(Stage stage){
        Quiz quiz = new Quiz();

        Fillin fillin = quiz.fillinArrayList.get(counterFillin);
        StackPane stackPane = new StackPane();
        StackPane stackP = new StackPane();
        TextField text = new TextField();
        text.setMaxWidth(200);

        text.setPromptText("Enter here your answer!");

        Button button = new Button("\nAnswer!");
        button.setOnAction(e -> {
            String answerOfField = String.valueOf(text.getText());
            StackPane stack = new StackPane();
            Text textkoi = new Text("Wait! Checking...");
            StackPane stackPanegoi = new StackPane();
            stackPanegoi.getChildren().add(textkoi);
            stack.getChildren().add(stackPanegoi);
            window = stage.getScene();
            window.setRoot(stack);
            textkoi.setFont(new Font(25));
            stack.setStyle("-fx-background-color: #46178f");

            if(fillin.getAnswer().equals(answerOfField)){
                textkoi.setText("You are correct!");
                counterOfCorrectAnswers++;
            } else {
                textkoi.setText("You are incorrect...\n\n" + "Correct answer: " + fillin.getAnswer());
            }
        });

        button.setMinWidth(200);

        button.setMaxWidth(200);
        button.setMinHeight(20);
        VBox vBox = new VBox();
        StackPane stack = new StackPane();
        stack.getChildren().addAll(button);
        StackPane textStack = new StackPane();
        textStack.getChildren().add(text);
        vBox.getChildren().addAll(textStack, stack);
        vBox.setAlignment(Pos.CENTER);
        stackP.getChildren().add(vBox);
        stackPane.getChildren().addAll(stackP);
        stackPane.setStyle("-fx-background-color: #46178f");
        counterFillin++;
        return stackPane;
    }
    int size;
    ArrayList<StackPane> list = new ArrayList<>();

    public void getScene(Stage stage){
        Quiz quiz = new Quiz();
        quiz = Quiz.loadFromFile(filePath);
        size = quiz.proverka.size();

        for (int i = 0; i < quiz.proverka.size(); i++) {
            if(quiz.proverka.get(i).equals("Test")) list.add(test(stage));
            else list.add(fillin(stage));
        }

    }

    int counter = 0;



}
