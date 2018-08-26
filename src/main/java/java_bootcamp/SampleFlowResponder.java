package java_bootcamp;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.FlowSession;
import net.corda.core.flows.InitiatedBy;

@InitiatedBy(SampleFlow.class)
public class SampleFlowResponder extends FlowLogic<Integer> {
    private FlowSession rcvdSession;

    public SampleFlowResponder(FlowSession rcvdSession) {
        this.rcvdSession = rcvdSession;
    }

    @Suspendable
    public Integer call() throws FlowException {
        Integer rcvdInt = rcvdSession.receive(Integer.class).unwrap(it -> {
            if(it>3)
                throw new IllegalArgumentException("Rcvd value is bigger.");
            return it;
        });

        int rcvdPlusOne = rcvdInt + 1;
        rcvdSession.send(rcvdPlusOne);

        return rcvdPlusOne;
    }
}
