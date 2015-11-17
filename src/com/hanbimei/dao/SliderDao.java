package com.hanbimei.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.hanbimei.entity.Slider;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SLIDER".
*/
public class SliderDao extends AbstractDao<Slider, Long> {

    public static final String TABLENAME = "SLIDER";

    /**
     * Properties of entity Slider.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Slider_id = new Property(1, Integer.class, "slider_id", false, "SLIDER_ID");
        public final static Property ImgUrl = new Property(2, String.class, "imgUrl", false, "IMG_URL");
        public final static Property Url = new Property(3, String.class, "url", false, "URL");
    };


    public SliderDao(DaoConfig config) {
        super(config);
    }
    
    public SliderDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SLIDER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"SLIDER_ID\" INTEGER," + // 1: slider_id
                "\"IMG_URL\" TEXT," + // 2: imgUrl
                "\"URL\" TEXT);"); // 3: url
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SLIDER\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Slider entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer slider_id = entity.getSlider_id();
        if (slider_id != null) {
            stmt.bindLong(2, slider_id);
        }
 
        String imgUrl = entity.getImgUrl();
        if (imgUrl != null) {
            stmt.bindString(3, imgUrl);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(4, url);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Slider readEntity(Cursor cursor, int offset) {
        Slider entity = new Slider( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // slider_id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // imgUrl
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // url
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Slider entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSlider_id(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setImgUrl(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUrl(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Slider entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Slider entity) {
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
