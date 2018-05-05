package dsl.story.storyitem.view;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dsl.story.storyitem.model.Choice;
import dsl.story.storyitem.model.Entry;
import dsl.story.R;

public class StoryView extends ActivityView {

    public static class ChoiceButtonPressedEvent {
        public Choice getChoice() {
            return choice;
        }
        private Choice choice;
        public ChoiceButtonPressedEvent(Choice choice){
            this.choice = choice;
        }
    }

    private static final int MAX_CHOICES = 4;
    private final Bus bus;
    private Entry entry;

    @BindView(R.id.text)
    TextView textView;

    @BindView(R.id.image)
    ImageView imageView;

    @BindViews({ R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3 })
    List<Button>  buttons;

    public StoryView(Activity activity, Bus bus) {
        super(activity);
        this.bus = bus;
        ButterKnife.bind(this, activity);
    }

    public void setEntryContent(Entry entry) {
        this.entry = entry;
        textView.setText(entry.getText());
        imageView.setImageResource(entry.getImage());


        //Set choices visibility and text
        ArrayList<Choice> choices = entry.getChoices();
        int choicesSize = choices.size();
        for (int i = 0; i < MAX_CHOICES; i++) {

            if (i < choicesSize) {
                buttons.get(i).setText(choices.get(i).getText());
            } else {
                buttons.get(i).setVisibility(View.GONE);
            }
        }
    }

    @OnClick({ R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3 })
    public void onButtonClick(View v) {
        ArrayList<Choice> choices = entry.getChoices();
        int choicesSize = choices.size();

        for (int i = 0; i < choicesSize; i++) {
            //TODO: See if there is a better way to associate a choiceId to each of the button, so we don't have to save the entry data and iterate searching the id for each button onClick
            int buttonId = buttons.get(i).getId();
            if (v.getId() == buttonId) {
                bus.post(new ChoiceButtonPressedEvent(choices.get(i)));
                break;
            }
        }
    }
}
