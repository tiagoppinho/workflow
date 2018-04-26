/*
 * @(#)CommentersForProcess.java
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
package module.workflow.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import module.workflow.domain.PermanentProcessCommentUser;
import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowSystem;
import module.workflow.presentationTier.actions.CommentBean;
import module.workflow.presentationTier.actions.UserNotificationBean;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

/**
 * 
 * @author João Antunes
 * @author Paulo Abrantes
 * 
 */
public class CommentersForProcess implements DataProvider {

    @Override
    public Converter getConverter() {
        return null;
    }

    @Override
    public Object provide(Object source, Object currentValue) {
        CommentBean bean = (CommentBean) source;
        Set<UserNotificationBean> availablePeopleToNotify = new HashSet<UserNotificationBean>();
        WorkflowProcess process = bean.getProcess();

        availablePeopleToNotify.add(new UserNotificationBean(process.getProcessCreator(), process));
        //let's also add the 'takers'/owners of the process
        if (process.getCurrentOwner() != null) {
            availablePeopleToNotify.add(new UserNotificationBean(process.getCurrentOwner(), process));
        }

        process.getCommentsSet().stream()
            .map(c -> new UserNotificationBean(c.getCommenter(), process))
            .forEach(b -> availablePeopleToNotify.add(b));

        process.getProcessWorkerStream().map(u -> new UserNotificationBean(u, process))
            .forEach(b -> availablePeopleToNotify.add(b));

        process.getObserversSet().stream()
            .map(o -> new UserNotificationBean(o, process))
            .forEach(b -> availablePeopleToNotify.add(b));

        WorkflowSystem.getInstance().getPermanentProcessCommentUserSet().forEach(ppcu -> getPeople(availablePeopleToNotify, ppcu, process));

        return new ArrayList<UserNotificationBean>(availablePeopleToNotify);
    }

    private void getPeople(final Set<UserNotificationBean> set, final PermanentProcessCommentUser ppcu, final WorkflowProcess process) {
        try {
            Class c = Class.forName(ppcu.getClassName());
            if (c.isAssignableFrom(process.getClass())) {
                ppcu.getUserSet().forEach(u -> set.add(new UserNotificationBean(u, process)));
            }
        } catch (final ClassNotFoundException e) {
            throw new Error(e);
        }
    }

}
