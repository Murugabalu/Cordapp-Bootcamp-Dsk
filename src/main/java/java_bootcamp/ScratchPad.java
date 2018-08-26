package java_bootcamp;

import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.ContractState;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.transactions.TransactionBuilder;

import java.security.PublicKey;
import java.util.List;

public class ScratchPad {
    public static void main(String[] args) {
        StateAndRef<ContractState> inputState = null;
        HouseState outputState = new HouseState("1 ctc cognizant", null);
        PublicKey owner = outputState.getOwner().getOwningKey();
        List<PublicKey> requiredSigners = ImmutableList.of(owner);

        TransactionBuilder tx = new TransactionBuilder();

        tx.addInputState(inputState)
                .addOutputState(outputState, "java_bootcamp.HouseContract")
                .addCommand(new HouseContract.Register(), requiredSigners);
    }
}
