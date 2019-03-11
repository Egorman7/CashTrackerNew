package liquidstars.cashtracker.model.database.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Account {
    @PrimaryKey(autoGenerate = true) long _id;
    String title;
    String currency;
    boolean type; // 0 - cash, 1 - card
    double value;

    public Account(long _id, String title, String currency, boolean type, double value) {
        this._id = _id;
        this.title = title;
        this.currency = currency;
        this.type = type;
        this.value = value;
    }

    @Ignore
    public Account(String title, String currency, boolean type, double value) {
        this.title = title;
        this.currency = currency;
        this.type = type;
        this.value = value;
    }

    public long get_id() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public String getCurrency() {
        return currency;
    }

    public boolean isType() {
        return type;
    }

    public double getValue() {
        return value;
    }
}
