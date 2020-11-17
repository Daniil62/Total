package ru.job4j.total;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recycler;
    private FileMaster master;
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
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        update(currentPath);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("path", toolbar.getTitle().toString());
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String path = savedInstanceState.getString("path");
        update(path);
        toolbar.setTitle(path);
    }
    private String directoryEditor(String directory) {
        String[] temp = directory.split("/");
        directory = directory.replace("/" + temp[temp.length - 1], "");
        return directory;
    }
    @Override
    public void onBackPressed() {
        String currentPath = master.getCurrentPath();
        String title = toolbar.getTitle().toString();
        String directory = directoryEditor(title);
        if (!title.equals(currentPath)) {
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
                String directory = file.getPath();
                if (master.getFiles(directory).size() > 0) {
                    String path = file.getPath();
                    toolbar.setTitle(path);
                    update(path);
                }
            });
        }
        @Override
        public int getItemCount() {
            return files.size();
        }
    }
}
