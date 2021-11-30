package pl.mjaron.filenode;

import java.io.*;
import java.util.Objects;

/**
 * Standard Java node implementation. Based on File interface.
 */
public class FileNode implements INode {

    private final File file;

    public FileNode(final File file) {
        this.file = file;
    }

    public FileNode(final String filePath) {
        this(new File(filePath));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    static void deleteFileOrDir(final File file) {
        if (file.isDirectory()) {
            for (final File childEntry : Objects.requireNonNull(file.listFiles())) {
                deleteFileOrDir(childEntry);
            }
        }
        file.delete();
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public String getPath() {
        return file.getAbsolutePath();
    }

    @Override
    public boolean isFile() {
        return file.isFile();
    }

    @Override
    public boolean isDirectory() {
        return file.isDirectory();
    }

    @Override
    public boolean exists() {
        return file.exists();
    }

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public INode mkdirs() {
        file.mkdirs();
        return this;
    }

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public INode touch() {
        try {
            file.createNewFile();
            return this;
        } catch (IOException e) {
            throw new RuntimeException("Failed to create a new file.", e);
        }
    }

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public INode remove() {
        deleteFileOrDir(file);
        return this;
    }

    @Override
    public long getSize() {
        return file.length();
    }

    @Override
    public InputStream getInputStream() {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to create input stream.", e);
        }
    }

    @Override
    public OutputStream getOutputStream() {
        try {
            return new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to create output stream.", e);
        }
    }

    @Override
    public String[] getChildren() {
        final File[] childFiles = file.listFiles();
        final String[] childNames = new String[(childFiles != null) ? childFiles.length : 0];
        if (childFiles != null) {
            for (int i = 0; i < childFiles.length; ++i) {
                childNames[i] = childFiles[i].getName();
            }
        }
        return childNames;
    }

    @Override
    public INode getChild(String name) {
        return new FileNode(new File(file, name));
    }

    @Override
    public File asJavaFile() {
        return file;
    }
}
