package dsl.story.storyitem.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
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

    private static final int INITIAL_STORY_ID = 3;
    private final Bus bus;

    private SQLiteDatabase db;
    private Entry currentEntry;

    public StoryModel(Context context, Bus bus) {
        this.bus = bus;

        SQLiteOpenHelper storyDataBaseHelper = new StoryDatabaseHelper(context);
        db = storyDataBaseHelper.getReadableDatabase();
    }

    public NewStoryEntryEvent getCurrentEntryEvent() {
        new GetEntryTask(this, this.bus, db).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, INITIAL_STORY_ID);
        return new NewStoryEntryEvent(null);
    }

    public void onGetEntryTaskComplete(Entry entry) {
        bus.post(new NewStoryEntryEvent(entry));
    }

    public void getNextEntry(Choice choice) {
        int entryId = choice.getNextEntryId();
        new GetEntryTask(this, bus, db).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, entryId);
    }

    public void destroy() {
        db.close();
    }
}
