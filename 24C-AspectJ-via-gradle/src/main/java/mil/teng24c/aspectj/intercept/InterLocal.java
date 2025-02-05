package mil.teng24c.aspectj.intercept;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
/**
 * common syntax
 * execution(modifiers-pattern? ret-type-pattern declaring-type-pattern?name-pattern(param-pattern) throws-pattern?)
 */

public class InterLocal {
    //@Before("call(public String mil.teng.q2024.animals4classes.habit.HomeHabit.playingWoolBall(int, java.lang.String))") //work,no warn
    //@Before("call(public * mil.teng24c.aspectj..*.*(..))")
    //@Before("call(* mil.teng24c.aspectj..*.*(..))")
    @Before("execution(public String mil.teng24c.aspectj.gradle.ExtCall24bAlib.simpleStringConcat(java.lang.String, java.lang.String))")
    public void simpleStringConcatFunc(JoinPoint jp) {
        Logger log = LoggerFactory.getLogger(jp.getTarget().getClass());
        log.error("!aspect-func-before.execution! Simple !{}!", jp.getSignature().toLongString());
    }

    //mil.teng24c.aspectj.gradle.UserLocalName.setAge
    @Around("execution(public void mil.teng24c.aspectj.gradle.UserLocalName.setAge(int))")
    public Object cutUserLocalNameSetAgeFunc(ProceedingJoinPoint pjp) throws Throwable {
        Logger log = LoggerFactory.getLogger(pjp.getTarget().getClass());
        log.error("!aspect-func! cutUserLocalNameSetAge before");
        Object[] objects = pjp.getArgs();
        Object valA = objects[0];
        Integer valB = (Integer) valA + 100;
        objects[0] = valB;
        log.error("!aspect-func! cutUserLocalNameSetAge args override {} -> {}", valA, valB);
        Object res = pjp.proceed(objects);
        log.error("!aspect-func! cutUserLocalNameSetAge after");
        return res;
    }

    //com.google.common.hash.AbstractByteHasher#putLong
    //public Hasher putLong(long l)
    //@Before("call(public * com.google..*.*(..))") //work
    //@Before("call(public * com.google.common.hash.Hasher.putLong(long))") //work
    @Before("call(public com.google.common.hash.Hasher com.google.common.hash.Hasher.putLong(long))") //work
    public void cutComGoogleHash_putLong(JoinPoint jp) {
        Logger log = LoggerFactory.getLogger(jp.getTarget().getClass());
        Object parmLong = jp.getArgs()[0];
        log.error("!aspect.cutComGoogleHash_putLong! parm={} sign=!{}!", parmLong, jp.getSignature().toLongString());
    }

    //com.google.common.hash.MessageDigestHashFunction.MessageDigestHasher#hash
    //public HashCode hash()
    //@Before("call(* com.google.common.hash.MessageDigestHashFunction.MessageDigestHasher.hash(..))") //not work
    //@Before("call(* com..*.*(..))") // all-request info
    //public abstract com.google.common.hash.HashCode com.google.common.hash.Hasher.hash()
    //@Before("call(* com.google.common.hash.Hasher.hash())") //working
    //@Before("call(public com.google.common.hash.HashCode com.google.common.hash.Hasher.hash())") //work
    //@Pointcut("call(public com.google.common.hash.HashCode com.google.common.hash.Hasher.hash())") //work
    @Pointcut("execution(public com.google.common.hash.HashCode com.google.common.hash.Hasher.hash())")
    public void pointcutComGoogleHash_hash() {
    }

    @Before("pointcutComGoogleHash_hash()") //work
    public void cutComGoogleHash_hash(JoinPoint jp) {
        Logger log = LoggerFactory.getLogger(jp.getTarget().getClass());
        log.error("!aspect.cutComGoogleHash_hash! before sign=!{}!", jp.getSignature().toLongString());
    }

//    @AfterReturning(pointcut = "pointcutComGoogleHash_hash()",returning = "retVal") //work
//    public void cutComGoogleHash_hash_ret(JoinPoint jp,String retVal) {
//        Logger log = LoggerFactory.getLogger(jp.getTarget().getClass());
//        log.error("!aspect.cutComGoogleHash_hash! retVal={}",retVal);
    }
}
