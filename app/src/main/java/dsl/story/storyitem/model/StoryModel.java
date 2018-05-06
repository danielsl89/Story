package dsl.story.storyitem.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.squareup.otto.Bus;

import java.util.ArrayList;

public class StoryModel {

    public static class NewStoryEntryEvent {
        private Entry entry;

        public NewStoryEntryEvent(Entry entry) {
            this.entry = entry;
        }

        public Entry getEntry() {
            return entry;
        }
    }

    public static class ErrorEvent {
    }

    private static final int INITIAL_STORY_ID = 1;
    private final Bus bus;

    private SQLiteDatabase db;
    private Cursor storyCursor;

    public StoryModel(Context context, Bus bus) {
        this.bus = bus;

        SQLiteOpenHelper storyDataBaseHelper = new StoryDatabaseHelper(context);
        db = storyDataBaseHelper.getReadableDatabase();
    }

    public NewStoryEntryEvent getInitialEvent() {
        Entry entry = getStoryEntry(INITIAL_STORY_ID);
        return new NewStoryEntryEvent(entry);
    }

    private Entry getStoryEntry(int id) {
        Entry entry;
        ArrayList<Choice> choices = new ArrayList<Choice>();
        Cursor choicesCursor = null;

        //TODO: This should be done in a loader (separate thread)
        //Query entry and choice from the DB
        try {
            storyCursor = db.query(StoryDatabaseHelper.ENTRY_TABLE_NAME, new String[]
                            {StoryDatabaseHelper.ENTRY_COL_ID, StoryDatabaseHelper.ENTRY_COL_TEXT, StoryDatabaseHelper.ENTRY_COL_IMAGE},
                    StoryDatabaseHelper.ENTRY_COL_ID + " = ?",
                    new String[]{"" + id}, null, null, null);
            choicesCursor = db.query(StoryDatabaseHelper.CHOICE_TABLE_NAME, new String[]
                            {StoryDatabaseHelper.CHOICE_COL_ID, StoryDatabaseHelper.CHOICE_COL_TEXT, StoryDatabaseHelper.CHOICE_COL_NEXT_ENTRY_ID},
                    StoryDatabaseHelper.CHOICE_COL_ENTRY_ID + " = ?",
                    new String[]{"" + id}, null, null, null);
        } catch (SQLiteException e) {
            Log.e("MainActivity", "Initial DB error");
            bus.post(new ErrorEvent());

        }
        //Create array of choices for the actual entry
        if (choicesCursor != null) {
            choicesCursor.moveToFirst();
            while (choicesCursor.isAfterLast() == false) {
                choices.add(new Choice(choicesCursor.getString(1), choicesCursor.getInt(2)));
                choicesCursor.moveToNext();
            }
        }
        //Create entry
        storyCursor.moveToFirst();
        entry = new Entry(storyCursor.getString(1), storyCursor.getInt(2), choices);

        return entry;
    }

    public void getNextEntry(Choice choice) {
        int entryId = choice.getNextEntryId();
        Entry nextEntry = getStoryEntry(entryId);
        bus.post(new NewStoryEntryEvent(nextEntry));
    }

    public void destroy() {
        db.close();
    }
}
