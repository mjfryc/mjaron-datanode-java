package pl.mjaron.filenode;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents single file or directory in any filesystem structure.
 */
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
     * Negation of {@link #isFile} function. Tells whether this node is a directory or not.
     *
     * @return True if this node is a directory, so it is possible to list children of this node.
     */
    boolean isDirectory();

    /**
     * @return True if given file exists.
     */
    boolean exists();

    /**
     * Create this file or directory if not exists.
     */
    void create();

    /**
     * Removes this file from filesystem.
     *
     * @throws RuntimeException when filesystem is readonly.
     */
    void remove();

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
     * @return File object if it is possible. It depends on its implementation. Usually it is possible only for local
     * filesystem implementations.
     * @throws RuntimeException when cannot return a Java file.
     */
    File asJavaFile();
}
