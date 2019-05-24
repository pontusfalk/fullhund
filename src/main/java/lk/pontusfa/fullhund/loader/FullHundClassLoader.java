package lk.pontusfa.fullhund.loader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

class FullHundClassLoader extends URLClassLoader {
    FullHundClassLoader(File webAppLocation) {
        super(new URL[] {}, ClassLoader.getSystemClassLoader());

        if (webAppLocation == null || !webAppLocation.exists()) {
            throw new IllegalArgumentException("web app location doesn't exist: " + webAppLocation);
        }

        try {
            addURL(webAppLocation.toURI().toURL());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
