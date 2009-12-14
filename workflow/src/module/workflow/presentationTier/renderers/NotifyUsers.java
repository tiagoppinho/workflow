package module.workflow.presentationTier.renderers;

import module.workflow.presentationTier.actions.UserNotificationBean;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

public class NotifyUsers extends OutputRenderer {

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
		UserNotificationBean bean = (UserNotificationBean) object;
		HtmlInlineContainer component = new HtmlInlineContainer();
		HtmlInlineContainer container = new HtmlInlineContainer();
		
		component.addChild(container);
		
		HtmlText text = new HtmlText(bean.getUser().getPresentationName());

		container.addChild(text);

		if (!bean.isAbleToNotify()) {
		    text.setClasses(getUnableToNotifyClasses());
		}

		return component;
	    }

	};
    }
}
