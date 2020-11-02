package com.example.nots.api;

import com.example.nots.model.Note;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    /**
     * СОХРАНЯЕМ ЗАМЕТКИ В БД.
     * @param title
     * @param note
     * @param color
     * @return
     */
    @FormUrlEncoded // Denotes that the request body will use form URL encoding
    @POST("save.php")
    Call<Note> saveNote(
            @Field("title") String title,
            @Field("note") String note,
            @Field("color") int color
    );

    /**
     * ПОЛУЧАЕМ ВСЕ ЗАМЕТКИ ИЗ БД.
     * @return
     */
    @GET("notes.php")
    Call<List<Note>> getNotes();

    /**
     * ОБНОВЛЯЕМ СОДЕРЖИМОЕ ЗАМЕТОК В БД.
     * @param id
     * @param title
     * @param note
     * @param color
     * @return
     */
    @FormUrlEncoded // Denotes that the request body will use form URL encoding
    @POST("update.php")
    Call<Note> updateNote(
            @Field("id") int id,
            @Field("title") String title,
            @Field("note") String note,
            @Field("color") int color
    );

    /**
     * УДАЛЯЕМ ЗАМЕТКИ ИЗ БД.
     * @param id
     * @return
     */
    @FormUrlEncoded // Denotes that the request body will use form URL encoding
    @POST("delete.php")
    Call<Note> deleteNote( @Field("id") int id);
}
