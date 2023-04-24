package com.example.demo2;

import java.io.*;
import java.util.*;

public class Quiz  {
    public static List<String> list = new ArrayList<>();
    public static List<String> allList = new ArrayList<>();
    public static ArrayList<Fillin> fillinArrayList = new ArrayList<>();
    public static ArrayList<Test> testArrayList = new ArrayList<>();
    public static List<String> proverka = new ArrayList<>();

    String name;
    static ArrayList<Question> questions = new ArrayList<>();
    public Quiz(){
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public static void addQuestion(Question question){
        questions.add(question);
    }

    public static Quiz loadFromFile(String name) {
        Quiz quiz = new Quiz();
        String line = "";
        try {
            File file = new File(name);
            FileReader fileReader = new FileReader(file);
            Scanner muh = new Scanner(fileReader);

            while (muh.hasNextLine()) {
                line = muh.nextLine();
                list.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int n = 0;
        for (int i = 0; i < list.size(); i++) {
            if(!list.get(i).isEmpty()){
                n++;
                allList.add(list.get(i));
            } else {
                if (n == 2) {
                    Fillin fillin = new Fillin();
                    int index = allList.get(0).indexOf("{");
                    int index2 = allList.get(0).indexOf("}");
                    String soz = allList.get(0).substring(0, index);
                    String soz2 = allList.get(0).substring(index2 + 1);
                    String soz3 = "";
                    soz3 += soz;
                    soz3 += "..... ";
                    soz3 += soz2;
                    fillin.setDescription(soz3);
                    fillin.setAnswer(allList.get(1));

                    quiz.fillinArrayList.add(fillin);
                    quiz.addQuestion(fillin);
                    proverka.add("Fillin");

                } else if (n == 5) {
                    Test test = new Test();
                    test.setDescription(allList.get(0));
                    test.setAnswer(allList.get(1));
                    proverka.add("Test");

                    String[] options = new String[4];
                    ArrayList<String> shuffle = new ArrayList<>();

                    shuffle.add(allList.get(1));
                    shuffle.add(allList.get(2));
                    shuffle.add(allList.get(3));
                    shuffle.add(allList.get(4));

                    for (int j = 0; j < test.numOfOptions; j++) {

                        options[j] = shuffle.get(j);
                    }
                    shuffle.clear();

                    test.setOptions(options);
                    quiz.testArrayList.add(test);
                    quiz.addQuestion(test);

                }
                n = 0;
                allList.clear();
            }

        }
        return quiz;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "name='" + name + '\'' +
                '}';
    }

    //START
    public void start(){
        String[] arr = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z".split(" ");
        Scanner muh = new Scanner(System.in);
        System.out.println("=============================\n" +
                "WELCOME TO " + getName() + " QUIZ!\n" +
                "-----------------------------\n");
        int counterFillin = 0;
        int testCounter = 0;
        int counterOfCorrectAnswers = 0;
        Collections.shuffle(fillinArrayList);
        Collections.shuffle(testArrayList);

        for (int i = 0; i < questions.size(); i++){
            if(proverka.get(i).equals("Fillin")){
                System.out.print(fillinArrayList.get(counterFillin).getDescription() + "\n" + "---------------------\n" + "Type your answer: ");
                String answer = muh.nextLine();
                if(fillinArrayList.get(counterFillin).getAnswer().equals(answer)){
                    System.out.println("Correct!");
                    counterOfCorrectAnswers++;
                } else System.out.println("Incorrect!");
                counterFillin++;
            } else {
                System.out.print(testArrayList.get(testCounter).getDescription() + "\n");
                for (int j = 0; j < 4; j++){
                    System.out.println(arr[j] + ". " + testArrayList.get(testCounter).getOptionAt(j));
                }
                System.out.print("-------------------\n" + "Enter the correct choice: ");
                String answer = muh.nextLine();
                while(!Test.checkCorrectInput(answer)){
                    System.out.print("Invalid choice! Try again (Ex. A, B, C, ...): ");
                    answer = muh.nextLine();
                }
                if(answer.equals("A")){
                    if(testArrayList.get(testCounter).getAnswer().equals(testArrayList.get(testCounter).getOptionAt(0))){
                        System.out.println("Correct!\n" + "_____________________________________\n");
                        counterOfCorrectAnswers++;
                    }
                    else System.out.println("Incorrect!\n" + "_____________________________________\n");
                } else if(answer.equals("B")){
                    if(testArrayList.get(testCounter).getAnswer().equals(testArrayList.get(testCounter).getOptionAt(1))){
                        System.out.println("Correct!\n" + "_____________________________________\n");
                        counterOfCorrectAnswers++;
                    }
                    else System.out.println("Incorrect!\n" + "_____________________________________\n");
                } else if(answer.equals("C")){
                    if(testArrayList.get(testCounter).getAnswer().equals(testArrayList.get(testCounter).getOptionAt(2))){
                        System.out.println("Correct!\n" + "_____________________________________\n");
                        counterOfCorrectAnswers++;
                    }
                    else System.out.println("Incorrect!\n" + "_____________________________________\n");
                } else if(answer.equals("D")){
                    if(testArrayList.get(testCounter).getAnswer().equals(testArrayList.get(testCounter).getOptionAt(3))){
                        System.out.println("Correct!\n" + "_____________________________________\n");
                        counterOfCorrectAnswers++;
                    }
                    else System.out.println("Incorrect!\n" + "_____________________________________\n");
                }
                testCounter++;
            }


        }
        System.out.println("Correct Answers: " + counterOfCorrectAnswers + " " + counterOfCorrectAnswers + "/" + questions.size() + " (" + (double)(counterOfCorrectAnswers * 100 / questions.size()) + "%)");
    }
}
