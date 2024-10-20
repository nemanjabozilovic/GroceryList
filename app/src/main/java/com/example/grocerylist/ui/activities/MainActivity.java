package com.example.grocerylist.ui.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grocerylist.R;
import com.example.grocerylist.data.models.Foodstuff;
import com.example.grocerylist.ui.adapters.FoodstuffAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView groceryListView;
    private ArrayList<Foodstuff> groceryListItems;
    private FoodstuffAdapter adapter;
    private EditText nameInputField, quantityInputField, pricePerPieceInputField;
    private Button addButton, resetInputFieldsButton, finishButton, resetListButton;

    private static final String NAME_VALIDATION_ERROR = "Please enter a valid name (letters only).";
    private static final String FILL_FIELDS_ERROR = "Please fill out all fields.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUIElements();

        groceryListItems = new ArrayList<>();
        adapter = new FoodstuffAdapter(this, groceryListItems, finishButton);
        groceryListView.setAdapter(adapter);

        updateFinishButtonState();
        setupListeners();
    }

    private void initializeUIElements() {
        groceryListView = findViewById(R.id.groceryList);
        nameInputField = findViewById(R.id.nameInputField);
        quantityInputField = findViewById(R.id.quantityInputField);
        pricePerPieceInputField = findViewById(R.id.pricePerPeaceInputField);
        addButton = findViewById(R.id.addButton);
        resetInputFieldsButton = findViewById(R.id.resetInputFieldsButton);
        finishButton = findViewById(R.id.finishButton);
        resetListButton = findViewById(R.id.resetListButton);
    }

    private void setupListeners() {
        addButton.setOnClickListener(v -> addItemToGroceryList());
        resetInputFieldsButton.setOnClickListener(v -> clearInputFields());
        finishButton.setOnClickListener(v -> showTotalPriceDialog());
        resetListButton.setOnClickListener(v -> resetGroceryList());
    }

    private void addItemToGroceryList() {
        String name = nameInputField.getText().toString();
        String quantityStr = quantityInputField.getText().toString();
        String priceStr = pricePerPieceInputField.getText().toString();

        if (!isValidName(name)) {
            showToast(NAME_VALIDATION_ERROR);
            return;
        }

        if (name.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty()) {
            showToast(FILL_FIELDS_ERROR);
            return;
        }

        addItemToAdapter(name, quantityStr, priceStr);
        clearInputFields();
        updateFinishButtonState();
    }

    private boolean isValidName(String name) {
        return name.matches("[a-zA-Z\\s]+");
    }

    private void addItemToAdapter(String name, String quantityStr, String priceStr) {
        int quantity = Integer.parseInt(quantityStr);
        double pricePerPiece = Double.parseDouble(priceStr);

        Foodstuff newItem = new Foodstuff(name, quantity, pricePerPiece);
        groceryListItems.add(newItem);
        adapter.notifyDataSetChanged();
    }

    private void clearInputFields() {
        nameInputField.setText("");
        quantityInputField.setText("");
        pricePerPieceInputField.setText("");
    }

    private void showTotalPriceDialog() {
        double totalPrice = calculateTotalPrice();

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Total Price");
        builder.setMessage("The total price of your grocery list is: " + String.format("%.2f", totalPrice) + " RSD");
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();

        disableInputFieldsAndButtons();
        adapter.setCopyAndDeleteButtonsVisible(false);
    }

    private double calculateTotalPrice() {
        double totalPrice = 0.0;
        for (Foodstuff item : groceryListItems) {
            totalPrice += item.Quantity * item.PricePerPiece;
        }
        return totalPrice;
    }

    private void resetGroceryList() {
        groceryListItems.clear();
        adapter.notifyDataSetChanged();

        clearInputFields();
        enableInputFieldsAndButtons();
        adapter.setCopyAndDeleteButtonsVisible(true);
        updateFinishButtonState();
    }

    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void updateFinishButtonState() {
        finishButton.setEnabled(!groceryListItems.isEmpty());
    }

    private void disableInputFieldsAndButtons() {
        setInputFieldsEnabled(false);
        addButton.setEnabled(false);
        resetInputFieldsButton.setEnabled(false);
        finishButton.setEnabled(false);
    }

    private void enableInputFieldsAndButtons() {
        setInputFieldsEnabled(true);
        addButton.setEnabled(true);
        resetInputFieldsButton.setEnabled(true);
        resetListButton.setEnabled(true);
        updateFinishButtonState();
    }

    private void setInputFieldsEnabled(boolean isEnabled) {
        nameInputField.setEnabled(isEnabled);
        quantityInputField.setEnabled(isEnabled);
        pricePerPieceInputField.setEnabled(isEnabled);
    }
}