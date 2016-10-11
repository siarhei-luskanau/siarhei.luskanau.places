package siarhei.luskanau.places.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.android.gms.location.places.Place;

import siarhei.luskanau.places.R;
import siarhei.luskanau.places.abstracts.BaseRecyclerArrayAdapter;
import siarhei.luskanau.places.abstracts.BindableViewHolder;
import siarhei.luskanau.places.databinding.ListItemPlaceBinding;

public class PlacesAdapter extends BaseRecyclerArrayAdapter<Place, BindableViewHolder> {

    private String selectedPlaceId;

    @Override
    public BindableViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.list_item_place, parent, false);
        return new BindableViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(BindableViewHolder holder, int position) {
        ListItemPlaceBinding binding = (ListItemPlaceBinding) holder.getBindings();
        binding.item.setPlace(getItem(position));
    }

}