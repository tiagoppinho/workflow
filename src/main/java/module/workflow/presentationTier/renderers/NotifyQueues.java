/*
 * @(#)NotifyQueues.java
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
package module.workflow.presentationTier.renderers;

import module.workflow.presentationTier.actions.QueueNotificationBean;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

/**
 * 
 * @author João Antunes
 * 
 */
public class NotifyQueues extends OutputRenderer {

    private String unableToNotifyClasses;

    public String getUnableToNotifyClasses() {
        return unableToNotifyClasses;
    }

    public void setUnableToNotifyClasses(String unableToNotifyClasses) {
        this.unableToNotifyClasses = unableToNotifyClasses;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                QueueNotificationBean bean = (QueueNotificationBean) object;
                HtmlInlineContainer component = new HtmlInlineContainer();
                HtmlInlineContainer container = new HtmlInlineContainer();

                component.addChild(container);

                HtmlText text = new HtmlText(bean.getQueue().getName());

                container.addChild(text);

                if (!bean.isAbleToNotify()) {
                    text.setClasses(getUnableToNotifyClasses());
                }

                return component;
            }

        };
    }
}
