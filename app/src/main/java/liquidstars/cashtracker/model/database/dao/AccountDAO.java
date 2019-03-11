package liquidstars.cashtracker.model.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import liquidstars.cashtracker.model.database.models.Account;

@Dao
public interface AccountDAO {
    @Insert
    long insert(Account account);

    @Update
    int update(Account account);

    @Delete
    void delete(Account account);

    @Query("select * from account")
    Single<List<Account>> getAccounts();

    @Query("select * from account")
    Flowable<List<Account>> getAccountsF();

    @Query("select * from account where _id = :id limit 1")
    Flowable<Account> getAccount(long id);

    @Query("select * from account order by _id asc limit 1")
    Single<Account> getFirstAccount();

    @Query("select value from account where _id = :id")
    Flowable<Double> getAccountValue(long id);

    @Query("select currency from account where _id = :id")
    Single<String> getCurrency(long id);

    @Query("select title from account where _id = :id")
    Single<String> getTitle(long id);
}
