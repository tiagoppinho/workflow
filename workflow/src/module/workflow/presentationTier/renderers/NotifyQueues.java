package module.workflow.presentationTier.renderers;

import module.workflow.presentationTier.actions.QueueNotificationBean;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

public class NotifyQueues extends OutputRenderer {

    private String unableToNotifyClasses;

    public String getUnableToNotifyClasses() {
	return unableToNotifyClasses;
    }

    public void setUnableToNotifyClasses(String unableToNotifyClasses) {
	this.unableToNotifyClasses = unableToNotifyClasses;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {
		QueueNotificationBean bean = (QueueNotificationBean) object;
		HtmlInlineContainer component = new HtmlInlineContainer();
		HtmlInlineContainer container = new HtmlInlineContainer();
		
		component.addChild(container);
		
		HtmlText text = new HtmlText(bean.getQueue().getName());

		container.addChild(text);

		if (!bean.isAbleToNotify()) {
		    text.setClasses(getUnableToNotifyClasses());
		}

		return component;
	    }

	};
    }
}
