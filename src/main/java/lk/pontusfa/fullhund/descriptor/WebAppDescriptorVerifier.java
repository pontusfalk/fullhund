package lk.pontusfa.fullhund.descriptor;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

class WebAppDescriptorVerifier {
    private final WebAppDescriptor webAppDescriptor;
    private final Collection<Error> errors = EnumSet.noneOf(Error.class);

    WebAppDescriptorVerifier(WebAppDescriptor webAppDescriptor) {
        this.webAppDescriptor = webAppDescriptor;
    }

    public Collection<Error> verify() {
        if (webAppDescriptor == null) {
            errors.add(Error.NO_WEB_APP_DESCRIPTOR);
            return errors;
        }

        verifyServletVersion();
        verifyServlets();
        verifyServletMappings();

        return errors;
    }

    private void verifyServletVersion() {
        if (!Objects.equals(webAppDescriptor.getVersion(), WebAppDescriptor.SERVLET_VERSION)) {
            errors.add(Error.BAD_SERVLET_VERSION);
        }
    }

    private void verifyServlets() {
        List<ServletDescriptor> servletDescriptors = webAppDescriptor.getServletDescriptors();
        for (ServletDescriptor servletDescriptor : servletDescriptors) {
            if (!isValid(servletDescriptor.getServletName())) {
                errors.add(Error.NO_SERVLET_NAME);
            }

            if (!isValid(servletDescriptor.getServletClass())) {
                errors.add(Error.NO_SERVLET_CLASS_NAME);
            }
        }
    }

    private void verifyServletMappings() {
        List<ServletMappingDescriptor> servletMappingDescriptors = webAppDescriptor.getServletMappingDescriptors();
        for (ServletMappingDescriptor mapping : servletMappingDescriptors) {
            if (!isValid(mapping.getServletName())) {
                errors.add(Error.NO_SERVLET_MAPPING_NAME);
            }

            if (!isValid(mapping.getUrlPattern())) {
                errors.add(Error.NO_SERVLET_MAPPING_URL_PATTERN);
            }
        }
    }

    private static boolean isValid(String str) {
        return str != null && str.isBlank();
    }

    enum Error {
        NO_WEB_APP_DESCRIPTOR,
        BAD_SERVLET_VERSION,
        NO_SERVLET_NAME,
        NO_SERVLET_CLASS_NAME,
        NO_SERVLET_MAPPING_NAME,
        NO_SERVLET_MAPPING_URL_PATTERN
    }
}
