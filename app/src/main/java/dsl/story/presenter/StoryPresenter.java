package dsl.story.presenter;

import dsl.story.model.StoryModel;
import dsl.story.view.StoryView;

public class StoryPresenter {
    private StoryModel model;
    private StoryView view;

    public StoryPresenter(StoryModel model, StoryView view) {
        this.model = model;
        this.view = view;
    }
}
