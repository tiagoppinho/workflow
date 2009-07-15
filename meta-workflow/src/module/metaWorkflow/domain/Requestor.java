package module.metaWorkflow.domain;

public abstract class Requestor extends Requestor_Base {

    public Requestor() {
	super();
	this.setOjbConcreteClass(this.getClass().getName());
    }

    public abstract String getName();

    public abstract String getEmail();

}
