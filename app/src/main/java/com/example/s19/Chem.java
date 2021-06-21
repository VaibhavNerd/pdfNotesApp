package com.example.s19;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class  Chem extends AppCompatActivity {
  public  EditText editText;
   public Button btn;
   StorageReference storageReference;
   DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chem);

        editText = findViewById(R.id.edittext4);
        btn = findViewById(R.id.upload4);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("TEST");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDF();
            }
        });


    }

    private void selectPDF() {
        Intent intent= new Intent();
        intent.setType("application/pdf");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent," SELECT PDF FILE"),12);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==12 && resultCode== RESULT_OK && data!= null && data.getData()!=null&& !(editText.getText().toString().equals(""))&&  !(editText.getText().toString().equals(" "))){
            uploadPDFFileFirebase(data.getData());
        }
      else{
          Toast.makeText(Chem.this, "Give name to your file, as it will be seen by other users after uploading",Toast.LENGTH_LONG).show();
        }
    }

    private void uploadPDFFileFirebase(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Ruko Jara Sabar Karo...");
        progressDialog.show();
        StorageReference reference= storageReference.child(editText.getText().toString()+".pdf");

        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri url = uriTask.getResult();
                        uploadPDF uploadPDF = new uploadPDF(editText.getText().toString(), url.toString());
                        databaseReference.child(databaseReference.push().getKey()).setValue(uploadPDF);
                        Toast.makeText(Chem.this, "HOGYA JI", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();editText.setText("");
                    }
                }) .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
              double progress =(100.0* snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
              progressDialog.setMessage("File Uploading..."+(int) progress+"%");
            }
        });



    }

    public void retrievePDF(View view) {
        startActivity(new Intent(getApplicationContext(),chemdow.class));

    }

}