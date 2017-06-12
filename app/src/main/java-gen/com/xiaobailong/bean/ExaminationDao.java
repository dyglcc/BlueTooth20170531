package com.xiaobailong.bean;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "EXAMINATION".
*/
public class ExaminationDao extends AbstractDao<Examination, Long> {

    public static final String TABLENAME = "EXAMINATION";

    /**
     * Properties of entity Examination.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Expired = new Property(1, boolean.class, "expired", false, "EXPIRED");
        public final static Property Break_ = new Property(2, String.class, "break_", false, "BREAK_");
        public final static Property False_ = new Property(3, String.class, "false_", false, "FALSE_");
        public final static Property Short_ = new Property(4, String.class, "short_", false, "SHORT_");
        public final static Property Minutes = new Property(5, Integer.class, "minutes", false, "MINUTES");
        public final static Property Devices = new Property(6, String.class, "devices", false, "DEVICES");
    }


    public ExaminationDao(DaoConfig config) {
        super(config);
    }
    
    public ExaminationDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"EXAMINATION\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"EXPIRED\" INTEGER NOT NULL ," + // 1: expired
                "\"BREAK_\" TEXT," + // 2: break_
                "\"FALSE_\" TEXT," + // 3: false_
                "\"SHORT_\" TEXT," + // 4: short_
                "\"MINUTES\" INTEGER," + // 5: minutes
                "\"DEVICES\" TEXT);"); // 6: devices
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"EXAMINATION\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Examination entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getExpired() ? 1L: 0L);
 
        String break_ = entity.getBreak_();
        if (break_ != null) {
            stmt.bindString(3, break_);
        }
 
        String false_ = entity.getFalse_();
        if (false_ != null) {
            stmt.bindString(4, false_);
        }
 
        String short_ = entity.getShort_();
        if (short_ != null) {
            stmt.bindString(5, short_);
        }
 
        Integer minutes = entity.getMinutes();
        if (minutes != null) {
            stmt.bindLong(6, minutes);
        }
 
        String devices = entity.getDevices();
        if (devices != null) {
            stmt.bindString(7, devices);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Examination entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getExpired() ? 1L: 0L);
 
        String break_ = entity.getBreak_();
        if (break_ != null) {
            stmt.bindString(3, break_);
        }
 
        String false_ = entity.getFalse_();
        if (false_ != null) {
            stmt.bindString(4, false_);
        }
 
        String short_ = entity.getShort_();
        if (short_ != null) {
            stmt.bindString(5, short_);
        }
 
        Integer minutes = entity.getMinutes();
        if (minutes != null) {
            stmt.bindLong(6, minutes);
        }
 
        String devices = entity.getDevices();
        if (devices != null) {
            stmt.bindString(7, devices);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Examination readEntity(Cursor cursor, int offset) {
        Examination entity = new Examination( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getShort(offset + 1) != 0, // expired
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // break_
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // false_
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // short_
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // minutes
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // devices
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Examination entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setExpired(cursor.getShort(offset + 1) != 0);
        entity.setBreak_(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setFalse_(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setShort_(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setMinutes(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setDevices(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Examination entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Examination entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Examination entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
