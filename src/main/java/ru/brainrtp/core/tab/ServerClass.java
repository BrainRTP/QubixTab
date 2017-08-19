package ru.brainrtp.core.tab;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ServerClass {

    private Object handle;
    private List<ServerField> fields = new ArrayList<ServerField>();

    public void setHandle(Object handle){
        if(handle == null){
            System.out.print("Cannot set handle to null!");
            return;
        }
        this.handle = handle;
    }

    public Object getHandle(){
        return this.handle;
    }

    public List<ServerField> getFields(){
        return this.fields;
    }

    public ServerField getField(String fieldName){
        return new ServerField(getHandle().getClass(), fieldName);
    }

    public ServerClass(Class<?> handleClass){
        try {
            setHandle(handleClass.newInstance());

            for(Field field : handleClass.getDeclaredFields()){
                if(!field.isAccessible()){
                    field.setAccessible(true);
                }
                this.fields.add(new ServerField(field));
            }
        } catch (InstantiationException e) {
            System.out.print("Could not instantiate class: " + handleClass.getSimpleName());
        } catch (IllegalAccessException e) {
            System.out.print("Could not create a new instance of class: " + handleClass.getSimpleName());
        }
    }

    public ServerClass(String className, BukkitTool.ClassType type){
        switch(type){
            case NMS : new ServerClass(BukkitTool.getNMSClass(className));
                break;
            case CB : new ServerClass(BukkitTool.getCBClass(className));
                break;
            default : new ServerClass(BukkitTool.getClass(className));
        }
    }

    public void write(String fieldName, Object value){
        new ServerField(getHandle().getClass(), fieldName).set(getHandle(), value);
    }

    public void write(int index, Object value){
        getFields().get(index).set(getHandle(), value);
    }

    public Object read(String fieldName){
        return new ServerField(getHandle().getClass(), fieldName).get(getHandle());
    }

    public static ServerClass create(String className, BukkitTool.ClassType type){
        return new ServerClass(className, type);
    }

    public Object read(int index){
        return getFields().get(index).get(getHandle());
    }

    public ServerMethod getMethod(String methodName, Class... paramTypes){
        return new ServerMethod(getHandle().getClass(), methodName, paramTypes);
    }

    /**
     * Server field stuff
     */

    public static class ServerField{

        private static Field field;

        public ServerField(Field field){
            if(!field.isAccessible()){
                field.setAccessible(true);
            }
            this.field = field;
        }

        public ServerField(Class<?> clazz, String fieldName){
            try {
                new ServerField(clazz.getDeclaredField(fieldName));
            } catch (NoSuchFieldException e) {
                System.out.print("No such field!");
            }
        }

        public Field getField(){
            return this.field;
        }

        public Object get(Object handle){
            try {
                return getField().get(handle);
            } catch (IllegalAccessException e) {
                System.out.print("Failed to access field!");
            }
            return null;
        }

        public void set(Object handle, Object value){
            try {
                getField().set(handle, value);
            } catch (IllegalAccessException e) {
                System.out.print("Failed to access field!");
            }
        }

        public static Object get(){
            try {
                return field.get(null);
            } catch (IllegalAccessException e) {
                System.out.print("Failed to access field!");
            }
            return null;
        }

    }

    /**
     * Server method stucc
     */

    public static class ServerMethod{

        private static Method method;

        public ServerMethod(Method method){
            this.method = method;
        }

        public ServerMethod(Class<?> clazz, String methodName, Class... params){
            try {
                new ServerMethod(clazz.getDeclaredMethod(methodName, params));
            } catch (NoSuchMethodException e) {
                System.out.print("No such method!");
            }
        }

        public Method getMethod(){
            return this.method;
        }

        public Object invoke(Object handle, Class... params){
            try {
                return method.invoke(handle, params);
            } catch (IllegalAccessException e) {
                System.out.print("Could not access method: " + method.toString());
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static Object invoke(Class... params){
            try {
                return method.invoke(null, params);
            } catch (IllegalAccessException e) {
                System.out.print("Could not access method: " + method.toString());
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
