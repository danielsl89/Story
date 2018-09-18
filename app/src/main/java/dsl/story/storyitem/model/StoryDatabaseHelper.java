package dsl.story.storyitem.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.JsonReader;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dsl.story.R;
import dsl.story.storyitem.model.entity.Choice;
import dsl.story.storyitem.model.entity.Entry;
import dsl.story.storyitem.model.entity.Story;

public class StoryDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "story";
    private static final int DB_VERSION = 23;

    public static final String ENTRY_TABLE_NAME = "ENTRY";
    public static final String ENTRY_COL_ID = "_id";
    public static final String ENTRY_COL_TEXT = "text";
    public static final String ENTRY_COL_IMAGE = "image";
    private static final String ENTRY_TABLE_DESC = ENTRY_COL_ID + " INTEGER PRIMARY KEY, " +
            ENTRY_COL_TEXT + " TEXT, " +
            ENTRY_COL_IMAGE + " TEXT";

    public static final String CHOICE_TABLE_NAME = "CHOICE";
    public static final String CHOICE_COL_ID = "_id";
    public static final String CHOICE_COL_ENTRY_ID = "entryId";
    public static final String CHOICE_COL_TEXT = "text";
    public static final String CHOICE_COL_NEXT_ENTRY_ID = "nextEntryId";
    private static final String CHOICE_TABLE_DESC = CHOICE_COL_ID + " INTEGER PRIMARY KEY, " +
            CHOICE_COL_ENTRY_ID + " INTEGER, " +
            CHOICE_COL_TEXT + " TEXT, " +
            CHOICE_COL_NEXT_ENTRY_ID + " INTEGER";

    private Context context;

    StoryDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    //TODO: This will actually get deleted when Room persistance library is fully integrated.
    //TODO: Update this stuff. It should be done in a Loader / Async task
    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: The following ln removes the entries table for debugging. Remove when app is done
        db.execSQL("DROP TABLE IF EXISTS " + ENTRY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CHOICE_TABLE_NAME);
        //if(oldVersion < 1){
        InputStream inputStream = context.getResources().openRawResource(R.raw.default_story);
        String jsonString = new Scanner(inputStream).useDelimiter("\\A").next();
        Gson gson = new Gson();
        Story story = gson.fromJson(jsonString, Story.class);

        db.execSQL("CREATE TABLE " + ENTRY_TABLE_NAME + " ( " + ENTRY_TABLE_DESC + ");");
        db.execSQL("CREATE TABLE " + CHOICE_TABLE_NAME + " ( " + CHOICE_TABLE_DESC + ");");
        ArrayList<Entry> entries = story.getEntries();
        for (Entry entry:entries) {
            insertEntry(db, entry.getId(), entry.getText(), entry.getImage());
            List<Choice> choices = entry.getChoices();
            if (choices != null) {
                for (Choice choice:choices) {
                    insertChoice(db, choice.getId(), entry.getId(), choice.getText(), choice.getNextEntryId());
                }
            }
        }
    }

    private static void insertEntry(SQLiteDatabase db, int id, String text, String image) {
        ContentValues values = new ContentValues();
        values.put(ENTRY_COL_ID, id);
        values.put(ENTRY_COL_TEXT, text);
        values.put(ENTRY_COL_IMAGE, image);
        db.insert(ENTRY_TABLE_NAME, null, values);
    }

    private static void insertChoice(SQLiteDatabase db, int id, int entryId, String text, int nextEntryId) {
        ContentValues values = new ContentValues();
        values.put(CHOICE_COL_ID, id);
        values.put(CHOICE_COL_ENTRY_ID, entryId);
        values.put(CHOICE_COL_TEXT, text);
        values.put(CHOICE_COL_NEXT_ENTRY_ID, nextEntryId);
        db.insert(CHOICE_TABLE_NAME, null, values);
    }
}