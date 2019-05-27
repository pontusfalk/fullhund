package lk.pontusfa.fullhund.servlet;

import javax.servlet.http.HttpServletMapping;
import javax.servlet.http.MappingMatch;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static javax.servlet.http.MappingMatch.*;

public class ServletMappingResolver {
    private final Map<String, String> mappingToServlet = new HashMap<>();
    private final Map<String, Set<String>> servletToMappings = new HashMap<>();

    void addMapping(String servlet, String mapping) {
        var normalizedMapping = normalizeMapping(mapping);
        if (!isValidMapping(normalizedMapping)) {
            throw new IllegalArgumentException("invalid mapping, see 12.2 of spec: " + normalizedMapping);
        }

        if (!mappingToServlet.containsKey(normalizedMapping)) {
            mappingToServlet.put(normalizedMapping, servlet);
            servletToMappings.computeIfAbsent(servlet, key -> new HashSet<>()).add(normalizedMapping);
        }
    }

    Collection<String> getMappingsForServlet(String servletName) {
        return servletToMappings.computeIfAbsent(servletName, key -> new HashSet<>());
    }

    Set<String> filterExistingMappings(Collection<String> mappings) {
        return mappings.stream()
                       .filter(mappingToServlet::containsKey)
                       .collect(Collectors.toSet());
    }

    HttpServletMapping resolve(String mapping) {
        var normalizedMapping = normalizeMapping(mapping);
        var servlet = mappingToServlet.get(normalizedMapping);
        HttpServletMapping httpServletMapping;

        if (servlet != null) {
            httpServletMapping = createExactMapping(normalizedMapping, servlet);
        } else {
            httpServletMapping = createPathMapping(normalizedMapping);
        }
        if (httpServletMapping == null && normalizedMapping.contains(".")) {
            httpServletMapping = createExtensionMapping(normalizedMapping);
        }

        return httpServletMapping;
    }

    private HttpServletMapping createPathMapping(String mapping) {
        //shortcut for mapping matches like /foo to /foo/*
        var servletForRootMatch = mappingToServlet.get(mapping + "/*");
        if (servletForRootMatch != null) {
            return new FullhundHttpServletMapping("", mapping + "/*", servletForRootMatch, PATH);
        }

        var mappingPath = URI.create(mapping);
        var pathMapping = mappingPath.resolve("."); // get directory, e.g. from /foo/bar.qux to /foo/

        while (!pathMapping.getPath().equals("/")) {
            var servlet = mappingToServlet.get(pathMapping.getPath() + '*');
            if (servlet != null) {
                return new FullhundHttpServletMapping(pathMapping.relativize(mappingPath).getPath(),
                                                      pathMapping.getPath() + '*', servlet, PATH);
            } else {
                pathMapping = pathMapping.resolve("..");
            }
        }

        return null;
    }

    private HttpServletMapping createExtensionMapping(String normalizedMapping) {
        var extStart = normalizedMapping.lastIndexOf('.');
        var extension = normalizedMapping.substring(extStart);
        var servlet = mappingToServlet.get('*' + extension);

        if (servlet != null) {
            String match = normalizedMapping.substring(1, extStart);
            return new FullhundHttpServletMapping(match, extension, servlet, EXTENSION);
        } else {
            return null;
        }
    }

    private static HttpServletMapping createExactMapping(String mapping, String servlet) {
        MappingMatch mappingMatch;
        if ("".equals(mapping)) {
            mappingMatch = CONTEXT_ROOT;
        } else if ("/".equals(mapping)) {
            mappingMatch = DEFAULT;
        } else {
            mappingMatch = EXACT;
        }

        return new FullhundHttpServletMapping(mapping.replace("/", ""), mapping, servlet, mappingMatch);
    }

    private static String normalizeMapping(String mapping) {
        return URI.create(mapping).normalize().getPath();
    }

    private static boolean isValidMapping(String mapping) {
        if (mapping.isEmpty()) {
            return true;
        }
        if (mapping.equals("/")) {
            return true;
        }
        if (mapping.startsWith("*.")) {
            return true;
        }
        if (mapping.startsWith("/") && mapping.endsWith("/*")) {
            return true;
        }
        return mapping.startsWith("/");
    }
}
