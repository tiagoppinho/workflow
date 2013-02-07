package module.workflow.domain;

import pt.ist.bennu.core.domain.User;

/**
 * 
 * @author João Antunes (joao.antunes@tagus.ist.utl.pt) - 31 de Ago de 2012
 * 
 *         Default Workflow group for {@link ProcessDocument}s. It basicly
 *         delegates the access control to a mixture between {@link WorkflowProcess#isFileSupportAvailable()} and
 *         {@link WorkflowProcess#isAccessible(User)}
 * 
 */
public class WFDocsDefaultReadGroup extends WFDocsDefaultReadGroup_Base {

    private static String NAME = "WorkFlow Documents Default Read Access Group";

    public WFDocsDefaultReadGroup(WorkflowProcess process) {
        super();
        setProcess(process);
    }

    /**
     * Only used by _Base, not to use directly, use {@link WFDocsDefaultReadGroup#WFDocsDefaultReadGroup(WorkflowProcess)} instead
     */
    @Deprecated
    public WFDocsDefaultReadGroup() {
    }

    @Override
    public boolean isMember(User user) {
        return getProcess().isFileSupportAvailable() && getProcess().isAccessible(user);
    }

    @Override
    public String getName() {
        return NAME;
    }

    public static module.workflow.domain.AbstractWFDocsGroup getOrCreateInstance(WorkflowProcess process) {
        WFDocsDefaultReadGroup readGroup =
                (process.getDocumentsRepository() == null || !(process.getDocumentsRepository().getReadGroup() instanceof WFDocsDefaultReadGroup)) ? null : (WFDocsDefaultReadGroup) process
                        .getDocumentsRepository().getReadGroup();
        if (readGroup == null) {
            readGroup = new WFDocsDefaultReadGroup(process);
        }
        return readGroup;
    }

}
