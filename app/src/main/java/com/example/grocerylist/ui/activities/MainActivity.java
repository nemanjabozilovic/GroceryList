package com.example.grocerylist.ui.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grocerylist.R;
import com.example.grocerylist.data.models.Foodstuff;
import com.example.grocerylist.ui.adapters.FoodstuffAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView groceryListView;
    ArrayList<Foodstuff> groceryListItems;
    FoodstuffAdapter adapter;
    EditText nameInputField, quantityInputField, pricePerPieceInputField;
    Button addButton, resetInputFieldsButton, finishButton, resetListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        groceryListView = findViewById(R.id.groceryList);
        nameInputField = findViewById(R.id.nameInputField);
        quantityInputField = findViewById(R.id.quantityInputField);
        pricePerPieceInputField = findViewById(R.id.pricePerPeaceInputField);
        addButton = findViewById(R.id.addButton);
        resetInputFieldsButton = findViewById(R.id.resetInputFieldsButton);
        finishButton = findViewById(R.id.finishButton);
        resetListButton = findViewById(R.id.resetListButton);

        groceryListItems = new ArrayList<>();
        adapter = new FoodstuffAdapter(this, groceryListItems, finishButton);
        groceryListView.setAdapter(adapter);

        updateFinishButtonState();

        addButton.setOnClickListener(v -> {
            String name = nameInputField.getText().toString();
            String quantityStr = quantityInputField.getText().toString();
            String priceStr = pricePerPieceInputField.getText().toString();

            if (name.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = Integer.parseInt(quantityStr);
            double pricePerPiece = Double.parseDouble(priceStr);

            Foodstuff newItem = new Foodstuff(name, quantity, pricePerPiece);
            groceryListItems.add(newItem);
            adapter.notifyDataSetChanged();

            nameInputField.setText("");
            quantityInputField.setText("");
            pricePerPieceInputField.setText("");

            updateFinishButtonState();
        });

        resetInputFieldsButton.setOnClickListener(v -> {
            nameInputField.setText("");
            quantityInputField.setText("");
            pricePerPieceInputField.setText("");
        });

        finishButton.setOnClickListener(v -> {
            double totalPrice = 0.0;

            for (Foodstuff item : groceryListItems) {
                totalPrice += item.Quantity * item.PricePerPiece;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Total Price");
            builder.setMessage("The total price of your grocery list is: " + String.format("%.2f", totalPrice) + " RSD");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();

            disableInputFieldsAndButtons();
            adapter.setCopyAndDeleteButtonsVisible(false);
        });

        resetListButton.setOnClickListener(v -> {
            groceryListItems.clear();
            adapter.notifyDataSetChanged();

            nameInputField.setText("");
            quantityInputField.setText("");
            pricePerPieceInputField.setText("");

            enableInputFieldsAndButtons();
            updateFinishButtonState();
        });
    }

    private void updateFinishButtonState() {
        finishButton.setEnabled(!groceryListItems.isEmpty());
    }

    private void disableInputFieldsAndButtons() {
        nameInputField.setEnabled(false);
        quantityInputField.setEnabled(false);
        pricePerPieceInputField.setEnabled(false);
        addButton.setEnabled(false);
        resetInputFieldsButton.setEnabled(false);
        finishButton.setEnabled(false);
    }

    private void enableInputFieldsAndButtons() {
        nameInputField.setEnabled(true);
        quantityInputField.setEnabled(true);
        pricePerPieceInputField.setEnabled(true);
        addButton.setEnabled(true);
        resetInputFieldsButton.setEnabled(true);
        resetListButton.setEnabled(true);
        updateFinishButtonState();
    }
}