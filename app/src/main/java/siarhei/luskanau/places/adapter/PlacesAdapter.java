package siarhei.luskanau.places.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.android.gms.location.places.Place;

import siarhei.luskanau.places.R;
import siarhei.luskanau.places.abstracts.BaseRecyclerArrayAdapter;
import siarhei.luskanau.places.abstracts.BindableViewHolder;
import siarhei.luskanau.places.databinding.ListItemPlaceBinding;

public class PlacesAdapter extends BaseRecyclerArrayAdapter<Place, BindableViewHolder> {

    private String selectedPlaceId;
    private Location location;

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public BindableViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.list_item_place, parent, false);
        return new BindableViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(BindableViewHolder holder, int position) {
        ListItemPlaceBinding binding = (ListItemPlaceBinding) holder.getBindings();
        Place place = getItem(position);
        binding.item.setPlace(place, location, place.getId().equals(selectedPlaceId));
    }

    public void setSelectedPlaceId(String selectedPlaceId) {
        this.selectedPlaceId = selectedPlaceId;
        notifyDataSetChanged();
    }

}