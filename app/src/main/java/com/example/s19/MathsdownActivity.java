package com.example.s19;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MathsdownActivity extends AppCompatActivity {
 DatabaseReference databaseReference;
 ListView listView;
 List<uploadPDF> uploadedPDF;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mathsdown);

        listView = findViewById(R.id.listview3);
        uploadedPDF = new ArrayList<>();
        retrievePDFFiles();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Toast.makeText(MathsdownActivity.this,"Redirecting....",Toast.LENGTH_LONG).show();
                uploadPDF uploadPDF = uploadedPDF.get(i);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("application/pdf");
                intent.setData(Uri.parse(uploadPDF.getUrl()));
                startActivity(intent);
            }
        });
    }

    private void retrievePDFFiles() {
        databaseReference = FirebaseDatabase.getInstance().getReference("MATHS");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()) {
                    uploadPDF uploadPDF = ds.getValue(com.example.s19.uploadPDF.class);
                    uploadedPDF.add(uploadPDF);
                }
                String[] uploadsName = new String[uploadedPDF.size()];
                for(int i=0 ;i<uploadsName.length;i++){
                    uploadsName[i]=uploadedPDF.get(i).getNam();
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,uploadsName){
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView textView = (TextView)view.findViewById(android.R.id.text1);
                        textView.setTextColor(Color.BLACK);
                        textView.setTextSize(20);
                        return view;
                    }
                };
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}