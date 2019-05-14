package lk.pontusfa.fullhund.assembler;

public class DeploymentDescriptorBuilder {
    private final WebAppDescriptor webAppDescriptor = new WebAppDescriptor();

    public DeploymentDescriptor build() {
        DeploymentDescriptor deploymentDescriptor = new DeploymentDescriptor();
        deploymentDescriptor.setWebAppDescriptor(webAppDescriptor);

        return deploymentDescriptor;
    }

    public DeploymentDescriptorBuilder servletDescriptor(String servletName, String servletClass) {
        webAppDescriptor.addServletDescriptor(new ServletDescriptor(servletName, servletClass));
        return this;
    }

    public DeploymentDescriptorBuilder servletMappingDescriptor(String servletName, String urlPattern) {
        webAppDescriptor.addServletMappingDescriptor(new ServletMappingDescriptor(servletName, urlPattern));
        return this;
    }
}
