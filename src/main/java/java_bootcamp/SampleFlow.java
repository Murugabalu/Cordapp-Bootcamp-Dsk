package java_bootcamp;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;

@InitiatingFlow
@StartableByRPC
public class SampleFlow extends FlowLogic<Integer> {
    private Party counterParty;

    public SampleFlow(Party counterParty) {
        this.counterParty = counterParty;
    }

    @Suspendable
    public Integer call() throws FlowException {
        FlowSession session = initiateFlow(counterParty);
        session.send(1);

        Integer val = session.receive(Integer.class).unwrap(it -> it);
        return val;
    }
}
