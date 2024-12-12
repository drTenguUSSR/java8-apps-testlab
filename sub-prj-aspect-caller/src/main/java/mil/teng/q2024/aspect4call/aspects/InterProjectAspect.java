package mil.teng.q2024.aspect4call.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * https://javarush.com/quests/lectures/questspring.level01.lecture61
 * Доступ к текущей JoinPoint
 * <p>
 * https://stackoverflow.com/questions/12423965/maven-aspectj-all-steps-to-configure-it
 * <p>
 * https://github.com/Nosfert/AspectJ-Tutorial-jayway/blob/master/annotations-element-value-pair/src/main/java/com/jayway/blog/YourAnnotation.java
 * https://github.com/Nosfert/AspectJ-Tutorial-jayway/blob/master/pom.xml
 */
@Aspect
public class InterProjectAspect {

    //mil.teng.q2024.aspect4call.InterProjectCall.someCall
    //@Pointcut("execution(* mil..*())") //work, no warn
    //@Pointcut("execution(void mil.teng.q2024..*())") //work, no warn
    //@Pointcut("execution(void mil.teng.q2024..*.*())") //work, no warn
    //@Pointcut("execution(public void mil.teng.q2024.aspect4call.InterProjectCall.someCall())") //work with warn
    //@Pointcut("execution(void mil.teng.q2024.aspect4call..*())") //work with warn
    //@Pointcut("execution(void mil.teng.q2024..InterProjectCall.*())") //work with warn
    //@Pointcut("execution(public void mil.teng.q2024.aspect4call.InterProjectCall.someCall())") //work with warn
    //@Pointcut("execution(public void mil.teng..*.someCall())") //work no warn
    @Pointcut("execution(public void mil.teng.q2024.aspect4call.InterProjectCall.someCall())") //work no warn
    public void interProjectCallInterceptor() {
    }

    @Before("interProjectCallInterceptor()")
    public void interProjectCallInterceptorFunc(JoinPoint jp) {
        Logger log = LoggerFactory.getLogger(jp.getTarget().getClass());
        log.warn("=ipc= before called for func !{}!", jp.getSignature().toLongString());
    }


}
