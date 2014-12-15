/*
 * @(#)ProcessFile.java
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
package module.workflow.domain;

import jvstm.cps.ConsistencyPredicate;
import module.workflow.util.ClassNameBundle;
import module.workflow.util.WorkflowFileUploadBean;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.User;

/**
 * 
 * @author João Antunes
 * @author Shezad Anavarali
 * @author Luis Cruz
 * @author Paulo Abrantes
 * @author Susana Fernandes
 * 
 *         TODO: make abstract
 * 
 */
@ClassNameBundle(bundle = "WorkflowResources")
public class ProcessFile extends ProcessFile_Base {

    public ProcessFile() {
        super();
    }

    public ProcessFile(String displayName, String filename, byte[] content) {
        super();
        init(displayName, filename, content);
    }

    @ConsistencyPredicate
    public boolean checkConnectedWithProcess() {
        return getProcess() != null || getProcessWithDeleteFile() != null;

    }

    public void fillInNonDefaultFields(WorkflowFileUploadBean bean) {

    }

    /**
     * Hook that is called each time someone tries to access the file (for now
     * this translates to Download, but a preview, which is not implemented yet,
     * should also invoke this method)
     * 
     * @author João Antunes
     */
    public void preFileContentAccess() {

    }

    /**
     * Hook that is called after someone accesses a file (for now this
     * translates to Download, but a preview, which is not implemented yet,
     * should also invoke this method)
     * 
     * @author João Antunes
     */
    public void postFileContentAccess() {

    }

    /**
     * 
     * @return true if one should log whenever someone tries to access the
     *         content of the file, false otherwise
     * @author João Antunes
     */
    public boolean shouldFileContentAccessBeLogged() {
        return false;
    }

    /**
     * Validates if this file is valid to be associated with the workflowProcess
     * 
     * @param workflowProcess
     *            the process to which this file is being associated
     * 
     * @throws module.workflow.domain.ProcessFileValidationException
     *             if does not validate
     */
    public void validateUpload(WorkflowProcess workflowProcess) throws ProcessFileValidationException {

    }

    /**
     * Before validation and adding the file this method is called. The default
     * behavior is doing nothing.
     * 
     * @param bean
     *            the bean that contains all the data used to create this file.
     */
    public void preProcess(WorkflowFileUploadBean bean) {

    }

    /**
     * After validation and adding the file this method is called. The default
     * behavior is doing nothing.
     * 
     * @param bean
     *            the bean that contains all the data used to create this file.
     */
    public void postProcess(WorkflowFileUploadBean bean) {

    }

    public boolean isParsableType() {
        return getFilename().toLowerCase().endsWith(".pdf");
    }

    /**
     * Tells if the file is able to be archieved or not at the given time the
     * method is called. (This will renderer a remove link on the interface when
     * returns true).
     * 
     * By default it returns true
     * @return true
     */
    public boolean isPossibleToArchieve() {
        return true;
    }

    @Override
    public void delete() {
        setProcess(null);
        setProcessWithDeleteFile(null);
        super.delete();
    }

    /**
     * This method is called after a file is archived in order to allow the user
     * to remove possible relations.
     * 
     * By default does nothing
     */
    public void processRemoval() {

    }

    public boolean isArchieved() {
        return getProcess() == null && getProcessWithDeleteFile() != null;
    }

    public String getPresentationName() {
        return StringUtils.isEmpty(getDisplayName()) ? getFilename() : getDisplayName();
    }

    @Override
    public boolean isAccessible(User user) {
        return getProcess().isAccessible(user);
    }

    @Override
    public WorkflowProcess getProcess() {
        return super.getProcess();
    }
}
