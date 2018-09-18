package dsl.story.storyitem.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import dsl.story.storyitem.model.entity.Choice;
import dsl.story.storyitem.model.entity.Entry;

@Database(entities = {Entry.class, Choice.class}, version = 1)
public abstract class StoryRoomDatabase extends RoomDatabase{
    public abstract StoryDao storyDao();
    private static StoryRoomDatabase INSTANCE;

    static StoryRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null) {
            synchronized (StoryRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            StoryRoomDatabase.class, "story_database")
                            .build();

                }
            }
        }
        return INSTANCE;
    }
}
