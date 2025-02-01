package mil.teng24c.aspectj.intercept;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class ExtLib24b {

    @Pointcut("call(public String mil.teng24b.aspectj.alib.Cat.playingWoolBall(int, java.lang.String))")
    public void extPlayingWoolBall() {
    }

    @Before("extPlayingWoolBall()")
    public void extPlayingWoolBallFunc(JoinPoint jp) {
        Logger log = LoggerFactory.getLogger(jp.getTarget().getClass());
        log.error("!aspect-func! WoolBall !{}!", jp.getSignature().toLongString());
    }
}
