package net.yangeorget.jelly;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AOP based tracer (used for debugging purposes).
 * @author y.georget
 */
@Aspect
public class Tracer {

    static class Indent {
        int value;

        public void inc() {
            value++;
        }

        public void dec() {
            value--;
        }
    }

    // Ideally, the Indent instance should be shared between the threads used by a single test (timeout is handled with
    // one additional thread for instance).
    private final ThreadLocal<Indent> indent = new ThreadLocal<Indent>() {
        @Override
        protected Indent initialValue() {
            return new Indent();
        }
    };

    /**
     * We exclude this class to avoid infinite loops.
     */
    private static final String TRACER_POINTCUT_STR = "execution(public * net.yangeorget.jelly..*.*(..)) && !within(net.yangeorget.jelly.Tracer)";

    @Pointcut(TRACER_POINTCUT_STR)
    void tracerPointcut() {
    }

    /**
     * An advice for tracing interactions with methods.
     * @param joinPoint the join point
     * @return the object that should have been returned by the method or null
     * @throws Throwable when the method should have thrown a throwable
     */
    @Around("tracerPointcut()")
    public Object aroundAdvice(final ProceedingJoinPoint joinPoint) throws Throwable {
        final Class<?> clazz = joinPoint.getStaticPart()
                                        .getSignature()
                                        .getDeclaringType();
        if (clazz.isAnonymousClass()) {
            return joinPoint.proceed();
        }
        final Logger logger = LoggerFactory.getLogger(clazz);
        try {
            logEntering(logger, joinPoint);
            final Object returnedObject = joinPoint.proceed();
            logExiting(logger, "void".equals(((MethodSignature) joinPoint.getSignature()).getReturnType()
                                                                                         .toString())
                    ? ""
                    : "returning (" + returnedObject + ") from ", joinPoint);
            return returnedObject;
        } catch (final Throwable throwable) {
            logExiting(logger, "throwing (" + throwable + ") in ", joinPoint);
            throw throwable;
        }
    }

    private void logEntering(final Logger logger, final JoinPoint joinPoint) {
        log(logger, "> ", joinPoint);
        indent.get()
              .inc();
    }

    private void logExiting(final Logger logger, final String msg, final JoinPoint joinPoint) {
        indent.get()
              .dec();
        log(logger, "< " + msg, joinPoint);
    }

    /**
     * Logs a message to describe the interaction with the method.
     * @param logger the SLF4J logger
     * @param msg the message to log
     * @param joinPoint the current join point
     */
    private void log(final Logger logger, final String msg, final JoinPoint joinPoint) {
        String indentString = "";
        for (int i = 0; i < indent.get().value; i++) {
            indentString += "  ";
        }
        logger.debug(indentString + msg + joinPoint.toShortString());
    }
}
