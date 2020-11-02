package com.example.nots.activity.editor;

import androidx.annotation.NonNull;

import com.example.nots.api.ApiClient;
import com.example.nots.api.ApiInterface;
import com.example.nots.model.Note;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
В этом классе происходит вся основная реализация на события, а также управление View
*/
public class EditorPresenter {

    // Объявление переменных
    private EditorView editorView;

    // Конструктор
    public EditorPresenter(EditorView editorView) {
        this.editorView = editorView;
    }

    /**
     * СОХРАНЯЕМ ЗАПИСИ
     * @param title
     * @param note
     * @param color
     */
    void saveNote(final String title, final String note, final int color) {
        // Показываем Progress Dialog
        editorView.showProgress();

        // Создаем экземпляр класса ApiInterface
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        // Создание call
        Call<Note> call = apiInterface.saveNote(title, note, color);

        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(@NonNull Call<Note> call, @NonNull Response<Note> response) {
                editorView.hideProgress();

                if (response.isSuccessful() && response.body() != null) {
                    Boolean success = response.body().getSuccess();
                    if (success) {
                        editorView.onAddSuccess(response.body().getMessage());
                    } else {
                        editorView.onAddError(response.body().getMessage());

                    }
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                editorView.hideProgress();
                editorView.onAddError(t.getLocalizedMessage());
            }
        });
    }

    /**
     * ОБНОВЛЯЕМ ЗАПИСИ
     * @param id
     * @param title
     * @param note
     * @param color
     */
    void updateNote(int id, String title, String note, int color){
        editorView.showProgress();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Note> call = apiInterface.updateNote(id, title, note, color);

        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                editorView.hideProgress();
                if(response.isSuccessful() && response.body() != null){
                    Boolean success  = response.body().getSuccess();
                    if(success){
                        editorView.onAddSuccess(response.body().getMessage());
                    }else {
                        editorView.onAddError(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                editorView.hideProgress();
                editorView.onAddError(t.getLocalizedMessage());
            }
        });
     }

    /**
     * УДАЛЯЕМ ЗАПИСИ
     * @param id
     */
     void deleteNote(int id){
         editorView.showProgress();

         ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
         Call<Note> call = apiInterface.deleteNote(id);
         call.enqueue(new Callback<Note>() {
             @Override
             public void onResponse(Call<Note> call, Response<Note> response) {
                 editorView.hideProgress();
                 if(response.isSuccessful() && response.body() != null){
                     Boolean success  = response.body().getSuccess();
                     if(success){
                         editorView.onAddSuccess(response.body().getMessage());
                     }else {
                         editorView.onAddError(response.body().getMessage());
                     }
                 }
             }

             @Override
             public void onFailure(Call<Note> call, Throwable t) {
                 editorView.hideProgress();
                 editorView.onAddError(t.getLocalizedMessage());
             }
         });
     }
}
