package com.example.nots.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.nots.R;
import com.example.nots.activity.editor.EditorActivity;
import com.example.nots.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView {

    // ОБЪЯВЛЕНИЕ ПЕРЕМЕННЫХ
    private static final int INTENT_EDIT = 200;
    private static final int INTENT_ADD = 100;
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;

    MainPresenter mainPresenter;
    MainAdapter adapter;
    MainAdapter.ItemClickListener itemClickListener;

    List<Note> note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация UI-элементов
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefresh = findViewById(R.id.swipeRefresh);

        floatingActionButton = findViewById(R.id.add);
        floatingActionButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        new Intent(MainActivity.this, EditorActivity.class), INTENT_ADD);
            }
        });
        // Создаем Presenter
        mainPresenter = new MainPresenter(this);
        mainPresenter.getData();

        // Обновляем данные
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mainPresenter.getData();
            }
        });

        // Обработчик нажатия на элемент RecyclerView, который передает данные в EditorActivity
        itemClickListener = new MainAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int id = note.get(position).getId();
                String title = note.get(position).getTitle();
                String notes = note.get(position).getNote();
                int color = note.get(position).getColor();

                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("title", title);
                intent.putExtra("notes", notes);
                intent.putExtra("color", color);
                startActivityForResult(intent, INTENT_EDIT);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == INTENT_ADD && resultCode == RESULT_OK) {
            mainPresenter.getData(); // reload data
        } else if (requestCode == INTENT_EDIT && resultCode == RESULT_OK) {
            mainPresenter.getData(); // reload data
        }
    }

    /**
     * ПОКАЗЫВАЕМ, ЧТО ИДЕТ ЗАГРУЗКА ДАННЫХ
     */
    @Override
    public void showLoading() {
        swipeRefresh.setRefreshing(true);
    }

    /**
     * СКРЫВАЕМ, ЧТО ИДЕТ ЗАГРУЗКА ДАННЫХ
     */
    @Override
    public void hideLoading() {
        swipeRefresh.setRefreshing(false);
    }

    /**
     * ПОЛУЧАЕМ ВСЕ ЗАМЕТКИ И ВЫВОДИМ ИХ В RecyclerView
     *
     * @param notes
     */
    @Override
    public void onGetResult(List<Note> notes) {
        adapter = new MainAdapter(this, notes, itemClickListener);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        note = notes;
    }

    /**
     * ВЫВОДИМ СООБЩЕНИЕ ОБ ОШИБКЕ
     */
    @Override
    public void onErrorLoading(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}