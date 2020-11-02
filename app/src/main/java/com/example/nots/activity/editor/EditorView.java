package com.example.nots.activity.editor;


/**
ЭТОТ ИНТЕРФЕЙС НУЖЕН ДЛЯ ТОГО, ЧТОБЫ В КЛАССЕ EditorPresenter БЫЛО МЕНЬШЕ КОДА, СВЯЗАННОГО С ПЛАТФОРМОЙ
*/
public interface EditorView {
    void showProgress();
    void hideProgress();
    void onAddSuccess(String message);
    void onAddError(String message);
}
