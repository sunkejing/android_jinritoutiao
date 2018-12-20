package com.ss.android.gamecommon.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ss.android.gamecommon.db.util.TablePropertyUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * 操作数据库
 */
public class TableOperate {
    private DbManager dbManager;
    private SQLiteDatabase db;

    public TableOperate(Context context) {
        dbManager = DbManager.getInstance(context);
        db = dbManager.getDb();
    }

    /**
     * 向数据库中插入数据
     *
     * @param tableName 数据库插入数据的数据表
     * @param object    数据库插入的对象
     */
    public int insert(String tableName, Object object) {
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();//获取该类所有的属性
        ContentValues values = new ContentValues();
        for (Field field : fields) {
            field.setAccessible(true);//取消权限控制检查，使private方法可以被调用
            try {
                if (field.getName().equals(TablePropertyUtil.SESSION_COL_ID)) {
                    continue;
                }
                Class fieldClass = field.getType();
                if (field.getName().equals("serialVersionUID")) {
                    continue;
                }
                // 根据属性类型为values赋值
                if (fieldClass == String.class) {
                    values.put(field.getName(), String.valueOf(field.get(object)));
                } else if (fieldClass == int.class || fieldClass == Integer.class) {
                    values.put(field.getName(), (Integer) field.get(object));
                } else if (fieldClass == Long.class || fieldClass == long.class) {
                    values.put(field.getName(), (Long) field.get(object));
                } else if (fieldClass == Double.class) {
                    values.put(field.getName(), (Double) field.get(object));
                } else if (fieldClass == Float.class) {
                    values.put(field.getName(), (Float) field.get(object));
                } else if (fieldClass == Short.class) {
                    values.put(field.getName(), (Short) field.get(object));
                }
                field.setAccessible(false);//恢复对权限控制的检查

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        long index = db.insert(tableName, null, values);
        return (int) index;
    }

    /**
     * 查询数据库
     *
     * @param tableName  查询数据库的名字
     * @param entityType 查询数据库所对应的module
     * @param fieldName  查询的字段名
     * @param value      查询的字段值
     * @param <T>        泛型代表类
     * @return 返回查询结果
     */
    public <T> ArrayList<T> query(String tableName, Class<T> entityType, String fieldName, String value) {
        ArrayList<T> arrayList = new ArrayList<>();
        Cursor cursor = db.query(tableName, null, fieldName, new String[]{value}, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            try {
                T t = entityType.newInstance();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String content = cursor.getString(i);
                    String columnName = cursor.getColumnName(i);
                    Field field = entityType.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(t, content);
                    field.setAccessible(false);
                }
                arrayList.add(t);
                cursor.moveToNext();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

        }
        return arrayList;
    }

    /**
     * 删除数据
     *
     * @param tableName 删除数据库的表名
     * @param fieldName 删除的字段名
     * @param value     删除的字段值
     */
    public void delete(String tableName, String fieldName, String value) {
        db.delete(tableName, fieldName, new String[]{value});

    }

    /**
     * 更改数据库
     *
     * @param tableName   更改数据库的表名
     * @param columnName  更改数据库的字段名
     * @param columnValue 更改数据库的字段值
     * @param object      更改的数据
     */
    public void update(String tableName, String columnName, String columnValue, Object object) {
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        ContentValues contentValues = new ContentValues();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object content = field.get(object);
                contentValues.put(field.getName(), String.valueOf(content));
                field.setAccessible(false);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        db.update(tableName, contentValues, columnName, new String[]{columnValue});

    }

    public boolean tableIsExist(String tableName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }
        Cursor cursor = null;
        try {
            String sql = "select name from Sqlite_master" + " where type ='table' and name ='" + tableName.trim() + "' ";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }
}
