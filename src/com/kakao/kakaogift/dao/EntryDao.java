package com.kakao.kakaogift.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.kakao.kakaogift.entity.Entry;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ENTRY".
*/
public class EntryDao extends AbstractDao<Entry, Long> {

    public static final String TABLENAME = "ENTRY";

    /**
     * Properties of entity Entry.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property ItemTarget = new Property(1, String.class, "itemTarget", false, "ITEM_TARGET");
        public final static Property TargetType = new Property(2, String.class, "targetType", false, "TARGET_TYPE");
        public final static Property NavText = new Property(3, String.class, "navText", false, "NAV_TEXT");
        public final static Property ImgUrl = new Property(4, String.class, "imgUrl", false, "IMG_URL");
    };


    public EntryDao(DaoConfig config) {
        super(config);
    }
    
    public EntryDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ENTRY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"ITEM_TARGET\" TEXT," + // 1: itemTarget
                "\"TARGET_TYPE\" TEXT," + // 2: targetType
                "\"NAV_TEXT\" TEXT," + // 3: navText
                "\"IMG_URL\" TEXT);"); // 4: imgUrl
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ENTRY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Entry entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String itemTarget = entity.getItemTarget();
        if (itemTarget != null) {
            stmt.bindString(2, itemTarget);
        }
 
        String targetType = entity.getTargetType();
        if (targetType != null) {
            stmt.bindString(3, targetType);
        }
 
        String navText = entity.getNavText();
        if (navText != null) {
            stmt.bindString(4, navText);
        }
 
        String imgUrl = entity.getImgUrl();
        if (imgUrl != null) {
            stmt.bindString(5, imgUrl);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Entry readEntity(Cursor cursor, int offset) {
        Entry entity = new Entry( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // itemTarget
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // targetType
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // navText
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // imgUrl
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Entry entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setItemTarget(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setTargetType(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setNavText(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setImgUrl(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Entry entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Entry entity) {
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
