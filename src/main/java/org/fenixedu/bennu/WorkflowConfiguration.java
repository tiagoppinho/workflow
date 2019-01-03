/**
 * Copyright © 2014 Instituto Superior Técnico
 *
 * This file is part of MGP Viewer.
 *
 * FenixEdu Spaces is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Spaces is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Spaces.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.bennu;

import org.fenixedu.bennu.spring.BennuSpringModule;
import org.fenixedu.commons.configuration.ConfigurationInvocationHandler;
import org.fenixedu.commons.configuration.ConfigurationManager;
import org.fenixedu.commons.configuration.ConfigurationProperty;
import org.fenixedu.employer.Employer;
import org.fenixedu.employer.backoff.ExponentialBackoff;
import org.fenixedu.employer.workflow.SimpleWorkflow;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("module.workflow.ui")
@BennuSpringModule(basePackages = "module.workflow.ui", bundles = "WorkflowResources")
public class WorkflowConfiguration {

    @ConfigurationManager(description = "Workflow Configuration")
    public interface ConfigurationProperties {
        //SmartSigner
        @ConfigurationProperty(key = "workflow.smartsigner.integration", description = "Wether integration with SmartSigner is enabled",
                defaultValue = "false")
        public boolean smartsignerIntegration();

        @ConfigurationProperty(key = "certifier.url", description = "Certifier Url")
        String certifierUrl();

        @ConfigurationProperty(key = "certifier.jwt.secret", description = "Certifier JWT Secret")
        String certifierJwtSecret();
    }

    public static ConfigurationProperties getConfiguration() {
        return ConfigurationInvocationHandler.getConfiguration(ConfigurationProperties.class);
    }

    @Bean
    public Employer employer() {
        return new Employer(new SimpleWorkflow(), new ExponentialBackoff(), 2);
    }
}
