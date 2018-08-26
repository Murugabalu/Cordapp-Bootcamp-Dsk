package java_bootcamp;

import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ContainerState implements ContractState {

    private int width;
    private int height;
    private int depth;

    private String message;

    private Party owner;
    private Party carrier;

    public ContainerState(int width, int height, int depth, String message, Party owner, Party carrier) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.message = message;
        this.owner = owner;
        this.carrier = carrier;
    }

    public static void main(String[] args) {
        ContainerState state = new ContainerState(1,2,3,"Container", null, null);
    }
    @NotNull
    @Override
    public List<AbstractParty> getParticipants() {
        return ImmutableList.of(owner, carrier);
    }

    public Party getCarrier() {
        return carrier;
    }

    public Party getOwner() {
        return owner;
    }

    public String getMessage() {
        return message;
    }

    public int getDepth() {
        return depth;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
