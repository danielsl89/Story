package dsl.story.storyitem.model;

import android.content.Context;

import com.squareup.otto.Bus;

import dsl.story.storyitem.model.entity.Choice;
import dsl.story.storyitem.model.entity.Entry;


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

    public static class ErrorEvent {}

    private static final int INITIAL_STORY_ID = 1;
    private Context context;
    private final Bus bus;

    public StoryModel(Context context, Bus bus) {
        this.context = context;
        this.bus = bus;
    }

    public NewStoryEntryEvent getInitialEntryEvent() {
        //When the model gets notified that the event listener was added, it runs the async task
        new GetEntryTask(context, this.bus).execute(INITIAL_STORY_ID);
        return null;
    }

    public void getNextEntry(Choice choice) {
        int entryId = choice.getNextEntryId();
        new GetEntryTask(context, bus).execute(entryId);
    }
}
