package dsl.story;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by danielsl89 on 2/25/2017.
 */

public class Entry implements Parcelable {
    private String text;
    private int image;
    private ArrayList<Choice> choices;

    public Entry(String text, int image, ArrayList<Choice> choices){
        this.text = text;
        this.image = image;
        this.choices = choices;
    }

    public String getText() {
        return text;
    }
    public int getImage() {
        return image;
    }
    public ArrayList<Choice> getChoices() {
        return choices;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeInt(this.image);
        dest.writeTypedList(this.choices);
    }

    protected Entry(Parcel in) {
        this.text = in.readString();
        this.image = in.readInt();
        this.choices = in.createTypedArrayList(Choice.CREATOR);
    }

    public static final Creator<Entry> CREATOR = new Creator<Entry>() {
        @Override
        public Entry createFromParcel(Parcel source) {
            return new Entry(source);
        }

        @Override
        public Entry[] newArray(int size) {
            return new Entry[size];
        }
    };
}
