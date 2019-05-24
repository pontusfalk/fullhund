package lk.pontusfa.fullhund.servlet;

import lk.pontusfa.fullhund.servlet.FullHundRegistration.Status;
import lk.pontusfa.fullhund.util.servlets.SimpleHttpServlet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.http.HttpServlet;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FullHundServletContextTest {
    private static final String SERVLET_NAME = "servletName";
    private static final String SERVLET_CLASS_NAME = SimpleHttpServlet.class.getName();

    private FullHundServletContext servletContext;

    @BeforeEach
    void setUp() {
        servletContext = new FullHundServletContext(getClass().getClassLoader());
    }

    @Test
    void addingServletByClassName() {
        Dynamic registration = servletContext.addServlet(SERVLET_NAME, SERVLET_CLASS_NAME);

        assertThat(registration.getName()).isEqualTo(SERVLET_NAME);
        assertThat(registration.getClassName()).isEqualTo(SERVLET_CLASS_NAME);
    }

    @Test
    void addingClassNotImplementingServletByClassNameThrows() {
        assertThatThrownBy(() -> servletContext.addServlet(SERVLET_NAME, getClass().getName()))
            .hasMessageContaining("does not implement Servlet");
    }

    @Test
    void addingNonExistentServletByClassNameThrows() {
        assertThatThrownBy(() -> servletContext.addServlet(SERVLET_NAME, "not.a.Class"))
            .hasMessageContaining("class not.a.Class not found");
    }

    @Test
    void addingServletByClassNameWithNoServletNameThrows() {
        assertThatThrownBy(() -> servletContext.addServlet(null, SERVLET_CLASS_NAME))
            .hasMessageContaining("servlet name must be a non-empty string");
    }

    @Test
    void addingServletByClassNameWithEmptyServletNameThrows() {
        assertThatThrownBy(() -> servletContext.addServlet("", SERVLET_CLASS_NAME))
            .hasMessageContaining("servlet name must be a non-empty string");
    }

    @Test
    void addingServletByClassNameWithNoServletClassNameThrows() {
        String noClassName = null;
        assertThatThrownBy(() -> servletContext.addServlet(SERVLET_NAME, noClassName))
            .hasMessageContaining("servlet class must not be empty");
    }

    @Test
    void addingServletByClassNameWithEmptyServletClassNameThrows() {
        assertThatThrownBy(() -> servletContext.addServlet(SERVLET_NAME, ""))
            .hasMessageContaining("servlet class must not be empty");
    }

    @Test
    void addingServletByClass() {
        Dynamic registration = servletContext.addServlet(SERVLET_NAME, SimpleHttpServlet.class);

        assertThat(registration.getName()).isEqualTo(SERVLET_NAME);
        assertThat(registration.getClassName()).isEqualTo(SERVLET_CLASS_NAME);
    }

    @Test
    void addingServletByClassWithNoServletNameThrows() {
        assertThatThrownBy(() -> servletContext.addServlet(null, SimpleHttpServlet.class))
            .hasMessageContaining("servlet name must be a non-empty string");
    }

    @Test
    void addingServletByClassWithEmptyServletNameThrows() {
        assertThatThrownBy(() -> servletContext.addServlet("", SimpleHttpServlet.class))
            .hasMessageContaining("servlet name must be a non-empty string");
    }

    @Test
    void addingServletByNullClassThrows() {
        Class<Servlet> nullServletClass = null;
        assertThatThrownBy(() -> servletContext.addServlet(SERVLET_NAME, nullServletClass))
            .hasMessageContaining("class must not be null");
    }

    @Test
    void addingServletByInstance() {
        Dynamic registration = servletContext.addServlet(SERVLET_NAME, new SimpleHttpServlet());

        assertThat(registration.getName()).isEqualTo(SERVLET_NAME);
        assertThat(registration.getClassName()).isEqualTo(SERVLET_CLASS_NAME);
    }

    @Test
    void addingServletByInstanceWithNoServletNameThrows() {
        assertThatThrownBy(() -> servletContext.addServlet(null, new SimpleHttpServlet()))
            .hasMessageContaining("servlet name must be a non-empty string");
    }

    @Test
    void addingServletByInstanceWithEmptyServletNameThrows() {
        assertThatThrownBy(() -> servletContext.addServlet("", new SimpleHttpServlet()))
            .hasMessageContaining("servlet name must be a non-empty string");
    }

    @Test
    void addingServletByNullInstanceThrows() {
        Servlet nullServlet = null;
        assertThatThrownBy(() -> servletContext.addServlet(SERVLET_NAME, nullServlet))
            .hasMessageContaining("servlet must not be null");
    }

    @Test
    void creatingServlet() throws ServletException {
        Servlet servlet = servletContext.createServlet(SimpleHttpServlet.class);

        assertThat(servlet).isExactlyInstanceOf(SimpleHttpServlet.class);
    }

    @Test
    void gettingServletRegistration() {
        servletContext.addServlet(SERVLET_NAME, SERVLET_CLASS_NAME);

        ServletRegistration registration = servletContext.getServletRegistration(SERVLET_NAME);

        assertThat(registration.getName()).isEqualTo(SERVLET_NAME);
        assertThat(registration.getClassName()).isEqualTo(SERVLET_CLASS_NAME);
    }

    @Test
    void gettingServletRegistrationForNonExistentServletIsNull() {
        ServletRegistration registration = servletContext.getServletRegistration(SERVLET_NAME);

        assertThat(registration).isNull();
    }

    @Test
    void gettingServletRegistrationWithoutNameIsNull() {
        ServletRegistration noNameRegistration = servletContext.getServletRegistration(null);
        ServletRegistration emptyNameRegistration = servletContext.getServletRegistration("");

        assertThat(noNameRegistration).isNull();
        assertThat(emptyNameRegistration).isNull();
    }

    @Test
    void gettingCreatedButNotAddedServletRegistrationIsNull() throws ServletException {
        servletContext.createServlet(SimpleHttpServlet.class);

        assertThat(servletContext.getServletRegistrations()).isEmpty();
    }

    @Test
    void initializingServletPutsItInService() {
        var registration = servletContext.addServlet(SERVLET_NAME, SERVLET_CLASS_NAME);

        Collection<String> errors = servletContext.initialize();

        assertThat(errors).isEmpty();
        assertThat(registration.getStatus()).isEqualTo(Status.IN_SERVICE);
    }

    @Test
    void initializingBadServletResultsInStatusFailedOnInit() {
        var registration = servletContext.addServlet(SERVLET_NAME, new HttpServlet() {
            public void init(ServletConfig config) throws ServletException {
                throw new ServletException("init servlet exception");
            }
        });

        Collection<String> errors = servletContext.initialize();

        assertThat(errors).anyMatch(error -> error.contains("init servlet exception"));
        assertThat(registration.getStatus()).isEqualTo(Status.FAILED_ON_INIT);
    }
}
