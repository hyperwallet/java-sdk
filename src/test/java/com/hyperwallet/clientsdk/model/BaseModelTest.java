package com.hyperwallet.clientsdk.model;


import com.fasterxml.jackson.annotation.JsonFilter;
import com.hyperwallet.clientsdk.util.HyperwalletJsonConfiguration;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;

/**
 * @author fkrauthan
 */
public abstract class BaseModelTest<T> {

    private T baseModel;
    private List<String> fieldNames;

    @BeforeMethod
    public void setUp() {
        this.baseModel = createBaseModel();
        this.fieldNames = collectFieldNames();
    }

    protected abstract T createBaseModel();

    protected abstract Class<T> createModelClass();

    @Test
    public void testJsonFilterAnnotationPresent() throws Exception {
        Class<T> clazz = createModelClass();
        T model = clazz.newInstance();

        if (model instanceof HyperwalletBaseMonitor) {
            JsonFilter filter = clazz.getAnnotation(JsonFilter.class);
            assertThat(filter, is(notNullValue()));
            assertThat(filter.value(), is(equalTo(HyperwalletJsonConfiguration.INCLUSION_FILTER)));
        } else {
            JsonFilter filter = clazz.getAnnotation(JsonFilter.class);
            assertThat(filter, is(nullValue()));
        }
    }

    @Test(dataProvider = "fieldNames")
    public void testSetterMethod(String fieldName) throws Exception {
        Class<T> clazz = createModelClass();
        T model = clazz.newInstance();

        Method getter = findGetter(clazz, fieldName);
        Method setter = findSetter(clazz, fieldName, getter.getReturnType());

        Object oldValue = getter.invoke(baseModel);
        assertThat(oldValue, is(notNullValue()));
        assertThat(getter.invoke(model), is(nullValue()));

        setter.invoke(model, oldValue);
        assertThat(getter.invoke(model), is(equalTo(oldValue)));

        if (model instanceof HyperwalletBaseMonitor) {
            assertThat(new ArrayList<String>(((HyperwalletBaseMonitor) model).getInclusions()), containsInAnyOrder(fieldName));
        }
    }

    @Test(dataProvider = "fieldNames")
    public void testBuilderMethod(String fieldName) throws Exception {
        Class<T> clazz = createModelClass();
        T model = clazz.newInstance();

        Method getter = findGetter(clazz, fieldName);
        Method setter = clazz.getMethod(fieldName, getter.getReturnType());

        Object oldValue = getter.invoke(baseModel);
        assertThat(oldValue, is(notNullValue()));
        assertThat(getter.invoke(model), is(nullValue()));

        Object ret = setter.invoke(model, oldValue);
        assertThat(ret, is(equalTo((Object) model)));
        assertThat(getter.invoke(model), is(equalTo(oldValue)));

        if (model instanceof HyperwalletBaseMonitor) {
            assertThat(new ArrayList<String>(((HyperwalletBaseMonitor) model).getInclusions()), containsInAnyOrder(fieldName));
        }
    }

    @Test(dataProvider = "fieldNames")
    public void testClearMethod(String fieldName) throws Exception {
        Class<T> clazz = createModelClass();
        T model = clazz.newInstance();

        if (!(model instanceof HyperwalletBaseMonitor)) {
            try {
                findClear(clazz, fieldName);
                fail("Clear method for " + fieldName + "should not exist!");
            } catch (NoSuchMethodException ignored) {
            }
            return;
        }

        assertThat(new ArrayList<String>(((HyperwalletBaseMonitor) model).getInclusions()), is(Matchers.<String>empty()));

        Method getter = findGetter(clazz, fieldName);
        Method clear = findClear(clazz, fieldName);

        Field f = clazz.getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(model, getter.invoke(baseModel));
        assertThat(new ArrayList<String>(((HyperwalletBaseMonitor) model).getInclusions()), is(Matchers.<String>empty()));

        assertThat(getter.invoke(model), is(equalTo(getter.invoke(baseModel))));

        clear.invoke(model);
        assertThat(getter.invoke(model), is(nullValue()));

        assertThat(new ArrayList<String>(((HyperwalletBaseMonitor) model).getInclusions()), containsInAnyOrder(fieldName));
    }

