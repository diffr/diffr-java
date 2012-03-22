package diffr.suffixtree.impl;

import java.util.ListIterator;
import java.util.NoSuchElementException;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Utility methods for creating various {@link ListIterators}.
 *
 * @author Jakub D Kozlowski
 * @since 0.1
 */
public final class ListIterators {

    /**
     * Creates an {@code listIterator} returning the first {@code limitSize} elements of the
     * given {@code listIterator}. If the original {@code listIterator} does not contain that many
     * elements, the returned {@code listIterator} will have the same behavior as the original
     * {@code listIterator}. The returned {@code listIterator} supports {@link ListIterator#remove()},
     * {@link ListIterator#set(Object)}, {@link ListIterator#remove()} and {@link ListIterator#add(Object)}
     * if the original {@code listIterator} does. Calling {@link ListIterator#previous()} decreases internal count of
     * elements returned.
     *
     * @param listIterator the listIterator to limit.
     * @param limitSize    the maximum number of elements in the returned listIterator.
     *
     * @throws NullPointerException     if {@code listIterator} is null.
     * @throws IllegalArgumentException if {@code limitSize} is negative.
     */
    public static <E> ListIterator<E> limit(final ListIterator<E> listIterator, final int limitSize) {
        checkNotNull(listIterator);
        checkArgument(limitSize >= 0);
        return new ListIterator<E>() {

            private int count;

            @Override
            public boolean hasNext() {
                return count < limitSize && listIterator.hasNext();
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                count++;
                return listIterator.next();
            }

            @Override
            public boolean hasPrevious() {
                return listIterator.hasPrevious();
            }

            @Override
            public E previous() {
                count--;
                return listIterator.previous();
            }

            @Override
            public int nextIndex() {
                return listIterator.nextIndex();
            }

            @Override
            public int previousIndex() {
                return listIterator.previousIndex();
            }

            @Override
            public void remove() {
                listIterator.remove();
            }

            @Override
            public void set(E e) {
                listIterator.set(e);
            }

            @Override
            public void add(E e) {
                listIterator.add(e);
            }
        };
    }
}
