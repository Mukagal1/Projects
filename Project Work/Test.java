package com.example.demo2;


import java.util.ArrayList;
import java.util.Arrays;

public class Test extends Question{
    public String[] options;
    public int numOfOptions = 4;
    public ArrayList<Character> labels = new ArrayList<>();

    Test(){
    }
    public void setOptions(String[] options){
        this.options = options;
    }
    public String getOptionAt(int n){
        return options[n];
    }
    public static boolean checkCorrectInput(String str){
        for (int i = 0; i < 4; i++) {
            if(str.equals("A") || str.equals("B") || str.equals("C") || str.equals("D")){
                return true;
            }
        }
        return false;
    }
    @Override
    public String toString() {
        return "Test{" +
                "options=" + Arrays.toString(options) +
                ", numOfOptions=" + numOfOptions +
                ", labels=" + labels +
                '}';
    }
}

