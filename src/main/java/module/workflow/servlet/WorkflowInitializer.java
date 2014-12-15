package module.workflow.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter;

@WebListener
public class WorkflowInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        RequestChecksumFilter.registerFilterRule(httpServletRequest -> !(httpServletRequest.getRequestURI().endsWith(
                "/workflowProcessManagement.do")
                && httpServletRequest.getQueryString() != null && httpServletRequest.getQueryString().contains(
                "method=viewTypeDescription")));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
