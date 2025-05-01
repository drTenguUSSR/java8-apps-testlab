package mil.teng251.codesnippets.ntfs;

import mil.teng251.codesnippets.SnipExec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * for IDE run: add VM options:
 * -Dlog4j2.configurationFile=config/log4j2.xml
 */
public class NtfsStreamsInfo implements SnipExec {
    private static final Logger log = LoggerFactory.getLogger(NtfsStreamsInfo.class);

    @Override
    public void execute(String[] args) {
        log.debug("called. args.size={} [",args.length);
        for(int i1=0;i1<args.length;i1++) {
            log.debug("- {}: {}",i1,args[i1]);
        }
        log.debug("]");
    }
}
