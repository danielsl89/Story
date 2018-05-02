package dsl.story;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity implements StoryFragment.OnChoiceSelectedListener {

    private static final int INITIAL_STORY_ID = 1;

    private SQLiteDatabase db;
    private Cursor storyCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // If we're being restored from a previous state, then we don't need to do anything and
        // should return or else we could end up with overlapping fragments.
        if (savedInstanceState != null) {
            return;
        }
        SQLiteOpenHelper storyDataBaseHelper = new StoryDatabaseHelper(this);
        db = storyDataBaseHelper.getReadableDatabase();

        //Create a new Fragment to be placed in the activity layout and send the initial story as input
        addStoryFragment(INITIAL_STORY_ID, true);
    }

    public void onChoiceSelected(Choice choice) {
        addStoryFragment(choice.getNextEntryId(), false);
    }

    private Entry getStoryEntry(int id) {
        Entry entry;
        ArrayList<Choice> choices = new ArrayList<Choice>();
        Cursor choicesCursor = null;

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
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
            Log.e("MainActivity", "Initial DB error");
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

    private void addStoryFragment(int entryId, boolean isInitial) {
        Entry initialEntry = getStoryEntry(entryId);
        StoryFragment storyFragment = new StoryFragment();
        Bundle args = new Bundle();
        args.putParcelable(StoryFragment.ARG_ENTRY, initialEntry);
        storyFragment.setArguments(args);
        // Add the fragment to the 'fragment_container' FrameLayout
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (isInitial) {
            fragmentTransaction.add(R.id.fragment_container, storyFragment);
        } else {
            fragmentTransaction.replace(R.id.fragment_container, storyFragment);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        }
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}