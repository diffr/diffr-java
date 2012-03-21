package diffr.suffixtree.impl;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;

/**
 * Tests {@link Node}.
 * 
 * @author Jakub D Kozlowski
 * @since 0.1
 */
@Test(groups = "fast")
public class NodeTest {
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorIndexLessThanZero() {
        new Node(-1);
    }
    
    @Test
    public void testConstructor() {
        assertThat(new Node(1).getIndex(), is(1));
    }
    
    @Test
    public void testToString() {
        assertThat(new Node(1), hasToString(equalTo("Node{index=1}")));
    }
}
