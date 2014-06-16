package module.workflow.presentationTier.renderers;

import module.workflow.util.WorkflowClassUtil;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

public class NameResolverRenderer extends OutputRenderer {

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {
            @Override
            public HtmlComponent createComponent(Object arg0, Class arg1) {
                return new HtmlText(WorkflowClassUtil.getNameForType((Class<?>) arg0));
            }
        };
    }

}
