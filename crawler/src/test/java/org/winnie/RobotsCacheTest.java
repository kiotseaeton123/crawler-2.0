package org.winnie;

import org.winnie.utils.*;

import java.util.HashSet;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertTrue;

/**
 * unit test robots cache class
 * @author winnie
 */
public class RobotsCacheTest {
    private String url;

    @Before
    public void init(){
        url="https://amazon.com";
    }

    @Test
    public void testGetRobots(){
        HashSet<String> robots=RobotsCache.getInstance().getRobots(url);
        assertTrue(robots.contains("/exec/obidos/account-access-login"));
    }
}
