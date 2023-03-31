package ru.shprot.sudokumobdevkz.model.game;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "statistic_table")
public class Statistic {

    @PrimaryKey
    private int difficulty;

    private long allTime = 0;
    private int bestTime = 0;
    private int averageTime = 0;
    private int gamesStarted = 0;
    private int gamesWon = 0;
    private int percentOfWins = 0;
    private int winsWithoutErrors = 0;
    private int bestWinsLine = 0;
    private int currentWinsLine = 0;

    public Statistic(int difficulty) {
        this.difficulty = difficulty;
    }



    public void updateStatistic(boolean win, String time, int errorCounter) {
        int currentTime = parseTimeFromString(time);
        updateWinsWithoutMistakes(errorCounter);
        updateAllTime(currentTime);
        updateBestTime(currentTime, win);
        updateGameWon(win);
        updateAverageTime(win);
        updatePercentOfWins();
        updateCurrentWinsLine(win);
        updateBestWinsLine();
    }

    private void updateAverageTime(boolean win) {
        if (gamesWon != 0 && win)
            averageTime = (int) (allTime / gamesWon);
    }

    private void updateBestTime(int currentTime, boolean win) {
        if (win) {
            if (bestTime > 0)
                bestTime = Math.min(bestTime, currentTime);
            else
                bestTime = currentTime;
        }
    }

    private void updateAllTime(int currentTime) {
        allTime += currentTime;
    }

    private void updatePercentOfWins() {
        percentOfWins = ((100 * gamesWon) / gamesStarted);
    }

    private void updateGameWon(boolean win) {
        if (win) gamesWon++;
    }

    private void updateWinsWithoutMistakes(int errorCounter) {
        if (errorCounter == 0) winsWithoutErrors++;
    }

    private void updateBestWinsLine() {
        bestWinsLine = Math.max(bestWinsLine, currentWinsLine);
    }

    private void updateCurrentWinsLine(boolean win) {
        if (!win) currentWinsLine = 0;
        else currentWinsLine += 1;
    }

    private int parseTimeFromString(String timeString) {
        String[] numbers = timeString.split(":");
        if (numbers.length == 3) {
            int hours = Integer.parseInt(numbers[0]);
            int minutes = Integer.parseInt(numbers[1]);
            int seconds = Integer.parseInt(numbers[2]);
            return hours * 3600 + minutes * 60 + seconds;
        } else if (numbers.length == 2) {
            int minutes = Integer.parseInt(numbers[0]);
            int seconds = Integer.parseInt(numbers[1]);
            return minutes * 60 + seconds;
        } else return 0;
    }




    public long getAllTime() {
        return allTime;
    }

    public void setAllTime(long allTime) {
        this.allTime = allTime;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getBestTime() {
        return bestTime;
    }

    public void setBestTime(int bestTime) {
        this.bestTime = bestTime;
    }

    public int getAverageTime() {
        return averageTime;
    }

    public void setAverageTime(int averageTime) {
        this.averageTime = averageTime;
    }

    public int getGamesStarted() {
        return gamesStarted;
    }

    public void setGamesStarted(int gamesStarted) {
        this.gamesStarted = gamesStarted;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public int getPercentOfWins() {
        return percentOfWins;
    }

    public void setPercentOfWins(int percentOfWins) {
        this.percentOfWins = percentOfWins;
    }

    public int getWinsWithoutErrors() {
        return winsWithoutErrors;
    }

    public void setWinsWithoutErrors(int winsWithoutErrors) {
        this.winsWithoutErrors = winsWithoutErrors;
    }

    public int getBestWinsLine() {
        return bestWinsLine;
    }

    public void setBestWinsLine(int bestWinsLine) {
        this.bestWinsLine = bestWinsLine;
    }

    public int getCurrentWinsLine() {
        return currentWinsLine;
    }

    public void setCurrentWinsLine(int currentWinsLine) {
        this.currentWinsLine = currentWinsLine;
    }
}
