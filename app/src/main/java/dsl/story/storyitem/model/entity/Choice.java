package dsl.story.storyitem.model.entity;

public class Choice {
    private int id;
    private int entryId;
    private String text;
    private int nextEntryId;

    public Choice(int id, int entryId, String text, int nextEntryId){
        this.id = id;
        this.entryId = entryId;
        this.text = text;
        this.nextEntryId = nextEntryId;
    }

    public String getText() {
        return text;
    }
    public int getNextEntryId() {
        return nextEntryId;
    }
    public int getId() {
        return id;
    }
    public int getEntryId() {
        return entryId;
    }
}