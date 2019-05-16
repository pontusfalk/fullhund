package lk.pontusfa.fullhund.servlet;

import lk.pontusfa.fullhund.servlet.registration.FullHundDynamicServletRegistration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.descriptor.JspConfigDescriptor;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

// todo: implement funcationality for methods with :
//  UnsupportedOperationException –
//  if this ServletContext was passed to the ServletContextListener.contextInitialized method of a
//  ServletContextListener that was neither declared in web.xml or web-fragment.xml,
//  nor annotated with javax.servlet.annotation.WebListener
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
    public URL getResource(String path) {
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
    public Servlet getServlet(String name) {
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
    //todo: handle servlets with servletName already added
    //todo: annotation introspection on servlet instance
    public Dynamic addServlet(String servletName, String className) {
        if (className == null || className.isBlank()) {
            throw new IllegalArgumentException("servlet class must not be empty");
        }

        try {
            Class<?> servletClass = classLoader.loadClass(className);
            if (!Servlet.class.isAssignableFrom(servletClass)) {
                throw new IllegalArgumentException(
                    String.format("could not add servlet %s, %s does not implement Servlet", servletName, className));
            }
            return addServlet(servletName, (Class<? extends Servlet>) servletClass);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(
                String.format("class %s not found, can't add servlet %s", className, servletName), e);
        }
    }

    @Override
    //todo: handle servlets with servletName already added
    //todo: annotation introspection on servlet instance
    public Dynamic addServlet(String servletName, Servlet servlet) {
        if (servlet == null) {
            throw new IllegalArgumentException("servlet must not be null");
        }
        if (servletName == null || servletName.isBlank()) {
            throw new IllegalArgumentException(
                String.format("could not add servlet %s, servlet name must be a non-empty string", servlet.getClass()));
        }

        var registration = new FullHundDynamicServletRegistration(servletName, servlet);
        servletRegistrations.put(servletName, registration);

        return registration;
    }

    @Override
    //todo: handle servlets with servletName already added
    //todo: annotation introspection on servlet instance
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
    //todo: annotation introspection on servlet instance
    public <T extends Servlet> T createServlet(Class<T> clazz) throws ServletException {
        if (clazz == null) {
            throw new IllegalArgumentException("class must not be null");
        }

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
        return (servletName != null && !servletName.isBlank()) ? servletRegistrations.get(servletName) : null;
    }

    @Override
    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        return Collections.unmodifiableMap(servletRegistrations);
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
    public <T extends Filter> T createFilter(Class<T> clazz) {
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
    public <T extends EventListener> T createListener(Class<T> clazz) {
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
