package slimeknights.tconstruct.library.utils;

import java.util.Arrays;
import net.minecraft.util.DefaultedList;

public final class ListUtil {

  public static <E> DefaultedList<E> getListFrom(E... element) {
    DefaultedList<E> list = DefaultedList.create();
    list.addAll(Arrays.asList(element));
    return list;
  }

  private ListUtil() {}
}
