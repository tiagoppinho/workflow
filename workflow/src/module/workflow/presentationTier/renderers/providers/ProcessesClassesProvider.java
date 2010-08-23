package module.workflow.presentationTier.renderers.providers;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import module.workflow.domain.WorkflowProcess;
import myorg.presentationTier.renderers.providers.AbstractDomainClassProvider;

public class ProcessesClassesProvider extends AbstractDomainClassProvider implements DataProvider {

    @Override
    protected Class getSuperClass() {
	return WorkflowProcess.class;
    }

    @Override
    protected boolean shouldContainerAbstractClasses() {
	return false;
    }

    @Override
    protected boolean shouldContainSuperClass() {
	return false;
    }

}
