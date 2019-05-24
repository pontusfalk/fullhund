package lk.pontusfa.fullhund.loader;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WarExploderTest {
    private WarExploder warExploder;

    @AfterEach
    void tearDown() {
        if (warExploder != null && warExploder.getExplodedWarLocation().exists()) {
            warExploder.getExplodedWarLocation().delete();
        }
    }

    @Test
    void explodedWarGetsCopied() throws IOException {
        var warPath = "war/one-servlet-one-mapping-xml-exploded/";

        warExploder = new WarExploder(warFile(warPath));
        var copy = warExploder.getExplodedWarLocation().toPath();

        assertTrue(copy.resolve("WEB-INF/web.xml").toFile().exists());
        assertTrue(copy.resolve("WEB-INF/classes/lk/pontusfa/fullhund/webapp/sample/SimpleServlet.class").toFile()
                       .exists());
    }

    @Test
    void warFileGetsCopied() throws IOException {
        var warPath = "war/one-servlet-one-mapping-xml.war";

        warExploder = new WarExploder(warFile(warPath));
        var copy = warExploder.getExplodedWarLocation().toPath();

        assertTrue(copy.resolve("WEB-INF/web.xml").toFile().exists());
        assertTrue(copy.resolve("WEB-INF/classes/lk/pontusfa/fullhund/webapp/sample/SimpleServlet.class").toFile()
                       .exists());
    }

    @Test
    void exploderMustGetAWarLocation() {
        @SuppressWarnings("ConstantConditions")
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new WarExploder(null));
        assertThat(exception).hasMessageContaining("not be null");
    }

    @Test
    void exploderMustGetExistingLocation() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                                                          () -> new WarExploder(new File("should/not/exist")));
        assertThat(exception).hasMessageContaining("not exist");
    }

    @Test
    void exploderMustGetProperWarFile() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                                                          () -> new WarExploder(warFile("log4j2-test.xml"))); //meh
        assertThat(exception).hasMessageContaining("not a war file");
    }

    private File warFile(String path) {
        try {
            URL explodedWar = getClass().getClassLoader().getResource(path);
            if (explodedWar != null) {
                return new File(explodedWar.toURI());
            } else {
                throw new RuntimeException("no such file: " + path);
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
