package ru.mherarsh.aop.agent;

import org.objectweb.asm.ClassReader;
import ru.mherarsh.aop.LogTransformer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class LoggingAgent {
    private static final String SCAN_CLASS_PREFIX = "ru/mherarsh";

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("Logging agent premain is call...");

        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) {

                if (className.startsWith(SCAN_CLASS_PREFIX)) {
                    return LogTransformer.transform(new ClassReader(classfileBuffer));
                }
                return classfileBuffer;
            }
        });
    }
}
