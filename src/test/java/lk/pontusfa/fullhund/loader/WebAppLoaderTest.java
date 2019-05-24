package lk.pontusfa.fullhund.loader;

import lk.pontusfa.fullhund.assembler.DeploymentDescriptor;
import lk.pontusfa.fullhund.assembler.DeploymentDescriptorAssembler;
import lk.pontusfa.fullhund.assembler.DeploymentDescriptorBuilder;
import lk.pontusfa.fullhund.servlet.WebApp;
import lk.pontusfa.fullhund.servlet.WebApp.Status;
import lk.pontusfa.fullhund.util.servlets.SimpleHttpServlet;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;

class WebAppLoaderTest {
    @Test
    void loaderWithNoWebXmlLoads() {
        var classLoaderWithNoWebXml = classLoaderWithWebXml(null);
        var webAppLoader = new WebAppLoader(new DeploymentDescriptorAssembler(), classLoaderWithNoWebXml);

        WebApp webApp = webAppLoader.load();

        assertThat(webApp.getStatus()).isEqualTo(Status.LOADED);
    }

    @Test
    void loaderWithBadWebXmlFails() {
        var classLoaderWithBadWebXml = classLoaderWithWebXml("<bad></no bueno>");
        var webAppLoader = new WebAppLoader(new DeploymentDescriptorAssembler(), classLoaderWithBadWebXml);

        WebApp webApp = webAppLoader.load();

        assertThat(webApp.getStatus()).isEqualTo(Status.FAILED_ON_LOAD);
        assertThat(webApp.getMessages()).anyMatch(message -> message.contains("javax.xml.bind.UnmarshalException"));
    }

    @Test
    void loadingSingleHttpServletWithOneMapping() {
        var deploymentDescriptor = new DeploymentDescriptorBuilder()
                                       .servletDescriptor("simpleServlet", SimpleHttpServlet.class.getName())
                                       .servletMappingDescriptor("simpleServlet", "/simpleServletUrl")
                                       .build();
        var webAppLoader = new WebAppLoader(assembler(deploymentDescriptor), classLoaderWithWebXml(""));

        WebApp webApp = webAppLoader.load();

        var servletRegistration = webApp.getServletContext().getServletRegistration("simpleServlet");
        assertThat(servletRegistration.getClassName()).isEqualTo(SimpleHttpServlet.class.getName());
        assertThat(servletRegistration.getMappings()).containsExactly("/simpleServletUrl");
    }

    @Test
    void loadingClassNotImplementingServletFails() {
        var deploymentDescriptor = new DeploymentDescriptorBuilder()
                                       .servletDescriptor("simpleServlet", getClass().getName())
                                       .build();
        var webAppLoader = new WebAppLoader(assembler(deploymentDescriptor), classLoaderWithWebXml(""));

        WebApp webApp = webAppLoader.load();

        assertThat(webApp.getStatus()).isEqualTo(Status.FAILED_ON_LOAD);
        assertThat(webApp.getServletContext().getServletRegistration("simpleServlet")).isNull();
        assertThat(webApp.getMessages()).anyMatch(message -> message.contains("does not implement Servlet"));
    }

    @Test
    void loadingNonExistentServletClassFails() {
        var deploymentDescriptor = new DeploymentDescriptorBuilder()
                                       .servletDescriptor("simpleServlet", "non.existent.Class")
                                       .build();
        var webAppLoader = new WebAppLoader(assembler(deploymentDescriptor), classLoaderWithWebXml(""));

        WebApp webApp = webAppLoader.load();

        assertThat(webApp.getStatus()).isEqualTo(Status.FAILED_ON_LOAD);
        assertThat(webApp.getServletContext().getServletRegistration("simpleServlet")).isNull();
        assertThat(webApp.getMessages()).anyMatch(message -> message.contains("non.existent.Class not found"));
    }

    @Test
    void loadingWithConflictingServletMappingFails() {
        var deploymentDescriptor = new DeploymentDescriptorBuilder()
                                       .servletDescriptor("simpleServlet", SimpleHttpServlet.class.getName())
                                       .servletMappingDescriptor("simpleServlet", "/servletUrl")
                                       .servletDescriptor("conflictingServlet", SimpleHttpServlet.class.getName())
                                       .servletMappingDescriptor("conflictingServlet", "/servletUrl").build();
        var webAppLoader = new WebAppLoader(assembler(deploymentDescriptor), classLoaderWithWebXml(""));

        WebApp webApp = webAppLoader.load();

        assertThat(webApp.getStatus()).isEqualTo(Status.FAILED_ON_LOAD);
        assertThat(webApp.getMessages()).containsExactly("servlet mapping conflicts: [/servletUrl]");
    }

    @Test
    void exceptionWhenLoadingDescriptorFails() {
        var exceptionMessage = "IO Exception during close";
        InputStream exceptionStream = new InputStream() {
            public int read() {
                return 0;
            }

            public void close() throws IOException {
                throw new IOException(exceptionMessage);
            }
        };
        var classLoader = new FullHundClassLoader(explodedWarFile()) {
            public InputStream getResourceAsStream(String name) {
                return exceptionStream;
            }
        };
        var deploymentDescriptor = new DeploymentDescriptorBuilder().build();
        var webAppLoader = new WebAppLoader(assembler(deploymentDescriptor), classLoader);

        WebApp webApp = webAppLoader.load();

        assertThat(webApp.getStatus()).isEqualTo(Status.FAILED_ON_LOAD);
        assertThat(webApp.getMessages()).containsExactly(singleton(exceptionMessage).toString());
    }

    private File explodedWarFile() {
        try {
            var warUri = getClass().getClassLoader().getResource("war/one-servlet-one-mapping-xml-exploded/").toURI();
            return new File(warUri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private ClassLoader classLoaderWithWebXml(String webXml) {
        return new FullHundClassLoader(explodedWarFile()) {
            public InputStream getResourceAsStream(String name) {
                if (webXml == null) {
                    return null;
                }
                return name.equals("WEB-INF/web.xml") ?
                       new ByteArrayInputStream(webXml.getBytes()) :
                       super.getResourceAsStream(name);
            }
        };
    }

    private static DeploymentDescriptorAssembler assembler(DeploymentDescriptor deploymentDescriptor) {
        return new DeploymentDescriptorAssembler() {
            @Override
            public DeploymentDescriptor assemble(InputStream xmlSource) {
                return deploymentDescriptor;
            }
        };
    }
}
