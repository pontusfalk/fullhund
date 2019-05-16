package lk.pontusfa.fullhund.servlet.registration;

import lk.pontusfa.fullhund.servlet.NotImplementedException;

import javax.servlet.Servlet;
import javax.servlet.ServletRegistration;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableCollection;

public class FullHundServletRegistration extends FullHundRegistration implements ServletRegistration {
    private final Collection<String> mappings = new HashSet<>();

    FullHundServletRegistration(String servletName, Servlet servlet) {
        super(servletName, servlet);
    }

    @Override
    public Set<String> addMapping(String... urlPatterns) {
        mappings.addAll(Arrays.asList(urlPatterns));
        return emptySet();
    }

    @Override
    public Collection<String> getMappings() {
        return unmodifiableCollection(mappings);
    }

    @Override
    public String getRunAsRole() {
        throw new NotImplementedException();
    }
}
