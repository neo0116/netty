package com.bytedance.netty.io.rpc.protocol;

import java.io.Serializable;

public class MyProtocol implements Serializable {

    private static final long serialVersionUID = -7035189451429881535L;
    private String className;

    private String methodName;

    private Class<?>[]  parameterTypes;

    private Object[] args;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
