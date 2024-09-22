package org.winnie;

import org.winnie.utils.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class WebpageTest {
    private String url;
    private Webpage webpage;

    @Before
    public void init() {
        url = "https://example.com";
        webpage=new Webpage(url);
    }

    @Test
    public void testGetLinks() throws NoLinksFoundException{
        assertEquals(1, webpage.getLinks().size());
        assertEquals("https://www.iana.org/domains/example", webpage.getLinks().get(0));
    }

    @Test
    public void testGetParagraphs(){
        assertEquals("This domain is for use in illustrative examples in documents. You may use this domain in literature without prior coordination or asking for permission. More information...", webpage.getParagraphs());
    }

}
