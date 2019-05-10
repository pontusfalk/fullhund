package lk.pontusfa.fullhund.descriptor;

import javax.xml.bind.annotation.XmlElement;

class ServletDescriptor {
    @XmlElement(name = "servlet-name")
    private String servletName;
    @XmlElement(name = "servlet-class")
    private String servletClass;

    @SuppressWarnings("unused") //for unmarshalling
    private ServletDescriptor() {
    }

    public ServletDescriptor(String servletName, String servletClass) {
        this.servletName = servletName;
        this.servletClass = servletClass;
    }

    String getServletName() {
        return servletName;
    }

    String getServletClass() {
        return servletClass;
    }

    @Override
    public String toString() {
        return "ServletDescriptor{" +
               "servletName='" + servletName + '\'' +
               ", servletClass='" + servletClass + '\'' +
               '}';
    }
}
