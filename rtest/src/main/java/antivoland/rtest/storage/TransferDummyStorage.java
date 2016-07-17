package antivoland.rtest.storage;

import antivoland.rtest.model.Transfer;
import antivoland.rtest.model.TransferStorage;

public class TransferDummyStorage extends DummyStorage<String, Transfer> implements TransferStorage {
    @Override
    protected Transfer copy(Transfer transfer) {
        return new Transfer(transfer);
    }
}
