/*
 * @(#)ProcessManagement.java
 *
 * Copyright 2009 Instituto Superior Tecnico
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
package module.workflow.presentationTier.actions;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.domain.MenuFunctionality;
import org.fenixedu.bennu.portal.model.Functionality;
import org.fenixedu.bennu.portal.servlet.BennuPortalDispatcher;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.base.BaseAction;

import com.google.gson.JsonObject;

import jvstm.cps.ConsistencyException;
import module.workflow.activities.ActivityException;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.ProcessFile;
import module.workflow.domain.ProcessFileValidationException;
import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowProcessComment;
import module.workflow.presentationTier.WorkflowLayoutContext;
import module.workflow.servlet.WorkflowContainerInitializer;
import module.workflow.util.FileUploadBeanResolver;
import module.workflow.util.PresentableProcessState;
import module.workflow.util.WorkflowFileUploadBean;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author Diogo Figueiredo
 * @author João Neves
 * @author João Antunes
 * @author Pedro Santos
 * @author Shezad Anavarali
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
@Mapping(path = "/workflowProcessManagement")
public class ProcessManagement extends BaseAction {

    public static final String workflowManagementURL = "/workflowProcessManagement.do?method=viewProcess&processId=";

    public static Map<Class<? extends WorkflowProcess>, ProcessRequestHandler<? extends WorkflowProcess>> handlers =
            new HashMap<Class<? extends WorkflowProcess>, ProcessRequestHandler<? extends WorkflowProcess>>();

    protected User getLoggedPerson() {
        return Authenticate.getUser();
    }

    private final ActionForward doForward(final HttpServletRequest request, final String body) {
        final WorkflowProcess process = getProcess(request);
        final WorkflowLayoutContext layout = process.getLayout();
        request.setAttribute("context", layout);
        if (BennuPortalDispatcher.getSelectedFunctionality(request) == null) {
            final Functionality functionality = WorkflowContainerInitializer.getFunctionalityForProcess(process.getClass());
            final MenuFunctionality menuFunctionality =
                    MenuFunctionality.findFunctionality(functionality.getProvider(), functionality.getKey());
            BennuPortalDispatcher.selectFunctionality(request, menuFunctionality);
        }
        return forward(body);
    }

    public ActionForward viewProcess(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws ClassNotFoundException {

        final WorkflowProcess process = getProcess(request);
        return viewProcess(process, request);
    }

    @SuppressWarnings("unchecked")
    public ActionForward viewProcess(final WorkflowProcess process, final HttpServletRequest request) {

        request.setAttribute("process", process);
        final ProcessRequestHandler<WorkflowProcess> handler =
                (ProcessRequestHandler<WorkflowProcess>) handlers.get(process.getClass());
        if (handler != null) {
            handler.handleRequest(process, request);
        }

        return doForward(request, "/workflow/viewProcess.jsp");
    }

    public ActionForward forwardToProcessPage(final WorkflowProcess process, final HttpServletRequest request) {

        final ActionForward forward = new ActionForward();
        forward.setRedirect(true);
        final String realPath = ProcessManagement.workflowManagementURL + process.getExternalId();
        forward.setPath(realPath + "&" + GenericChecksumRewriter.CHECKSUM_ATTRIBUTE_NAME + "="
                + GenericChecksumRewriter.calculateChecksum(request.getContextPath() + realPath, request.getSession()));
        return forward;
    }

    public ActionForward process(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        final WorkflowProcess process = getProcess(request);
        final WorkflowActivity<WorkflowProcess, ActivityInformation<WorkflowProcess>> activity = getActivity(process, request);
        final ActivityInformation<WorkflowProcess> information = getRenderedObject("activityBean");
        return doLifeCycle(information, process, activity, request);
    }

    public ActionForward activityDefaultPostback(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        final WorkflowProcess process = getProcess(request);
        final WorkflowActivity<WorkflowProcess, ActivityInformation<WorkflowProcess>> activity = getActivity(process, request);
        final ActivityInformation<WorkflowProcess> information = getRenderedObject("activityBean");
        RenderUtils.invalidateViewState();
        return executeActivity(process, request, activity, information);
    }

    public ActionForward actionLink(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {

        final WorkflowProcess process = getProcess(request);
        final WorkflowActivity<WorkflowProcess, ActivityInformation<WorkflowProcess>> activity = getActivity(process, request);
        try {
            final ActivityInformation<WorkflowProcess> information = populateInformation(process, activity, request);
            return executeActivity(process, request, activity, information);
        } catch (final DomainException domainEx) {
            addLocalizedMessage(request, domainEx.getLocalizedMessage());
            RenderUtils.invalidateViewState();
            return viewProcess(process, request);
        }
    }

    private ActivityInformation<WorkflowProcess> populateInformation(final WorkflowProcess process,
            final WorkflowActivity<WorkflowProcess, ActivityInformation<WorkflowProcess>> activity, final HttpServletRequest request)
            throws Exception {
        final ActivityInformation<WorkflowProcess> activityInformation = activity.getActivityInformation(process);
        final String parameters = request.getParameter("parameters");
        final Class<? extends ActivityInformation> activityClass = activityInformation.getClass();
        if (!StringUtils.isEmpty(parameters)) {
            for (final String parameter : parameters.split(",")) {

                final Field field = getField(activityClass, parameter);
                final Class<?> type = field.getType();
                final Object convertedValue = convert(type, request.getParameter(parameter));
                final Method declaredMethod =
                        getMethod("set" + parameter.substring(0, 1).toUpperCase() + parameter.substring(1), activityClass,
                                convertedValue.getClass());
                declaredMethod.invoke(activityInformation, convertedValue);
            }
        }
        return activityInformation;
    }

    private Field getField(final Class activityClass, final String parameter) throws SecurityException, NoSuchFieldException {
        if (activityClass == null) {
            throw new NoSuchFieldException();
        }
        Field field;
        try {
            field = activityClass.getDeclaredField(parameter);
        } catch (final NoSuchFieldException ex) {
            field = null;
        }
        return field == null ? getField(activityClass.getSuperclass(), parameter) : field;
    }

    private Method getMethod(final String methodName, final Class<? extends ActivityInformation> activityClass,
            Class<? extends Object> argumentClass) {
        while (!argumentClass.equals(Object.class)) {
            try {
                return activityClass.getMethod(methodName, argumentClass);
            } catch (NoSuchMethodException e) {
                argumentClass = argumentClass.getSuperclass();
            }
        }
        return null;
    }

    private Object convert(final Class<?> type, final String parameterValue) throws Exception {
        if (DomainObject.class.isAssignableFrom(type)) {
            return FenixFramework.getDomainObject(parameterValue);
        }
        if (type == Integer.class) {
            return Integer.parseInt(parameterValue);
        }
        if (type == Double.class) {
            return Double.parseDouble(parameterValue);
        }
        if (type == Float.class) {
            return Float.parseFloat(parameterValue);
        }
        if (type == String.class) {
            return parameterValue;
        }
        throw new IllegalArgumentException("Invalid type" + type.getName());
    }

    private ActionForward doLifeCycle(ActivityInformation<WorkflowProcess> information, final WorkflowProcess process,
            final WorkflowActivity<WorkflowProcess, ActivityInformation<WorkflowProcess>> activity, final HttpServletRequest request) {
        if (information == null) {
            information = activity.getActivityInformation(process);
        } else {
            information.markHasForwardedFromInput();
        }

        return executeActivity(process, request, activity, information);
    }

    public ActionForward executeActivity(final WorkflowProcess process, final HttpServletRequest request,
            final WorkflowActivity<WorkflowProcess, ActivityInformation<WorkflowProcess>> activity,
            final ActivityInformation<WorkflowProcess> information) {
        if (information.hasAllneededInfo()) {

            try {
                activity.execute(information);
            } catch (final ActivityException e) {
                addLocalizedMessage(request, e.getMessage());
                RenderUtils.invalidateViewState();
                return information.isForwardedFromInput() ? forwardProcessForInput(activity, request, information) : viewProcess(
                        process, request);
            } catch (final DomainException domainEx) {
                addLocalizedMessage(request, domainEx.getLocalizedMessage());
                RenderUtils.invalidateViewState();
                return information.isForwardedFromInput() ? forwardProcessForInput(activity, request, information) : viewProcess(
                        process, request);
            } catch (final ConsistencyException exc) {
                displayConsistencyException(exc, request);

                RenderUtils.invalidateViewState();
                return information.isForwardedFromInput() ? forwardProcessForInput(activity, request, information) : viewProcess(
                        process, request);
            }

            return forwardToProcessPage(process, request);
        }

        return forwardProcessForInput(activity, request, information);
    }

    private final <T extends WorkflowProcess> ActionForward forwardProcessForInput(final WorkflowActivity activity,
            final HttpServletRequest request, final ActivityInformation<T> information) {
        request.setAttribute("information", information);
        if (activity.isDefaultInputInterfaceUsed()) {
            return doForward(request, "/workflow/activityInput.jsp");
        } else {
            request.setAttribute("inputInterface", activity.getClass().getName().replace('.', '/') + ".jsp");
            return doForward(request, "/workflow/nonDefaultActivityInput.jsp");
        }
    }

    public ActionForward viewComments(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        final WorkflowProcess process = getProcess(request);
        request.setAttribute("process", process);

        final Set<WorkflowProcessComment> comments = new TreeSet<WorkflowProcessComment>(WorkflowProcessComment.COMPARATOR);
        comments.addAll(process.getCommentsSet());

        process.markCommentsAsReadForUser(Authenticate.getUser());
        request.setAttribute("comments", comments);
        request.setAttribute("bean", new CommentBean(process));

        return doForward(request, "/workflow/viewComments.jsp");
    }

    public ActionForward addComment(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        final WorkflowProcess process = getProcess(request);

        final String displayedInlineString = request.getParameter("displayedInLine");
        final Boolean displayedInline = Boolean.valueOf(displayedInlineString);

        final CommentBean bean = getRenderedObject("comment");

        process.createComment(Authenticate.getUser(), bean);

        RenderUtils.invalidateViewState();
        if (!displayedInline) {
            return viewComments(mapping, form, request, response);
        } else {
            return viewProcess(process, request);
        }
    }

    private ActionForward forwardToUpload(final HttpServletRequest request, final WorkflowFileUploadBean bean) {

        if (!bean.isDefaultUploadInterfaceUsed()) {
            request.setAttribute("interface", "/" + bean.getSelectedInstance().getName().replace('.', '/') + "-upload.jsp");
        }
        return doForward(request, "/workflow/fileUpload.jsp");
    }

    private ActionForward forwardToDocumentUpload(final HttpServletRequest request, final WorkflowFileUploadBean bean) {

        if (!bean.isDefaultUploadInterfaceUsed()) {
            request.setAttribute("interface", "/" + bean.getSelectedInstance().getName().replace('.', '/') + "-upload.jsp");
        }
        return doForward(request, "/workflow/documentUpload.jsp");
    }

    public ActionForward documentFileUpload(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        final WorkflowProcess process = getProcess(request);
        final Class<? extends ProcessFile> selectedInstance = process.getUploadableFileTypes().get(0);
        final WorkflowFileUploadBean bean = FileUploadBeanResolver.getBeanForProcessFile(process, selectedInstance);
        bean.setSelectedInstance(selectedInstance);

        request.setAttribute("bean", bean);
        request.setAttribute("process", process);

        return forwardToDocumentUpload(request, bean);
    }

    public ActionForward fileUpload(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        final WorkflowProcess process = getProcess(request);
        final Class<? extends ProcessFile> selectedInstance = process.getUploadableFileTypes().get(0);
        final WorkflowFileUploadBean bean = FileUploadBeanResolver.getBeanForProcessFile(process, selectedInstance);
        bean.setSelectedInstance(selectedInstance);

        request.setAttribute("bean", bean);
        request.setAttribute("process", process);

        return forwardToUpload(request, bean);
    }

    public ActionForward upload(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        final WorkflowFileUploadBean bean = getRenderedObject("uploadFile");
        final WorkflowProcess process = getProcess(request);

        try {
            process.addFile(bean.getSelectedInstance(), bean.getDisplayName(), bean.getFilename(),
                    consumeInputStream(bean.getInputStream()), bean);
        } catch (final ProcessFileValidationException e) {
            request.setAttribute("bean", bean);
            request.setAttribute("process", process);
            addLocalizedMessage(request, e.getLocalizedMessage());
            return forwardToUpload(request, bean);
        } catch (final DomainException e) {
            request.setAttribute("bean", bean);
            request.setAttribute("process", process);
            addLocalizedMessage(request, e.getLocalizedMessage());
            RenderUtils.invalidateViewState();
            return forwardToUpload(request, bean);
        }

        return viewProcess(process, request);

    }

    private WorkflowFileUploadBean fileUploadRoundTrip(final Class<? extends ProcessFile> selectedInstance,
            final HttpServletRequest request) {

        final WorkflowProcess process = getProcess(request);
        final WorkflowFileUploadBean bean = FileUploadBeanResolver.getBeanForProcessFile(process, selectedInstance);
        bean.setSelectedInstance(selectedInstance);

        request.setAttribute("bean", bean);
        request.setAttribute("process", process);

        return bean;
    }

    private WorkflowFileUploadBean documentUploadRoundTrip(final Class<? extends ProcessFile> selectedInstance,
            final HttpServletRequest request) {

        final WorkflowProcess process = getProcess(request);
        WorkflowFileUploadBean bean = FileUploadBeanResolver.getBeanForProcessFile(process, selectedInstance);
        bean.setSelectedInstance(selectedInstance);

        request.setAttribute("bean", bean);
        request.setAttribute("process", process);

        return bean;
    }

    public ActionForward uploadPostBack(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        WorkflowFileUploadBean bean = getRenderedObject("uploadFile");
        bean = fileUploadRoundTrip(bean.getSelectedInstance(), request);

        RenderUtils.invalidateViewState("uploadFile");

        return forwardToUpload(request, bean);
    }

    public ActionForward invalidFileUpload(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        WorkflowFileUploadBean bean = getRenderedObject("uploadFile");
        bean = fileUploadRoundTrip(bean.getSelectedInstance(), request);

        return forwardToUpload(request, bean);
    }

    public ActionForward invalidDocumentUpload(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        WorkflowFileUploadBean bean = getRenderedObject("uploadFile");
        bean = documentUploadRoundTrip(bean.getSelectedInstance(), request);

        return forwardToDocumentUpload(request, bean);
    }

    public ActionForward viewLogs(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        final WorkflowProcess process = getProcess(request);

        request.setAttribute("operationLogs", process.getExecutionLogsSet());

        request.setAttribute("process", process);
        return doForward(request, "/workflow/viewLogs.jsp");
    }

    public ActionForward downloadFile(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {

        final ProcessFile file = getDomainObject(request, "fileId");
        final WorkflowProcess process = file.getProcess();
        if (process != null) {
            process.preAccessFile(file);
        }
        download(response, file.getFilename(), file.getStream(), file.getContentType());
        if (process != null) {
            process.postAccessFile(file);
        }
        return null;
    }

    public ActionForward removeFile(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        final ProcessFile file = getDomainObject(request, "fileId");
        final WorkflowProcess process = file.getProcess();
        process.removeFiles(file);

        return viewProcess(process, request);

    }

    public ActionForward viewFilesDetails(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        final WorkflowProcess process = getProcess(request);
        final List<ProcessFile> listFiles = process.getFilesIncludingDeleted(process.getAvailableFileTypes(), true);
        request.setAttribute("process", process);
        request.setAttribute("listFiles", listFiles);
        return doForward(request, "/workflow/viewFilesDetails.jsp");

    }

    private <T extends WorkflowProcess> WorkflowActivity<T, ActivityInformation<T>> getActivity(final WorkflowProcess process,
            final HttpServletRequest request) {
        final String activityName = request.getParameter("activity");
        return process.getActivity(activityName);
    }

    protected <T extends WorkflowProcess> T getProcess(final HttpServletRequest request) {
        return (T) getDomainObject(request, "processId");
    }

    /**
     * Utility method that forwards to the main page of the process.
     * 
     * @param process
     *            to be seen
     * @return ActionFoward to the main page of the process
     */
    public static ActionForward forwardToProcess(final WorkflowProcess process) {
        return new ActionForward("/workflowProcessManagement.do?method=viewProcess&processId=" + process.getExternalId());
    }

    /**
     * Utility method that forwards to the activity page (or executes it in case
     * it doesn't need any input) for a given process
     * 
     * @param process
     *            the process to run the activity against
     * @param activity
     *            the activity to run
     * @return ActionForward to the Activity running lifecycle entry point
     */
    public static ActionForward forwardToActivity(final WorkflowProcess process, final WorkflowActivity activity) {
        return new ActionForward("/workflowProcessManagement.do?method=process&processId=" + process.getExternalId()
                + "&activity=" + activity.getClass().getSimpleName());
    }

    /**
     * Even though a default postback method is given -
     * {@link ProcessManagement#activityDefaultPostback(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)} - in
     * case some other operations should be done during the postback, this
     * method should be use.
     * 
     * Define the postback to your own postback destination and then in that
     * method return the result of this method.
     * 
     * @param information
     *            from the information postback
     * @param request
     *            the httpservlet request
     * @return ActionForward for the activity input interface.
     */
    public ActionForward performActivityPostback(final ActivityInformation<? extends WorkflowProcess> information,
            final HttpServletRequest request) {
        return forwardProcessForInput(information.getActivity(), request, information);
    }

    /*
     * Use this method to register objects of the type {@link ProcessRequestHandler}
     * 
     * @param workflowProcessClass
     *            The type of class for which the handle will perform work
     * @param handler
     *            the handler that performs the specific code
     */
    public static <T extends WorkflowProcess> void registerProcessRequestHandler(final Class<T> workflowProcessClass,
            final ProcessRequestHandler<T> handler) {
        handlers.put(workflowProcessClass, handler);
    }

    /**
     * The ProcessRequestHandler is an handler that allows the user to take
     * control of the HttpServletRequest before the main page of the process is
     * viewed. This is useful if there are layout changes and more objects want
     * to be put in the request.
     * 
     * @param <T>: Some kind of WorkflowProcess
     */
    public static interface ProcessRequestHandler<T extends WorkflowProcess> {
        public void handleRequest(T process, HttpServletRequest request);
    }

    public ActionForward viewTypeDescription(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        String classname = request.getParameter("classname");
        final int indexOfInnerClassInEnum = classname.indexOf("$");
        if (indexOfInnerClassInEnum > 0) {
            classname = classname.substring(0, indexOfInnerClassInEnum);
        }
        final PresentableProcessState type;
        try {
            final Class<Enum> stateEnum = (Class<Enum>) Class.forName(classname);
            type = (PresentableProcessState) Enum.valueOf(stateEnum, request.getParameter("type"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        request.setAttribute("name", type.getLocalizedName());

        final JsonObject reply = new JsonObject();

        reply.addProperty("name", type.getLocalizedName());
        reply.addProperty("description", type.getDescription());

        try (final OutputStream outputStream = response.getOutputStream()) {
            final byte[] jsonReply = reply.toString().getBytes();
            response.setContentType("text");
            response.setContentLength(jsonReply.length);
            outputStream.write(jsonReply);
            outputStream.flush();
        }

        return null;
    }

}
