package com.example.grocerylist.ui.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grocerylist.R;
import com.example.grocerylist.data.models.Foodstuff;

import java.util.ArrayList;

public class FoodstuffAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Foodstuff> groceryListItems;
    private final Button finishButton;
    private boolean areButtonsVisible = true;

    public FoodstuffAdapter(Context context, ArrayList<Foodstuff> groceryListItems, Button finishButton) {
        this.context = context;
        this.groceryListItems = groceryListItems;
        this.finishButton = finishButton;
    }

    @Override
    public int getCount() {
        return groceryListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return groceryListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView != null ? convertView : LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

        Foodstuff foodstuff = (Foodstuff) getItem(position);
        setupTextView(view, foodstuff);
        setupButtons(view, position, foodstuff);

        return view;
    }

    private void setupTextView(View view, Foodstuff foodstuff) {
        TextView itemName = view.findViewById(R.id.itemName);
        itemName.setText(foodstuff.Name + ", " + foodstuff.Quantity + " piece/s, " + foodstuff.PricePerPiece + " RSD per piece");
    }

    private void setupButtons(View view, int position, Foodstuff foodstuff) {
        ImageView copyButton = view.findViewById(R.id.copyButton);
        ImageView deleteButton = view.findViewById(R.id.deleteButton);

        setButtonVisibility(copyButton, deleteButton);
        setCopyButtonListener(copyButton, foodstuff);
        setDeleteButtonListener(deleteButton, position);
    }

    private void setButtonVisibility(ImageView copyButton, ImageView deleteButton) {
        if (areButtonsVisible) {
            copyButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        } else {
            copyButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        }
    }

    private void setCopyButtonListener(ImageView copyButton, Foodstuff foodstuff) {
        copyButton.setOnClickListener(v -> {
            groceryListItems.add(new Foodstuff(foodstuff.Name, foodstuff.Quantity, foodstuff.PricePerPiece));
            notifyDataSetChanged();
            updateFinishButtonState();
        });
    }

    private void setDeleteButtonListener(ImageView deleteButton, int position) {
        deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Grocery List Item Deletion")
                    .setMessage("Are you sure you want to remove the selected item from the list?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        groceryListItems.remove(position);
                        notifyDataSetChanged();
                        updateFinishButtonState();
                        dialog.dismiss();
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    public void setCopyAndDeleteButtonsVisible(boolean visible) {
        this.areButtonsVisible = visible;
        notifyDataSetChanged();
    }

    private void updateFinishButtonState() {
        finishButton.setEnabled(!groceryListItems.isEmpty());
    }
}