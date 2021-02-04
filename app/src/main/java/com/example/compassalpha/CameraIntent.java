package com.example.compassalpha;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class CameraIntent {
    private static final String tag = "CameraIntent";

    private static final int CAMERA_PERM_CODE = 100;
    private static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;

    private Uri fileUri;
    private String imagePath;
    private String compassTag;


    // CONSTRUCTORS
    /**
     * Default Constructor
     */
    public CameraIntent(){}

    /**
     * Constructor used if a different request code is needed
     *
     * @param requestCode   integer request code to replace default one
     */
    public CameraIntent(int requestCode){
        CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = requestCode;
    }


    // PUBLIC METHODS

    /**
     * Sends a take picture intent with default tag
     *
     * @param context       activity context object
     */
    public void goToCamera(Context context){
        goToCamera(context, "000N");
    }

    /**
     * Sends a take picture intent
     *
     * @param context       activity context object
     * @param compassTag    string of current camera heading (default 000N)
     */
    public void goToCamera(Context context, String compassTag){
        this.compassTag = compassTag;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) context, new String[] {Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERM_CODE);
        } else {
            dispatchTakePictureIntent((Activity) context);
        }
    }

    /**
     * Sends user to camera app for capturing an image
     *
     * @param activity  Activity object which calls method
     */

    public void dispatchTakePictureIntent(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoURI = null;
        try{
            photoURI = getOutputMediaFileUri(activity);
        } catch (IOException ex) {
            Log.d(tag, ex.toString());
        }

        if(photoURI != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            try{
                activity.startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            } catch (ActivityNotFoundException ex) {
                Log.d(tag, ex.toString());
            }
        }
    }

    /**
     * Callback to Activity.onActivityResult
     * Prompts user that image was saved successfully
     * Sends scan broadcast for making picture appear in Gallery
     *
     * @param context           Context (Activity) object which calls method
     * @param requestCode       Callback requestCode
     * @param resultCode        Callback resultCode
     * @param data              Callback data
     */
    public void onActivityResult(Context context, int requestCode, int resultCode, Intent data){
        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Toast.makeText(context, "Image successfully saved at: " + fileUri.getPath(), Toast.LENGTH_SHORT).show();
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File f = new File(imagePath);
                Uri fUri = Uri.fromFile(f);
                mediaScanIntent.setData(fUri);
                context.sendBroadcast(mediaScanIntent);
            }else if(resultCode == RESULT_CANCELED){
                Toast.makeText(context, "Image cancelled", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Image capture has failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Activity callback
     * Checks that permissions were given and redirects user to camera
     *
     * @param context       activity object
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(Context context, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED && grantResults[2]==PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent((Activity) context);
            } else {
                Toast.makeText(context,"Camera Permission is required to use the camera",Toast.LENGTH_SHORT).show();
            }
        }
    }


    // PRIVATE METHODS
    /**
     * Creates Uri object for saving the image
     *
     * @param context           Context object from calling activity
     * @return                  Uri object of file
     * @throws IOException
     */
    private Uri getOutputMediaFileUri(Context context) throws IOException{
        File photoFile = getOutputMediaFile(context);
        Uri photoURI = FileProvider.getUriForFile(context,
                "com.example.android.fileprovider69",
                photoFile);
        fileUri = photoURI;
        return photoURI;
    }

    /**
     * Returns a File object with a constructed name
     *
     * @param context
     * @return File object
     * @throws IOException
     */
    private File getOutputMediaFile(Context context) throws IOException{
        // Create a media file name
//        @SuppressLint("SimpleDateFormat") String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "IMG_" + compassTag + "_" + timestamp;
        String imageFileName = "IMG_" + compassTag + "_";

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,      // prefix
                ".jpg",      // suffix
                storageDir          // directory
        );
        imagePath = image.getAbsolutePath();
        return image;
    }
}
