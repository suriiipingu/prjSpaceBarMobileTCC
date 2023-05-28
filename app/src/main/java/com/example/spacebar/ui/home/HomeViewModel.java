package com.example.spacebar.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.spacebar.ItemLista;
import com.example.spacebar.ListaAdapter;
import com.example.spacebar.R;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<ItemLista>> itemList;

    public HomeViewModel() {
        itemList = new MutableLiveData<>();
        setupItemList();
    }

    private void setupItemList() {
        //List<ItemLista> items = new ArrayList<>();
        //items.add(new ItemLista(R.drawable.icon, "Texto 1"));
        //items.add(new ItemLista(R.drawable.icon, "Texto 2"));
        //itemList.setValue(items);
    }

    public LiveData<List<ItemLista>> getItemList() {
        return itemList;
    }
}
