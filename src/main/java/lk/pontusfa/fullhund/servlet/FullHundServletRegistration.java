package lk.pontusfa.fullhund.servlet;

import javax.servlet.Servlet;
import javax.servlet.ServletRegistration;
import java.util.*;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableCollection;

class FullHundServletRegistration extends FullHundRegistration implements ServletRegistration {
    private final ServletMappingResolver servletMappingResolver;

    FullHundServletRegistration(String servletName, Servlet servlet, ServletMappingResolver servletMappingResolver) {
        super(servletName, servlet);
        this.servletMappingResolver = servletMappingResolver;
    }

    @Override
    public Set<String> addMapping(String... urlPatterns) {
        if (urlPatterns == null || urlPatterns.length == 0) {
            throw new IllegalArgumentException("provide at least 1 url pattern");
        }
        var newUrlPatterns = new ArrayList<>(Arrays.asList(urlPatterns));
        newUrlPatterns.removeAll(servletMappingResolver.getMappingsForServlet(getName()));

        var conflictingUrlPatterns = servletMappingResolver.filterExistingMappings(newUrlPatterns);
        if (!conflictingUrlPatterns.isEmpty()) {
            return conflictingUrlPatterns;
        }

        Arrays.stream(urlPatterns)
              .filter(Objects::nonNull)
              .forEach(pattern -> servletMappingResolver.addMapping(getName(), pattern));

        return emptySet();
    }

    @Override
    public Collection<String> getMappings() {
        return unmodifiableCollection(servletMappingResolver.getMappingsForServlet(getName()));
    }

    @Override
    public String getRunAsRole() {
        throw new NotImplementedException();
    }
}
