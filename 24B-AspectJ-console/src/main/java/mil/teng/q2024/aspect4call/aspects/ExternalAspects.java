package mil.teng.q2024.aspect4call.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class ExternalAspects {
    //mil.teng.q2024.animals4classes.habit.HomeHabit.playingWoolBall
    //@Pointcut("call(public * mil.teng.q2024.animals4classes.Cat.playingWoolBall(..))")
    //@Pointcut("call(public * mil.teng.q2024..*.*(..))")
    //@Pointcut("call(public String mil.teng.q2024..playingWoolBall(..))") //work-warn
    //@Pointcut("execution(public String mil.teng.q2024.animals4classes.habit.HomeHabit.playingWoolBall(int,String))") //not work
    //@Pointcut("call(public String mil.teng.q2024..playingWoolBall(..))") //work, no warn
    //@Pointcut("call(public String mil.teng.q2024.animals4classes.habit.HomeHabit.playingWoolBall(..))") //work, no warn
    @Pointcut("call(public String mil.teng.q2024.animals4classes.habit.HomeHabit.playingWoolBall(int, java.lang.String))") //work,no warn
    public void playingWoolBallInterceptor() {
    }

    @Before("playingWoolBallInterceptor()")
    public void playingWoolBallInterceptorFunc(JoinPoint jp) {
        Logger log = LoggerFactory.getLogger(jp.getTarget().getClass());
        log.warn("=pwb= before called for func !{}!", jp.getSignature().toLongString());
    }

    //@Pointcut("call(public * mil.teng.q2024..getName(..))") //work
    //@Pointcut("execution(public String mil.teng.q2024.animals4classes.Animal.getName())") //not work
    //@Pointcut("call(* mil..*.getName())") //work, no warn
    //@Pointcut("call(* mil..*.getName())") // work, no warn
    @Pointcut("call(public String mil.teng.q2024.animals4classes.Animal.getName())") //work, no warn
    public void animalGetNameInterceptor() {
    }

    @Before("animalGetNameInterceptor()")
    public void animalGetNameInterceptorFunc(JoinPoint jp) {
        Logger log = LoggerFactory.getLogger(jp.getTarget().getClass());
        log.warn("=agn= before called for func !{}!", jp.getSignature().toLongString());
    }
}
