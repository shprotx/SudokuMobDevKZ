package ru.shprot.sudokumobdevkz.model.game;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import ru.shprot.sudokumobdevkz.model.database.DraftsVisibilityConverter;

@Entity(tableName = "square_table")
public class Square implements Parcelable {

    @PrimaryKey
    private int position;
    private int x;
    private int y;
    private int region;
    private int color;
    private int cellColor;
    private int value;
    @TypeConverters({DraftsVisibilityConverter.class})
    public int[] draftsVisibility = {4, 4, 4, 4, 4, 4, 4, 4, 4};
    private boolean isVisible;

    public Square() {}

    protected Square(Parcel in) {
        position = in.readInt();
        x = in.readInt();
        y = in.readInt();
        region = in.readInt();
        color = in.readInt();
        cellColor = in.readInt();
        value = in.readInt();
        draftsVisibility = in.createIntArray();
        isVisible = in.readByte() != 0;
    }

    public static final Creator<Square> CREATOR = new Creator<Square>() {
        @Override
        public Square createFromParcel(Parcel in) {
            return new Square(in);
        }

        @Override
        public Square[] newArray(int size) {
            return new Square[size];
        }
    };

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getCellColor() {
        return cellColor;
    }

    public void setCellColor(int cellColor) {
        this.cellColor = cellColor;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(position);
        dest.writeInt(x);
        dest.writeInt(y);
        dest.writeInt(region);
        dest.writeInt(color);
        dest.writeInt(cellColor);
        dest.writeInt(value);
        dest.writeIntArray(draftsVisibility);
        dest.writeByte((byte) (isVisible ? 1 : 0));
    }
}
