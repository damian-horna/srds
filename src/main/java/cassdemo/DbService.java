package cassdemo;

import cassdemo.backend.BackendSession;

public class DbService {
    BackendSession session;

    public DbService(BackendSession session) {
        this.session = session;
    }
}
