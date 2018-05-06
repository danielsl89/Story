package dsl.story;

import android.app.Activity;
import android.os.Bundle;

import com.squareup.otto.Bus;

import dsl.story.storyitem.model.StoryModel;
import dsl.story.storyitem.presenter.StoryPresenter;
import dsl.story.storyitem.view.StoryView;
import dsl.story.utils.BusProvider;

public class MainActivity extends Activity {

    private StoryPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bus bus = BusProvider.getInstance();
        presenter = new StoryPresenter(new StoryModel(this, bus), new StoryView(this, bus));
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.register(presenter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.unregister(presenter);
    }
}