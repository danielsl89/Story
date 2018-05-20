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

import dsl.story.storyitem.model.entity.Choice;
import dsl.story.storyitem.model.entity.Entry;

public class GetEntryTask extends AsyncTask<Integer, Integer, Entry> {
    private Context context;
    private Bus bus;

    public GetEntryTask(Context context, Bus bus) {
        super();
        this.context = context;
        this.bus = bus;
    }

    @Override
    protected Entry doInBackground(Integer... entryIds) {
        int entryId = entryIds[0];
        SQLiteOpenHelper storyDataBaseHelper = new StoryDatabaseHelper(context);
        SQLiteDatabase db = storyDataBaseHelper.getReadableDatabase();

        Entry entry = null;
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
            return null;

        }
        //Create array of choices for the actual entry
        if (choicesCursor != null) {
            choicesCursor.moveToFirst();
            while (choicesCursor.isAfterLast() == false) {
                choices.add(new Choice(
                        choicesCursor.getInt(0),
                        entryId,
                        choicesCursor.getString(1),
                        choicesCursor.getInt(2)));
                choicesCursor.moveToNext();
            }
        }
        //Create entry
        storyCursor.moveToFirst();
        entry = new Entry(
                storyCursor.getInt(0),
                storyCursor.getString(1),
                storyCursor.getString(2), choices);

        return entry;
    }


    @Override
    protected void onPostExecute(Entry entry) {
        if (entry == null) {
            bus.post(new StoryModel.ErrorEvent());
        } else {
            bus.post(new StoryModel.NewStoryEntryEvent(entry));
        }
        bus = null;
        context = null;
    }
}
