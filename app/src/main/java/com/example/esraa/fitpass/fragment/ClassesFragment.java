package com.example.esraa.fitpass.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.esraa.fitpass.R;
import com.example.esraa.fitpass.adapter.ClassesAdapter;
import com.example.esraa.fitpass.model.ClassModel;
import com.example.esraa.fitpass.presenter.ClassesFragmentPresenter;
import com.example.esraa.fitpass.presenter.IClassesFragmentPresenter;
import com.example.esraa.fitpass.util.Constants;
import com.example.esraa.fitpass.view.IClassesFragmentView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassesFragment extends Fragment implements IClassesFragmentView {

    @BindView(R.id.classes_recycler_view)
    RecyclerView classesRecyclerView;
    private ClassesAdapter classesAdapter;
    private IClassesFragmentPresenter presenter;

    public static ClassesFragment newInstance(List<ClassModel> gymClasses) {
        ClassesFragment fragment = new ClassesFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.GYM_CLASSES, (Serializable) gymClasses);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classes, container, false);
        ButterKnife.bind(this, view);
        setupRecyclerAndAdapter();
        presenter = new ClassesFragmentPresenter(getContext(), this);
        if (getArguments() != null && getArguments().containsKey(Constants.GYM_CLASSES) &&
                getArguments().getSerializable(Constants.GYM_CLASSES) != null) {
            notifyFragmentWithClassesList((List<ClassModel>) getArguments().getSerializable(Constants.GYM_CLASSES));
        } else {
            presenter.getAllClassesFromFireBase();
        }
        return view;
    }

    private void setupRecyclerAndAdapter() {
        List<ClassModel> classModelList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        classesAdapter = new ClassesAdapter(classModelList);
        classesRecyclerView.setLayoutManager(layoutManager);
        classesRecyclerView.setAdapter(classesAdapter);
    }

    @Override
    public void notifyFragmentWithClassesList(List<ClassModel> classesList) {
        classesAdapter.setAdapter(classesList);
        classesAdapter.notifyDataSetChanged();
    }
}
