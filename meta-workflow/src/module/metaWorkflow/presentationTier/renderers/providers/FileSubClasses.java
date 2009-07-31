package module.metaWorkflow.presentationTier.renderers.providers;

import module.workflow.domain.ProcessFile;
import myorg.presentationTier.renderers.providers.AbstractDomainClassProvider;

public class FileSubClasses extends AbstractDomainClassProvider {

    @Override
    protected Class getSuperClass() {
	return ProcessFile.class;
    }

    @Override
    protected boolean shouldContainSuperClass() {
	return true;
    }

}
