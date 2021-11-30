# mjaron-filenode-java

[![Java CI with Gradle](https://github.com/mjfryc/mjaron-filenode-java/actions/workflows/gradle.yml/badge.svg)](https://github.com/mjfryc/mjaron-filenode-java/actions/workflows/gradle.yml)
[![Gradle Package](https://github.com/mjfryc/mjaron-filenode-java/actions/workflows/gradle-publish.yml/badge.svg)](https://github.com/mjfryc/mjaron-filenode-java/actions/workflows/gradle-publish.yml)

**[DRAFT]** Abstract file tree with several implementations.

## API description

This library defines
universal [INode](https://github.com/mjfryc/mjaron-filenode-java/blob/main/src/main/java/pl/mjaron/filenode/INode.java)
interface and provides some implementations for this interface:

* FileNode - standard filesystem node based on `java.io.File`.

```java
import pl.mjaron.filenode;

public class Sample {
    public static void main(String[] args) {
        final String sentence = "Hello world\nByeWorld.";
        final INode root = new FileNode("root_dir");
        root
                .getChild("subdirectory").mkdirs()
                .getChild("file.txt").remove().touch()
                .write(sentence);

        final String checkedContent = new FileNode("root_dir/subdirectory/file.txt").readString();
        Assertions.assertEquals(sentence, checkedContent);
    }
}
```
