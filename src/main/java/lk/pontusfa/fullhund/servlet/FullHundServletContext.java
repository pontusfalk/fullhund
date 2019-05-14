package lk.pontusfa.fullhund.servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.descriptor.JspConfigDescriptor;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class FullHundServletContext implements ServletContext {
    private static final Logger logger = LogManager.getLogger();

    private final ClassLoader classLoader;
    private final Map<String, ServletRegistration> servletRegistrations = new HashMap<>();

    public FullHundServletContext(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public String getContextPath() {
        throw new NotImplementedException();
    }

    @Override
    public ServletContext getContext(String uripath) {
        throw new NotImplementedException();
    }

    @Override
    public int getMajorVersion() {
        throw new NotImplementedException();
    }

    @Override
    public int getMinorVersion() {
        throw new NotImplementedException();
    }

    @Override
    public int getEffectiveMajorVersion() {
        throw new NotImplementedException();
    }

    @Override
    public int getEffectiveMinorVersion() {
        throw new NotImplementedException();
    }

    @Override
    public String getMimeType(String file) {
        throw new NotImplementedException();
    }

    @Override
    public Set<String> getResourcePaths(String path) {
        throw new NotImplementedException();
    }

    @Override
    public URL getResource(String path) throws MalformedURLException {
        throw new NotImplementedException();
    }

    @Override
    public InputStream getResourceAsStream(String path) {
        throw new NotImplementedException();
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        throw new NotImplementedException();
    }

    @Override
    public RequestDispatcher getNamedDispatcher(String name) {
        throw new NotImplementedException();
    }

    @Override
    @Deprecated
    public Servlet getServlet(String name) throws ServletException {
        throw new NotImplementedException();
    }

    @Override
    @Deprecated
    public Enumeration<Servlet> getServlets() {
        throw new NotImplementedException();
    }

    @Override
    @Deprecated
    public Enumeration<String> getServletNames() {
        throw new NotImplementedException();
    }

    @Override
    public void log(String msg) {
        logger.info(msg);
    }

    @Override
    public void log(Exception exception, String msg) {
        throw new NotImplementedException();
    }

    @Override
    public void log(String message, Throwable throwable) {
        throw new NotImplementedException();
    }

    @Override
    public String getRealPath(String path) {
        throw new NotImplementedException();
    }

    @Override
    public String getServerInfo() {
        throw new NotImplementedException();
    }

    @Override
    public String getInitParameter(String name) {
        throw new NotImplementedException();
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        throw new NotImplementedException();
    }

    @Override
    public boolean setInitParameter(String name, String value) {
        throw new NotImplementedException();
    }

    @Override
    public Object getAttribute(String name) {
        throw new NotImplementedException();
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        throw new NotImplementedException();
    }

    @Override
    public void setAttribute(String name, Object object) {
        throw new NotImplementedException();
    }

    @Override
    public void removeAttribute(String name) {
        throw new NotImplementedException();
    }

    @Override
    public String getServletContextName() {
        throw new NotImplementedException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Dynamic addServlet(String servletName, String className) {
        try {
            Class<?> servletClass = classLoader.loadClass(className);
            if (!Servlet.class.isAssignableFrom(servletClass)) {
                throw new IllegalArgumentException(
                    String.format("could not add servlet %s, %s does not implement Servlet", servletName, className));
            }
            return addServlet(servletName, (Class<? extends Servlet>) servletClass);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(
                String.format("could not add servlet %s (%s)", servletName, className), e);
        }
    }

    @Override
    public Dynamic addServlet(String servletName, Servlet servlet) {
        if (servletName == null || servletName.isBlank()) {
            throw new IllegalArgumentException(
                String.format("could not add servlet %s, servlet name must be a non-empty string", servlet.getClass()));
        }

        var registration = new FullHundDynamicServletRegistration(servletName, servlet);
        servletRegistrations.put(servletName, registration);

        return registration;
    }

    @Override
    public Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
        try {
            return addServlet(servletName, createServlet(servletClass));
        } catch (ServletException e) {
            throw new RuntimeException(String.format("could not add servlet %s (%s)", servletName, servletClass), e);
        }
    }

    @Override
    public Dynamic addJspFile(String servletName, String jspFile) {
        throw new NotImplementedException();
    }

    @Override
    public <T extends Servlet> T createServlet(Class<T> clazz) throws ServletException {
        try {
            return clazz
                       .getDeclaredConstructor()
                       .newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new ServletException(e);
        }
    }

    @Override
    public ServletRegistration getServletRegistration(String servletName) {
        return servletRegistrations.get(servletName);
    }

    @Override
    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        throw new NotImplementedException();
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, String className) {
        throw new NotImplementedException();
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
        throw new NotImplementedException();
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) {
        throw new NotImplementedException();
    }

    @Override
    public <T extends Filter> T createFilter(Class<T> clazz) throws ServletException {
        throw new NotImplementedException();
    }

    @Override
    public FilterRegistration getFilterRegistration(String filterName) {
        throw new NotImplementedException();
    }

    @Override
    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        throw new NotImplementedException();
    }

    @Override
    public SessionCookieConfig getSessionCookieConfig() {
        throw new NotImplementedException();
    }

    @Override
    public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {
        throw new NotImplementedException();

    }

    @Override
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        throw new NotImplementedException();
    }

    @Override
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        throw new NotImplementedException();
    }

    @Override
    public void addListener(String className) {
        throw new NotImplementedException();
    }

    @Override
    public <T extends EventListener> void addListener(T t) {
        throw new NotImplementedException();

    }

    @Override
    public void addListener(Class<? extends EventListener> listenerClass) {
        throw new NotImplementedException();

    }

    @Override
    public <T extends EventListener> T createListener(Class<T> clazz) throws ServletException {
        throw new NotImplementedException();
    }

    @Override
    public JspConfigDescriptor getJspConfigDescriptor() {
        throw new NotImplementedException();
    }

    @Override
    public ClassLoader getClassLoader() {
        throw new NotImplementedException();
    }

    @Override
    public void declareRoles(String... roleNames) {
        throw new NotImplementedException();

    }

    @Override
    public String getVirtualServerName() {
        throw new NotImplementedException();
    }

    @Override
    public int getSessionTimeout() {
        throw new NotImplementedException();
    }

    @Override
    public void setSessionTimeout(int sessionTimeout) {
        throw new NotImplementedException();

    }

    @Override
    public String getRequestCharacterEncoding() {
        throw new NotImplementedException();
    }

    @Override
    public void setRequestCharacterEncoding(String encoding) {
        throw new NotImplementedException();
    }

    @Override
    public String getResponseCharacterEncoding() {
        throw new NotImplementedException();
    }

    @Override
    public void setResponseCharacterEncoding(String encoding) {
        throw new NotImplementedException();

    }
}
