package pl.mjaron.filenode;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents single file or directory in any filesystem structure.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface INode {

    /**
     * @return Name of current node. It is just file name with extension or directory name.
     */
    String getName();

    /**
     * @return Absolute path to the node if possible. Result depends on implementation.
     */
    String getPath();

    /**
     * Tells whether this node is a file or not.
     *
     * @return True if this node is a file, so it is potentially possible to write or read from a file.
     */
    boolean isFile();

    /**
     * Tells whether this node is a directory or not. Usually negation of {@link #isFile} function when given node
     * exists.
     *
     * @return True if this node is a directory, so it is possible to list children of this node.
     */
    boolean isDirectory();

    /**
     * @return True if given file exists.
     */
    boolean exists();

    /**
     * Creates this directory if it is possible.
     *
     * @return This reference.
     * @throws RuntimeException when filesystem is read only or because of other errors.
     */
    INode mkdirs();

    /**
     * Creates this file if it is possible.
     *
     * @return This reference.
     * @throws RuntimeException when filesystem is read only or because of other errors.
     */
    INode touch();

    /**
     * Removes this file from filesystem.
     *
     * @return This reference.
     * @throws RuntimeException when filesystem is readonly.
     */
    INode remove();

    /**
     * @return Size of the file.
     * @throws RuntimeException when this is impossible to obtain file size.
     */
    long getSize();

    /**
     * Allows reading from the file.
     *
     * @return InputStream which allows reading content of the file.
     * @throws RuntimeException when given node is not a file, or it is impossible to create input stream.
     */
    InputStream getInputStream();

    /**
     * Allows writing to the file.
     *
     * @return OutputStream which allows writing to the file.
     * @throws RuntimeException when given node is not a file, or it is impossible to create output stream.
     */
    OutputStream getOutputStream();

    /**
     * @return Names of child nodes.
     */
    String[] getChildrenNames();

    /**
     * @return Direct children nodes.
     */
    List<INode> getChildren();

    /**
     * @param name Name of child node.
     * @return Child node.
     */
    INode getChild(String name);

    /**
     * @return File object if it is possible. It depends on its implementation. Usually it is possible only for local
     * filesystem implementations.
     * @throws RuntimeException when cannot return a Java file.
     */
    File asJavaFile();

    /**
     * Writes given data using new output stream.
     *
     * @param what Data to write.
     * @return This reference.
     */
    default INode write(final byte[] what) {
        try (OutputStream out = getOutputStream()) {
            out.write(what);
        } catch (final IOException e) {
            throw new RuntimeException("Output stream failure.", e);
        }
        return this;
    }

    /**
     * Writes given String using new output stream.
     *
     * @param what    String to write.
     * @param charset Charset used to encode String.
     * @return This reference.
     */
    default INode write(final String what, final java.nio.charset.Charset charset) {
        return write(what.getBytes(charset));
    }

    /**
     * Writes given String using new output stream.
     *
     * @param what String to write as UTF_8 bytes.
     * @return This reference.
     */
    default INode write(final String what) {
        return write(what, StandardCharsets.UTF_8);
    }

    default byte[] readBytes() {
        try (InputStream inputStream = getInputStream()) {
            // Source: https://www.baeldung.com/convert-input-stream-to-array-of-bytes#java-1
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            return buffer.toByteArray();
        } catch (final IOException e) {
            throw new RuntimeException("Input stream failure.", e);
        }
    }

    default String readString(final java.nio.charset.Charset charset) {
        try (InputStream inputStream = getInputStream()) {
            // Source: https://stackoverflow.com/a/35446009/6835932
            final ByteArrayOutputStream result = new ByteArrayOutputStream();
            final byte[] buffer = new byte[1024];
            for (int length; (length = inputStream.read(buffer)) != -1; ) {
                result.write(buffer, 0, length);
            }
            // StandardCharsets.UTF_8.name() > JDK 7
            return result.toString(charset.name());
        } catch (IOException e) {
            throw new RuntimeException("Input stream failure.", e);
        }
    }

    default String readString() {
        return readString(StandardCharsets.UTF_8);
    }

    /**
     * @return Count of (direct) children.
     */
    default int getChildrenCount() {
        return this.getChildrenNames().length;
    }

    /**
     * @param result After calling, it will contain all child nodes which are files, including nested files.
     */
    default void getFileDescendants(final List<INode> result) {
        for (final INode child : this.getChildren()) {
            if (child.isFile()) {
                result.add(child);
            } else {
                child.getFileDescendants(result);
            }
        }
    }

    /**
     * @return All child nodes which are files, including nested files.
     */
    default List<INode> getFileDescendants() {
        List<INode> result = new ArrayList<>();
        getFileDescendants(result);
        return result;
    }

    /**
     * @param result After calling, it will contain all child nodes including directories and other nested files.
     */
    default void getDescendants(final List<INode> result) {
        for (final INode child : this.getChildren()) {
            result.add(child);
            if (child.isDirectory()) {
                child.getDescendants(result);
            }
        }
    }

    /**
     * @return All child nodes including directories and other nested files.
     */
    default List<INode> getDescendants() {
        List<INode> result = new ArrayList<>();
        getDescendants(result);
        return result;
    }

    /**
     * Removes all children but not this node.
     *
     * @return This reference.
     */
    default INode removeChildren() {
        for (final INode child : this.getChildren()) {
            child.remove();
        }
        return this;
    }

    /**
     * Checks whether this node exists, throws when it doesn't.
     *
     * @return This reference.
     * @throws RuntimeException when this node doesn't exist.
     */
    default INode assertExists() {
        if (this.exists()) {
            return this;
        }
        throw new RuntimeException("Assertion failed: Node doesn't exist: [" + getPath() + "].");
    }
}
