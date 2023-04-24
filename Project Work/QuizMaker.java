package com.example.demo2;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class QuizMaker extends Application {

    public Button kahootButton(String text, String color) {
        Font font = Font.font("Times New Roman", FontWeight.BOLD, 12);
        Button button = new Button(text);
        button.setMinWidth(290);
        button.setMinHeight(90);
        button.setStyle("-fx-background-color: " + color);
        button.setTextFill(Color.WHITE);
        button.setFont(font);

        return button;
    }
    public int W = 600;
    public int H = 600;

    Scene window;
    String pin = "";
    ServerSocket serverSocket;

    ArrayList<Socket> sockets = new ArrayList<>();
    public String pin(){
        for (int i = 0; i < 7; i++) {
            pin += (int)(Math.random() * 10);
        }
        return pin;
    }
    int counter = 0;

    @Override
    public void start(Stage stage) throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT File", "*txt"));
        File file = fileChooser.showOpenDialog(stage);
        filePath = file.toURI().toString();

        BorderPane pane = new BorderPane();
        Button start = new Button("Start!");
        start.setMinWidth(100);
        start.setMinHeight(25);
        Text text = new Text();
        text.setText("Game PIN\n" + pin() + "\n" + "Players:\n");
        text.setFont(new Font(30));
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(text);
        pane.setTop(stackPane);
        StackPane startPane = new StackPane();
        startPane.getChildren().add(start);
        pane.setBottom(startPane);
        pane.setStyle("-fx-background-color: #3e147f");
        window = new Scene(pane, W, H);
        stage.setScene(window);
        stage.show();

        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(8081);

                while (true) {
                    Socket socket = serverSocket.accept();
                    sockets.add(socket);

                    DataInputStream fromClient = new DataInputStream(socket.getInputStream());
                    DataOutputStream toClient = new DataOutputStream(socket.getOutputStream());

                    toClient.writeUTF(filePath.substring(6));

                        String from = fromClient.readUTF();
                        if (from.equals(pin)) toClient.writeUTF("Success");
                        else toClient.writeUTF("Wrong Pin!");

                    String nickname = fromClient.readUTF();
                    text.setText(text.getText() + nickname + "\n");

                    start.setOnAction(e -> {
                        System.out.println(sockets);
//                        try {
////                            Thread.sleep(10000);
//                        } catch (InterruptedException ex) {
//                            ex.printStackTrace();
//                        }
                        new Thread(() -> {
                            for (int i = 0; i < sockets.size(); i++) {
                                DataOutputStream out = null;
                                DataInputStream in = null;
                                try {
                                    out = new DataOutputStream(sockets.get(i).getOutputStream());
                                    in = new DataInputStream(sockets.get(i).getInputStream());
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                try {
                                    out.writeUTF("started");
                                    out.flush();



                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                            try {
//                                Thread.sleep(15000);
                                startkoi(stage);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }


                        }).start();

                    });
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }

    public void startkoi(Stage primaryStage) throws Exception {
            try {
                get(primaryStage);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }

        primaryStage.setTitle("Project Work 3");
        primaryStage.show();
    }


    String filePath;
    int size;
    Scene scene;
    Boolean shuffle = false;
    ArrayList<String> listkoi = new ArrayList<>();
    ArrayList<Integer> listkoi2 = new ArrayList<>();

    TreeMap<String, Integer> hashMap = new TreeMap();
    String last = "";


    int sanau = 1;
    ArrayList<BorderPane> all = new ArrayList<>();
    public void get(Stage primaryStage) throws FileNotFoundException, InterruptedException {
        Media media = new Media(new File("kahoot_music.mp3").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
          mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
          mediaPlayer.play();

        String filePathAlu = "";
        filePathAlu += filePath.substring(6);

        Quiz quiz = new Quiz();
        quiz = Quiz.loadFromFile(filePathAlu);

        if(shuffle == true){
            Collections.shuffle(quiz.proverka);
        }

        for (int i = 0; i < quiz.proverka.size(); i++) {
            if(quiz.proverka.get(i).equals("Test")){
                all.add(test());
            } else all.add(fillin());
        }
        all.add(button22());
        size = all.size() - 1;



        window = primaryStage.getScene();
        window.setRoot(all.get(0));


        timer.schedule(timerTask, 1000, 1000);
        timerTask.run();

        while(counter <= all.size() - 2){
            Thread.sleep(20000);
            counter++;
            sanau++;
            new Thread(() -> {
                for (int i = 0; i < sockets.size(); i++) {
                    DataOutputStream out = null;
                    DataInputStream in = null;
                    try {
                        out = new DataOutputStream(sockets.get(i).getOutputStream());
                        in = new DataInputStream(sockets.get(i).getInputStream());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        if(counter <= all.size() - 2){
                        out.writeUTF("next");
                        out.flush();
                        }
                        else {
                            out.writeUTF("end");
                            out.flush();
                            Text text = new Text("Winners:\n");
                            String[] strings = new String[sockets.size()];
                            int[] ints = new int[sockets.size()];
                                try{String client = in.readUTF();
                                    System.out.println(client);
                                    String[] player = client.split("---");
                                    listkoi.add(player[0]);
                                    listkoi2.add(Integer.valueOf(player[1]));
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            for (int j = 0; j < listkoi.size(); j++) {
                                strings[j] = listkoi.get(j);
                                ints[j] = listkoi2.get(j);
//                                text.setText(text.getText() + "\n" + soz);
                            }

                            for (int j = 0; j < strings.length - 1; j++) {
                                for (int k = 0; k < strings.length - 1; ++k) {
                                    if(ints[k] < ints[k + 1]){
                                        int n = ints[k];
                                        ints[k] = ints[k + 1];
                                        ints[k + 1] = n;
                                        String str = strings[k];
                                        strings[k] = strings[k + 1];
                                        strings[k + 1] = str;
                                    }
                                }
                            }

                            for (int j = 0; j < strings.length; j++) {
                                text.setText(text.getText() + "\n" + strings[j] + " " + ints[j] + "/" + (all.size() - 1));
                            }

                            Platform.runLater(() -> {

                                StackPane stack = new StackPane();
                                stack.getChildren().add(text);
                                text.setFont(new Font(25));
                                BorderPane panek = new BorderPane();
                                panek.setTop(stack);
                                panek.setStyle("-fx-background-color: #3e147f");

                                window = primaryStage.getScene();
                                window.setRoot(panek);
//                                try {
//                                    Thread.sleep(40000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }

//                            } catch (Exception em){
//                                em.printStackTrace();
//                            }

                        });
                        }


                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();
            window = primaryStage.getScene();
            window.setRoot(all.get(counter));
        }



    }

    int testCounter = 0;
    int iFillin = 0;
    public BorderPane test() throws FileNotFoundException {
        Quiz quiz = new Quiz();
        Test test = quiz.testArrayList.get(testCounter);

        BorderPane mainPane = new BorderPane();



        Button red = kahootButton(test.getOptionAt(0), "red");
        Button orange = kahootButton(test.getOptionAt(1), "orange");

        Button blue = kahootButton(test.getOptionAt(2), "blue");
        Button green = kahootButton(test.getOptionAt(3), "green");

        red.setOnAction(e -> {
            if(test.getAnswer().equals(red.getText())){
                correctAnswers++;
            }
        });
        orange.setOnAction(e -> {
            if(test.getAnswer().equals(orange.getText())){
                correctAnswers++;
            }
        });
        blue.setOnAction(e -> {
            if(test.getAnswer().equals(blue.getText())){
                correctAnswers++;
            }
        });
        green.setOnAction(e -> {
            if(test.getAnswer().equals(green.getText())){
                correctAnswers++;
            }
        });


        ToggleGroup toggleGroup = new ToggleGroup();
        VBox vBox2 = new VBox(3);
        vBox2.getChildren().addAll(blue, green);

        VBox vBox1 = new VBox(3);
        vBox1.getChildren().addAll(red, orange);

        Image testImage = new Image(new FileInputStream("logo.png"));
        ImageView testImageView = new ImageView(testImage);
        testImageView.setFitHeight(200);
        testImageView.setFitWidth(350);

        Label txtLabel = new Label(toString(iFillin) + ". " + test.getDescription());
        Font font = Font.font("Times New Roman", FontWeight.BOLD, 15);
        txtLabel.setFont(font);
        txtLabel.setMinWidth(500);
        txtLabel.setAlignment(Pos.CENTER);

        StackPane textLabel = new StackPane();
        textLabel.getChildren().add(txtLabel);


        Text textTime = new Text();
        textTime.textProperty().bind(text.textProperty());
        StackPane time = new StackPane();
        time.getChildren().add(textTime);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(textLabel, time);

        HBox hBox = new HBox(3);
        hBox.getChildren().addAll(vBox1, vBox2);
        hBox.setMaxWidth(600);
        hBox.setMaxHeight(100);

        hBox.setAlignment(Pos.CENTER);
        mainPane.setTop(vBox);
        mainPane.setBottom(hBox);
        mainPane.setCenter(testImageView);

        BorderPane.setMargin(hBox, new Insets(0, 0, 10, 0));
        testCounter++;
        iFillin++;
        return mainPane;
    }

    int fillinCounter;
    int allCounter = 0;
    int correctAnswers = 0;

    public String toString(int i){
        return String.valueOf(i + 1);
    }

    int second = 20;
    Text text = new Text();
    int min = 0;
    Timer timer = new Timer();


    public BorderPane fillin() throws FileNotFoundException {
        Quiz quiz = new Quiz();
        Fillin fillinClass = quiz.fillinArrayList.get(fillinCounter);
        BorderPane fillin = new BorderPane();

        Image imageSmall = new Image(new FileInputStream("k.png"));
        ImageView imageViewSmall = new ImageView(imageSmall);
        imageViewSmall.setFitHeight(25);
        imageViewSmall.setFitWidth(50);

        Text suraq = new Text(toString(iFillin) + ". " + fillinClass.getDescription());
        Font font = Font.font("Times New Roman", FontWeight.BOLD, 15);
        suraq.setFont(font);
        HBox suraqSmall = new HBox();
        suraqSmall.getChildren().addAll(imageViewSmall, suraq);
        suraqSmall.setAlignment(Pos.CENTER);

        Text textTime = new Text();
        textTime.textProperty().bind(text.textProperty());
        StackPane time = new StackPane();
        time.getChildren().add(textTime);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(suraqSmall, time);

        TextField textField = new TextField();
        Image imageBig = new Image(new FileInputStream("fillin.png"));
        ImageView imageViewBig = new ImageView(imageBig);
        imageViewBig.setFitWidth(500);
        imageViewBig.setFitHeight(350);

        textField.setPromptText("Type your answer here:");
        textField.setMaxHeight(30);
        textField.setMaxWidth(350);
        VBox vBoxTextField = new VBox();
        vBoxTextField.getChildren().addAll(imageViewBig, textField);
        vBoxTextField.setAlignment(Pos.CENTER);


        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(vBoxTextField);

        fillin.setTop(vBox);
        fillin.setCenter(stackPane);
        iFillin++;
        fillinCounter++;

        return fillin;
    }

int number = 0;
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            text.setText(String.valueOf(second));
            if(second == 0){
                  number++;
                  second = 20;
            }
            second--;
        }
    };


   private BorderPane button22(){

        BorderPane pane = new BorderPane();
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        StackPane stack = new StackPane();
        stack.getChildren().add(vBox);
        pane.setTop(stack);
        pane.setStyle("-fx-background-color: #3e147f");
        return pane;

    }


}

