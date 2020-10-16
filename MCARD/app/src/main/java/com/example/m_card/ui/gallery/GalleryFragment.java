package com.example.m_card.ui.gallery;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.m_card.DataModel;
import com.example.m_card.R;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    QRGEncoder qrgEncoder;
    ImageView imageView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        imageView = root.findViewById(R.id.imageView5);

        generateQrCode();
        return root;
    }


    public void generateQrCode(){
        String inputValue = DataModel.id;
        if (inputValue.length() > 0) {

            int width = 200;
            int height = 300;
            int smallerDimension = width < height ? width : height;
            smallerDimension = smallerDimension * 3 / 4;

            qrgEncoder = new QRGEncoder(
                    inputValue, null,
                    QRGContents.Type.TEXT,
                    smallerDimension);
            try {
                Bitmap bitmap = qrgEncoder.encodeAsBitmap();
                imageView.setImageBitmap(bitmap);




            } catch (WriterException e) {

            }
        } else {

            Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
        }
    }
}