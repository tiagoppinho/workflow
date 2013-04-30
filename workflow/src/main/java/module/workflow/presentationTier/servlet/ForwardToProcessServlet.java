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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.workflow.domain.WorkflowProcess;
import module.workflow.presentationTier.actions.ProcessManagement;

import org.apache.struts.action.ActionForward;

import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.bennu.core.domain.User;
import pt.ist.fenixWebFramework.Config.CasConfig;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter.ChecksumPredicate;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class ForwardToProcessServlet extends HttpServlet {

    static {
        RequestChecksumFilter.registerFilterRule(new ChecksumPredicate() {
            @Override
            public boolean shouldFilter(HttpServletRequest httpServletRequest) {
                return !httpServletRequest.getRequestURI().contains("/ForwardToProcess/");
            }
        });
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        final String uri = request.getRequestURI();
        final int lastSlash = uri.lastIndexOf('/');
        final String externalId = uri.substring(lastSlash + 1);
        final WorkflowProcess workflowProcess = FenixFramework.getDomainObject(externalId);

        final User user = UserView.getCurrentUser();
        if (user == null) {
            final String serverName = request.getServerName();
            final CasConfig casConfig = FenixWebFramework.getConfig().getCasConfig(serverName);
            if (casConfig != null && casConfig.isCasEnabled()) {
                final String casLoginUrl = casConfig.getCasLoginUrl();
                final StringBuilder url = new StringBuilder();
                url.append(casLoginUrl);
                url.append(getDownloadUrlForDomainObject(request, workflowProcess));
                response.sendRedirect(url.toString());
                return;
            }
            throw new Error("unauthorized.access");
        } else {
            final ActionForward actionForward = ProcessManagement.forwardToProcess(workflowProcess);
            final String path = request.getContextPath() + actionForward.getPath();
            final String args =
                    "&" + GenericChecksumRewriter.CHECKSUM_ATTRIBUTE_NAME + "=" + GenericChecksumRewriter.calculateChecksum(path);
            response.sendRedirect(path + args);
            response.getOutputStream().close();
            return;
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
