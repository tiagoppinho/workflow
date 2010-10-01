package module.workflow.util;

public interface PresentableProcessState {

    public boolean showFor(PresentableProcessState state);

    public String getLocalizedName();

    public String getDescription();

}
