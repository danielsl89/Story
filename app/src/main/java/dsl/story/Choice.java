package dsl.story;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by danielsl89 on 2/25/2017.
 */

public class Choice implements Parcelable {
    private String text;
    private int nextEntryId;

    public Choice(String text, int nextEntryId){
        this.text = text;
        this.nextEntryId = nextEntryId;
    }

    public String getText() {
        return text;
    }

    public int getNextEntryId() {
        return nextEntryId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeInt(this.nextEntryId);
    }

    protected Choice(Parcel in) {
        this.text = in.readString();
        this.nextEntryId = in.readInt();
    }

    public static final Creator<Choice> CREATOR = new Creator<Choice>() {
        @Override
        public Choice createFromParcel(Parcel source) {
            return new Choice(source);
        }

        @Override
        public Choice[] newArray(int size) {
            return new Choice[size];
        }
    };
}