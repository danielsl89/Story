package dsl.story.storyitem.model;

public class Choice {
    private String text;
    private int nextEntryId;

    public Choice(String text, int nextEntryId){
        this.text = text;
        this.nextEntryId = nextEntryId;
    }

    public String getText() {
        return text;
    }

    public int getNextEntryId() {
        return nextEntryId;
    }
}