package dsl.story.storyitem.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.otto.Bus;

import java.util.ArrayList;

public class GetEntryTask extends AsyncTask<Integer, Integer, Entry>{
    private StoryModel entryTaskListener;
    private SQLiteDatabase db;
    private Bus bus;

    public GetEntryTask(StoryModel entryTaskListener, Bus bus, SQLiteDatabase db) {
        super();
        this.entryTaskListener = entryTaskListener;
        this.bus = bus;
        this.db = db;
    }

    @Override
    protected Entry doInBackground(Integer... entryId) {

        Entry entry;
        ArrayList<Choice> choices = new ArrayList<Choice>();
        Cursor storyCursor = null;
        Cursor choicesCursor = null;

        //Query entry and choice from the DB
        try {
            storyCursor = db.query(StoryDatabaseHelper.ENTRY_TABLE_NAME, new String[]
                            {StoryDatabaseHelper.ENTRY_COL_ID, StoryDatabaseHelper.ENTRY_COL_TEXT, StoryDatabaseHelper.ENTRY_COL_IMAGE},
                    StoryDatabaseHelper.ENTRY_COL_ID + " = ?",
                    new String[]{"" + entryId}, null, null, null);
            choicesCursor = db.query(StoryDatabaseHelper.CHOICE_TABLE_NAME, new String[]
                            {StoryDatabaseHelper.CHOICE_COL_ID, StoryDatabaseHelper.CHOICE_COL_TEXT, StoryDatabaseHelper.CHOICE_COL_NEXT_ENTRY_ID},
                    StoryDatabaseHelper.CHOICE_COL_ENTRY_ID + " = ?",
                    new String[]{"" + entryId}, null, null, null);
        } catch (SQLiteException e) {
            Log.e("MainActivity", "Initial DB error");
            bus.post(new StoryModel.ErrorEvent());

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


    @Override
    protected void onPostExecute(Entry entry) {
        super.onPostExecute(entry);
        entryTaskListener.onGetEntryTaskComplete(entry);
        entryTaskListener = null;
        bus = null;
        db = null;
    }
}
