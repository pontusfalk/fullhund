package lk.pontusfa.fullhund.servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletMapping;

import static javax.servlet.http.MappingMatch.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * examples from HttpServletMapping:
 * <servlet-name>MyServlet</servlet-name>
 * <url-pattern>/MyServlet</url-pattern>
 * <url-pattern>""</url-pattern>
 * <url-pattern>*.extension</url-pattern>
 * <url-pattern>/path/*</url-pattern>
 *
 * URI Path (in quotes)     matchValue      pattern     mappingMatch
 * ""                       ""              ""          CONTEXT_ROOT
 * "/index.html"            ""              /           DEFAULT
 * "/MyServlet"             MyServlet       /MyServlet  EXACT
 * "/foo.extension"         foo             .extension  EXTENSION
 * "/path/foo"              foo             /path/*     PATH
 */
class ServletMappingResolverTest {
    private static final String SERVLET_NAME = "servletName";

    private static final String ROOT_PATH_MAPPING = "";
    private static final String DEFAULT_PATH_MAPPING = "/";
    private static final String EXACT_PATH_MAPPING = "/exactPath";
    private static final String PATH_MAPPING = "/pathMapping/*";
    private static final String EXTENSION_MAPPING = "*.jsp";

    private ServletMappingResolver resolver;

    @BeforeEach
    void setup() {
        resolver = new ServletMappingResolver();
        resolver.addMapping(SERVLET_NAME, ROOT_PATH_MAPPING);
        resolver.addMapping(SERVLET_NAME, DEFAULT_PATH_MAPPING);
        resolver.addMapping(SERVLET_NAME, EXACT_PATH_MAPPING);
        resolver.addMapping(SERVLET_NAME, PATH_MAPPING);
        resolver.addMapping(SERVLET_NAME, EXTENSION_MAPPING);
    }

    // "" ->   ""  ""  CONTEXT_ROOT
    @Test
    void rootMappingResolves() {
        var expectedPath = new FullhundHttpServletMapping("", "", SERVLET_NAME, CONTEXT_ROOT);

        HttpServletMapping resolvedPath = resolver.resolve("");

        assertThat(resolvedPath).isEqualTo(expectedPath);
    }

    // / ->   ""  /   DEFAULT
    @Test
    void defaultMappingResolves() {
        var expectedPath = new FullhundHttpServletMapping("", "/", SERVLET_NAME, DEFAULT);

        HttpServletMapping resolvedPath = resolver.resolve("/");

        assertThat(resolvedPath).isEqualTo(expectedPath);
    }

    // /exactPath ->  exactPath   /exactPath  EXACT
    @Test
    void exactMappingResolves() {
        var expectedPath = new FullhundHttpServletMapping("exactPath", "/exactPath", SERVLET_NAME, EXACT);

        HttpServletMapping resolvedPath = resolver.resolve("/exactPath");

        assertThat(resolvedPath).isEqualTo(expectedPath);
    }

    // /foo.jsp -> foo .jsp    EXTENSION
    @Test
    void extensionMappingResolves() {
        var expectedPath = new FullhundHttpServletMapping("foo", ".jsp", SERVLET_NAME, EXTENSION);

        HttpServletMapping resolvedPath = resolver.resolve("/foo.jsp");

        assertThat(resolvedPath).isEqualTo(expectedPath);
    }

    // /pathMapping/foo ->  foo /pathMapping/*  PATH
    @Test
    void pathMappingResolves() {
        var expectedPath = new FullhundHttpServletMapping("foo", "/pathMapping/*", SERVLET_NAME, PATH);

        HttpServletMapping resolvedPath = resolver.resolve("/pathMapping/foo");

        assertThat(resolvedPath).isEqualTo(expectedPath);
    }

    @Test
    void exactMappingTakesPrecedenceOverPathMapping() {
        resolver.addMapping(SERVLET_NAME, "/*");
        resolver.addMapping(SERVLET_NAME, "/foo");
        var expectedPath = new FullhundHttpServletMapping("foo", "/foo", SERVLET_NAME, EXACT);

        HttpServletMapping resolvedPath = resolver.resolve("/foo");

        assertThat(resolvedPath).isEqualTo(expectedPath);
    }

    @Test
    void exactMappingTakesPrecedenceOverExtensionMapping() {
        resolver.addMapping(SERVLET_NAME, "*.jsp");
        resolver.addMapping(SERVLET_NAME, "/foo.jsp");
        var expectedPath = new FullhundHttpServletMapping("foo.jsp", "/foo.jsp", SERVLET_NAME, EXACT);

        HttpServletMapping resolvedPath = resolver.resolve("/foo.jsp");

        assertThat(resolvedPath).isEqualTo(expectedPath);
    }

    @Test
    void longestPathMappingResolves() {
        resolver.addMapping(SERVLET_NAME, "/*");
        resolver.addMapping(SERVLET_NAME, "/foo/bar/*");
        resolver.addMapping(SERVLET_NAME, "/foo/*");
        var expectedPath = new FullhundHttpServletMapping("baz/qux", "/foo/bar/*", SERVLET_NAME, PATH);

        HttpServletMapping resolvedPath = resolver.resolve("/foo/bar/baz/qux");

        assertThat(resolvedPath).isEqualTo(expectedPath);
    }

    @Test
    void pathMappingResolvesRootPath() {
        resolver.addMapping(SERVLET_NAME, "/foo/*");
        var expectedPath = new FullhundHttpServletMapping("", "/foo/*", SERVLET_NAME, PATH);

        HttpServletMapping resolvedPath = resolver.resolve("/foo");

        assertThat(resolvedPath).isEqualTo(expectedPath);
    }
}
