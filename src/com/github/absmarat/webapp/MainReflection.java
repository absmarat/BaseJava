package com.github.absmarat.webapp;

import com.github.absmarat.webapp.model.Resume;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainReflection {
    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException,
            InvocationTargetException {
        Resume resume = new Resume();
        Field field = resume.getClass().getDeclaredFields()[0];
        field.setAccessible(true);
        System.out.println(field.getName());
        System.out.println(field.get(resume));
        field.set(resume, "new_uuid");

        // TODO : invoke r.toString via reflection
        Method toStringMethod = resume.getClass().getMethod("toString");
        String result = (String) toStringMethod.invoke(resume);
        System.out.println("Вызов через рефлексию: " + result);
    }
}
