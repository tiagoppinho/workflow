package module.workflow.util;

import java.util.List;

public interface HasPresentableProcessState {

    public PresentableProcessState getPresentableAcquisitionProcessState();

    public List<? extends PresentableProcessState> getAvailablePresentableStates();

}
