package lk.pontusfa.fullhund.loader;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FullHundClassLoaderTest {
    @Test
    void classLoaderWithWebApp() throws IOException {
        var webAppLocation = File.createTempFile("pref", "suff");
        webAppLocation.deleteOnExit();

        assertThatCode(() -> new FullHundClassLoader(webAppLocation)).doesNotThrowAnyException();
    }

    @Test
    void classLoaderWithNonExistantWebAppThrows() throws IOException {
        var parent = File.createTempFile("pref", "suff");
        parent.deleteOnExit();
        var nonExistantWebAppLocation = parent.toPath().resolve("no-such-file").toFile();

        assertThatThrownBy(() -> new FullHundClassLoader(nonExistantWebAppLocation))
            .hasMessageContaining("web app location doesn't exist");
    }

    @Test
    void classLoaderWithNullWebAppThrows() {
        assertThatThrownBy(() -> new FullHundClassLoader(null))
            .hasMessage("web app location doesn't exist: null");
    }
}
