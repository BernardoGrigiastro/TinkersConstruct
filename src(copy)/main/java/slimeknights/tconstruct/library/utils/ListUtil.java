package slimeknights.tconstruct.library.utils;

import net.minecraft.util.DefaultedList;

import java.util.Arrays;

public final class ListUtil {

    private ListUtil() {}

    public static <E> DefaultedList<E> getListFrom(E... element) {
        DefaultedList<E> list = DefaultedList.create();
        list.addAll(Arrays.asList(element));
        return list;
    }
}
