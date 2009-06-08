package module.workflow.presentationTier.renderers;

import module.workflow.domain.GenericFile;
import module.workflow.util.FileTypeNameResolver;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

public class FileTypeNameRenderer extends OutputRenderer {

    @Override
    protected Layout getLayout(Object arg0, Class arg1) {
	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object arg0, Class arg1) {
		return new HtmlText(FileTypeNameResolver.getNameFor((Class<? extends GenericFile>) arg0));
	    }

	};
    }
}
