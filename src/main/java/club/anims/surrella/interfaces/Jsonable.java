package club.anims.surrella.interfaces;

import com.google.gson.Gson;

public interface Jsonable<T> {
    default String toJson() {
        return new Gson().toJson(this);
    }

    default T fromJson(String json) {
        return new Gson().fromJson(json, (Class<T>) this.getClass());
    }
}
