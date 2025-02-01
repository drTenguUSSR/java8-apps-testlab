package mil.teng24c.aspectj.intercept;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class InterLocal {
    //@Pointcut("call(public String mil.teng.q2024.animals4classes.habit.HomeHabit.playingWoolBall(int, java.lang.String))") //work,no warn
    //@Pointcut("call(public String mil.teng24c.aspectj.gradle.ExtCall24bAlib.simpleStringConcat(java.lang.String, java.lang.String))")
    //@Pointcut("call(public * mil.teng24c.aspectj..*.*(..))")
    @Pointcut("call(* mil.teng24c.aspectj..*.*(..))")
    public void simpleStringConcat() {
    }

    @Before("simpleStringConcat()")
    public void simpleStringConcatFunc(JoinPoint jp) {
        Logger log = LoggerFactory.getLogger(jp.getTarget().getClass());
        log.error("!aspect-func! before called for func !{}!", jp.getSignature().toLongString());
        System.out.println("!aspect-func!");
    }
}
