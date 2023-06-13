package pl.mjaron.datanode;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class HttpNodeTest {

    void checkNode(INode node) {
        System.out.println("Checking node: " + node.toString());
        System.out.println("Node exists: " + node.exists());
        System.out.println("Node content size: " + node.getSize());
        System.out.println("Node content:\n" + node.readString());
    }

    void checkNode(final String url) {
        HttpNode httpNode = new HttpNode(url);
        checkNode(httpNode);
    }

    @Test
    void basic() {
        checkNode("http://example.com/");
        checkNode("https://example.com/");
    }

    @Test
    void getChildTest() {
        INode node = new HttpNode("http://example.com/").getChild("/fish").getChild("long/").getChild("/wide/").getChild("big/").getChild("index.html/");
        System.out.println("Crazy node is: " + node);
        assertEquals("http://example.com/fish/long/wide/big/index.html/", node.toString());
        assertEquals("/fish/long/wide/big/index.html/", node.getPath());
    }

    @Test
    void getNameTest() {
        INode n2 = new HttpNode("http://example.com/");
        assertEquals("", n2.getName());
        assertEquals("", n2.getChild("/").getName());
        assertEquals("", n2.getChild("/").getChild("/").getName());
        assertEquals("a", n2.getChild("/a").getName());
        assertEquals("a", n2.getChild("/a/").getName());
        assertEquals("b", n2.getChild("/a/").getChild("/b").getName());
        assertEquals("index.html", new HttpNode("http://example.com/index.html").getName());
    }

    @Test
    void exists() {
        assertFalse(new HttpNode("http://example.com/fish").exists());

        assertFalse(new HttpNode("http://example.com/fish/").exists());

        assertTrue(new HttpNode("http://example.com/").exists());

        assertTrue(new HttpNode("https://www.iana.org").getChild("domains").getChild("reserved").isFile());
    }
}
