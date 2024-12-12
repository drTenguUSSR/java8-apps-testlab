package mil.teng.q2024.aspect4call.aspects;

import mil.teng.q2024.aspect4call.App;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class ExternalSubcallAspects {
    // !public static void mil.teng.q2024.aspect4call.App.makeSound()!
    // !public abstract void mil.teng.q2024.animals4classes.Animal.makeSound()!

    //mil.teng.q2024.animals4classes.SoundToolObj.makeSound
    // -> public abstract void mil.teng.q2024.animals4classes.Animal.makeSound()
    //@Pointcut("call(public void mil.teng.q2024.animals4classes.SoundToolObj.makeSound(..))") //not work
    //@Pointcut("call(public void mil..*.makeSound(..))") //working
    //@Pointcut("call(public abstract void mil.teng.q2024.animals4classes.Animal.makeSound())")
    //@Pointcut("call(public void mil.teng.q2024.animals4classes.Animal.makeSound())") //static not work
    //@Pointcut("call(public void mil..*.makeSound())") //work-all
    //@Pointcut("call(public void mil..Animal.makeSound())") // work abstract,not work static
    @Pointcut("call(public void mil.teng.q2024.animals4classes.Animal.makeSound())") //work abstract,not work static
    public void stMakeSoundAbstract() {
    }

    //@Pointcut("call(public static void mil.teng.q2024.animals4classes.Animal.makeSound())")
    @Pointcut("call(public static void mil.teng.q2024.aspect4call.App.makeSound())")
    public void stMakeSoundStatic() {
    }

    @Before("stMakeSoundAbstract()||stMakeSoundStatic()")
    public void stMakeSoundFunc(JoinPoint jp) {
        Logger log = LoggerFactory.getLogger(jp.getTarget() == null ? App.class : jp.getTarget().getClass());
        log.warn("=stMakeSound= before called for func !{}!", jp.getSignature().toLongString());
    }
}
