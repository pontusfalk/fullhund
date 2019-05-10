package lk.pontusfa.fullhund.assembler;

import lk.pontusfa.fullhund.assembler.WebAppDescriptorVerifier.Error;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class WebAppDescriptorVerifierTest {
    private WebAppDescriptorVerifier verifier;
    private WebAppDescriptor webAppDescriptor;

    @BeforeEach
    void setup() {
        webAppDescriptor = new WebAppDescriptor();
        verifier = new WebAppDescriptorVerifier(webAppDescriptor);
    }

    @Test
    void emptyWebAppVerifies() {
        Collection<Error> errors = verifier.verify();

        assertThat(errors).isEmpty();
    }

    @Test
    void webAppMustNotBeNull() {
        verifier = new WebAppDescriptorVerifier(null);

        Collection<Error> errors = verifier.verify();

        assertThat(errors).contains(Error.NO_WEB_APP_DESCRIPTOR);
    }

    @Test
    void servletVersionMustBe4_0() {
        webAppDescriptor.setVersion("3.1");

        Collection<Error> errors = verifier.verify();

        assertThat(errors).contains(Error.BAD_SERVLET_VERSION);
    }

    @Test
    void servletMustHaveName() {
        webAppDescriptor.addServletDescriptor(new ServletDescriptor(null, "className"));

        Collection<Error> errors = verifier.verify();

        assertThat(errors).contains(Error.NO_SERVLET_NAME);
    }

    @Test
    void servletMustHaveClassName() {
        webAppDescriptor.addServletDescriptor(new ServletDescriptor("name", null));

        Collection<Error> errors = verifier.verify();

        assertThat(errors).contains(Error.NO_SERVLET_CLASS_NAME);
    }

    @Test
    void servletMappingMustHaveServletName() {
        webAppDescriptor.addServletMappingDescriptor(new ServletMappingDescriptor(null, "/url"));

        Collection<Error> errors = verifier.verify();

        assertThat(errors).contains(Error.NO_SERVLET_MAPPING_NAME);
    }

    @Test
    void servletMappingMustHaveUrlPattern() {
        webAppDescriptor.addServletMappingDescriptor(new ServletMappingDescriptor("servlet name", null));

        Collection<Error> errors = verifier.verify();

        assertThat(errors).contains(Error.NO_SERVLET_MAPPING_URL_PATTERN);
    }
}
