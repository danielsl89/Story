package dsl.story.storyitem.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Entry {
    private String text;
    private int image;
    private ArrayList<Choice> choices;

    public Entry(String text, int image, ArrayList<Choice> choices){
        this.text = text;
        this.image = image;
        this.choices = choices;
    }

    public String getText() {
        return text;
    }
    public int getImage() {
        return image;
    }
    public ArrayList<Choice> getChoices() {
        return choices;
    }
}
