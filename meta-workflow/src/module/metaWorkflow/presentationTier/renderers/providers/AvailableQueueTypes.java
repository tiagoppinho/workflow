package module.metaWorkflow.presentationTier.renderers.providers;

import module.metaWorkflow.domain.WorkflowQueue;
import myorg.presentationTier.renderers.providers.AbstractDomainClassProvider;

public class AvailableQueueTypes extends AbstractDomainClassProvider {

    @Override
    protected Class getSuperClass() {
	return WorkflowQueue.class;
    }

}
