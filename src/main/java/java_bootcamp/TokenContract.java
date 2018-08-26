package java_bootcamp;

import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.Party;
import net.corda.core.transactions.LedgerTransaction;

import java.security.PublicKey;
import java.util.List;

import static net.corda.core.contracts.ContractsDSL.requireThat;

/* Our contract, governing how our state will evolve over time.
 * See src/main/kotlin/examples/ExampleContract.java for an example. */
public class TokenContract implements Contract {
    public static String ID = "java_bootcamp.TokenContract";

    @Override
    public void verify(LedgerTransaction tx) throws IllegalArgumentException {

        if(!(tx.getInputStates().size()==0))
            throw new IllegalArgumentException("Issue transaction should not have any input.");

        if(tx.getOutputStates().size()!=1)
            throw new IllegalArgumentException("Issue transaction must have one output.");

        if(tx.getCommands().size()!=1)
            throw new IllegalArgumentException("Transaction must have one command.");

        ContractState output = tx.getOutput(0);

        TokenState outputState = (TokenState) output;

        if(!(outputState instanceof TokenState))
            throw new IllegalArgumentException("Output is not a registered House.");

        if(!(outputState.getAmount()>0))
            throw new IllegalArgumentException("Amount is not a positive number.");

        Command command = tx.getCommand(0);
        CommandData commandData = command.getValue();

        List<PublicKey> requiredSigners = command.getSigners();
        Party issuer = outputState.getIssuer();
        PublicKey issuerKey = issuer.getOwningKey();

        if(!(commandData instanceof Issue))
            throw new IllegalArgumentException("Command must be of type Issue.");

        if(!(requiredSigners.contains(issuerKey)))
            throw new IllegalArgumentException("Issuer must sign this transaction.");

    }

    public static class Issue implements CommandData {}
}