package lk.pontusfa.fullhund.servlet;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

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
