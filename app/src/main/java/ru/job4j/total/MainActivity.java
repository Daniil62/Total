package ru.job4j.total;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recycler;
    private FileMaster master;
    private TotalAdapter adapter;
    private List<String> paths = new ArrayList<>();
    private int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        master = new FileMaster();
        String currentPath = master.getCurrentPath();
        if (toolbar != null) {
            toolbar.setTitle(currentPath);
            setSupportActionBar(toolbar);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> adapter.onArrowClick());
        adapter = new TotalAdapter(master.getFiles(currentPath));
        if (savedInstanceState == null) {
            paths.add(currentPath);
        }
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        update(currentPath);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("path", toolbar.getTitle().toString());
        outState.putStringArrayList("paths", (ArrayList<String>) paths);
        outState.putInt("count", count);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String path = savedInstanceState.getString("path");
        update(path);
        toolbar.setTitle(path);
        paths = savedInstanceState.getStringArrayList("paths");
        count = savedInstanceState.getInt("count");
    }
    private String directoryEditor(String directory) {
        String[] temp = directory.split("/");
        directory = directory.replace("/" + temp[temp.length - 1], "");
        return directory;
    }
    @Override
    public void onBackPressed() {
        String title = toolbar.getTitle().toString();
        String directory = directoryEditor(title);
        if (count > 1) {
            update(directoryEditor(title));
            toolbar.setTitle(directory);
        } else {
            finish();
        }
    }
    private void update(String path) {
        if (path != null) {
            recycler.setAdapter(new TotalAdapter(master.getFiles(path)));
        }
    }
    private class TotalHolder extends RecyclerView.ViewHolder {
        private View view;
        TotalHolder(@NonNull View view) {
            super(view);
            this.view = itemView;
        }
    }
    private class TotalAdapter extends  RecyclerView.Adapter<TotalHolder> {
        private List<File> files;
        TotalAdapter(List<File> list) {
            this.files = list;
        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @NonNull
        @Override
        public TotalHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View view = inflater.inflate(R.layout.module, viewGroup, false);
            return new TotalHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull TotalHolder holder, int i) {
            File file = files.get(i);
            TextView fileName = holder.view.findViewById(R.id.module_textView);
            fileName.setText(file.getName());
            holder.view.setOnClickListener(v -> {
                ++count;
                String directory = file.getPath();
                if (master.getFiles(directory).size() > 0) {
                    String path = file.getPath();
                    toolbar.setTitle(path);
                    paths.add(path);
                    update(path);
                }
                else if (file.getPath().contains(".ogg")) {
                    callSimplePlayer(file);
                }
            });
        }
        private void callSimplePlayer(File file) {
            Intent intent = new Intent("android.intent.action.simplePlayer");
            intent.setType("application/ogg");
            if (intent.resolveActivity(getPackageManager()) != null) {
                intent.putExtra("uri", file.getPath());
                intent.putExtra("track_name", file.getName());
                startActivity(intent);
            } else {
                Log.d("<<< IMPLICIT INTENT >>>",
                        "Что-то какая-то хрень произошла.");
            }
        }
        void onArrowClick() {
            if (count > 1) {
                --count;
                update(paths.get(count));
                String title = toolbar.getTitle().toString();
                String directory = directoryEditor(title);
                update(directoryEditor(title));
                toolbar.setTitle(directory);
            }
        }
        @Override
        public int getItemCount() {
            return files.size();
        }
    }
}
