package ru.mherarsh.aop.utils;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.HashMap;
import java.util.Map;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.DLOAD;
import static org.objectweb.asm.Opcodes.DRETURN;
import static org.objectweb.asm.Opcodes.FLOAD;
import static org.objectweb.asm.Opcodes.FRETURN;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.LLOAD;
import static org.objectweb.asm.Opcodes.LRETURN;

//За основу взята утилита из Guava
//https://github.com/apache/groovy/blob/cac291068cf80ba23b05184d4b4a526b8189bdeb/src/main/java/org/codehaus/groovy/classgen/asm/util/TypeUtil.java
public class AsmUtils {
    public static int getReturnInsnByType(Type type) {
        Integer insn = PRIMITIVE_TYPE_TO_RETURN_INSN_MAP.get(type);

        if (null != insn) {
            return insn;
        }

        return ARETURN;
    }

    public static int getLoadInsnByType(Type type) {
        Integer insn = PRIMITIVE_TYPE_TO_LOAD_INSN_MAP.get(type);

        if (null != insn) {
            return insn;
        }

        return ALOAD;
    }

    public static int getReturnOpc(String descriptor) {
        Type returnType = Type.getReturnType(descriptor);

        if (returnType == Type.VOID_TYPE) {
            return Opcodes.RETURN;
        } else {
            return getReturnInsnByType(returnType);
        }
    }

    public static String getRecipe(String name, int length) {
        var recipe = "executed method: " + name;

        if (length == 0) {
            return recipe;
        }

        recipe += ", param: " + "\u0001,".repeat(length);
        recipe = recipe.substring(0, recipe.length() - 1);

        return recipe;
    }

    private static final Map<Type, Integer> PRIMITIVE_TYPE_TO_RETURN_INSN_MAP = new HashMap<>() {
        {
            put(Type.BOOLEAN_TYPE, IRETURN);
            put(Type.BYTE_TYPE, IRETURN);
            put(Type.CHAR_TYPE, IRETURN);
            put(Type.DOUBLE_TYPE, DRETURN);
            put(Type.FLOAT_TYPE, FRETURN);
            put(Type.INT_TYPE, IRETURN);
            put(Type.LONG_TYPE, LRETURN);
            put(Type.SHORT_TYPE, IRETURN);
        }
    };

    private static final Map<Type, Integer> PRIMITIVE_TYPE_TO_LOAD_INSN_MAP = new HashMap<>() {
        {
            put(Type.BOOLEAN_TYPE, ILOAD);
            put(Type.BYTE_TYPE, ILOAD);
            put(Type.CHAR_TYPE, ILOAD);
            put(Type.DOUBLE_TYPE, DLOAD);
            put(Type.FLOAT_TYPE, FLOAD);
            put(Type.INT_TYPE, ILOAD);
            put(Type.LONG_TYPE, LLOAD);
            put(Type.SHORT_TYPE, ILOAD);
        }
    };
}
