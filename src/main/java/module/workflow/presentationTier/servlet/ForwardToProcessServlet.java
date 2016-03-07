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

import module.workflow.domain.WorkflowProcess;
import module.workflow.presentationTier.actions.BasicSearchProcessBean;
import module.workflow.presentationTier.actions.ProcessManagement;

import org.apache.struts.action.ActionForward;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.bennu.portal.BennuPortalConfiguration;
import org.fenixedu.bennu.portal.domain.PortalConfiguration;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.ist.fenixframework.DomainObject;
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
        String[] split = uri.split("ForwardToProcess/");
        final String externalId = split[1];
        BasicSearchProcessBean searchBean = new BasicSearchProcessBean();
        searchBean.setProcessId(externalId);
        Set<WorkflowProcess> search = searchBean.search();
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

    private String getDownloadUrlForDomainObject(final HttpServletRequest request, final DomainObject domainObject) {
        return getDownloadUrl(request.getScheme(), request.getServerName(), request.getServerPort(), request.getContextPath(),
                domainObject);
    }

    private String getDownloadUrl(final String scheme, final String servername, final int serverPort, final String contextPath,
            final DomainObject domainObject) {
        final StringBuilder url = new StringBuilder();
        url.append(scheme);
        url.append("://");
        url.append(servername);
        if (serverPort > 0 && serverPort != 80 && serverPort != 443) {
            url.append(":");
            url.append(serverPort);
        }
        url.append(contextPath);
        url.append("/ForwardToProcess/");
        url.append(domainObject.getExternalId());
        return url.toString();
    }

}
