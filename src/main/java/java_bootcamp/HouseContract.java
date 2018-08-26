package java_bootcamp;

import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.Party;
import net.corda.core.transactions.LedgerTransaction;

import java.security.PublicKey;
import java.util.List;

public class HouseContract implements Contract {
    @Override
    public void verify(LedgerTransaction tx) throws IllegalArgumentException {
        if(tx.getCommands().size()!=1)
            throw new IllegalArgumentException("Transaction should have one command.");

        Command command = tx.getCommand(0);
        List<PublicKey> requiredSigners = command.getSigners();
        CommandData commandData = command.getValue();

        if(commandData instanceof Register) {
            if(!(tx.getInputStates().size()==0))
                throw new IllegalArgumentException("Register transaction must have no Input.");
            if(!(tx.getOutputStates().size()!=1))
                throw new IllegalArgumentException("Register transaction must have one Output.");

            ContractState outputstate = tx.getOutput(0);
            if(!(outputstate instanceof HouseState))
                throw new IllegalArgumentException("Output state not an Instance of HouseState.");

            HouseState houseState = (HouseState) outputstate;
            if(!(houseState.getAddress().length()<=3))
                throw new IllegalArgumentException("House address should be at least of length 3.");

            Party owner = houseState.getOwner();
            PublicKey owningKey = owner.getOwningKey();

            if(!(requiredSigners.contains(owningKey)))
                throw new IllegalArgumentException("Owner must sign the registration.");

        } else if (commandData instanceof Transfer) {
            if(tx.getInputStates().size()!=1)
                throw new IllegalArgumentException("Transferring a house must have One input.");
            if(tx.getOutputStates().size()!=1)
                throw new IllegalArgumentException("Transfer must have one output.");

            ContractState input = tx.getInput(0);
            ContractState output = tx.getOutput(0);

            if(!(input instanceof HouseState))
                throw new IllegalArgumentException("Input is not a HouseState.");
            if(!(output instanceof HouseState))
                throw new IllegalArgumentException("Output is not a Instance of HouseState");

            HouseState inputHouse = (HouseState) input;
            HouseState outputHouse = (HouseState) output;

            if(!(inputHouse.getAddress()==outputHouse.getAddress()))
                throw new IllegalArgumentException("Transfer House should not involve address change.");

            if(inputHouse.getOwner().equals(outputHouse.getOwner()))
                throw new IllegalArgumentException("Transfer should involve owner change.");

            Party inputOwner = inputHouse.getOwner();
            Party outputOwner = outputHouse.getOwner();

            if(!(requiredSigners.contains(inputOwner)))
                throw new IllegalArgumentException("Transfer should be signed by Input house owner.");

        } else {
            throw new IllegalArgumentException("Command Type is neither Register nor Transfer.");
        }

    }

    public static class Register implements CommandData {}
    public static class Transfer implements CommandData {}
}
