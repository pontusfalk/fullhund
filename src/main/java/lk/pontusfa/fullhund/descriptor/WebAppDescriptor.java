package lk.pontusfa.fullhund.descriptor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "web-app")
class WebAppDescriptor {
    static final String SERVLET_VERSION = "4.0";

    @XmlElement(name = "servlet")
    private final List<ServletDescriptor> servletDescriptors = new ArrayList<>();
    @XmlElement(name = "servlet-mapping")
    private final List<ServletMappingDescriptor> servletMappingDescriptors = new ArrayList<>();
    @XmlElement
    private String version = SERVLET_VERSION;

    List<ServletDescriptor> getServletDescriptors() {
        return servletDescriptors;
    }

    List<ServletMappingDescriptor> getServletMappingDescriptors() {
        return servletMappingDescriptors;
    }

    String getVersion() {
        return version;
    }

    void setVersion(String version) {
        this.version = version;
    }

    void addServletDescriptor(ServletDescriptor servletDescriptor) {
        servletDescriptors.add(servletDescriptor);
    }

    void addServletMappingDescriptor(ServletMappingDescriptor servletMappingDescriptor) {
        servletMappingDescriptors.add(servletMappingDescriptor);
    }

    @Override
    public String toString() {
        return "WebAppDescriptor{" +
               "servletDescriptors=" + servletDescriptors +
               ", servletMappingDescriptors=" + servletMappingDescriptors +
               ", version='" + version + '\'' +
               '}';
    }
}
