package org.consoleApp.parametrizedDAOTest;

import org.assertj.core.api.Assertions;

import java.util.List;
import java.util.function.Function;

public class AssertionUtils {
    private AssertionUtils(){}

    public static <T, R> void assertSameList(List<T> actual, List<T> expected, Function<T, R> function){
        if (actual.size() != expected.size()) {
            throw new AssertionError("Lists have different sizes");
        }

        List<R> actualTransformed = actual.stream().map(function).toList();
        List<R> expectedTransformed = expected.stream().map(function).toList();

        Assertions.assertThat(actualTransformed).containsExactlyInAnyOrderElementsOf(expectedTransformed);
    }
}
