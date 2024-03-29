package pl.mjaron.datanode;

import org.junit.jupiter.api.*;

import java.util.List;
import java.util.regex.Pattern;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FileNodeTest {

    @Test
    @Order(-1)
    void demoTest() {
        final String sentence = "Hello world\r\nByeWorld\n\r";
        final INode root = new FileNode("root_dir");
        root
                .getChild("subdirectory").mkdirs()
                .getChild("file.txt").remove().touch()
                .write(sentence);

        final String checkedContent = new FileNode("root_dir/subdirectory/file.txt").readString();
        Assertions.assertEquals(sentence, checkedContent);
    }

    @Test
    @Order(0)
    void getName() {
        INode node = new FileNode(".");
        System.out.println("Node name: [" + node.getName() + "].");
        Assertions.assertFalse(node.getName().isEmpty());
    }

    @Test
    @Order(1)
    void getPath() {
        INode node = new FileNode(".");
        System.out.println("Node path: [" + node.getPath() + "].");
        Assertions.assertFalse(node.getPath().isEmpty());
    }

    @Test
    @Order(2)
    void isFile() {
        INode node = new FileNode("myFile.txt");
        node.touch();
        Assertions.assertTrue(node.isFile());
    }

    @Test
    @Order(3)
    void isDirectory() {
        INode node = new FileNode("myDir");
        node.mkdirs();
        Assertions.assertTrue(node.isDirectory());
    }

    @Test
    @Order(4)
    void exists() {
        INode node = new FileNode("myDir");
        Assertions.assertTrue(node.exists());
    }

    @Test
    @Order(5)
    void remove() {
        INode node = new FileNode("myDir");
        node.remove();
        Assertions.assertFalse(node.exists());
    }

    @Test
    @Order(6)
    void getSize() {
        INode node = new FileNode("myFile.txt");
        Assertions.assertEquals(0, node.getSize());
    }

    @Test
    @Order(7)
    void mkdirs() {
        INode node = new FileNode("myDir");
        node.getChild("myNestedDir").mkdirs();
    }

    @Test
    @Order(8)
    void touch() {
        INode node = new FileNode("myDir");
        node.getChild("myNestedDir").getChild("nestedFile.txt").touch();
    }

    @Test
    @Order(9)
    void getInputStream() {
    }

    @Test
    @Order(10)
    void getOutputStream() {
    }

    @Test
    @Order(11)
    void getChildren() {
        INode node = new FileNode("myDir");
        Assertions.assertEquals(1, node.getChildrenNames().size());
    }

    @Test
    @Order(11)
    void getChildrenPatternOk() {
        INode node = new FileNode("myDir");
        Assertions.assertEquals(1, node.getChildren(Pattern.compile("myNested.*")).size());
    }

    @Test
    @Order(11)
    void getChildrenPatternNok() {
        INode node = new FileNode("myDir");
        Assertions.assertEquals(0, node.getChildren(Pattern.compile("wrong.*")).size());
    }

    @Test
    @Order(11)
    void noChildrenTest() {
        INode node = new FileNode("notExistingDir");
        Assertions.assertEquals(0, node.getChildren().size());
    }

    @Test
    @Order(12)
    void getChild() {
        INode node = new FileNode("myDir");
        Assertions.assertTrue(node.getChild("myNestedDir").exists());
    }

    @Test
    @Order(13)
    void asJavaFile() {
        INode node = new FileNode("myFile.txt");
        Assertions.assertNotNull(node.asJavaFile());
    }

    @Test
    @Order(14)
    void getFileDescendants() {
        INode node = new FileNode("myDir");
        List<INode> descendants = node.getFileDescendants();
        System.out.println("Descendants: " + descendants);
        Assertions.assertEquals(1, descendants.size());
    }

    @Test
    @Order(15)
    void getDescendants() {
        INode node = new FileNode("myDir");
        List<INode> descendants = node.getDescendants();
        System.out.println("Descendants: " + descendants);
        Assertions.assertEquals(2, descendants.size());
    }
}