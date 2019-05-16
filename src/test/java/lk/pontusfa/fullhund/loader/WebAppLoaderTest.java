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
import java.io.InputStream;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

class WebAppLoaderTest {
    @Test
    void loaderWithNoWebXmlReturnsALoadedWebApp() {
        var classLoaderWithNoWebXml = classLoaderWithWebXml(null);
        var webAppLoader = new WebAppLoader(new DeploymentDescriptorAssembler(), classLoaderWithNoWebXml);

        WebApp webApp = webAppLoader.load();

        assertThat(webApp.getStatus()).isEqualTo(Status.LOADED);
    }

    @Test
    void loaderWithBadWebXmlReturnsFailedOnLoadWebApp() {
        var classLoaderWithBadWebXml = classLoaderWithWebXml("<bad xml></no bueno>");
        var webAppLoader = new WebAppLoader(new DeploymentDescriptorAssembler(), classLoaderWithBadWebXml);

        WebApp webApp = webAppLoader.load();

        assertThat(webApp.getStatus()).isEqualTo(Status.FAILED_ON_LOAD);
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
    void loadingClassNotImplementingServletReturnsFailedWebApp() {
        var deploymentDescriptor = new DeploymentDescriptorBuilder()
                                       .servletDescriptor("simpleServlet", getClass().getName())
                                       .build();
        var webAppLoader = new WebAppLoader(assembler(deploymentDescriptor), classLoaderWithWebXml(""));

        WebApp webApp = webAppLoader.load();

        assertThat(webApp.getStatus()).isEqualTo(Status.FAILED_ON_LOAD);
        assertThat(webApp.getServletContext().getServletRegistration("simpleServlet")).isNull();
    }

    @Test
    void loadingNonExistentServletClassReturnsFailedWebApp() {
        var deploymentDescriptor = new DeploymentDescriptorBuilder()
                                       .servletDescriptor("simpleServlet", "non.existent.Class")
                                       .build();
        var webAppLoader = new WebAppLoader(assembler(deploymentDescriptor), classLoaderWithWebXml(""));

        WebApp webApp = webAppLoader.load();

        assertThat(webApp.getStatus()).isEqualTo(Status.FAILED_ON_LOAD);
        assertThat(webApp.getServletContext().getServletRegistration("simpleServlet")).isNull();
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
