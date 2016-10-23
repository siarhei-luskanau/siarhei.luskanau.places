package siarhei.luskanau.places.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collection;

import rx.Observable;
import rx.functions.Func2;
import siarhei.luskanau.places.R;
import siarhei.luskanau.places.abstracts.BaseRecyclerArrayAdapter;
import siarhei.luskanau.places.abstracts.BindableViewHolder;
import siarhei.luskanau.places.databinding.ListItemPlaceBinding;
import siarhei.luskanau.places.model.PlaceModel;

public class PlacesAdapter extends BaseRecyclerArrayAdapter<PlaceModel, BindableViewHolder> {

    private String selectedPlaceId;
    private Location location;

    private Func2<PlaceModel, PlaceModel, Integer> placeDistanceComparator = new Func2<PlaceModel, PlaceModel, Integer>() {
        @Override
        public Integer call(PlaceModel place1, PlaceModel place2) {
            if (location != null) {
                Location location1 = new Location("");
                location1.setLatitude(place1.getLatitude());
                location1.setLongitude(place1.getLongitude());
                Location location2 = new Location("");
                location2.setLatitude(place2.getLatitude());
                location2.setLongitude(place2.getLongitude());
                Integer compare = Float.compare(location.distanceTo(location1), location.distanceTo(location2));
                if (compare == 0) {
                    compare = String.valueOf(place1.getName()).compareTo(String.valueOf(place2.getName()));
                }
                return compare;
            }
            return 0;
        }
    };

    public void setData(Location location, Collection<PlaceModel> data) {
        this.location = location;
        if (location != null && data != null) {
            super.setData(Observable.from(data)
                    .toSortedList(placeDistanceComparator)
                    .toBlocking().first());
        } else {
            super.setData(data);
        }
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
        PlaceModel place = getItem(position);
        binding.item.setPlace(place, location, place.getId().equals(selectedPlaceId));
    }

    public void setSelectedPlaceId(String selectedPlaceId) {
        this.selectedPlaceId = selectedPlaceId;
        notifyDataSetChanged();
    }

}