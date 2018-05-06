package dsl.story.storyitem.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import dsl.story.R;

public class StoryDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "story";
    private static final int DB_VERSION = 17;

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

    StoryDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
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

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: The following ln removes the entries table for debugging. Remove when app is done
        db.execSQL("DROP TABLE IF EXISTS " + ENTRY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CHOICE_TABLE_NAME);
        //if(oldVersion < 1){
            db.execSQL("CREATE TABLE " + ENTRY_TABLE_NAME + " ( " + ENTRY_TABLE_DESC + ");");
            insertEntry(db, 1, "Esta es una historia random", R.drawable.question);
            insertEntry(db, 2, "Paso 2 de historia random", R.drawable.background);
            insertEntry(db, 3, "Paso 2 alternativo de historia random", R.drawable.background);
            insertEntry(db, 4, "Fin de la historia!", R.drawable.background);
            insertEntry(db, 5, "Fin de la historia alternativa buena!", R.drawable.background);
            insertEntry(db, 6, "Fin de la historia alternativa mala!", R.drawable.background);

            db.execSQL("CREATE TABLE " + CHOICE_TABLE_NAME + " ( " + CHOICE_TABLE_DESC + ");");
            insertChoice(db, 1, 1, "Opción 1", 2);
            insertChoice(db, 2, 1, "Opción 2", 2);
            insertChoice(db, 3, 1, "Opción 3", 3);
            insertChoice(db, 4, 2, "Terminar historia", 4);
            insertChoice(db, 5, 3, "Terminar historia alternativa por las buenas", 5);
            insertChoice(db, 6, 3, "Terminar historia alternativa por las malas", 6);
        //}
    }
}