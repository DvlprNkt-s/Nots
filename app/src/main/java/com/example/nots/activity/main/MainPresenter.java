package com.example.nots.activity.main;

import com.example.nots.api.ApiClient;
import com.example.nots.api.ApiInterface;
import com.example.nots.model.Note;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 В этом классе происходит вся основная реализация на события, а также управление View
 */

public class MainPresenter {
    // Объявление интерфейса
    private MainView mainView;

    // Конструктор
    public MainPresenter(MainView mainView) {
        this.mainView = mainView;
    }

    /**
     *  ПОЛУЧАЕМ ИНФОРМАЦИЮ С СЕРВЕРА
     */
    void getData() {
        mainView.showLoading();

        // Запрос на сервер
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<List<Note>> call = apiInterface.getNotes();
        call.enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                mainView.hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    // Передаем ответ с сервера в метод onGetResult
                    mainView.onGetResult(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                mainView.hideLoading();
                mainView.onErrorLoading(t.getLocalizedMessage());
            }
        });
    }
}
