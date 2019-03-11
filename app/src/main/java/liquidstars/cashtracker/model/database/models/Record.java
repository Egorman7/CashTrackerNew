package liquidstars.cashtracker.model.database.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity (foreignKeys = {@ForeignKey(
                entity = Account.class,
                parentColumns = "_id",
                childColumns = "account_id"),
        @ForeignKey(entity = Record.class,
            parentColumns = "_id",
            childColumns = "category_id")})
public class Record {
    @PrimaryKey(autoGenerate = true) long _id;
    @ColumnInfo(index = true)
    long account_id; // Id of account Foreign Key
    double value;
    String date;
    @ColumnInfo(index = true)
    long category_id; // Id of category Foreign Key
    boolean type; // 0 - outcome, 1 - income
    String commentary;

    public Record(long _id, long account_id, double value, String date, long category_id, boolean type, String commentary) {
        this._id = _id;
        this.account_id = account_id;
        this.value = value;
        this.date = date;
        this.category_id = category_id;
        this.type = type;
        this.commentary = commentary;
    }

    @Ignore
    public Record(long account_id, double value, String date, long category_id, boolean type, String commentary) {
        this.account_id = account_id;
        this.value = value;
        this.date = date;
        this.category_id = category_id;
        this.type = type;
        this.commentary = commentary;
    }

    public long get_id() {
        return _id;
    }

    public long getAccount_id() {
        return account_id;
    }

    public double getValue() {
        return value;
    }

    public String getDate() {
        return date;
    }

    public long getCategory_id() {
        return category_id;
    }

    public boolean isType() {
        return type;
    }

    public String getCommentary() {
        return commentary;
    }
}
