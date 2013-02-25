/*
 * @(#)MetaTypeManagement.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Meta-Workflow Module.
 *
 *   The Meta-Workflow Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Meta-Workflow Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Meta-Workflow Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.metaWorkflow.presentationTier.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.metaWorkflow.domain.MetaField;
import module.metaWorkflow.domain.MetaFieldSet;
import module.metaWorkflow.domain.WorkflowMetaType;
import module.metaWorkflow.domain.WorkflowMetaTypeDescription;
import module.metaWorkflow.presentationTier.dto.MetaFieldBean;
import module.metaWorkflow.util.WorkflowMetaTypeBean;
import module.workflow.domain.WorkflowQueue;
import module.workflow.domain.WorkflowSystem;

import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.bennu.core.presentationTier.actions.ContextBaseAction;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/metaTypeManagement")
/**
 * 
 * @author João Neves
 * @author Anil Kassamali
 * @author Paulo Abrantes
 * 
 */
public class MetaTypeManagement extends ContextBaseAction {

    public ActionForward manageMetaType(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        request.setAttribute("metaTypes", WorkflowSystem.getInstance().getMetaTypes());
        return forward(request, "/metaWorkflow/metaType/manageMetaTypes.jsp");
    }

    public ActionForward prepareCreateMetaType(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        request.setAttribute("bean", new WorkflowMetaTypeBean());
        return forward(request, "/metaWorkflow/metaType/createMetaType.jsp");
    }

    public ActionForward viewMetaType(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        WorkflowMetaType type = getDomainObject(request, "metaTypeId");
        request.setAttribute("metaType", type);

        return forward(request, "/metaWorkflow/metaType/viewMetaType.jsp");
    }

    public ActionForward createNewMetaType(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        WorkflowMetaTypeBean bean = getRenderedObject("newMetaType");
        WorkflowMetaType.createNewMetaType(bean.getName(), bean.getDescription(), bean.getOrganizationModel(),
                bean.getFileClassNames());

        RenderUtils.invalidateViewState("newMetaType");
        return manageMetaType(mapping, form, request, response);
    }

    public ActionForward editMetaType(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        WorkflowMetaType type = getDomainObject(request, "metaTypeId");
        request.setAttribute("metaType", type);
        return forward(request, "/metaWorkflow/metaType/editMetaType.jsp");
    }

    public ActionForward manageFields(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        WorkflowMetaType metaType = FenixFramework.getDomainObject(request.getParameter("metaTypeId"));
        request.setAttribute("metaType", metaType);
        String fieldSetId = request.getParameter("fieldSetId");
        if (fieldSetId != null) {
            request.setAttribute("fieldSet", FenixFramework.getDomainObject(fieldSetId));
        } else {
            request.setAttribute("fieldSet", metaType.getFieldSet());
        }

        return forward(request, "/metaWorkflow/metaType/manageFields.jsp");
    }

    public ActionForward prepareAddField(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        WorkflowMetaType metaType = FenixFramework.getDomainObject(request.getParameter("metaTypeId"));
        request.setAttribute("metaType", metaType);
        MetaFieldSet fieldSet = FenixFramework.getDomainObject(request.getParameter("fieldSetId"));
        request.setAttribute("fieldSet", fieldSet);

        request.setAttribute("fieldBean", new MetaFieldBean());

        return forward(request, "/metaWorkflow/metaType/addField.jsp");
    }

    public ActionForward addField(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        MetaFieldBean fieldBean = getRenderedObject("fieldBean");
        WorkflowMetaType metaType = FenixFramework.getDomainObject(request.getParameter("metaTypeId"));
        request.setAttribute("metaType", metaType);
        MetaFieldSet fieldSet = FenixFramework.getDomainObject(request.getParameter("fieldSetId"));
        request.setAttribute("fieldSet", fieldSet);

        if ((fieldBean.getName() == null) || (fieldBean.getName().getContent() == null)
                || fieldBean.getName().getContent().trim().isEmpty()) {
            request.setAttribute("fieldBean", fieldBean);
            addLocalizedMessage(request,
                    BundleUtil.getStringFromResourceBundle("resources/MetaWorkflowResources", "label.error.fieldNameRequired"));
            return forward(request, "/metaWorkflow/metaType/addField.jsp");
        }

        MetaField.createMetaField(fieldBean, fieldSet);

        return forward(request, "/metaWorkflow/metaType/manageFields.jsp");
    }

    public ActionForward removeField(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        WorkflowMetaType metaType = FenixFramework.getDomainObject(request.getParameter("metaTypeId"));
        request.setAttribute("metaType", metaType);
        MetaFieldSet fieldSet = FenixFramework.getDomainObject(request.getParameter("fieldSetId"));
        request.setAttribute("fieldSet", fieldSet);

        MetaField fieldToRemove = FenixFramework.getDomainObject(request.getParameter("fieldId"));

        fieldToRemove.delete();

        return forward(request, "/metaWorkflow/metaType/manageFields.jsp");
    }

    public ActionForward doDiff(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        String revision1 = request.getParameter("rev1");
        String revision2 = request.getParameter("rev2");
        if (!StringUtils.isEmpty(revision1) && !StringUtils.isEmpty(revision2)) {
            Integer version1 = Integer.valueOf(revision1);
            Integer version2 = Integer.valueOf(revision2);

            WorkflowMetaType metaType = getDomainObject(request, "metaTypeId");
            WorkflowMetaTypeDescription descriptionV1 = metaType.getDescriptionAtVersionOld(version1);
            WorkflowMetaTypeDescription descriptionV2 = metaType.getDescriptionAtVersionOld(version2);

            request.setAttribute("version1", descriptionV1);
            request.setAttribute("version2", descriptionV2);
            request.setAttribute("diff", descriptionV1.getDiffWith(descriptionV2));

        }

        return viewMetaType(mapping, form, request, response);
    }

    public ActionForward viewVersion(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        WorkflowMetaType metaType = getDomainObject(request, "metaTypeId");
        Integer version = Integer.valueOf(request.getParameter("version"));
        request.setAttribute("historyVersion", metaType.getDescriptionAtVersion(version));

        return viewMetaType(mapping, form, request, response);
    }

    public ActionForward manageQueues(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        WorkflowMetaType metaType = getDomainObject(request, "metaTypeId");
        request.setAttribute("metaType", metaType);
        request.setAttribute("presentQueues", metaType.getQueues());
        request.setAttribute("possibleQueues", WorkflowQueue.getQueues(new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return ((WorkflowQueue) arg0).getMetaType() == null;
            }
        }));

        return forward(request, "/metaWorkflow/metaType/manageAssociatedQueues.jsp");
    }

    public ActionForward addQueue(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        WorkflowMetaType metaType = getDomainObject(request, "metaTypeId");
        WorkflowQueue queue = getDomainObject(request, "queueId");

        metaType.addQueues(queue);

        return manageQueues(mapping, form, request, response);
    }

    public ActionForward removeQueue(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        WorkflowMetaType metaType = getDomainObject(request, "metaTypeId");
        WorkflowQueue queue = getDomainObject(request, "queueId");

        metaType.removeQueues(queue);

        return manageQueues(mapping, form, request, response);
    }

}
