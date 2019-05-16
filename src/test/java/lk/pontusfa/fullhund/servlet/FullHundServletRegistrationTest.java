package lk.pontusfa.fullhund.servlet;

import lk.pontusfa.fullhund.util.servlets.SimpleHttpServlet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FullHundServletRegistrationTest {
    private static final String SERVLET_NAME = "servletName";
    private FullHundServletRegistration registration;
    private ServletMappingResolver resolver;

    @BeforeEach
    void setup() {
        resolver = new ServletMappingResolver();
        registration = new FullHundServletRegistration(SERVLET_NAME, new SimpleHttpServlet(), resolver);
    }

    @Test
    void startsWithNoMapping() {
        assertThat(registration.getMappings()).isEmpty();
    }

    @Test
    void addsRootMapping() {
        registration.addMapping("");

        assertThat(registration.getMappings()).containsExactly("");
    }

    @Test
    void addsPathMapping() {
        registration.addMapping("/path/*");

        assertThat(registration.getMappings()).containsExactly("/path/*");
    }

    @Test
    void addsExtensionMapping() {
        registration.addMapping("*.jsp");

        assertThat(registration.getMappings()).containsExactly("*.jsp");
    }

    @Test
    void addsExactMapping() {
        registration.addMapping("/path");

        assertThat(registration.getMappings()).containsExactly("/path");
    }

    @Test
    void addsDefaultMapping() {
        registration.addMapping("/");

        assertThat(registration.getMappings()).containsExactly("/");
    }

    @Test
    void addInvalidMappingThrows() {
        assertThatThrownBy(() -> registration.addMapping("invalid/mapping"))
            .hasMessageContaining("invalid mapping");
    }

    @Test
    void addNullMappingThrows() {
        registration.addMapping((String) null);

        assertThat(registration.getMappings()).isEmpty();
    }

    @Test
    void addNoMappingThrows() {
        assertThatThrownBy(() -> registration.addMapping())
            .hasMessageContaining("at least 1 url pattern");

        assertThatThrownBy(() -> registration.addMapping((String[]) null))
            .hasMessageContaining("at least 1 url pattern");
    }

    @Test
    void mappingsIsntUpdatedWhenMappingConflicts() {
        new FullHundServletRegistration("anotherServlet", new SimpleHttpServlet(), resolver)
            .addMapping("/");

        Set<String> conflicts = registration.addMapping("/");

        assertThat(conflicts).containsExactly("anotherServlet");
        assertThat(registration.getMappings()).isEmpty();
    }

    @Test
    void addsMappingNormalized() {
        registration.addMapping("/path/../");

        assertThat(registration.getMappings()).containsExactly("/");
    }

    @Test
    void addsSameMappingDenormalizedOnce() {
        registration.addMapping("/");
        registration.addMapping("/path/..");

        assertThat(registration.getMappings()).containsExactly("/");
    }
}
