package lk.pontusfa.fullhund.assembler;

import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static lk.pontusfa.fullhund.assembler.DeploymentDescriptor.Status.COMPLETE;
import static lk.pontusfa.fullhund.assembler.DeploymentDescriptor.Status.ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class DeploymentDescriptorAssemblerTest {

    @Test
    void descriptorWithEmptyWebApp() {
        DeploymentDescriptorAssembler assembler =
            assemblerFromXml("<?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
                             "<web-app xmlns=\"http://xmlns.jcp.org/xml/ns/javaee\" " +
                             "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                             "xsi:schemaLocation=\"http://xmlns.jcp.org/xml/ns/javaee " +
                             "http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd\" " +
                             "version=\"4.0\"></web-app>");

        DeploymentDescriptor descriptor = assembler.assemble();

        assertThat(descriptor.getStatus()).isEqualTo(COMPLETE);
        assertThat(descriptor.getWebAppDescriptor()).isNotNull();
    }

    @Test
    void emptyDescriptorFails() {
        DeploymentDescriptorAssembler assembler = assemblerFromXml("");

        DeploymentDescriptor descriptor = assembler.assemble();

        assertThat(descriptor.getStatus()).isEqualTo(ERROR);
        assertThat(descriptor.getErrors())
            .containsExactly("org.xml.sax.SAXParseException; lineNumber: 1; columnNumber: 1; Premature end of file.");
    }

    @Test
    void nullWebAppDescriptorFails() {
        DeploymentDescriptorAssembler assembler = new DeploymentDescriptorAssembler(null);

        DeploymentDescriptor descriptor = assembler.assemble();

        assertThat(descriptor.getStatus()).isEqualTo(ERROR);
        assertThat(descriptor.getErrors())
            .containsExactly("java.lang.IllegalArgumentException: reader parameter must not be null");
    }

    @Test
    void servletHasNameAndClass() {
        DeploymentDescriptorAssembler assembler =
            assemblerFromXml("<?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
                             "<web-app xmlns=\"http://xmlns.jcp.org/xml/ns/javaee\" " +
                             "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                             "xsi:schemaLocation=\"http://xmlns.jcp.org/xml/ns/javaee " +
                             "http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd\" " +
                             "version=\"4.0\">" +
                             "<servlet>" +
                             "<servlet-name>serv</servlet-name>" +
                             "<servlet-class>className</servlet-class>" +
                             "</servlet>" +
                             "</web-app>");

        DeploymentDescriptor descriptor = assembler.assemble();

        assertThat(descriptor.getStatus()).isEqualTo(COMPLETE);
        assertThat(descriptor.getWebAppDescriptor().getServletDescriptors())
            .extracting("servletName", "servletClass")
            .containsExactly(tuple("serv", "className"));
    }

    @Test
    void servletMappingHasNameAndUrlPattern() {
        DeploymentDescriptorAssembler assembler =
            assemblerFromXml("<?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
                             "<web-app xmlns=\"http://xmlns.jcp.org/xml/ns/javaee\" " +
                             "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                             "xsi:schemaLocation=\"http://xmlns.jcp.org/xml/ns/javaee " +
                             "http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd\" " +
                             "version=\"4.0\">" +
                             "<servlet-mapping>" +
                             "<servlet-name>name</servlet-name>" +
                             "<url-pattern>pattern</url-pattern>" +
                             "</servlet-mapping>" +
                             "</web-app>");

        DeploymentDescriptor descriptor = assembler.assemble();

        assertThat(descriptor.getStatus()).isEqualTo(COMPLETE);
        assertThat(descriptor.getWebAppDescriptor().getServletMappingDescriptors())
            .extracting("servletName", "urlPattern")
            .containsExactly(tuple("name", "pattern"));
    }

    private static DeploymentDescriptorAssembler assemblerFromXml(String xmlSource) {
        return new DeploymentDescriptorAssembler(new StringReader(xmlSource));
    }
}
