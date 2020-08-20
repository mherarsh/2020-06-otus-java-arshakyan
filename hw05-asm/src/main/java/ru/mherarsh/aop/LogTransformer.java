package ru.mherarsh.aop;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import ru.mherarsh.aop.visitors.AnnotationScanner;
import ru.mherarsh.aop.visitors.MethodProxifier;

public class LogTransformer {
    final static int API_VERSION = Opcodes.ASM5;
    final static String LOG_ANNOTATION = "Lru/mherarsh/aop/annotations/Log;";

    public static byte[] transform(ClassReader cr) {
        var annotationScanner = new AnnotationScanner(API_VERSION, LOG_ANNOTATION);
        cr.accept(annotationScanner, API_VERSION);

        var methodsForProxy = annotationScanner.getAnnotatedMethods();

        var cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);

        var methodProxifier = new MethodProxifier(API_VERSION, cw, cr.getClassName(), methodsForProxy);
        cr.accept(methodProxifier, API_VERSION);

        return cw.toByteArray();
    }
}
