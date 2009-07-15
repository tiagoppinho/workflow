/*
 * @(#)ActivityInformation.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: João Figueiredo, Luis Cruz, Paulo Abrantes, Susana Fernandes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the MyOrg web application infrastructure.
 *
 *   MyOrg is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published
 *   by the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.*
 *
 *   MyOrg is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with MyOrg. If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package module.workflow.activities;

import java.io.Serializable;

import module.workflow.domain.WorkflowProcess;
import pt.ist.fenixWebFramework.util.DomainReference;

public class ActivityInformation<P extends WorkflowProcess> implements Serializable {

    private DomainReference<P> process;
    private Class<? extends WorkflowActivity> activityClass;
    private String activityName;
    private String localizedName;
    private boolean forwardFromInput;

    public boolean isForwardedFromInput() {
	return forwardFromInput;
    }

    public void markHasForwardedFromInput() {
	this.forwardFromInput = true;
    }

    public String getActivityName() {
	return activityName;
    }

    public void setActivity(WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	this.activityName = activity.getName();
	this.activityClass = activity.getClass();
	this.localizedName = activity.getLocalizedName();
    }

    public ActivityInformation(P process, WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	setProcess(process);
	setActivity(activity);
	forwardFromInput = false;
    }

    public P getProcess() {
	return process.getObject();
    }

    public String getLocalizedName() {
	return localizedName;
    }

    public void setLocalizedName(String localizedName) {
	this.localizedName = localizedName;
    }

    public void setProcess(P process) {
	this.process = new DomainReference<P>(process);
    }

    public boolean hasAllneededInfo() {
	return true;
    }

    public void execute() {
	getActivity().execute(this);
    }

    public WorkflowActivity<P, ActivityInformation<P>> getActivity() {
	return getProcess().getActivity(getActivityName());
    }

    public Class getActivityClass() {
	return this.activityClass;
    }
}
