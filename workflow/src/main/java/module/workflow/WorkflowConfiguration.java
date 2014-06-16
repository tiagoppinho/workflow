package module.workflow;

import org.fenixedu.commons.configuration.ConfigurationInvocationHandler;
import org.fenixedu.commons.configuration.ConfigurationManager;
import org.fenixedu.commons.configuration.ConfigurationProperty;

public class WorkflowConfiguration {

    @ConfigurationManager(description = "Workflow Configuration")
    public static interface ConfigurationProperties {

        @ConfigurationProperty(key = "workflow.photo.prefix", defaultValue = "http://placehold.it/100x100/",
                description = "Prefix for the URL to display a user's photo. The username is appended to this URL.")
        public String getPhotoPrefix();

    }

    public static ConfigurationProperties getConfiguration() {
        return ConfigurationInvocationHandler.getConfiguration(ConfigurationProperties.class);
    }

}
