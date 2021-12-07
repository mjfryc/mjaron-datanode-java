# mjaron-filenode-java

[![Java CI with Gradle](https://github.com/mjfryc/mjaron-filenode-java/actions/workflows/gradle.yml/badge.svg)](https://github.com/mjfryc/mjaron-filenode-java/actions/workflows/gradle.yml)
[![Gradle Package](https://github.com/mjfryc/mjaron-filenode-java/actions/workflows/gradle-publish.yml/badge.svg)](https://github.com/mjfryc/mjaron-filenode-java/actions/workflows/gradle-publish.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.mjfryc/mjaron-filenode-java?color=dark-green&style=flat)](https://search.maven.org/artifact/io.github.mjfryc/mjaron-filenode-java/)

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

## Integration

### Maven Central

`dependencies {`
```gradle
implementation 'io.github.mjfryc:mjaron-filenode-java:0.0.7'
```
`}`

### GitHub Packages

Click the [Packages section](https://github.com/mjfryc?tab=packages&repo_name=mjaron-filenode-java) on the right.

### Download directly

1. Click the [Packages section](https://github.com/mjfryc?tab=packages&repo_name=mjaron-filenode-java) on the right.
2. Find and download jar package from files list to e.g. `your_project_root/libs` dir.
3. Add this jar to project dependencies in build.gradle, e.g:

```gradle
implementation files(project.rootDir.absolutePath + '/libs/mjaron-filenode-java-0.0.3.jar')
```
