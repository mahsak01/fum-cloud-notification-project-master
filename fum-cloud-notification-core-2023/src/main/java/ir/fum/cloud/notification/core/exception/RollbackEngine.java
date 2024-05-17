package ir.fum.cloud.notification.core.exception;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ali Mojahed on 12/19/2022
 * @project noticom
 **/
public abstract class RollbackEngine<A, T> {
    private List<Pair<A, T>> actions = new ArrayList<>();

    public abstract void doRollback() throws NotificationException;

    public void addToRollbackList(A action, T info) {
        actions.add(Pair.of(action, info));
    }

    public void clearRollbackList() {
        actions.clear();
    }

    public List<Pair<A, T>> getActions() {
        return actions;
    }
}
