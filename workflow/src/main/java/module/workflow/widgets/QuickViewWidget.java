/*
 * @(#)QuickViewWidget.java
 *
 * Copyright 2010 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Case Handleing Based Workflow Module.
 *
 *   The Case Handleing Based Workflow Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Workflow Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Workflow Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
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
import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.bennu.core.domain.contents.Node;
import pt.ist.bennu.core.presentationTier.actions.ContextBaseAction;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.bennu.core.util.ClassNameBundle;

import org.apache.struts.action.ActionForward;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.ist.fenixframework.plugins.luceneIndexing.DomainIndexer;

@ClassNameBundle(bundle = "resources/WorkflowResources", key = "title.quickAccess")
/**
 * 
 * @author Paulo Abrantes
 * 
 */
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
