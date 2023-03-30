package ru.shprot.sudokumobdevkz.model.game;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import ru.shprot.sudokumobdevkz.model.database.DraftsVisibilityConverter;

@Entity(tableName = "game_state_table")
public class GameState implements Parcelable {

    @PrimaryKey
    private int id = 0;
    private int difficulty;
    private String difficultyString = "Unknown";
    private String timer = "00:00";
    private int time = 0;
    private boolean isGameFinished = true;
    private boolean isDraftPressed = false;
    private int errorCounter = 0;
    @TypeConverters({DraftsVisibilityConverter.class})
    private int[] numbers = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public int emptySquareCounter = 81;
    private int hintCounter = 0;

    private boolean isDraftEnabled = true;

    private boolean isHintEnabled = true;
    private boolean isGamePaused = false;



    public GameState() {}

    protected GameState(Parcel in) {
        id = in.readInt();
        difficulty = in.readInt();
        difficultyString = in.readString();
        timer = in.readString();
        time = in.readInt();
        isGameFinished = in.readByte() != 0;
        isDraftPressed = in.readByte() != 0;
        errorCounter = in.readInt();
        numbers = in.createIntArray();
        emptySquareCounter = in.readInt();
        hintCounter = in.readInt();
        isDraftEnabled = in.readByte() != 0;
        isHintEnabled = in.readByte() != 0;
        isGamePaused = in.readByte() != 0;
    }



    public static final Creator<GameState> CREATOR = new Creator<GameState>() {
        @Override
        public GameState createFromParcel(Parcel in) {
            return new GameState(in);
        }

        @Override
        public GameState[] newArray(int size) {
            return new GameState[size];
        }
    };


    public boolean isGamePaused() {
        return isGamePaused;
    }

    public void setGamePaused(boolean gamePaused) {
        isGamePaused = gamePaused;
    }

    public void increment(int number) {
        numbers[number]++;
    }

    public boolean isOver(int number) {
        return numbers[number] == 9;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getDifficultyString() {
        return difficultyString;
    }

    public void setDifficultyString(String difficultyString) {
        this.difficultyString = difficultyString;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isDraftEnabled() {
        return isDraftEnabled;
    }

    public void setDraftEnabled(boolean draftEnabled) {
        isDraftEnabled = draftEnabled;
    }

    public boolean isHintEnabled() {
        return isHintEnabled;
    }

    public void setHintEnabled(boolean hintEnabled) {
        isHintEnabled = hintEnabled;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isGameFinished() {
        return isGameFinished;
    }

    public void setGameFinished(boolean gameFinished) {
        isGameFinished = gameFinished;
    }

    public boolean isDraftPressed() {
        return isDraftPressed;
    }

    public void setDraftPressed(boolean draftPressed) {
        isDraftPressed = draftPressed;
    }

    public int getErrorCounter() {
        return errorCounter;
    }

    public void setErrorCounter(int errorCounter) {
        this.errorCounter = errorCounter;
    }

    public int[] getNumbers() {
        return numbers;
    }

    public void setNumbers(int[] numbers) {
        this.numbers = numbers;
    }

    public int getHintCounter() {
        return hintCounter;
    }

    public void setHintCounter(int hintCounter) {
        this.hintCounter = hintCounter;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(difficulty);
        dest.writeString(difficultyString);
        dest.writeString(timer);
        dest.writeLong(time);
        dest.writeByte((byte) (isGameFinished ? 1 : 0));
        dest.writeByte((byte) (isDraftPressed ? 1 : 0));
        dest.writeInt(errorCounter);
        dest.writeIntArray(numbers);
        dest.writeInt(emptySquareCounter);
        dest.writeInt(hintCounter);
        dest.writeByte((byte) (isDraftEnabled ? 1 : 0));
        dest.writeByte((byte) (isHintEnabled ? 1 : 0));
        dest.writeByte((byte) (isGamePaused ? 1 : 0));
    }
}
