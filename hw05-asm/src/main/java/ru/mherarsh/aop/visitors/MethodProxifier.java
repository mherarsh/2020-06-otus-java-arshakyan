package ru.mherarsh.aop.visitors;

import org.objectweb.asm.*;
import ru.mherarsh.aop.dto.MethodDescription;
import ru.mherarsh.aop.utils.AsmUtils;

import java.lang.invoke.*;
import java.util.List;

public class MethodProxifier extends ClassVisitor {
    private final ClassVisitor classVisitor;
    private final String className;
    private final List<MethodDescription> methodsForProxy;

    private static final String STRING_CONCAT_FACTORY = "java/lang/invoke/StringConcatFactory";

    private static final Handle MAKE_CONCAT_WITH_CONSTANTS = new Handle(
            Opcodes.H_INVOKESTATIC, STRING_CONCAT_FACTORY, "makeConcatWithConstants",
            MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class,
                    MethodType.class, String.class, Object[].class).toMethodDescriptorString(),
            false);

    public MethodProxifier(int api, ClassVisitor classVisitor, String className, List<MethodDescription> methodsForProxy) {
        super(api, classVisitor);
        this.className = className;
        this.methodsForProxy = methodsForProxy;
        this.classVisitor = classVisitor;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (isNeedProxyfied(name, descriptor)) {
            proxyMethod(access, name, descriptor, signature, exceptions);
            return super.visitMethod(Opcodes.ACC_PRIVATE, getProxyMethodName(name), descriptor, signature, exceptions);
        }

        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    private void proxyMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        var mv = classVisitor.visitMethod(access, name, descriptor, signature, exceptions);
        var argumentTypes = Type.getArgumentTypes(descriptor);

        printLogMessage(name, descriptor, mv, argumentTypes);
        invokeOriginalMethod(name, descriptor, mv, argumentTypes);

        mv.visitInsn(AsmUtils.getReturnOpc(descriptor));
        mv.visitMaxs(0, 0);
    }

    private void invokeOriginalMethod(String name, String descriptor, MethodVisitor mv, Type[] argumentTypes) {
        mv.visitVarInsn(Opcodes.ALOAD, 0);

        loadParameters(mv, argumentTypes);

        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, className, getProxyMethodName(name), descriptor, false);
    }

    private void printLogMessage(String name, String descriptor, MethodVisitor mv, Type[] argumentTypes) {
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

        var recipe = AsmUtils.getRecipe(name, argumentTypes.length);

        if (argumentTypes.length > 0) {
            loadParameters(mv, argumentTypes);

            mv.visitInvokeDynamicInsn(
                    MAKE_CONCAT_WITH_CONSTANTS.getName(),
                    MethodType.fromMethodDescriptorString(descriptor, this.getClass().getClassLoader())
                            .changeReturnType(String.class)
                            .toMethodDescriptorString(),
                    MAKE_CONCAT_WITH_CONSTANTS,
                    recipe);
        } else {
            mv.visitLdcInsn(recipe);
        }

        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
    }

    private void loadParameters(MethodVisitor mv, Type[] argumentTypes) {
        for (int i = 0; i < argumentTypes.length; i++) {
            mv.visitVarInsn(AsmUtils.getLoadInsnByType(argumentTypes[i]), i + 1);
        }
    }

    private boolean isNeedProxyfied(String methodName, String methodDescription) {
        for (var md : methodsForProxy) {
            if (md.getName().equals(methodName) && md.getDescription().equals(methodDescription)) {
                return true;
            }
        }
        return false;
    }

    private String getProxyMethodName(String methodName) {
        return methodName + "Proxied";
    }
}
