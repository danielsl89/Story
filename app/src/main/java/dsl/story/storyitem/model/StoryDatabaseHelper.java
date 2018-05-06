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
import java.util.Scanner;

import dsl.story.R;
import dsl.story.storyitem.model.entity.Choice;
import dsl.story.storyitem.model.entity.Entry;
import dsl.story.storyitem.model.entity.Story;

public class StoryDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "story";
    private static final int DB_VERSION = 20;

    public static final String ENTRY_TABLE_NAME = "ENTRY";
    public static final String ENTRY_COL_ID = "_id";
    public static final String ENTRY_COL_TEXT = "text";
    public static final String ENTRY_COL_IMAGE = "image";
    private static final String ENTRY_TABLE_DESC = ENTRY_COL_ID + " INTEGER PRIMARY KEY, " +
            ENTRY_COL_TEXT + " TEXT, " +
            ENTRY_COL_IMAGE + " INTEGER";

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
        ArrayList<Entry> entries = story.getEntries();
        for (Entry entry:entries) {
            insertEntry(db, entry.getId(), entry.getText(), entry.getImage());
        }

        db.execSQL("CREATE TABLE " + CHOICE_TABLE_NAME + " ( " + CHOICE_TABLE_DESC + ");");
        ArrayList<Choice> choices = story.getChoices();
        for (Choice choice:choices) {
            insertChoice(db, choice.getId(), choice.getEntryId(), choice.getText(), choice.getNextEntryId());
        }
        /*
        TODO: See how can I point from a custom JSON file to a resource from drawable folder. Do I have the id, or should I save the name and use getResources().getIdentifier function
        insertEntry(db, 1, "Esta es una historia random", R.drawable.question);
        insertEntry(db, 2, "Paso 2 de historia random", R.drawable.background);
        insertEntry(db, 3, "Paso 2 alternativo de historia random", R.drawable.background);
        insertEntry(db, 4, "Fin de la historia!", R.drawable.background);
        insertEntry(db, 5, "Fin de la historia alternativa buena!", R.drawable.background);
        insertEntry(db, 6, "Fin de la historia alternativa mala!", R.drawable.background);
        */
        //}
    }

    private static void insertEntry(SQLiteDatabase db, int id, String text, int image) {
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