package ru.yomu.slaviksoft.led2;

import android.os.Parcel;
import android.os.Parcelable;

public class LeadItem implements Parcelable{

    public long id;
    public String title;
    public String description;
    public String street;

    public LeadItem(){}

    public LeadItem(String title, String description, String street) {
        this.title = title;
        this.description = description;
        this.street = street;
    }

    public LeadItem(Parcel parcel) {
        id = parcel.readLong();
        title = parcel.readString();
        description = parcel.readString();
        street = parcel.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(street);
    }


    public static final Parcelable.Creator<LeadItem> CREATOR = new Parcelable.Creator<LeadItem>() {

        public LeadItem createFromParcel(Parcel in) {
            return new LeadItem(in);
        }

        public LeadItem[] newArray(int size) {
            return new LeadItem[size];
        }

    };



}
