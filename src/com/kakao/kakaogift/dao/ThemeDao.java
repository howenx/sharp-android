package com.kakao.kakaogift.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.kakao.kakaogift.entity.Theme;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "THEME".
*/
public class ThemeDao extends AbstractDao<Theme, Long> {

    public static final String TABLENAME = "THEME";

    /**
     * Properties of entity Theme.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Item_id = new Property(1, Integer.class, "item_id", false, "ITEM_ID");
        public final static Property ThemeImg = new Property(2, String.class, "themeImg", false, "THEME_IMG");
        public final static Property ThemeUrl = new Property(3, String.class, "themeUrl", false, "THEME_URL");
        public final static Property Width = new Property(4, Integer.class, "width", false, "WIDTH");
        public final static Property Height = new Property(5, Integer.class, "height", false, "HEIGHT");
        public final static Property SortNum = new Property(6, Integer.class, "sortNum", false, "SORT_NUM");
        public final static Property Tag = new Property(7, String.class, "tag", false, "TAG");
        public final static Property Type = new Property(8, String.class, "type", false, "TYPE");
        public final static Property Title = new Property(9, String.class, "title", false, "TITLE");
        public final static Property ThemeConfigInfo = new Property(10, String.class, "themeConfigInfo", false, "THEME_CONFIG_INFO");
    };


    public ThemeDao(DaoConfig config) {
        super(config);
    }
    
    public ThemeDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"THEME\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"ITEM_ID\" INTEGER," + // 1: item_id
                "\"THEME_IMG\" TEXT," + // 2: themeImg
                "\"THEME_URL\" TEXT," + // 3: themeUrl
                "\"WIDTH\" INTEGER," + // 4: width
                "\"HEIGHT\" INTEGER," + // 5: height
                "\"SORT_NUM\" INTEGER," + // 6: sortNum
                "\"TAG\" TEXT," + // 7: tag
                "\"TYPE\" TEXT," + // 8: type
                "\"TITLE\" TEXT," + // 9: title
                "\"THEME_CONFIG_INFO\" TEXT);"); // 10: themeConfigInfo
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"THEME\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Theme entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer item_id = entity.getItem_id();
        if (item_id != null) {
            stmt.bindLong(2, item_id);
        }
 
        String themeImg = entity.getThemeImg();
        if (themeImg != null) {
            stmt.bindString(3, themeImg);
        }
 
        String themeUrl = entity.getThemeUrl();
        if (themeUrl != null) {
            stmt.bindString(4, themeUrl);
        }
 
        Integer width = entity.getWidth();
        if (width != null) {
            stmt.bindLong(5, width);
        }
 
        Integer height = entity.getHeight();
        if (height != null) {
            stmt.bindLong(6, height);
        }
 
        Integer sortNum = entity.getSortNum();
        if (sortNum != null) {
            stmt.bindLong(7, sortNum);
        }
 
        String tag = entity.getTag();
        if (tag != null) {
            stmt.bindString(8, tag);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(9, type);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(10, title);
        }
 
        String themeConfigInfo = entity.getThemeConfigInfo();
        if (themeConfigInfo != null) {
            stmt.bindString(11, themeConfigInfo);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Theme readEntity(Cursor cursor, int offset) {
        Theme entity = new Theme( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // item_id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // themeImg
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // themeUrl
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // width
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // height
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // sortNum
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // tag
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // type
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // title
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10) // themeConfigInfo
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Theme entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setItem_id(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setThemeImg(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setThemeUrl(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setWidth(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setHeight(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setSortNum(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setTag(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setType(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setTitle(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setThemeConfigInfo(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Theme entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Theme entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
