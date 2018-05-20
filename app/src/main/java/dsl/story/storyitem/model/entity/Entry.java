package dsl.story.storyitem.model.entity;

import java.util.ArrayList;

public class Entry {
    private int id;
    private String text;
    private String image;
    private ArrayList<Choice> choices;

    public Entry(int id, String text, String image, ArrayList<Choice> choices){
        this.id = id;
        this.text = text;
        this.image = image;
        this.choices = choices;
    }

    public String getText() {
        return text;
    }
    public String getImage() {
        return image;
    }
    public ArrayList<Choice> getChoices() {
        return choices;
    }
    public int getId() {
        return id;
    }
}
