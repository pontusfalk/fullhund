package lk.pontusfa.fullhund.loader;

import lk.pontusfa.fullhund.BadWebAppConfigurationException;
import lk.pontusfa.fullhund.assembler.DeploymentDescriptor;
import lk.pontusfa.fullhund.assembler.DeploymentDescriptorAssembler;
import lk.pontusfa.fullhund.assembler.ServletDescriptor;
import lk.pontusfa.fullhund.assembler.ServletMappingDescriptor;
import lk.pontusfa.fullhund.servlet.FullHundServletContext;
import lk.pontusfa.fullhund.servlet.WebApp;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static lk.pontusfa.fullhund.assembler.DeploymentDescriptor.Status.ERROR;
import static lk.pontusfa.fullhund.servlet.WebApp.Status.FAILED_ON_LOAD;
import static lk.pontusfa.fullhund.servlet.WebApp.Status.LOADED;

class WebAppLoader {
    private static final String WEB_XML_FILE = "WEB-INF/web.xml";

    private final DeploymentDescriptorAssembler deploymentDescriptorAssembler;
    private final ClassLoader webAppClassLoader;

    WebAppLoader(DeploymentDescriptorAssembler deploymentDescriptorAssembler, ClassLoader webAppClassLoader) {
        this.deploymentDescriptorAssembler = deploymentDescriptorAssembler;
        this.webAppClassLoader = webAppClassLoader;
    }

    WebApp load() {
        var deploymentDescriptor = loadDeploymentDescriptor();
        var servletContext = new FullHundServletContext(webAppClassLoader);
        var webApp = new WebApp(servletContext);

        if (deploymentDescriptor.getStatus() == ERROR) {
            webApp.setStatus(FAILED_ON_LOAD);
            webApp.addMessage(deploymentDescriptor.getErrors().toString());
            return webApp;
        }

        try {
            var webAppDescriptor = deploymentDescriptor.getWebAppDescriptor();
            var registrationForServlets = addServlets(servletContext, webAppDescriptor.getServletDescriptors());
            addServletMappings(registrationForServlets, webAppDescriptor.getServletMappingDescriptors());
        } catch (RuntimeException e) {
            webApp.setStatus(FAILED_ON_LOAD);
            webApp.addMessage(e.getLocalizedMessage());
            return webApp;
        }

        webApp.setStatus(LOADED);
        return webApp;
    }

    private DeploymentDescriptor loadDeploymentDescriptor() {
        DeploymentDescriptor deploymentDescriptor;
        try (var webAppXmlStream = webAppClassLoader.getResourceAsStream(WEB_XML_FILE)) {
            deploymentDescriptor = webAppXmlStream != null ?
                                   deploymentDescriptorAssembler.assemble(webAppXmlStream) :
                                   new DeploymentDescriptor();
        } catch (IOException e) {
            deploymentDescriptor = new DeploymentDescriptor();
            deploymentDescriptor.setStatus(ERROR);
            deploymentDescriptor.addError(e.getLocalizedMessage());
        }
        return deploymentDescriptor;
    }

    private static Map<String, ServletRegistration> addServlets(ServletContext servletContext,
                                                                Collection<ServletDescriptor> servletDescriptors) {
        Map<String, ServletRegistration> registrations = new HashMap<>();
        for (var servletDescriptor : servletDescriptors) {
            var servletName = servletDescriptor.getServletName();
            var registration = servletContext.addServlet(servletName, servletDescriptor.getServletClass());
            registrations.put(servletName, registration);
        }

        return registrations;
    }

    private static void addServletMappings(Map<String, ServletRegistration> registrationForServlets,
                                           Collection<ServletMappingDescriptor> servletMappingDescriptors) {
        Collection<String> conflictingUrlPatterns = new HashSet<>();

        for (var servletMappingDescriptor : servletMappingDescriptors) {
            var registration = registrationForServlets.get(servletMappingDescriptor.getServletName());
            if (registration != null) {
                conflictingUrlPatterns.addAll(registration.addMapping(servletMappingDescriptor.getUrlPattern()));
            }
        }

        if (!conflictingUrlPatterns.isEmpty()) {
            throw new BadWebAppConfigurationException("servlet mapping conflicts: " + conflictingUrlPatterns);
        }
    }
}
