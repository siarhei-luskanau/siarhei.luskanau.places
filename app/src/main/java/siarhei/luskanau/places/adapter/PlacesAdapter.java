package siarhei.luskanau.places.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.android.gms.location.places.Place;

import java.util.Collection;

import rx.Observable;
import rx.functions.Func2;
import siarhei.luskanau.places.R;
import siarhei.luskanau.places.abstracts.BaseRecyclerArrayAdapter;
import siarhei.luskanau.places.abstracts.BindableViewHolder;
import siarhei.luskanau.places.databinding.ListItemPlaceBinding;

public class PlacesAdapter extends BaseRecyclerArrayAdapter<Place, BindableViewHolder> {

    private String selectedPlaceId;
    private Location location;

    private Func2<Place, Place, Integer> placeDistanceComparator = new Func2<Place, Place, Integer>() {
        @Override
        public Integer call(Place place1, Place place2) {
            if (location != null) {
                Location location1 = new Location("");
                location1.setLatitude(place1.getLatLng().latitude);
                location1.setLongitude(place1.getLatLng().longitude);
                Location location2 = new Location("");
                location2.setLatitude(place2.getLatLng().latitude);
                location2.setLongitude(place2.getLatLng().longitude);
                Integer compare = Float.compare(location.distanceTo(location1), location.distanceTo(location2));
                if (compare == 0) {
                    compare = String.valueOf(place1.getName()).compareTo(String.valueOf(place2.getName()));
                }
                return compare;
            }
            return 0;
        }
    };

    public void setData(Location location, Collection<Place> data) {
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
        Place place = getItem(position);
        binding.item.setPlace(place, location, place.getId().equals(selectedPlaceId));
    }

    public void setSelectedPlaceId(String selectedPlaceId) {
        this.selectedPlaceId = selectedPlaceId;
        notifyDataSetChanged();
    }

}