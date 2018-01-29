/*
 * @(#)ForwardToProcessServlet.java
 *
 * Copyright 2011 Instituto Superior Tecnico
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
package module.workflow.presentationTier.servlet;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.CoreConfiguration;

import module.workflow.domain.WorkflowProcess;
import module.workflow.presentationTier.actions.BasicSearchProcessBean;
import module.workflow.presentationTier.actions.ProcessManagement;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author Luis Cruz
 * 
 */
@WebServlet(urlPatterns = "/ForwardToProcess/*", loadOnStartup = 4)
public class ForwardToProcessServlet extends HttpServlet {

    private static final long serialVersionUID = 8189116661407520520L;

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        final String uri = request.getRequestURI();
        final String[] split = uri.split("ForwardToProcess/");
        final String externalId = split[1];

        final User user = Authenticate.getUser();
        if (user == null) {
            final String applicationUrl = CoreConfiguration.getConfiguration().applicationUrl();
            final String path = applicationUrl + "/login??callback=" + applicationUrl + "/ForwardToProcess/" + externalId;
            response.sendRedirect(path);
            response.getOutputStream().close();            
            return;
        }

        final BasicSearchProcessBean searchBean = new BasicSearchProcessBean();
        searchBean.setProcessId(externalId);
        final Set<WorkflowProcess> search = searchBean.search();
        WorkflowProcess workflowProcess = null;
        if (search.size() != 1) {
            workflowProcess = FenixFramework.getDomainObject(externalId);
        } else {
            workflowProcess = search.iterator().next();
        }
        if (workflowProcess.isAccessibleToCurrentUser()) {
            final ActionForward actionForward = ProcessManagement.forwardToProcess(workflowProcess);
            final String path = request.getContextPath() + actionForward.getPath();
            final String args =
                    "&" + GenericChecksumRewriter.CHECKSUM_ATTRIBUTE_NAME + "="
                            + GenericChecksumRewriter.calculateChecksum(path, request.getSession());
            response.sendRedirect(path + args);
            response.getOutputStream().close();
            return;
        } else {
            throw new Error("unauthorized.access");
        }
    }

}
