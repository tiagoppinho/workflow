package module.workflow.widgets;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import module.dashBoard.presentationTier.WidgetRequest;
import module.dashBoard.widgets.WidgetController;
import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowProcess.WorkflowProcessIndex;
import module.workflow.presentationTier.ProcessNodeSelectionMapper;
import module.workflow.presentationTier.actions.BasicSearchProcessBean;
import module.workflow.presentationTier.actions.ProcessManagement;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.contents.Node;
import myorg.presentationTier.actions.ContextBaseAction;
import myorg.util.BundleUtil;
import myorg.util.ClassNameBundle;

import org.apache.struts.action.ActionForward;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.ist.fenixframework.plugins.luceneIndexing.DomainIndexer;

@ClassNameBundle(bundle = "resources/WorkflowResources", key = "title.quickAccess")
public class QuickViewWidget extends WidgetController {

    public static String NOT_FOUND = "NF";

    @Override
    public void doView(WidgetRequest request) {
	request.setAttribute("searchBean", new BasicSearchProcessBean());
    }

    @Override
    public ActionForward doSubmit(WidgetRequest request) {

	BasicSearchProcessBean searchBean = getRenderedObject("quickAccess");
	Set<WorkflowProcess> search = searchBean.search();
	// List<WorkflowProcess> search =
	// DomainIndexer.getInstance().search(WorkflowProcess.class,
	// WorkflowProcessIndex.NUMBER,
	// searchBean.getProcessId(), 2);

	HttpServletResponse response = request.getResponse();
	response.setContentType("text");
	ServletOutputStream stream = null;

	try {
	    String write = null;
	    if (search.size() != 1) {
		write = QuickViewWidget.NOT_FOUND;
	    } else {
		WorkflowProcess process = search.iterator().next();
		List<Node> nodes = ProcessNodeSelectionMapper.getForwardFor(process.getClass());
		write = (!process.isAccessibleToCurrentUser()) ? QuickViewWidget.NOT_FOUND : GenericChecksumRewriter
			.injectChecksumInUrl(request.getContextPath(), ProcessManagement.workflowManagementURL
				+ process.getExternalId() + "&" + ContextBaseAction.CONTEXT_PATH + "="
				+ ((nodes.size() > 0) ? nodes.get(nodes.size() - 1).getContextPath() : ""));
	    }

	    stream = response.getOutputStream();
	    response.setContentLength(write.length());
	    stream.write(write.getBytes());
	    stream.flush();
	    stream.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return null;
    }

    protected <T extends Object> T getRenderedObject(final String id) {
	final IViewState viewState = RenderUtils.getViewState(id);
	return (T) getRenderedObject(viewState);
    }

    protected <T extends Object> T getRenderedObject(final IViewState viewState) {
	if (viewState != null) {
	    MetaObject metaObject = viewState.getMetaObject();
	    if (metaObject != null) {
		return (T) metaObject.getObject();
	    }
	}
	return null;
    }

    @Override
    public String getWidgetDescription() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkflowResources", "widget.description.QuickViewWidget");
    }
}