package org.winnie;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.winnie.db_utils.Database;
import org.winnie.geolocation_utils.IP2GeoResolver;

public class IP2GeoResolverTest {

    private IP2GeoResolver resolver;
    private Database db;

    @Before
    public void init() {
        db = new Database("geodata.db");
        resolver = new IP2GeoResolver(db);
    }

    @Test
    public void testResolveIP4() {
        String result = resolver.resolveIPv4("1.0.1.25");
        assertEquals(result, "AS,CN");
    }

    @Test
    public void testResolveIP6() {
        String result = resolver.resolveIPv6("2001:0200:0000:0000:0000:0000:0000:0001");
        assertEquals(result, "AS,JP");
    }

    @After
    public void close(){
        db.close();
    }
}
