package dsl.story;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoryFragment extends Fragment implements View.OnClickListener{

    public interface OnChoiceSelectedListener{
        void onChoiceSelected(Choice choice);
    }

    public static final String ARG_ENTRY = "story";
    private static final int MAX_CHOICES = 4;
    private Entry entry;


    public StoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_story, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            entry = bundle.getParcelable(ARG_ENTRY);

            //Set entry text
            TextView textView = (TextView)view.findViewById(R.id.text);
            textView.setText(entry.getText());
            //Set entry image
            ImageView imageView = (ImageView)view.findViewById(R.id.image);
            imageView.setImageResource(entry.getImage());
            //Set available choices, set choices text and click listeners
            Button button;
            ArrayList<Choice> choices = entry.getChoices();
            int choicesSize = choices.size();
            String packageName = getActivity().getApplicationContext().getPackageName();
            for(int i = 0; i < MAX_CHOICES; i++) {
                int buttonId = getResources().getIdentifier("button_" + i, "id", packageName);
                button = (Button)view.findViewById(buttonId);
                if(i < choicesSize) {
                    button.setText(choices.get(i).getText());
                    button.setOnClickListener(this);
                } else {
                    button.setVisibility(View.GONE);
                }
            }
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        ArrayList<Choice> choices = entry.getChoices();
        int choicesSize = choices.size();
        String packageName = getActivity().getApplicationContext().getPackageName();
        for(int i = 0; i < choicesSize; i++) {
            int buttonId = getResources().getIdentifier("button_" + i, "id", packageName);
            if(v.getId() == buttonId)
            {
                ((OnChoiceSelectedListener) getActivity()).onChoiceSelected(choices.get(i));
                break;
            }
        }
    }
}