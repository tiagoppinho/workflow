package module.metaWorkflow.domain;

import pt.ist.fenixWebFramework.util.DomainReference;
import module.metaWorkflow.util.WorkflowQueueBean;
import module.organization.domain.Unit;

public class WorkflowUnitQueueBean extends WorkflowQueueBean {

    private DomainReference<Unit> unit;

    public Unit getUnit() {
        return unit.getObject();
    }

    public void setUnit(Unit unit) {
        this.unit = new DomainReference<Unit>(unit);
    }
    
}
