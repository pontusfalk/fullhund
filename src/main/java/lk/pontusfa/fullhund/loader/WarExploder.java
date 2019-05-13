package lk.pontusfa.fullhund.loader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

class WarExploder {
    private static final Logger logger = LogManager.getLogger();

    private final File warLocation;
    private final File explodedWarLocation;

    WarExploder(File warLocation) throws IOException {
        this.warLocation = warLocation;
        validateWarLocation();

        explodedWarLocation = Files.createTempDirectory("fullhund." + warLocation.getName()).toFile();
        if (warLocation.isDirectory()) {
            copyExplodedWar();
            logger.debug("copied exploded {} to {}", warLocation, explodedWarLocation);
        } else {
            explodeWar();
            logger.debug("exploded {} to {}", warLocation, explodedWarLocation);
        }
    }

    public File getExplodedWarLocation() {
        return explodedWarLocation;
    }

    private void validateWarLocation() {
        if (warLocation == null) {
            throw new IllegalArgumentException("war location must not be null");
        }

        if (!warLocation.exists()) {
            throw new IllegalArgumentException("location does not exist: " + warLocation.getAbsolutePath());
        }

        if (warLocation.isFile() && !warLocation.getName().endsWith(".war")) {
            throw new IllegalArgumentException("not a war file: " + warLocation.getAbsolutePath());
        }
    }

    private void explodeWar() throws IOException {
        try (JarFile jarFile = new JarFile(warLocation)) {
            for (JarEntry original : Collections.list(jarFile.entries())) {
                File copy = new File(explodedWarLocation, original.getName());
                if (original.isDirectory()) {
                    copy.mkdirs();
                } else {
                    try (InputStream is = jarFile.getInputStream(original);
                         OutputStream os = new FileOutputStream(copy)) {
                        copy.getParentFile().mkdirs();
                        os.write(is.read());
                        logger.debug("copied {} to {}", original, copy);
                    }
                }
            }
        }
    }

    private void copyExplodedWar() throws IOException {
        try (Stream<Path> walk = Files.walk(warLocation.toPath())) {
            walk.forEach(this::copyFile);
        }
    }

    private void copyFile(Path original) {
        if (!original.equals(warLocation.toPath())) {
            try {
                var originalRelPath = warLocation.toPath().relativize(original).toString();
                Path copy = Paths.get(explodedWarLocation.getPath(), originalRelPath);
                Files.copy(original, copy, StandardCopyOption.REPLACE_EXISTING);
                if (copy.toFile().isFile()) {
                    logger.debug("copied {} to {}", original, copy);
                }
            } catch (IOException e) {
                throw new UncheckedIOException(String.format("could not copy %s to %s", original, explodedWarLocation), e);
            }
        }
    }
}
