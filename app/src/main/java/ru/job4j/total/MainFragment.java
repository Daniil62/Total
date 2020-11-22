package ru.job4j.total;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.io.File;
import java.util.List;
import java.util.Objects;

public class MainFragment extends Fragment {
    private Toolbar toolbar;
    private RecyclerView recycler;
    private FileMaster master;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        ActionBar actionBar = (
                (AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        recycler = view.findViewById(R.id.recycler);
        master = new FileMaster();
        recycler.setLayoutManager(new LinearLayoutManager(
                Objects.requireNonNull(getActivity()).getApplicationContext()));
        String currentPath = master.getCurrentPath();
        update(currentPath);
        return view;
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
