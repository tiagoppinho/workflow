package module.workflow.presentationTier.renderers.providers;

import module.workflow.domain.WorkflowQueue;
import myorg.presentationTier.renderers.providers.AbstractDomainClassProvider;

public class AvailableQueueTypes extends AbstractDomainClassProvider {

    @Override
    protected Class getSuperClass() {
	return WorkflowQueue.class;
    }
}