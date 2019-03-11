package liquidstars.cashtracker.presenter.callbacks;

import java.util.List;

import io.reactivex.annotations.Nullable;
import liquidstars.cashtracker.model.database.models.Category;

public abstract class DatabaseCallback {
    public void onComplete(){}
    public void onComplete(Category category){}
    public void onError(){}
    public void onComplete(long id){}
    public void onCompleteList(List<?> list){}
    public void onCompleteObject(Object obj){}
}
