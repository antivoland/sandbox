package antivoland.rtest.storage;

import antivoland.rtest.model.Wallet;
import antivoland.rtest.model.WalletStorage;

public class WalletDummyStorage extends DummyStorage<String, Wallet> implements WalletStorage {
    @Override
    protected Wallet copy(Wallet wallet) {
        return new Wallet(wallet);
    }
}
