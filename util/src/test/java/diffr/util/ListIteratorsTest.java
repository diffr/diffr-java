package diffr.util;

import com.google.common.collect.Lists;
import org.testng.annotations.Test;

import java.util.List;
import java.util.ListIterator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

/**
 * Tests {@link ListIteratorsTest}.
 *
 * @author Jakub D Kozlowski
 * @since 0.2
 */
public class ListIteratorsTest {

    @Test(expectedExceptions = NullPointerException.class)
    public void testLimitNullListIterator() {
        ListIterators.limit(null, 0);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testLimitNegativeLimitSize() {
        ListIterators.limit(mock(ListIterator.class), -1);
    }

    @Test
    public void testLimitWithoutPrevious() {
        final List<Integer> fullList = Lists.newArrayList(1, 2, 3, 4, 5);
        final ListIterator<Integer> subListIterator = fullList.listIterator(1);
        final ListIterator<Integer> limitSubListIterator = ListIterators.limit(fullList.listIterator(1), 3);

        for (int i = 0; i < 3; i++) {
            final Integer subListElement = subListIterator.next();
            final Integer limitSubListElement = limitSubListIterator.next();
            assertThat(subListIterator.previousIndex(), is(limitSubListIterator.previousIndex()));
            assertThat(subListIterator.nextIndex(), is(limitSubListIterator.nextIndex()));
            assertThat(subListElement, is(limitSubListElement));
        }

        assertThat(limitSubListIterator.hasNext(), is(false));
    }
}
