package liquidstars.cashtracker.model.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import liquidstars.cashtracker.model.database.models.Record;

@Dao
public interface RecordDAO {
    @Insert
    Long insert(Record record);

    @Delete
    void delete(Record record);

    @Query("select * from record where account_id = :account_id order by _id desc limit :limit offset :from")
    Single<List<Record>> getRecordsByAccount(long account_id, long from, long limit);

    @Query("select * from record where account_id = :account_id and date >= :date_start and date <= :date_end")
    Flowable<List<Record>> getRecordsInRange(long account_id, String date_start, String date_end);
}
