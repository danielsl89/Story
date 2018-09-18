package dsl.story.storyitem.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dsl.story.storyitem.model.entity.Choice;
import dsl.story.storyitem.model.entity.Entry;
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

    @OnClick({ R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3 })
    public void onButtonClick(View v) {
        bus.post(new ChoiceButtonPressedEvent((Choice) v.getTag()));
    }

    public void setEntryContent(Entry entry) {
        if (entry != null) {
            textView.setText(entry.getText());
            Context context = imageView.getContext();
            int id = context.getResources().getIdentifier(entry.getImage(), "drawable", context.getPackageName());
            imageView.setImageResource(id);

            //Set choices visibility and text
            List<Choice> choices = entry.getChoices();
            int choicesSize = choices.size();
            for (int i = 0; i < MAX_CHOICES; i++) {

                if (i < choicesSize) {
                    buttons.get(i).setText(choices.get(i).getText());
                    buttons.get(i).setTag(choices.get(i));
                } else {
                    buttons.get(i).setVisibility(View.GONE);
                    buttons.get(i).setTag(null);
                }
            }
        }
    }

    public void showError() {
        Toast toast = Toast.makeText(getContext(), R.string.error_message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
