package lk.pontusfa.fullhund.servlet;

import javax.servlet.http.HttpServletMapping;
import javax.servlet.http.MappingMatch;

class FullhundHttpServletMapping implements HttpServletMapping {
    private final String matchValue;
    private final String pattern;
    private final String servletName;
    private final MappingMatch mappingMatch;

    FullhundHttpServletMapping(String matchValue, String pattern, String servletName, MappingMatch mappingMatch) {
        this.matchValue = matchValue;
        this.pattern = pattern;
        this.servletName = servletName;
        this.mappingMatch = mappingMatch;
    }

    @Override
    public String getMatchValue() {
        return matchValue;
    }

    @Override
    public String getPattern() {
        return pattern;
    }

    @Override
    public String getServletName() {
        return servletName;
    }

    @Override
    public MappingMatch getMappingMatch() {
        return mappingMatch;
    }

    @Override
    public int hashCode() {
        int result = matchValue.hashCode();
        result = 31 * result + pattern.hashCode();
        result = 31 * result + servletName.hashCode();
        result = 31 * result + mappingMatch.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        FullhundHttpServletMapping that = (FullhundHttpServletMapping) obj;

        if (!matchValue.equals(that.matchValue)) {
            return false;
        }
        if (!pattern.equals(that.pattern)) {
            return false;
        }
        if (!servletName.equals(that.servletName)) {
            return false;
        }
        return mappingMatch == that.mappingMatch;
    }

    @Override
    public String toString() {
        return "FullhundHttpServletMapping{" +
               "matchValue='" + matchValue + '\'' +
               ", pattern='" + pattern + '\'' +
               ", servletName='" + servletName + '\'' +
               ", mappingMatch=" + mappingMatch +
               '}';
    }
}
