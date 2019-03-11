package liquidstars.cashtracker.model.database.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Category {
    @PrimaryKey (autoGenerate = true) long _id;
    String title;
    boolean type; // 0 - outcome, 1 - income
    int color;

    public Category(long _id, String title, boolean type, int color) {
        this._id = _id;
        this.title = title;
        this.type = type;
        this.color = color;
    }

    @Ignore
    public Category(String title, boolean type, int color) {
        this.title = title;
        this.type = type;
        this.color = color;
    }

    public long get_id() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isType() {
        return type;
    }

    public int getColor() {
        return color;
    }
}
