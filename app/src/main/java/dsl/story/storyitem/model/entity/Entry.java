package dsl.story.storyitem.model.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import java.util.List;

@Entity
public class Entry {
    @PrimaryKey
    private int id;
    private String text;
    private String image;
    @Ignore
    private List<Choice> choices;

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public List<Choice> getChoices() {
        return choices;
    }
    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
