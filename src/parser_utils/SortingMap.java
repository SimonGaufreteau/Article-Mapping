package parser_utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A static class used to sort a Map by values. The entrySet of a map will be sorted after using the {@link #sortByValue(Map)} method.
 * @param <L> Type of the Key.
 * @param <V> Type of the Values.
 */
public class SortingMap<L,V extends Comparable<? super V>> {

    public SortingMap(){}
    public Map<L,V> sortByValue(final Map<L, V> map) {
        return map.entrySet()
                .stream()
                .sorted((Map.Entry.<L, V>comparingByValue()).reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
