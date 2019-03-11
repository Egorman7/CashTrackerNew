package liquidstars.cashtracker.model.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import liquidstars.cashtracker.model.database.models.Category;

@Dao
public interface CategoryDAO {
    @Insert
    long insert(Category category);

    @Insert
    long[] insertAll(Category... categories);

    @Update
    int update(Category category);

    @Delete
    void delete(Category category);

    @Query("select * from category")
    Flowable<List<Category>> getAllCategories();

    @Query("select * from category where type = :type")
    Flowable<List<Category>> getCategoriesByType(boolean type);

    @Query("select title from category where type = :type and _id = :id")
    Flowable<String> getCategoryTitle(boolean type, long id);

    @Query("select * from category where type = :type limit 1")
    Single<Category> getFirstCategory(boolean type);
}
