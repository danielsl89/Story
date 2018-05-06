package dsl.story.storyitem.model.entity;

import java.util.ArrayList;

public class Story {
    private ArrayList<Entry> entries;
    private ArrayList<Choice> choices;

    public Story(ArrayList<Entry> entries, ArrayList<Choice> choices) {
        this.entries = entries;
        this.choices = choices;
    }

    public ArrayList<Entry> getEntries() {
        return entries;
    }

    public ArrayList<Choice> getChoices() {
        return choices;
    }
}
