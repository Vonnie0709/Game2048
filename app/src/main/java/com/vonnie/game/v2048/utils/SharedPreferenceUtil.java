package com.vonnie.game.v2048.utils;


import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * this is a tool class
 * by using put and get method, save and query data
 *
 * @author LongpingZou
 */
public class SharedPreferenceUtil {


    /**
     * name of file name
     */
    private static final String FILE_NAME = "game_share";

    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    /**
     * according to data type,saving data
     *
     * @param context
     * @param key
     * @param object
     */
    public static void put(Context context, String key, Object object) {
        if (sp == null || editor == null) {
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            editor = sp.edit();
        }

        if (object == null) {
            return;
        }
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    /**
     * To get saved data, according to data type
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject) {
        if (sp == null) {
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        try {
            if (defaultObject instanceof String) {
                return sp.getString(key, (String) defaultObject);
            } else if (defaultObject instanceof Integer) {
                return sp.getInt(key, (Integer) defaultObject);
            } else if (defaultObject instanceof Boolean) {
                return sp.getBoolean(key, (Boolean) defaultObject);
            } else if (defaultObject instanceof Float) {
                return sp.getFloat(key, (Float) defaultObject);
            } else if (defaultObject instanceof Long) {
                return sp.getLong(key, (Long) defaultObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return defaultObject;
        }


        return defaultObject;
    }

    /**
     * remove the value of some key
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        if (sp == null || editor == null) {
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            editor = sp.edit();
        }
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * clear all data
     *
     * @param context
     */
    public static void clear(Context context) {
        if (sp == null || editor == null) {
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            editor = sp.edit();
        }
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * query if some key is existing
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {
        if (sp == null) {
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        return sp.contains(key);
    }

    /**
     * return all of key/map
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        return sp.getAll();
    }


    /**
     * Create a compatible class to solve the SharedPreferencesCompat.apply method
     */
    private static class SharedPreferencesCompat {
        private static final Method S_APPLY_METHOD = findApplyMethod();

        /**
         * find apply method by reflection
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * If found, use apply to execute, otherwise use commit.
         *
         * @param editor
         */
        static void apply(SharedPreferences.Editor editor) {
            try {
                if (S_APPLY_METHOD != null) {
                    S_APPLY_METHOD.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ignored) {
            }
            editor.commit();
        }


    }


}

