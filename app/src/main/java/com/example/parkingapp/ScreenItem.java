package com.example.parkingapp;

public class ScreenItem {
    int TextIntroduction, ScreenImg;

    public ScreenItem(int textIntroduction, int screenImg) {
        TextIntroduction = textIntroduction;
        ScreenImg = screenImg;
    }

    public void setTextIntroduction(int textIntroduction) {
        TextIntroduction = textIntroduction;
    }

    public void setScreenNumber(int screenImg) {
        ScreenImg = screenImg;
    }

    public int getTextIntroduction() {
        return TextIntroduction;
    }

    public int getScreenImg() {
        return ScreenImg;
    }
}

