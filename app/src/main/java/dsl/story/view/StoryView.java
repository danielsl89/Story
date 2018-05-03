package dsl.story.view;

import android.app.Activity;

import com.squareup.otto.Bus;

public class StoryView extends ActivityView {
    private final Bus bus;


    public StoryView(Activity activity, Bus bus) {
        super(activity);
        this.bus = bus;
    }
}
