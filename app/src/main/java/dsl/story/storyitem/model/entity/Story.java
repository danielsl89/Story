package dsl.story.storyitem.model.entity;

import java.util.ArrayList;

public class Story {
    private ArrayList<Entry> entries;

    public Story(ArrayList<Entry> entries) {
        this.entries = entries;
    }

    public ArrayList<Entry> getEntries() {
        return entries;
    }
}
