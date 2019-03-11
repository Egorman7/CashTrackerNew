package liquidstars.cashtracker.model.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;

import java.util.concurrent.Executors;

import liquidstars.cashtracker.model.database.dao.AccountDAO;
import liquidstars.cashtracker.model.database.dao.CategoryDAO;
import liquidstars.cashtracker.model.database.dao.RecordDAO;
import liquidstars.cashtracker.model.database.models.Account;
import liquidstars.cashtracker.model.database.models.Category;
import liquidstars.cashtracker.model.database.models.Record;

@android.arch.persistence.room.Database(entities = {Account.class, Category.class, Record.class}, version = Database.BD_VERSION, exportSchema = false)
public abstract class Database extends RoomDatabase {
    // pre-populated CATEGORIES
    private static Category[] CATEGORIES = {
            new Category("Other", true, Color.GRAY),
            new Category("Other", false, Color.GRAY)
    };
    private static Account ACCOUNT = new Account("Main Account", "UAH", false, 0);

    private static Database INSTANCE;

    final static int BD_VERSION = 1;
    static final String DB_NAME = "cashtracker_liquidstars";

    public abstract AccountDAO getAccountDao();
    public abstract CategoryDAO getCategoryDao();
    public abstract RecordDAO getRecordDao();

    public synchronized static Database getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = buildDatabase(context);
        }
        return INSTANCE;
    }

    private static Database buildDatabase(final Context context){
        return Room.databaseBuilder(context,
                Database.class,
                DB_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                getInstance(context).getCategoryDao().insertAll(CATEGORIES);
                                //getInstance(context).getAccountDao().insert(ACCOUNT);
                                //getInstance(context).getRecordDao().insert(new Record(1, 20, "2018-09-12", 1, true, "kek"));
                            }
                        });
                    }
                }).build();
    }
}
