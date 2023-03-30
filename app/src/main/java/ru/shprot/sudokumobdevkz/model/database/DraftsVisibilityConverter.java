package ru.shprot.sudokumobdevkz.model.database;

import androidx.room.TypeConverter;

import java.util.Arrays;

public class DraftsVisibilityConverter {

    @TypeConverter
    public String fromDraftsVisibility(int[] draftsVisibility) {
        return Arrays.toString(draftsVisibility);
    }

    @TypeConverter
    public int[] toDraftsVisibility(String s) {
        String[] array = s.substring(1, s.length()-1).split(", ");
        int[] a = new int[array.length];
        for (int i = 0; i < a.length; i++)
            a[i] = Integer.parseInt(array[i]);
        return a;
    }
}
