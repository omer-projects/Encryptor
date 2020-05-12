package com.omeredut.encryptor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {//@link EditImageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditImageFragment#//newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditImageFragment extends BottomSheetDialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    public static final String SELECT_IMAGE = "SELECT_IMAGE";
    public static final String EDIT_IMAGE = "EDIT_IMAGE";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int BASE_BLANK_IMAGE = R.drawable.baseline_add_photo_alternate_24;
    //private static final int BASE_CAMERA_IMAGE = R.drawable.icon_camera;
    //private static final int BASE_GALLERY_IMAGE = R.drawable.icon_gallery;
    //private static final int BASE_TRASH_IMAGE = R.drawable.icon_trash;
    private static final String SELECT_TITLE = "Select Image";
    private static final String EDIT_TITLE = "Edit Image";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    //private String title;
    private Context context;
    private Bitmap bitmapImage;

    private TextView titleTextView;
    private ImageView showImageView;
    private ImageButton cameraImageButton;
    private ImageButton galleryImageButton;
    private ImageButton trashImageButton;
    private LinearLayout trashContainer;


    /*private OnFragmentInteractionListener mListener;*/
    private OnImageChangeListener mListener;

    public EditImageFragment(){}

    public EditImageFragment(Context context) {
        // Required empty public constructor.
        this.context = context;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param //param1 Parameter 1.
     * @param //param2 Parameter 2.
     * @return A new instance of fragment EditImageFragment.
     */
    // TODO: Rename and change types and number of parameters
    /*public static EditImageFragment newInstance(String param1, String param2) {
        EditImageFragment fragment = new EditImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        bitmapImage = null;
        //mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View editImageFragment = inflater.inflate(R.layout.fragment_edit_image, container, false);
        titleTextView = editImageFragment.findViewById(R.id.title_text_view);
        showImageView = editImageFragment.findViewById(R.id.image_to_show_image_view);
        showImageView.setImageResource(BASE_BLANK_IMAGE);
        showImageView.setVisibility(View.GONE);
        cameraImageButton = editImageFragment.findViewById(R.id.camera_image_button);
        cameraImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCameraIntent();
            }
        });
        galleryImageButton = editImageFragment.findViewById(R.id.gallery_image_button);
        galleryImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalleryIntent();
            }
        });
        trashContainer = editImageFragment.findViewById(R.id.trash_container);
        trashImageButton = editImageFragment.findViewById(R.id.trash_image_button);
        trashImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitmapImage = null;
                showImageView.setImageResource(BASE_BLANK_IMAGE);
                if (mListener != null){
                    mListener.onImageChange(bitmapImage);
                }
                dismiss();

            }
        });
        if (getTag().equals(SELECT_IMAGE)){
            titleTextView.setText(SELECT_TITLE);
            //showImageView.setVisibility(View.GONE);
            trashContainer.setVisibility(View.GONE);
        } else {
            Bundle args = getArguments();
            //String fileName = args.getString(getString(R.string.name_file_image));
            //String path = args.getString("path_image");
            //bitmapImage = loadImageFromStorage(path, fileName);
            //showImageView.setImageBitmap(bitmapImage);
            //showImageView.setVisibility(View.VISIBLE);
            titleTextView.setText(EDIT_TITLE);
            trashContainer.setVisibility(View.VISIBLE);
        }
        return editImageFragment;
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnImageChangeListener) {
            mListener = (OnImageChangeListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnImageChangeListener{
        void onImageChange(Bitmap bitmap);
    }
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/




    public void setOnImageChangeListener(OnImageChangeListener listener){
        mListener = listener;
    }


    private static int REQUEST_GALLERY = 1;
    private static int REQUEST_CAMERA = 2;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Bitmap bitmap = null;
            switch (requestCode) {
                case 1:
                    bitmapImage = getPictureFromGallery(data);
                    if (mListener != null){
                        mListener.onImageChange(bitmapImage);
                    }
                    showImageView.setImageBitmap(ImageDesigner.squareImage(bitmapImage));
                    dismiss();
                    break;
                case 2:
                    bitmapImage = getPhotoFromCamera(data);
                    if (mListener != null){
                        mListener.onImageChange(bitmapImage);
                    }
                    showImageView.setImageBitmap(ImageDesigner.squareImage(bitmapImage));
                    dismiss();
                    break;
            }
        }
    }

    /**
     * This method gets the data that return from the gallery and returns it in bitmap image
     * @param data image bitmap
     * @return bitmap image
     */
    private Bitmap getPictureFromGallery(Intent data) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    /**
     * This method gets the data that return from the camera and returns it in bitmap image
     * @param data that return from the camera
     * @return bitmat image
     */
    private Bitmap getPhotoFromCamera(Intent data) {
        // BitMap is data structure of image file which store the image in memory
        Bitmap photo = null;
        photo = (Bitmap) data.getExtras().get("data");
        return photo;
    }


    /**
     * This method send the app to the camera intent.
     */
    private void openCameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE/*"android.media.action.IMAGE_CAPTURE"*/);
        getActivity().startActivityForResult(intent, REQUEST_CAMERA);
    }

    /**
     * This method send the app to the gallery intent.
     */
    private void openGalleryIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"),REQUEST_GALLERY);
    }
}
