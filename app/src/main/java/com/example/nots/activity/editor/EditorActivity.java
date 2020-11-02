package com.example.nots.activity.editor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nots.R;
import com.thebluealliance.spectrum.SpectrumPalette;

import org.w3c.dom.Text;

public class EditorActivity extends AppCompatActivity implements EditorView {

    // Объявляем переменные
    EditText et_title, et_note;
    ProgressDialog progressDialog;
    SpectrumPalette spectrumPalette;

    private int color, id;
    private String title, note;

    Menu actionMenu;

    // Use MVP (Model-View-Presenter) pattern
    EditorPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Инициализация Edit Text
        et_title = findViewById(R.id.title);
        et_note = findViewById(R.id.note);

        // Инициализация Palette
        spectrumPalette = findViewById(R.id.palette);

        // Устанавливаем Listener
        spectrumPalette.setOnColorSelectedListener(new SpectrumPalette.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int clr) {
                color = clr;
            }
        });

        // Создаем экземпляр Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");

        // Создаем Presenter и передаем в конструктор данное View
        presenter = new EditorPresenter(this);

        // Получаем данные от Intent в Main Activity
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        title = intent.getStringExtra("title");
        note = intent.getStringExtra("notes");
        color = intent.getIntExtra("color", 0);

        setDataFromIntentExtra();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Устанавливаем своё Menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editor, menu);
        actionMenu = menu;

        // Кнопки, позволяющие изменить или удалить заметку
        if (id != 0){
            actionMenu.findItem(R.id.edit).setVisible(true);
            actionMenu.findItem(R.id.delete).setVisible(true);
            actionMenu.findItem(R.id.save).setVisible(false);
            actionMenu.findItem(R.id.update).setVisible(false);
        }else {
            actionMenu.findItem(R.id.edit).setVisible(false);
            actionMenu.findItem(R.id.delete).setVisible(false);
            actionMenu.findItem(R.id.save).setVisible(true);
            actionMenu.findItem(R.id.update).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Сохраняем текст, введенный в Edit Text
        String title = et_title.getText().toString().trim();
        String note = et_note.getText().toString().trim();
        int color = this.color;

        // Обрабатываем нажатие на содержимое Menu
        switch (item.getItemId()) {
            // Нажата кнопка Save - сохраняем заметку
            case R.id.save:
                // Проверяем Edit Text
                if (title.isEmpty()) {
                    et_title.setError("Please enter a title");
                } else if (note.isEmpty()) {
                    et_note.setError("Please enter a note");
                } else {
                    presenter.saveNote(title, note, color);
                }
                return true;

                // Нажата кнопка Edit - возможность изменить содержимое заметки
            case R.id.edit:
                // Вызов метода, который позволит изменить заметку
                editMode();
                // Скрываем ненужные элементы Menu
                actionMenu.findItem(R.id.edit).setVisible(false);
                actionMenu.findItem(R.id.delete).setVisible(false);
                actionMenu.findItem(R.id.save).setVisible(false);
                actionMenu.findItem(R.id.update).setVisible(true);
                return true;
            // Нажата кнопка Update - содержимое заметки изменяется и сохраняется
            case R.id.update:
                if (title.isEmpty()) {
                    et_title.setError("Please enter a title");
                } else if (note.isEmpty()) {
                    et_note.setError("Please enter a note");
                } else {
                    presenter.updateNote(id, title, note, color);
                }
                return true;

                // Нажата кнопка Delete - возможность удалить заметку
            case R.id.delete:

                // Диалоговое окно
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Confirm!");
                alertDialog.setMessage("Are you sure?");
                alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        presenter.deleteNote(id);
                    }
                });
                alertDialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     *  Показывает progressDialog
     */
    @Override
    public void showProgress() {
        progressDialog.show();
    }

    /**
     *  Скрывает progressDialog
     */
    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    /**
     * Выводит сообщение об успехе на экран
     **/
    @Override
    public void onAddSuccess(String message) {
        Toast.makeText(EditorActivity.this,
                message,
                Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    /**
     * Выводит ошибку на экран
     **/
    @Override
    public void onAddError(String message) {
        Toast.makeText(EditorActivity.this,
                message,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Метод, позволяющий установить данные из MainActivity в EditorActivity
     */
    private void setDataFromIntentExtra() {
        if (id != 0) {
            // Устанавливаем данные, которые были до этого
            et_title.setText(title);
            et_note.setText(note);
            spectrumPalette.setSelectedColor(color);

            getSupportActionBar().setTitle("Update Note");
            readMode();
        }else {
            // Устанавливаем цвет Palette по умолчанию
            spectrumPalette.setSelectedColor(getResources().getColor(R.color.white));
            color = getResources().getColor(R.color.white);
            editMode();
        }
    }

    /**
     *  Метод, который позволяет изменить текст в Edit Text
     */
    private void editMode() {
        // Возможность изменять Edit Text
        et_title.setFocusableInTouchMode(true);
        et_note.setFocusableInTouchMode(true);
        spectrumPalette.setEnabled(true);
    }

    /**
     * Метод, который не позволяет изменять информацию в Edit Text, а также - цвет.
     */
    private void readMode() {
        // Нет возможности изменять Edit Text
        et_title.setFocusableInTouchMode(false);
        et_note.setFocusableInTouchMode(false);
        et_title.setFocusable(false);
        et_note.setFocusable(false);
        spectrumPalette.setEnabled(false);
    }
}