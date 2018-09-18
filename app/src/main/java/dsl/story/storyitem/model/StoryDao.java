package dsl.story.storyitem.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import java.util.List;

import dsl.story.storyitem.model.entity.Choice;
import dsl.story.storyitem.model.entity.Entry;

@Dao
public interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEntry(Entry entry);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChoice(Choice choice);

    @Query("SELECT * from Entry where id = :id")
    Entry getEntry(int id);

    @Query("SELECT * from Choice where entryId = :entryId")
    List<Choice> getChoices(int entryId);
}
