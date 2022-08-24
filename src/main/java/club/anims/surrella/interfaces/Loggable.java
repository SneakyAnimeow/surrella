package club.anims.surrella.interfaces;

import org.slf4j.Logger;

public interface Loggable {
    default Logger getLogger() {
        return org.slf4j.LoggerFactory.getLogger(getClass());
    }
}
