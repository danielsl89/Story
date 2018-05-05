package dsl.story.storyitem.presenter;

import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import dsl.story.storyitem.model.StoryModel;
import dsl.story.storyitem.view.StoryView;

public class StoryPresenter {
    private StoryModel model;
    private StoryView view;

    public StoryPresenter(StoryModel model, StoryView view) {
        this.model = model;
        this.view = view;
    }

    @Produce
    public StoryModel.NewStoryEntryEvent onFirstProducedEvent() {
        return model.getCurrentEntryEvent();
    }

    @Subscribe
    public void onChoiceButtonPressedEvent(StoryView.ChoiceButtonPressedEvent event) {
        model.getNextEntry(event.getChoice());
    }

    @Subscribe
    public void onNewStoryEntryAvailable(StoryModel.NewStoryEntryEvent event) {
        view.setEntryContent(event.getEntry());
    }

    @Subscribe
    public void onErrorEvent(StoryModel.ErrorEvent event) {
        view.showError();
    }

    public void destroy() {
        model.destroy();
    }
}
