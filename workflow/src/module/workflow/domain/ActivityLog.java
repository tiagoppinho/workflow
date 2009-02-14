/*
 * @(#)ActivityLog.java
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

package module.workflow.domain;

import myorg.domain.MyOrg;
import myorg.domain.User;

import org.joda.time.DateTime;

public class ActivityLog extends ActivityLog_Base {
    
    public ActivityLog(WorkflowProcess process, User person, String operationName) {
	super();
	setMyOrg(MyOrg.getInstance());
	setProcess(process);
	setActivityExecutor(person);
	setOperation(operationName);
	setWhenOperationWasRan(new DateTime());
	setOjbConcreteClass(this.getClass().getName());
    }
}
