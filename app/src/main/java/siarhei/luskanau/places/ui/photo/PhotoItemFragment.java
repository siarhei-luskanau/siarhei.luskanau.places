package siarhei.luskanau.places.ui.photo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;

import siarhei.luskanau.places.R;
import siarhei.luskanau.places.abstracts.BaseFragment;
import siarhei.luskanau.places.api.RxGoogleApiInterface;
import siarhei.luskanau.places.utils.AppUtils;
import siarhei.luskanau.places.utils.glide.PlacePhotoId;

public class PhotoItemFragment extends BaseFragment {

    private static final String POSITION = "POSITION";
    private ImageView placePhoto;

    public static PhotoItemFragment newInstance(int position) {
        PhotoItemFragment fragment = new PhotoItemFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        placePhoto = (ImageView) view.findViewById(R.id.placePhoto);
    }

    @Override
    public void onStart() {
        super.onStart();
        updatePhoto();
    }

    public void updatePhoto() {
        if (placePhoto == null) {
            return;
        }

        PlacePhotosFragment placePhotosFragment = (PlacePhotosFragment) getParentFragment();
        int position = getArguments().getInt(POSITION);
        Place place = placePhotosFragment.getPlace();
        PlacePhotoMetadata placePhotoMetadata = placePhotosFragment.getPlacePhotoMetadata(position);
        if (place != null && placePhotoMetadata != null) {
            PlacePhotoId placePhotoId = new PlacePhotoId(place, position, placePhotoMetadata,
                    AppUtils.getParentInterface(RxGoogleApiInterface.class, getActivity()).getRxGoogleApi());
            Glide.with(getContext())
                    .load(placePhotoId)
                    .fitCenter()
                    .placeholder(null)
                    .into(placePhoto);
        }
    }

}