package pl.mjaron.datanode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class HttpNode implements INode {

    URL url = null;
    String method = "GET";

    HttpURLConnection cachedConnection = null;

    HttpURLConnection connect() {
        if (cachedConnection != null) {
            return cachedConnection;
        }
        try {
            cachedConnection = (HttpURLConnection) url.openConnection();
            cachedConnection.setRequestMethod(method);
            cachedConnection.connect();
            return cachedConnection;
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpNode(final URL url) {
        this.url = url;
    }

    public HttpNode(final String url) {
        try {
            this.url = new URL(url);
        } catch (final MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        String file = url.getFile();
        if (file.isEmpty() || file.equals("/"))
        {
            return "";
        }
        String name;
        int endIndex = file.length();
        if (file.charAt(endIndex - 1) == '/')
        {
            --endIndex;
        }
        int lastSlash = file.lastIndexOf('/', endIndex - 1);
        if (lastSlash >= 0) {
            name = file.substring(lastSlash + 1, endIndex);
        } else {
            name = file.substring(0, endIndex);
        }
        return name;
    }

    @Override
    public String toString() {
        return url.toString();
    }

    @Override
    public String getPath() {
        return url.getPath();
    }

    @Override
    public boolean isFile() {
        return exists();
    }

    @Override
    public boolean isDirectory() {
        return !exists();
    }

    @Override
    public boolean exists() {
        final HttpURLConnection c = connect();
        try {
            final int responseCode = c.getResponseCode();
            return 200 == responseCode;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public INode mkdirs() {
        return this;
    }

    @Override
    public INode touch() {
        return this;
    }

    @Override
    public INode remove() {
        return this;
    }

    @Override
    public long getSize() {
        return connect().getContentLength();
    }

    @Override
    public InputStream getInputStream() {
        try {
            return connect().getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public OutputStream getOutputStream() {
        try {
            return connect().getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getChildrenNames() {
        return Collections.emptyList();
    }

    @Override
    public List<INode> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public INode getChild(String name) {
        try {
            String path;
            final int slashCount = ((url.getPath().endsWith("/")) ? 1 : 0) + ((name.startsWith("/")) ? 1 : 0);
            if (slashCount == 0) {
                path = url.getPath() + "/" + name;
            } else if (slashCount == 1) {
                path = url.getPath() + name;
            } else {
                path = url.getPath() + name.substring(1);
            }
            return new HttpNode(new URL(url, path));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public File asJavaFile() {
        return null;
    }
}