    @Test(dependsOnMethods = "testSetterMethod")
    public void testAllSetterMethods() throws Exception {
        Class<T> clazz = createModelClass();
        T model = clazz.newInstance();

        // Set values
        for (String field : fieldNames) {
            Method getter = findGetter(clazz, field);
            Method setter = findSetter(clazz, field, getter.getReturnType());

            Object oldValue = getter.invoke(baseModel);
            assertThat("Testing old instance value for " + field, oldValue, is(notNullValue()));
            assertThat("Testing new instance value for " + field, getter.invoke(model), is(nullValue()));

            setter.invoke(model, oldValue);
            assertThat("Testing new instance value for " + field, getter.invoke(model), is(equalTo(oldValue)));
        }

        // Compare changed list
        if (model instanceof HyperwalletBaseMonitor) {
            assertThat(new ArrayList<String>(((HyperwalletBaseMonitor) model).getInclusions()), containsInAnyOrder(fieldNames.toArray()));
        }
    }

    @Test(dependsOnMethods = "testBuilderMethod")
    public void testAllBuilderMethods() throws Exception {
        Class<T> clazz = createModelClass();
        T model = clazz.newInstance();

        // Set values
        for (String field : fieldNames) {
            Method getter = findGetter(clazz, field);
            Method setter = clazz.getMethod(field, getter.getReturnType());

            Object oldValue = getter.invoke(baseModel);
            assertThat("Testing old instance value for " + field, oldValue, is(notNullValue()));
            assertThat("Testing new instance value for " + field, getter.invoke(model), is(nullValue()));

            Object ret = setter.invoke(model, oldValue);
            assertThat("Return value is instance of model for " + field, ret, is(equalTo((Object) model)));
            assertThat("Testing new instance value for " + field, getter.invoke(model), is(equalTo(oldValue)));
        }

        // Compare changed list
        if (model instanceof HyperwalletBaseMonitor) {
            assertThat(new ArrayList<String>(((HyperwalletBaseMonitor) model).getInclusions()), containsInAnyOrder(fieldNames.toArray()));
        }
    }

    @Test(dependsOnMethods = "testClearMethod")
    public void testAllClearMethods() throws Exception {
        Class<T> clazz = createModelClass();
        T model = clazz.newInstance();

        if (!(model instanceof HyperwalletBaseMonitor)) {
            return;
        }

        // Set values
        for (String field : fieldNames) {
            Method getter = findGetter(clazz, field);
            Method clear = findClear(clazz, field);

            Field f = clazz.getDeclaredField(field);
            f.setAccessible(true);
            f.set(model, getter.invoke(baseModel));

            assertThat("Testing new instance value for " + field, getter.invoke(model), is(equalTo(getter.invoke(baseModel))));

            clear.invoke(model);
            assertThat("Testing new instance value for " + field, getter.invoke(model), is(nullValue()));
        }

        // Compare changed list
        assertThat(new ArrayList<String>(((HyperwalletBaseMonitor) model).getInclusions()), containsInAnyOrder(fieldNames.toArray()));
    }

    private Method findSetter(Class<T> clazz, String fieldName, Class<?> paramClazz) throws Exception {
        String getterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return clazz.getMethod(getterName, paramClazz);
    }

    private Method findGetter(Class<T> clazz, String fieldName) throws Exception {
        String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return clazz.getMethod(getterName);
    }

    private Method findClear(Class<T> clazz, String fieldName) throws Exception {
        String getterName = "clear" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return clazz.getMethod(getterName);
    }

    private List<String> collectFieldNames() {
        Class<T> clazz = createModelClass();

        List<String> fieldNames = new ArrayList<String>();
        for (Field field : clazz.getDeclaredFields()) {
            if (!Modifier.isPrivate(field.getModifiers())) {
                continue;
            }
            fieldNames.add(field.getName());
        }

        return fieldNames;
    }

    @DataProvider(name = "fieldNames")
    public Iterator<Object[]> createFieldNamesProvider() {
        List<String> fieldNames = collectFieldNames();
        List<Object[]> objects = new ArrayList<Object[]>(fieldNames.size());
        for (String fieldName : fieldNames) {
            objects.add(new Object[]{fieldName});
        }
        return objects.iterator();
    }
}
